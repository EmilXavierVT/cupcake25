package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminController {

    private ConnectionPool connectionPool;

    public AdminController(ConnectionPool connectionPool)
    {
        this.connectionPool = connectionPool;
    }

    public static void addRoutes(Javalin app)
    {
    }
}
