package app.controllers;

import app.entities.CupcakeInOrder;
import app.entities.DiscountCode;
import app.entities.User;
import app.entities.UserDefinedCupcake;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class CartController
{

    public static void addRoutes(Javalin app)
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        app.get("/cart",ctx ->
        {
            ctx.sessionAttribute("cupcakeInOrder",CupcakeController.getCupcakesInOrder());
            ctx.sessionAttribute("orderPrice",getPriceForOrder(ctx));
            ctx.sessionAttribute("pickupDate",getPickupDate());
            System.out.println(CupcakeController.getCupcakesInOrder());
            setAddress(ctx);
            setOrderAndPickUpDate(ctx);
            ctx.render("cart.html");
        });
        app.get("/pay-page",ctx -> ctx.render("pay-page.html"));
        app.get("/apply_discount", ctx -> findDiscountCode(ctx, connectionPool));
        app.post("/apply_discount", ctx -> findDiscountCode(ctx, connectionPool));
        app.post("/order-confirmation", ctx ->
        {
            paymentConfirmed(ctx,connectionPool);
            findDiscountCode(ctx,connectionPool);
        });
        app.get("/order-confirmation", ctx -> ctx.render("order-confirmation.html"));
    }


    private static LocalDate getPickupDate()
    {
        LocalDate date = LocalDate.now();
        return date.plusDays(2);
    }


    private static float getPriceForOrder(Context ctx)
    {
        ArrayList<CupcakeInOrder> allCupcakes = ctx.sessionAttribute("cupcakeInOrder");
        int price = 0;
        for (CupcakeInOrder c : allCupcakes)
        {
            price += (c.getUdc().getBottom().getBottomPrice() + c.getUdc().getIcing().getIcingPrice()) * c.getAmount();

        }
        if(ctx.sessionAttribute("discount") != null){
            float discount = ctx.sessionAttribute("discount");
            // float discou
//            float discountFloat = Float.parseFloat(String.valueOf(discount));

            float discountedPrice = (price * discount) / 100;
            float actualPrice = price - discountedPrice;
            ctx.sessionAttribute("orderPrice", actualPrice);
            return actualPrice;

        }

        return price;
    }


    private static void paymentConfirmed(Context ctx, ConnectionPool connectionPool)
    {
        try  {
            LocalDate date = LocalDate.now();
            User user = ctx.sessionAttribute("currentUser");
            ArrayList<CupcakeInOrder> cupcakesInOrder = CupcakeController.getCupcakesInOrder();

            if (user != null && !cupcakesInOrder.isEmpty())
            {
                int userId = user.getId();
                int orderId = ctx.sessionAttribute("order_id");
                float price = 0;

                for (CupcakeInOrder c : cupcakesInOrder )
                {
                    float bottomPrice = c.getUdc().getBottom().getBottomPrice();
                    float icingPrice = c.getUdc().getIcing().getIcingPrice();
                    int amount = c.getAmount();
                    price += (bottomPrice + icingPrice) * amount;
                }

                if (ctx.sessionAttribute("discount") != null )
                {
                    float discount =  ctx.sessionAttribute("discount");
                    price = (price * discount) / 100;

                }

                if (UserMapper.findUserWallet(connectionPool, userId, price))
                {
                    CupcakeMapper cupcakeMapper = new CupcakeMapper();
                    OrderMapper orderMapper = new OrderMapper();
                    UserDefinedCupcake userDefinedCupcake = null;
                    orderMapper.saveOrder(userId, date, orderId, 0, connectionPool);

                    for (CupcakeInOrder cupcakeInOrder : cupcakesInOrder)
                    {
                        userDefinedCupcake = cupcakeMapper.saveUserDefinedCupcake(cupcakeInOrder.getUdc().getBottom().getBottomId(), cupcakeInOrder.getUdc().getIcing().getIcingId(), connectionPool);
                        cupcakeMapper.saveCupcakeInOrder(orderId, userDefinedCupcake.getId(), cupcakeInOrder.getAmount(), connectionPool);
                    }

                    UserMapper.subtractFunds(connectionPool, price, userId);
                    cupcakesInOrder.clear();
                    ctx.sessionAttribute("order_id", 0);
                    ctx.redirect("/order-confirmation");
                    ctx.render("/order-confirmation");
                }else
                {
                    System.out.println("error in getting ot wallet ");
                }

            }else if (user == null)
            {
                ctx.sessionAttribute("errorLogin", "Du skal være logget ind for at betale!");
                ctx.redirect("/login");
                ctx.render("/login",Map.of("errorLogin","Du skal være logget ind for at betale!"));
            }

        } catch (DatabaseException e)
        {
            System.out.println("paymentConfirmed, CartController: " + e.getMessage());
            ctx.redirect("/login");
        }
    }


    private static void findDiscountCode(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try
        {
            System.out.println("you are now looking for a discount code");
            String discountCode = ctx.formParam("discountCode");
            OrderMapper orderMapper = new OrderMapper();
            DiscountCode dc = orderMapper.findDiscountPercentage(discountCode, connectionPool);

            if(dc == null)
            {
                float zero = 0.0f;
             ctx.sessionAttribute("discount", zero);
                ctx.sessionAttribute("discountMessage", "Der blev ikke fundet et discount kode med det kodeord: " + discountCode);
                ctx.render("/cart");
                }
            else
                {
                float discount =  dc.getDiscountPercentage();
                ctx.sessionAttribute("discount", discount);

                ctx.render("/cart", Map.of("discount",discount));
                ctx.redirect("/cart");
            }

        } catch (DatabaseException e)
        {
            throw new DatabaseException("findDiscountCode controller", e.getMessage());
        }
    }


    private static void setOrderAndPickUpDate(Context ctx)
    {
        LocalDate today = LocalDate.now();
        LocalDate pickUpDate = today.plusDays(3);
        ctx.sessionAttribute("pick_up_date",pickUpDate);
        ctx.sessionAttribute("todays_date",today);
        ctx.render("/cart", Map.of("todays_date", today.toString(), "pick_up_date", pickUpDate.toString()));
    }


    private static void setAddress(Context ctx)
    {
        User currentUser = ctx.sessionAttribute("currentUser");

        if(currentUser != null)
        {
            String address = currentUser.getStreetName();
            ctx.sessionAttribute("pick_up_address", address);
            ctx.render("/cart", Map.of("pick_up_address", address));
        }
        ctx.render("/cart");
    }
}
