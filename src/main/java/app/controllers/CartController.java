package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CartController {
    static float price =0;

    public static void addRoutes(Javalin app) {

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
    private static void paymentConfirmed(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {


        try (Connection connection = connectionPool.getConnection()) {
            LocalDate date = LocalDate.now();

            User user = ctx.sessionAttribute("currentUser");
            int userId = user.getId();
            int orderId = ctx.sessionAttribute("order_id");
            ArrayList<CupcakeInOrder> cupcakesInOrder = CupcakeController.getCupcakesInOrder();

            float price = 0;
            for (CupcakeInOrder c : cupcakesInOrder) {
                float bottomPrice = c.getUdc().getBottom().getBottomPrice();
                float icingPrice = c.getUdc().getIcing().getIcingPrice();
                int amount = c.getAmount();
                price += (bottomPrice + icingPrice) * amount;
            }
            if (ctx.sessionAttribute("discount") != null) {
                int discount = ctx.sessionAttribute("discount");

                price = (price * discount) / 100;
            }

            if (UserMapper.findUserWallet(connectionPool, userId, price)) {


            CupcakeMapper cupcakeMapper = new CupcakeMapper();
            OrderMapper orderMapper = new OrderMapper();
            UserDefinedCupcake userDefinedCupcake = null;
            orderMapper.saveOrder(userId, date, orderId, 0);

            for (CupcakeInOrder cupcakeInOrder : cupcakesInOrder) {
                userDefinedCupcake = cupcakeMapper.saveUserDefinedCupcake(cupcakeInOrder.getUdc().getBottom().getBottomId(), cupcakeInOrder.getUdc().getIcing().getIcingId(), connectionPool);
                cupcakeMapper.saveCupcakeInOrder(orderId, userDefinedCupcake.getId(), cupcakeInOrder.getAmount(), connectionPool);
            }
            UserMapper.subtractFunds(connectionPool,price,userId);
                ctx.redirect("/order-confirmation");
                ctx.render("/order-confirmation");
        }
            ctx.redirect("/pay-page");
            ctx.attribute("message","ikke nok penge på konto, kontakt administrator for at regulere konto");


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }



    private static void findDiscountCode(Context ctx, ConnectionPool connectionPool) {
        try (Connection connection = connectionPool.getConnection()) {
            System.out.println("you are now looking for a discount code");
            String discountCode = ctx.formParam("discountCode");

            OrderMapper orderMapper = new OrderMapper();
            DiscountCode dc = orderMapper.findDiscountPercentage(discountCode, connectionPool);

            if(dc == null) {
             ctx.sessionAttribute("discount", 0);
                ctx.sessionAttribute("message", "Der blev ikke fundet endiscountCode med det kodeord: " + discountCode);
                } else {
                int discount = dc.getDiscountPercentage();
                ctx.sessionAttribute("discount", discount);

                ctx.render("/cart");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
