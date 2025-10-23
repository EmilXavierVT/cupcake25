package app.controllers;

import app.entities.CupcakeInOrder;
import app.entities.User;
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

public class CartControler {

    public static void addRoutes(Javalin app) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        app.get("/cart",ctx -> ctx.render("cart.html"));
        app.get("/pay-page",ctx -> ctx.render("pay-page.html"));
        app.post("/order-confirmation", ctx -> paymentConfirmed(ctx,connectionPool));
    }
    private static void paymentConfirmed(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        try (Connection connection = connectionPool.getConnection()) {
            LocalDate date = LocalDate.now();

            User  user = ctx.sessionAttribute("currentUser");
            int userId = user.getId();
            int orderId = ctx.sessionAttribute("order_id");
            ArrayList<CupcakeInOrder> cupcakesInOrder = CupcakeController.getCupcakesInOrder();
            CupcakeMapper cupcakeMapper = new CupcakeMapper();
            OrderMapper orderMapper = new OrderMapper();
            for (CupcakeInOrder cupcakeInOrder : cupcakesInOrder) {
                cupcakeMapper.saveUserDefinedCupcake(cupcakeInOrder.getUdc().getBottom().getBottomId(), cupcakeInOrder.getUdc().getIcing().getIcingId(), connectionPool);
                cupcakeMapper.saveCupcakeInOrder(orderId, cupcakeInOrder.getUdc().getId(), cupcakeInOrder.getAmount(), connectionPool);
                orderMapper.saveOrder(userId,date,orderId,0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
