package app.entities;

public class CupcakeInOrder
{
    private int orderId;
    private UserDefinedCupcake udc;
    private int amount;


    public CupcakeInOrder(int orderId, UserDefinedCupcake udc, int amount)
    {
        this.orderId = orderId;
        this.udc = udc;
        this.amount = amount;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public UserDefinedCupcake getUdc()
    {
        return udc;
    }

    public void setUdc(UserDefinedCupcake udc)
    {
        this.udc = udc;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }




}


