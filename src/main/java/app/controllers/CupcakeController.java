package app.controllers;

import app.entities.UserDefinedCupcake;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;

public class CupcakeController {

    public static void addRoutes(Javalin app) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        app.post("/cupcake/create", ctx -> createCupcake(ctx));
        app.get("/cupcake/all", ctx -> getAllCupcakes(ctx));
        app.get("/cupcake/{id}", ctx -> getUDCById(ctx));
    }

    private static void createCupcake(Context ctx) throws DatabaseException {
        int bottomId = Integer.parseInt(ctx.formParam("bottom_id"));
        int icingId = Integer.parseInt(ctx.formParam("icing_id"));
        CupcakeMapper cupcakeMapper = new CupcakeMapper();

        try{
            UserDefinedCupcake cupcake = cupcakeMapper.createUserDefinedCupcake(bottomId,icingId);
            ctx.attribute("udc", + cupcake.getId());
        }
        catch ( SQLException e) {
            throw new RuntimeException(e);
        }
        // Add implementation for creating cupcake
        ctx.render("cupcake/shop_page.html");
    }

    private static void getAllCupcakes(Context ctx) {
        // Add implementation for getting all cupcakes
        ctx.render("cupcake/list.html");
    }

    private static void getUDCById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));


        // Add implementation for getting cupcake by id
        ctx.render("cupcake/details.html");
    }
}
