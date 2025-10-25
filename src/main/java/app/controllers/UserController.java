package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

public class UserController
{
    public static void addRoutes(Javalin app)
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        app.get("/login",ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> login(ctx));
        app.get("logout", ctx -> logout(ctx));
        app.get("/registerPassword", ctx -> ctx.render("registerPassword.html"));
        app.post("/registerPassword", ctx -> createUser(ctx));
        app.post("/registerInfo", ctx -> registerInfo(ctx, connectionPool));
        app.get("/registerInfo", ctx -> ctx.render("registerInfo.html"));
        app.get("/adminIndex", ctx -> {ctx.render("adminPages/adminIndex.html");getTodaySalesNumber(ctx,connectionPool);});
        app.get("/admin-customer-page", ctx -> ctx.render("adminPages/admin-customer-page"));
        app.get("/admin-order-page", ctx -> ctx.render("adminPages/admin-order-page.html"));
        app.post("/adminIndex", ctx -> {insertMoney(ctx, connectionPool); });

    }
//    lets go


    private static void getTodaySalesNumber(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        try(Connection connection =connectionPool.getConnection()){
            int dailysale = AdminMapper.calulateDailySales(AdminMapper.findDailySales(connectionPool));
            ctx.sessionAttribute("today_sales",dailysale);
            ctx.render("adminPages/adminIndex.html", Map.of(
                            "today_sales", Objects.requireNonNull(ctx.sessionAttribute("today_sales"))
                    ));

        } catch (SQLException e) {
            throw new DatabaseException("something when getting today sales number" ,e.getMessage());
        }
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
        try(Connection connection = connectionPool.getConnection()) {
            UserMapper.updateUser(userId,firstName,lastName,zipCode,streetName,streetNumber,floor);
            ctx.sessionAttribute("message","Du har opdateret din profil !");
            ctx.render("index.html");
        } catch (SQLException e) {
            throw new DatabaseException("RegisterInfo error", e.getMessage());
        }
    }




    public static void insertMoney(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        String email = ctx.formParam("email");
        float amount = Float.parseFloat("amount");
        try(Connection connection = connectionPool.getConnection()) {
            UserMapper.insertMoney(email,amount);
            ctx.sessionAttribute("message","Du har nu indsat penge på brugers konto !");
        } catch (SQLException e) {
            throw new DatabaseException("RegisterInfo error", e.getMessage());
        }
    }

}
