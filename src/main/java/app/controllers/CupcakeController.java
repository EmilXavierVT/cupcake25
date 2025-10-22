package app.controllers;

import app.entities.Bottom;
import app.entities.CupcakeInOrder;
import app.entities.Icing;
import app.entities.UserDefinedCupcake;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeController {

    static ArrayList<UserDefinedCupcake> userDefinedCupcakesList = new ArrayList<>();

    public static void addRoutes(Javalin app)
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
//
//        app.post("/create", ctx -> createCupcake(ctx));
//        app.get("/all", ctx -> getAllCupcakes(ctx));
//        app.get("/{id}", ctx -> getUDCById(ctx));
        app.get("/product-page", ctx -> {getAllBottoms(ctx, connectionPool);
            getAllIcings(ctx, connectionPool);});
        app.post("/product-page", ctx -> putCupcakeInAnOrder(ctx, connectionPool));

    }

    private static void createCupcake(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        int bottomId = Integer.parseInt(ctx.formParam("bottom_id"));
        int icingId = Integer.parseInt(ctx.formParam("icing_id"));
        int amount = Integer.parseInt(ctx.formParam("amount"));
        System.out.println(amount);
        CupcakeMapper cupcakeMapper = new CupcakeMapper();
        try(Connection connection = connectionPool.getConnection()) {
            UserDefinedCupcake cupcake = cupcakeMapper.createUserDefinedCupcake(bottomId, icingId, connectionPool);
            ctx.attribute("udc", cupcake.getId());
            // Add implementation for creating cupcake
            ctx.redirect("/product-page");
        } catch (DatabaseException e) {
            System.out.println("!>" + e.getMessage() + "<!");
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private static void createCupcakeArrayList(Context ctx, ConnectionPool connectionPool) throws DatabaseException, SQLException {

        int bottomId = Integer.parseInt(ctx.formParam("bottom_id"));
        int icingId = Integer.parseInt(ctx.formParam("icing_id"));
        int amount = Integer.parseInt(ctx.formParam("amount"));
        int orderId = Integer.parseInt(ctx.formParam("order_id"));
        CupcakeMapper cupcakeMapper = new CupcakeMapper();
        UserDefinedCupcake cupcake = new UserDefinedCupcake(cupcakeMapper.getBottomById(bottomId,connectionPool),cupcakeMapper.getIcingById(icingId,connectionPool));

        // chokolade + vanilje, 11
//         banana + is, 1
//
        CupcakeInOrder cupcakeInOrder = new CupcakeInOrder(orderId,cupcake,amount);


            try (Connection connection = connectionPool.getConnection()) {
                userDefinedCupcakesList.add(cupcake);
                ctx.redirect("/product-page");
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        }

    private static void getAllCupcakes(Context ctx)
    {
        // Add implementation for getting all cupcakes
        ctx.render("cupcake/list.html");
    }

    private static void getUDCById(Context ctx)
    {
        int id = Integer.parseInt(ctx.pathParam("id"));


        // Add implementation for getting cupcake by id
        ctx.render("cupcake/details.html");
    }

    private static void getAllBottoms(Context ctx, ConnectionPool connectionPool) {
        List<Bottom> allBottoms = null;
        try
        {
            CupcakeMapper cupcakeMapper = new CupcakeMapper();
            allBottoms = cupcakeMapper.getAllBottoms(connectionPool);
            ctx.sessionAttribute("bottom_options", allBottoms);
            ctx.attribute("bottom_options", allBottoms);
            ctx.render("product-page.html");
        }catch(DatabaseException e)
        {
            System.out.println(e.getMessage());
            ctx.redirect("/");
        }
    }
    private static void getAllIcings(Context ctx, ConnectionPool connectionPool) {
        List<Icing> allIcings = null;
        try
        {
            CupcakeMapper cupcakeMapper = new CupcakeMapper();
            allIcings = cupcakeMapper.getAllIcings(connectionPool);
            ctx.sessionAttribute("icing_options", allIcings);
            ctx.attribute("icing_options", allIcings);
            ctx.render("product-page.html");
        }catch(DatabaseException e)
        {
            System.out.println(e.getMessage());
            ctx.redirect("/");
        }
    }

    private static void putCupcakeInAnOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
//        vi skal lave en cp som skal til knyttes et antal fra js og som skal tilføjes med user_id og order id
        createCupcake(ctx, connectionPool);

    }

}
