package app.controllers;

import app.entities.CupcakeInOrder;
import app.entities.DiscountCode;
import app.entities.User;
import app.entities.UserDefinedCupcake;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CartController {
    static float price =0;

    public static void addRoutes(Javalin app)
    {

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        app.get("/cart",ctx -> {
            ctx.sessionAttribute("cupcakeInOrder",CupcakeController.getCupcakesInOrder());
            System.out.println(CupcakeController.getCupcakesInOrder());
//            setDisplayOfOrders(ctx);
            ctx.render("cart.html");
    });



        app.get("/pay-page",ctx -> ctx.render("pay-page.html"));
        app.get("/apply_discount", ctx -> findDiscountCode(ctx, connectionPool));
        app.post("/apply_discount", ctx -> findDiscountCode(ctx, connectionPool));
        app.post("/order-confirmation", ctx -> {
            paymentConfirmed(ctx,connectionPool);
            findDiscountCode(ctx,connectionPool);
        });
        app.get("/order-confirmation", ctx -> ctx.render("order-confirmation.html"));




    }

//    private static void setDisplayOfOrders(Context ctx) {
//        CupcakeController cupcakeController = new CupcakeController();
//        ArrayList<CupcakeInOrder> cupcakeList = CupcakeController.getCupcakesInOrder();
//
//
//
//    }
    private static void paymentConfirmed(Context ctx, ConnectionPool connectionPool) throws DatabaseException {



        try (Connection connection = connectionPool.getConnection()) {
            LocalDate date = LocalDate.now();

            User  user = ctx.sessionAttribute("currentUser");
            int userId = user.getId();
            int orderId = ctx.sessionAttribute("order_id");
            ArrayList<CupcakeInOrder> cupcakesInOrder = CupcakeController.getCupcakesInOrder();
            CupcakeMapper cupcakeMapper = new CupcakeMapper();
            OrderMapper orderMapper = new OrderMapper();
            UserDefinedCupcake userDefinedCupcake = null;
            orderMapper.saveOrder(userId,date,orderId,0, connectionPool);
            for (CupcakeInOrder cupcakeInOrder : cupcakesInOrder) {
                 userDefinedCupcake = cupcakeMapper.saveUserDefinedCupcake(cupcakeInOrder.getUdc().getBottom().getBottomId(), cupcakeInOrder.getUdc().getIcing().getIcingId(), connectionPool);
                 price += cupcakeInOrder.getUdc().getBottom().getBottomPrice() + cupcakeInOrder.getUdc().getIcing().getIcingPrice();
                cupcakeMapper.saveCupcakeInOrder(orderId, userDefinedCupcake.getId(), cupcakeInOrder.getAmount(), connectionPool);

            }

            } catch (DatabaseException e)
        {
            throw new DatabaseException("paymentConfirmed controller",e.getMessage());
        }
        if(ctx.sessionAttribute("discount") != null)
        {
            int discount = ctx.sessionAttribute("discount");

                float finalPrice = (price * discount) / 100;
        }
        ctx.redirect("/order-confirmation");
        ctx.render("/order-confirmation");
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
             ctx.sessionAttribute("discount", 0);
                ctx.sessionAttribute("message", "Der blev ikke fundet endiscountCode med det kodeord: " + discountCode);
                }
            else
                {
                int discount = dc.getDiscountPercentage();
                ctx.sessionAttribute("discount", discount);

                ctx.render("/cart");
            }

        } catch (DatabaseException e)
        {
            throw new DatabaseException("findDiscountCode controller", e.getMessage());
        }
    }
}
