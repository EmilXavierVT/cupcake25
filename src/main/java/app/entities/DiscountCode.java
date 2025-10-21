package app.entities;

public class DiscountCode
{
    private int discountId;
    private String discountCode;
    private int discount;


    public DiscountCode(String discountCode, int discount)
    {
        this.discountCode = discountCode;
        this.discount = discount;
    }

    public int getDiscountId()
    {
        return discountId;
    }



    public String getDiscountCode()
    {
        return discountCode;
    }

    public void setDiscountCode(String discountCode)
    {
        this.discountCode = discountCode;
    }

    public int getDiscount()
    {
        return discount;
    }

    public void setDiscount(int discount)
    {
        this.discount = discount;
    }
}
