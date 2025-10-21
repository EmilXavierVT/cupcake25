package app.entities;

public class CupcakeInOrder
{
    private int orderId;
    private int udcId;
    private int amount;

    public CupcakeInOrder()
    {
        this.amount = 1;
    }

    public CupcakeInOrder(int orderId, int udcId, int amount)
    {
        this.orderId = orderId;
        this.udcId = udcId;
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

    public int getUdcId()
    {
        return udcId;
    }

    public void setUdcId(int udcId)
    {
        this.udcId = udcId;
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
