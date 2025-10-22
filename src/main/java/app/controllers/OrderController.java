package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.SQLException;

public class OrderController {
    public static void addRoutes(Javalin app)
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
    app.get("/get_order_id",ctx -> getOrderID(ctx,connectionPool));
        //app.get("/product-page",ctx -> getOrderID(ctx,connectionPool));

    }
    private static void getOrderID(Context ctx, ConnectionPool connectionPool) throws DatabaseException, SQLException {

        try(Connection connection = connectionPool.getConnection()){
            System.out.println("you are now in the session");
            Integer orderId = 0;
            //int orderId = Integer.parseInt(ctx.queryParam("order_id"));
            orderId = ctx.sessionAttribute("order_id");
            int newOrderId = new OrderMapper().getAvailableOrderid(connectionPool);

            if(orderId == 0){
                orderId = newOrderId;
                ctx.sessionAttribute("order_id", orderId);
                System.out.println(newOrderId);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
