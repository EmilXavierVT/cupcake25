package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPOutputStream;

public class UserController
{

    public static void addRoutes(Javalin app) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        app.get("/login", ctx -> ctx.render("login.html"));
        app.get("/registerInfo", ctx -> ctx.render("registerInfo.html"));
        app.get("logout", ctx -> logout(ctx));
        app.get("/registerPassword", ctx -> ctx.render("registerPassword.html"));
        app.get("/profile-page", ctx -> getUserOrders(ctx, connectionPool));
        app.get("/about", ctx -> ctx.render("about.html"));
        app.post("/registerPassword", ctx -> createUser(ctx));
        app.post("/registerInfo", ctx -> registerInfo(ctx, connectionPool));
        app.post("/login", ctx -> login(ctx));

    }

    private static void getUserOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        List<Order> allOrders = new OrderMapper().getAllOrders(connectionPool);
        allOrders = allOrders.stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(),list ->
                        {
                            Collections.reverse(list);
                            return list;
                        }));

        User user = ctx.sessionAttribute("currentUser");
        int userId = user.getId();
        List<Order> userOrders = new ArrayList<>();

        for(Order order : allOrders)
        {
            if(order.getUserId() == userId)
            {
                userOrders.add(order);
            }
            ctx.sessionAttribute("user_orders",userOrders);
            ctx.render("/profile-page", Map.of(
                    "user_orders", userOrders
            ));
        }
    }


    private static void createUser(Context ctx)
    {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String confirmPassword = ctx.formParam("confirm_password");

        if (password.equals(confirmPassword))
        {
            try
            {
                User user = UserMapper.createUser(email, password);
                ctx.sessionAttribute("currentUser", user);
                ctx.attribute("message", "Du er hermed oprettet med brugernavn: " + email +
                        ". Nu skal du logge på.");
                ctx.render("registerInfo.html", Map.of("currentUSer", user));
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
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try
        {
            User user = UserMapper.login(email, password);
            ctx.sessionAttribute("currentUser", user);
            ctx.attribute("currentUser", user);

           if(UserMapper.checkIfAdmin(user) == 1)
            {
                ctx.sessionAttribute("admin", true);
                ctx.attribute("message", "Du er nu logget ind som admin.");

                ctx.redirect("/adminIndex");
                ctx.render("adminPages/adminIndex.html", Map.of("message", "Du er nu logget ind som admin."));
            }
            else
            {
                ctx.sessionAttribute("admin", false);
                ctx.sessionAttribute("loginMessage", "Du er nu logget ind");
                ctx.redirect("/profile-page");
                ctx.render("/profile-page", Map.of("loginMessage", "Du er nu logget ind"));
            }
        }
        catch (DatabaseException e)
        {
            ctx.sessionAttribute("errorLogin", "login fejlede!");
            System.out.println("login logs errors");
            ctx.redirect("/login");
            ctx.render("/login",Map.of("errorLogin", "login fejlede!"));
        }
    }


    public static void registerInfo(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        String firstName = ctx.formParam("first_name");
        String lastName = ctx.formParam("last_name");
        String streetName = ctx.formParam("street_name");
        String floor = ctx.formParam("floor");
        int zipCode = Integer.parseInt(ctx.formParam("post_code"));
        int streetNumber = Integer.parseInt(ctx.formParam("street_number"));
        User user = ctx.sessionAttribute("currentUser");
        int userId = user.getId();

        user = UserMapper.updateUser(userId,firstName,lastName,zipCode,streetName,streetNumber,floor,connectionPool);

        ctx.sessionAttribute("currentUser",user);
        ctx.sessionAttribute("message","Du har opdateret din profil !");
        ctx.render("index.html");
    }
    
}
