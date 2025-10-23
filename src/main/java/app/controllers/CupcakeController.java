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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeController {

    static ArrayList<CupcakeInOrder> cupcakesInOrder = new ArrayList<>();

    public static void addRoutes(Javalin app)
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
//
//        app.post("/create", ctx -> createCupcake(ctx));
//        app.get("/all", ctx -> getAllCupcakes(ctx));
//        app.get("/{id}", ctx -> getUDCById(ctx));
        app.get("/product-page", ctx -> {getAllBottoms(ctx, connectionPool);
            getAllIcings(ctx, connectionPool);
        getOrderID(ctx, connectionPool);});
        app.post("/product-page", ctx -> addToCupcakeOrderArrayList(ctx, connectionPool));
        app.post("/add-to-order", ctx -> addToCupcakeOrderArrayList(ctx, connectionPool));

//        app.post("/order-confirmation", ctx -> paymentConfirmed(ctx,connectionPool));

    }

    private static void createCupcake(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        int bottomId = Integer.parseInt(ctx.formParam("bottom_id"));
        int icingId = Integer.parseInt(ctx.formParam("icing_id"));
        int amount = Integer.parseInt(ctx.formParam("amount"));
        System.out.println(amount);
        CupcakeMapper cupcakeMapper = new CupcakeMapper();
        try(Connection connection = connectionPool.getConnection()) {
            UserDefinedCupcake cupcake = cupcakeMapper.saveUserDefinedCupcake(bottomId, icingId, connectionPool);
            ctx.attribute("udc", cupcake.getId());
            // Add implementation for creating cupcake
            ctx.redirect("/product-page");
        } catch (DatabaseException e) {
            System.out.println("!>" + e.getMessage() + "<!");
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private static void getOrderID(Context ctx, ConnectionPool connectionPool) throws DatabaseException, SQLException {

        try (Connection connection = connectionPool.getConnection()) {
            System.out.println("you are now in the session");
            Integer orderId = ctx.sessionAttribute("order_id");

            if (orderId == null || orderId == 0) {
                int newOrderId = new OrderMapper().getAvailableOrderid(connectionPool);
                ctx.sessionAttribute("order_id", newOrderId);
                System.out.println(newOrderId);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addToCupcakeOrderArrayList(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        int bottomId = Integer.parseInt(ctx.formParam("bottom_id"));
        int icingId = Integer.parseInt(ctx.formParam("icing_id"));
        int amount = Integer.parseInt(ctx.formParam("amount"));
        int orderId = ctx.sessionAttribute("order_id");
        CupcakeMapper cupcakeMapper = new CupcakeMapper();
        UserDefinedCupcake cupcake = null;
        try {

            cupcake = new UserDefinedCupcake(cupcakeMapper.getBottomById(bottomId,connectionPool),cupcakeMapper.getIcingById(icingId,connectionPool));
//            CupcakeInOrder cupcakeInOrder = new CupcakeInOrder(orderId,cupcake,amount);
            cupcakesInOrder.add(new CupcakeInOrder(orderId, cupcake, amount));

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        ctx.redirect("/product-page");

        // chokolade + vanilje, 11
//         banana + is, 1
//


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

    public static ArrayList<CupcakeInOrder> getCupcakesInOrder() {
        if(cupcakesInOrder.isEmpty()) {
            System.out.println("Er vi her?");
        }
        return cupcakesInOrder;
    }

//    private static void paymentConfirmed(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
////        int userId = ctx.sessionAttribute("user");
//        ArrayList<CupcakeInOrder> cupcakesInOrder = CupcakeController.getCupcakesInOrder();
//        CupcakeMapper cupcakeMapper = new CupcakeMapper();
//        for (CupcakeInOrder cupcakeInOrder : cupcakesInOrder) {
//            cupcakeMapper.saveUserDefinedCupcake(cupcakeInOrder.getUdc().getBottom().getBottomId(),cupcakeInOrder.getUdc().getIcing().getIcingId(),connectionPool);
//            cupcakeMapper.saveCupcakeInOrder(cupcakeInOrder.getOrderId(),cupcakeInOrder.getUdc().getId(),cupcakeInOrder.getAmount(),connectionPool);
//        }
//    }


}
