package app.entities;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.UserMapper;

import java.time.LocalDate;
import java.util.ArrayList;


public class Order
{
    private int orderId;
    private int userId;
    private LocalDate date;
    private Integer appliedDiscount;
    User user;
    ArrayList<CupcakeInOrder> cupcakesInOrder = new ArrayList<CupcakeInOrder>();

    public Order(int orderId, int userId, LocalDate date, Integer appliedDiscount)
    {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.appliedDiscount = appliedDiscount;
        setUser(userId);
        setCupcakesInOrder();

    }

    public Order(int userId, LocalDate date, Integer appliedDiscount)
    {
        this.userId = userId;
        this.date = date;
        this.appliedDiscount = appliedDiscount;
        setUser(userId);
        setCupcakesInOrder();
    }
    private void setUser(int userId){
        try {
            this.user = UserMapper.getUser(userId);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    private void setCupcakesInOrder(){
        CupcakeMapper cupcakeMapper = new CupcakeMapper();
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            this.cupcakesInOrder = cupcakeMapper.getCupcakesInOrder(this.orderId, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public Integer getAppliedDiscount()
    {
        return appliedDiscount;
    }

    public void setAppliedDiscount(Integer appliedDiscount)
    {
        this.appliedDiscount = appliedDiscount;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<CupcakeInOrder> getCupcakesInOrder() {
        return cupcakesInOrder;
    }
}

