package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

public class UserController
{
    public static void addRoutes(Javalin app)
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        app.get("/login",ctx -> ctx.render("login.html"));
        app.get("/registerInfo", ctx -> ctx.render("registerInfo.html"));
        app.get("logout", ctx -> logout(ctx));
        app.get("/registerPassword", ctx -> ctx.render("registerPassword.html"));
        app.get("/adminIndex", ctx ->
        {
            ctx.render("adminPages/adminIndex.html");
            getTodaySalesNumber(ctx,connectionPool);
            getLastSevenDaysOrders(ctx,connectionPool);
            getTopUsers(ctx,connectionPool);
            getTotalSales(ctx,connectionPool);
        });
        app.get("/admin-customer-page", ctx ->
        {
            ctx.render("adminPages/admin-customer-page.html");
            getAllUsers(ctx, connectionPool);
        });
        app.get("/admin-order-page", ctx -> {
            ctx.render("adminPages/admin-order-page.html");
            getAllOrders(ctx,connectionPool);
        });
        app.post("/registerPassword", ctx -> createUser(ctx));
        app.post("/registerInfo", ctx -> registerInfo(ctx, connectionPool));
        app.post("/insertMoney", ctx -> {insertMoney(ctx, connectionPool); });
        app.post("/login", ctx -> login(ctx));


    }
//    lets go


    private static void getTopUsers(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        try {
            HashMap<Integer,User> topUsers = AdminMapper.findMostActiveUsers(connectionPool);

            List<Integer> topUserPurchaseAmounts = new ArrayList<>();
            List<User> topUserObjects = new ArrayList<>();

            int i = 0;
            for (Map.Entry<Integer, User> entry : topUsers.entrySet()) {
                if (i >= 6) break;
                topUserPurchaseAmounts.add(entry.getKey());
                topUserObjects.add(entry.getValue());

                i++;
            }
            topUserPurchaseAmounts = topUserPurchaseAmounts.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                        Collections.reverse(list);
                        return list;
                    }));

