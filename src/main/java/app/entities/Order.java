package app.entities;

import app.exceptions.DatabaseException;
import app.persistence.UserMapper;

import java.time.LocalDate;

public class Order
{
    private int id;
    private int userId;
    private LocalDate date;
    private Integer appliedDiscount;
    User user;

    public Order(int id, int userId, LocalDate date, Integer appliedDiscount)
    {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.appliedDiscount = appliedDiscount;
        setUser(userId);

    }

    public Order(int userId, LocalDate date, Integer appliedDiscount)
    {
        this.userId = userId;
        this.date = date;
        this.appliedDiscount = appliedDiscount;
        setUser(userId);
    }
    private void setUser(int userId){
        try {
            this.user = UserMapper.getUser(userId);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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
}
