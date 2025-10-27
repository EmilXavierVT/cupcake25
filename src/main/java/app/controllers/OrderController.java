package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import app.entities.DiscountCode;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

public class OrderController {
    public static void addRoutes(Javalin app)
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
    app.get("/get_order_id",ctx -> getOrderID(ctx,connectionPool));
        //app.get("/product-page",ctx -> getOrderID(ctx,connectionPool));
    }


    private static void getOrderID(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try{
            System.out.println("you are now in the session");
            Integer orderId = ctx.sessionAttribute("order_id");

            if(orderId == null || orderId == 0)
            {
                int newOrderId = new OrderMapper().getAvailableOrderid(connectionPool);
                ctx.sessionAttribute("order_id", orderId);
                System.out.println(newOrderId);
            }


        } catch (DatabaseException e)
        {
            throw new DatabaseException("getOrderID in OrderController", e.getMessage());
        }

    }



    private static void setOrderIdToZero(Context ctx)
    {
        ctx.sessionAttribute("order_id", 0);
    }
}
