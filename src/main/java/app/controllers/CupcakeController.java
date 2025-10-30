package app.controllers;

import app.entities.Bottom;
import app.entities.CupcakeInOrder;
import app.entities.Icing;
import app.entities.UserDefinedCupcake;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.OrderMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CupcakeController
{
    static ArrayList<CupcakeInOrder> cupcakesInOrder = new ArrayList<>();

    public static void addRoutes(Javalin app)
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        app.get("/product-page", ctx -> {getAllBottoms(ctx, connectionPool);
            getAllIcings(ctx, connectionPool);
        getOrderID(ctx, connectionPool);});
        app.post("/product-page", ctx -> addToCupcakeOrderArrayList(ctx, connectionPool));
        app.post("/add-to-order", ctx -> addToCupcakeOrderArrayList(ctx, connectionPool));
        app.post("/delete_cupcake_in_current_order", ctx -> removeOneCupcakeFromCart(ctx));
//        app.post("/order-confirmation", ctx -> paymentConfirmed(ctx,connectionPool));
    }

    private static void removeOneCupcakeFromCart(Context ctx)
    {
        int newPrice = 0;
        int cupcakeId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("cupcake_id")));
        CupcakeInOrder cupcakeInOrder = null;
        for (CupcakeInOrder cupcakeInOrder1 : cupcakesInOrder)
        { newPrice += (int) ((cupcakeInOrder1.getUdc().getBottom().getBottomPrice() + cupcakeInOrder1.getUdc().getIcing().getIcingPrice()) * cupcakeInOrder1.getAmount());

            if(cupcakeInOrder1.getUdc().getId() == cupcakeId)
            {
                cupcakeInOrder = cupcakeInOrder1;
                newPrice-= (int) ((cupcakeInOrder1.getUdc().getBottom().getBottomPrice() + cupcakeInOrder1.getUdc().getIcing().getIcingPrice()) * cupcakeInOrder1.getAmount());
            }
        }
        cupcakesInOrder.remove(cupcakeInOrder);
        ctx.sessionAttribute("orderPrice",newPrice);
        ctx.render("/cart", Map.of("cupcakesInOrder", cupcakesInOrder));
        ctx.render("/cart", Map.of("orderPrice", newPrice));
    }


//    private static void createCupcake(Context ctx, ConnectionPool connectionPool) throws DatabaseException
//    {
//        int bottomId = Integer.parseInt(ctx.formParam("bottom_id"));
//        int icingId = Integer.parseInt(ctx.formParam("icing_id"));
//        int amount = Integer.parseInt(ctx.formParam("amount"));
//        CupcakeMapper cupcakeMapper = new CupcakeMapper();
//
//        try
//        {
//            UserDefinedCupcake cupcake = cupcakeMapper.saveUserDefinedCupcake(bottomId, icingId, connectionPool);
//            ctx.attribute("udc", cupcake.getId());
//            ctx.redirect("/product-page");
//
//        } catch (DatabaseException e)
//        {
//            throw new DatabaseException("createCupcake Controller", e.getMessage());
//        }
//    }

    private static void getOrderID(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try  {
            System.out.println("you are now in the session");
            Integer orderId = ctx.sessionAttribute("order_id");

            if (orderId == null || orderId == 0)
            {
                int newOrderId = new OrderMapper().getAvailableOrderid(connectionPool);
                ctx.sessionAttribute("order_id", newOrderId);
                System.out.println(newOrderId);
            }

        } catch (DatabaseException e)
        {
            throw new DatabaseException("getOrderID controller", e.getMessage());
        }
    }

    private static void addToCupcakeOrderArrayList(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        int bottomId = Integer.parseInt(ctx.formParam("bottom_id"));
        int icingId = Integer.parseInt(ctx.formParam("icing_id"));
        int amount = Integer.parseInt(ctx.formParam("amount"));
        int orderId = ctx.sessionAttribute("order_id");
        CupcakeMapper cupcakeMapper = new CupcakeMapper();
        UserDefinedCupcake cupcake = null;

        try
        {
            cupcake = new UserDefinedCupcake(cupcakesInOrder.size(),cupcakeMapper.getBottomById(bottomId,connectionPool),cupcakeMapper.getIcingById(icingId,connectionPool));
//            CupcakeInOrder cupcakeInOrder = new CupcakeInOrder(orderId,cupcake,amount);
            cupcakesInOrder.add(new CupcakeInOrder(orderId, cupcake, amount));
            String msg = "Du har bestilt en " + cupcake.getBottom().getBottomName() +" cupcake med " + cupcake.getIcing().getIcingName() + " icing og du skal da ha' " + amount + " stk.";
            ctx.sessionAttribute("add_to_cart_message", msg);
            ctx.redirect("/product-page");
            ctx.render("product-page.html", Map.of("add_to_cart_message",msg));
        } catch (DatabaseException e)
        {
           ctx.redirect("/product-page");
           ctx.sessionAttribute("add_to_cart_message", "Der kan ikke bestilles cupcakes nu, kontakt venligst ejeren");
           ctx.render("product-page.html", Map.of("add_to_cart", "Der kan ikke bestilles cupcakes nu, kontakt venligst ejeren"));
        }
    }

    private static void getAllBottoms(Context ctx, ConnectionPool connectionPool)
    {
        List<Bottom> allBottoms = null;

        try
        {
            CupcakeMapper cupcakeMapper = new CupcakeMapper();
            allBottoms = cupcakeMapper.getAllBottoms(connectionPool);
            ctx.sessionAttribute("bottom_options", allBottoms);
            ctx.attribute("bottom_options", allBottoms);
            ctx.render("product-page.html");
        }
        catch(DatabaseException e)
        {
            System.out.println(e.getMessage());
            ctx.redirect("/");
        }
    }

    private static void getAllIcings(Context ctx, ConnectionPool connectionPool)
    {
        List<Icing> allIcings = null;

        try
        {
            CupcakeMapper cupcakeMapper = new CupcakeMapper();
            allIcings = cupcakeMapper.getAllIcings(connectionPool);
            ctx.sessionAttribute("icing_options", allIcings);
            ctx.attribute("icing_options", allIcings);
            ctx.render("product-page.html");
        }
        catch(DatabaseException e)
        {
            System.out.println(e.getMessage());
            ctx.redirect("/");
        }
    }

    public static ArrayList<CupcakeInOrder> getCupcakesInOrder()
    {
           return cupcakesInOrder;
    }

}