// Reverse topUserObjects
            topUserObjects = topUserObjects.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                        Collections.reverse(list);
                        return list;
                    }));


            // Example: pass lists to session attributes or use as needed
            ctx.sessionAttribute("top_user_amount", topUserPurchaseAmounts);
            ctx.sessionAttribute("top_users", topUserObjects);
            ctx.render("adminPages/adminIndex.html", Map.of("top_user_amount",topUserPurchaseAmounts));
            ctx.render("adminPages/adminIndex.html", Map.of("top_users",topUserObjects));

        } catch (SQLException e) {
            throw new DatabaseException("something when getting top users" ,e.getMessage());
        }
    }


    private static void getTodaySalesNumber(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        try{
            int dailysale = AdminMapper.calulateDailySales(AdminMapper.findDailySales(connectionPool));
            ctx.sessionAttribute("today_sales",dailysale);
            ctx.render("adminPages/adminIndex.html", Map.of(
                            "today_sales", Objects.requireNonNull(ctx.sessionAttribute("today_sales"))
                    ));

        } catch (SQLException e) {
            throw new DatabaseException("something when getting today sales number" ,e.getMessage());
        }
    }

    private static void getLastSevenDaysOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        List<Order> last7DaysOrder = new OrderMapper().getOrdersLastSevenDays();
        ctx.sessionAttribute("orders_of_seven_days",last7DaysOrder);
        ctx.render("adminPages/adminIndex.html", Map.of(
                        "orders_of_seven_days", last7DaysOrder
                ));
    }


    private static void createUser(Context ctx)
    {
        // Hent form parametre
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String confirmPassword = ctx.formParam("confirm_password");

        if (password.equals(confirmPassword))
        {
            try
            {
                UserMapper.createUser(email, password);
                ctx.attribute("message", "Du er hermed oprettet med brugernavn: " + email +
                        ". Nu skal du logge på.");
                ctx.render("registerInfo.html");
            }

            catch (DatabaseException e)
            {
                ctx.attribute("message", "Dit brugernavn findes allerede. Prøv igen, eller log ind");
                ctx.render("registerPassword.html");
            }
        } else
        {
            ctx.attribute("message", "Dine to passwords matcher ikke! Prøv igen");
            ctx.render("registerPassword.html");
        }

    }

    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }


    public static void login(Context ctx)
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();


        // Hent form parametre
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        // Check om bruger findes i DB med de angivne username + password
        try
        {
            User user = UserMapper.login(email, password);
            ctx.sessionAttribute("currentUser", user);
            ctx.attribute("currentUser", user);
           if(UserMapper.checkIfAdmin(user) == 1)
            {
                ctx.sessionAttribute("admin", true);
                ctx.attribute("message", "Du er nu logget ind som admin.");
                ctx.render("adminPages/adminIndex.html");
                ctx.redirect("/adminIndex");

            }
            else
            {

                ctx.sessionAttribute("admin", false);
                ctx.attribute("message", "Du er nu logget ind");
                ctx.render("index.html");
                ctx.redirect("/");
            }

            // Hvis ja, send videre til forsiden med login besked

        }
        catch (DatabaseException e)
        {
            // Hvis nej, send tilbage til login side med fejl besked
            ctx.attribute("message", e.getMessage() );
            System.out.println("login logs errors");
            ctx.render("index.html");
        }

    }

    public static void registerInfo(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        String firstName = ctx.formParam("first_name");
        String lastName = ctx.formParam("last_name");
        int zipCode = Integer.parseInt(ctx.formParam("post_code"));
        String streetName = ctx.formParam("street_name");
        int streetNumber = Integer.parseInt(ctx.formParam("street_number"));
        String floor = ctx.formParam("floor");
        int userId = ctx.sessionAttribute("user");
        UserMapper.updateUser(userId,firstName,lastName,zipCode,streetName,streetNumber,floor,connectionPool);
        ctx.sessionAttribute("message","Du har opdateret din profil !");
        ctx.render("index.html");
    }




    public static void insertMoney(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        String email = ctx.formParam("email");
        float amount = Float.parseFloat(ctx.formParam("amount"));
        try(Connection connection = connectionPool.getConnection()) {
            UserMapper.insertMoney(email,amount);
            ctx.sessionAttribute("message","Du har nu indsat penge på brugers konto !");
            ctx.render("adminPages/adminIndex.html");
        } catch (SQLException e) {
            throw new DatabaseException("RegisterInfo error", e.getMessage());
        }
    }

    private static void getAllUsers(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        try (Connection connection = connectionPool.getConnection()) {
            List<User> users = UserMapper.getAllUsers(connectionPool);
            ctx.sessionAttribute("all_users",users);
            ctx.render("adminPages/admin-customer-page.html", Map.of(
                    "all_users", users
            ));
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all users", e.getMessage());
        }
    }

    private static void getAllOrders(Context ctx, ConnectionPool connectionPool){
        try{
            List<Order> allOrders = new OrderMapper().getAllOrders(connectionPool);
            allOrders = allOrders.stream()
                    .collect(Collectors
                            .collectingAndThen(Collectors.toList(),list ->{
                                Collections.reverse(list);
                                return list;
                            }));
            ctx.sessionAttribute("all_orders",allOrders);
            ctx.render("adminPages/admin-order-page.html", Map.of(
                    "all_orders", allOrders
            ));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getTotalSales(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        float totalSales = 0;
        try {
            List<Order> allOrders = new OrderMapper().getAllOrders(connectionPool);
        for(Order order : allOrders) {
            for(CupcakeInOrder cupcake :order.getCupcakesInOrder()){
                totalSales +=(cupcake.getUdc().getBottom().getBottomPrice() +
                        cupcake.getUdc().getIcing().getIcingPrice()*cupcake.getAmount());
            }
        }
        ctx.sessionAttribute("total_sales",totalSales);
        ctx.render("adminPages/adminIndex.html", Map.of(
                "total_sales", Objects.requireNonNull(ctx.sessionAttribute("total_sales"))
        ));

        } catch (DatabaseException e) {
            throw new DatabaseException("something in all orders"+ e.getMessage());
        }

    }

}
