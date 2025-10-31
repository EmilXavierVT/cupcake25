package app.controllers;

import app.entities.CupcakeInOrder;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;
import java.util.stream.Collectors;

public class AdminController
{
 public static void addRoutes(Javalin app) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        app.post("/insertMoney", ctx -> {insertMoney(ctx, connectionPool);});
        app.post("/delete_order", ctx -> deleteOrder(ctx, connectionPool));
        app.post("/search_customer", ctx -> inSearchCustomer(ctx));
        app.post("/search_orders", ctx -> inSearchOrders(ctx));

        app.get("/admin-customer-page", ctx ->
        {
            ctx.sessionAttribute("all_users", UserMapper.getAllUsers(connectionPool));
            ctx.render("adminPages/admin-customer-page.html", Map.of("all_users", UserMapper.getAllUsers(connectionPool)));
        });

        app.get("/admin-order-page", ctx -> {
            ctx.sessionAttribute("all_orders", getAllOrders(ctx, connectionPool));
            ctx.render("adminPages/admin-order-page.html", Map.of("all_orders", getAllOrders(ctx, connectionPool)));

        });
        app.get("/adminIndex", ctx ->
        {
            ctx.render("adminPages/adminIndex.html");
            getTodaySalesNumber(ctx, connectionPool);
            getLastSevenDaysOrders(ctx, connectionPool);
            getTopUsers(ctx, connectionPool);
            getTotalSales(ctx, connectionPool);
        });

    }


    public static void insertMoney(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        String email = ctx.formParam("email");
        float amount = Float.parseFloat(ctx.formParam("amount"));

        try
        {
            UserMapper.insertMoney(email, amount, connectionPool);
            ctx.sessionAttribute("gift_card_message", "Du har nu indsat penge på brugers konto !");
            ctx.redirect("/adminIndex");
        } catch (DatabaseException e) {
            ctx.redirect("/adminIndex");
            ctx.sessionAttribute("gift_card_message", "Forkerte email, valgte kunde findes ikke !");
        }
    }


    private static void deleteOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        int orderId = Integer.parseInt(ctx.formParam("order_id"));
        new OrderMapper().removeOrder(orderId, connectionPool);
        List<Order> updatedOrders = getAllOrders(ctx, connectionPool);
        ctx.sessionAttribute("all_orders", updatedOrders);
        ctx.redirect("/admin-order-page");
        ctx.render("adminPages/admin-order-page.html", Map.of("all_orders", updatedOrders));
    }


    private static List<Order> getAllOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try
        {
            List<Order> allOrders = new OrderMapper().getAllOrders(connectionPool);

            allOrders = allOrders.stream()
                    .collect(Collectors
                            .collectingAndThen(Collectors.toList(), list -> {
                                Collections.reverse(list);
                                return list;
                            }));

            return allOrders;
        } catch (DatabaseException e)
        {
            throw new DatabaseException("GetAllOrders exception", e.getMessage());
        }
    }


    private static void inSearchCustomer(Context ctx)
    {
        List<User> users = ctx.sessionAttribute("all_users");
        List<User> userInSearch;
        String search = ctx.formParam("search_text");

        userInSearch = users.stream()
                .filter(user ->
                {
                    assert search != null;
                    return user.getEmail().contains(search);
                })
                .toList();

        ctx.sessionAttribute("users_in_search", userInSearch);
        ctx.redirect("/admin-customer-page");
        ctx.render("adminPages/admin-customer-page.html", Map.of(
                "users_in_search", userInSearch));
    }


    private static void inSearchOrders(Context ctx)
    {
        List<Order> orders = ctx.sessionAttribute("all_orders");
        List<Order> orderInSearch;
        String search = ctx.formParam("search_text");

        orderInSearch = orders.stream()
                .filter(order ->
                {
                    assert search != null;
                    return order.getUser().getEmail().contains(search);
                })
                .toList();

        ctx.sessionAttribute("orders_in_search", orderInSearch);
        ctx.redirect("/admin-order-page");
        ctx.render("adminPages/admin-order-page.html", Map.of(
                "orders_in_search", orderInSearch));
    }


    private static void getTodaySalesNumber(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try
        {
            int dailysale = AdminMapper.calulateDailySales(AdminMapper.findDailySales(connectionPool));

            ctx.sessionAttribute("today_sales", dailysale);
            ctx.render("adminPages/adminIndex.html", Map.of(
                    "today_sales", Objects.requireNonNull(ctx.sessionAttribute("today_sales"))
            ));

        } catch (DatabaseException e)
        {
            throw new DatabaseException("something when getting today sales number", e.getMessage());
        }
    }


    private static void getLastSevenDaysOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        List<Order> last7DaysOrder = new OrderMapper().getOrdersLastSevenDays(connectionPool);

        ctx.sessionAttribute("orders_of_seven_days", last7DaysOrder);
        ctx.render("adminPages/adminIndex.html", Map.of(
                "orders_of_seven_days", last7DaysOrder
        ));
    }


    private static void getTotalSales(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        float totalSales = 0;

        try
        {
            List<Order> allOrders = new OrderMapper().getAllOrders(connectionPool);

            for (Order order : allOrders)
            {
                for (CupcakeInOrder cupcake : order.getCupcakesInOrder())
                {
                    totalSales += (cupcake.getUdc().getBottom().getBottomPrice() +
                            cupcake.getUdc().getIcing().getIcingPrice() * cupcake.getAmount());
                }
            }

            ctx.sessionAttribute("total_sales", totalSales);
            ctx.render("adminPages/adminIndex.html", Map.of(
                    "total_sales", Objects.requireNonNull(ctx.sessionAttribute("total_sales"))
            ));

        } catch (DatabaseException e)
        {
            throw new DatabaseException("something in all orders" + e.getMessage());
        }
    }


    private static void getTopUsers(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try
        {
                        HashMap<Integer, User> topUsers = AdminMapper.findMostActiveUsers(connectionPool);
            List<Integer> topUserPurchaseAmounts = new ArrayList<>();
            List<User> topUserObjects = new ArrayList<>();
            int i = 0;

            for (Map.Entry<Integer, User> entry : topUsers.entrySet())
            {
                if (i >= 6) break;
                topUserPurchaseAmounts.add(entry.getKey());
                topUserObjects.add(entry.getValue());
                i++;
            }
            topUserPurchaseAmounts = topUserPurchaseAmounts.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list ->
                    {
                        Collections.reverse(list);
                        return list;
                    }));
            topUserObjects = topUserObjects.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list ->
                    {
                        Collections.reverse(list);
                        return list;
                    }));
            // Example: pass lists to session attributes or use as needed
            ctx.sessionAttribute("top_user_amount", topUserPurchaseAmounts);
            ctx.sessionAttribute("top_users", topUserObjects);
            ctx.render("adminPages/adminIndex.html", Map.of("top_user_amount", topUserPurchaseAmounts));
            ctx.render("adminPages/adminIndex.html", Map.of("top_users", topUserObjects));
        } catch (DatabaseException e)
        {
            throw new DatabaseException("something when getting top users", e.getMessage());
        }
    }
}
