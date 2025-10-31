package app.entities;

public class DiscountCode
{
    private float discountId;
    private String discountCode;
    private float discount;


    public DiscountCode(float discountId, String discountCode, float discount)
    {
        this.discountId = discountId;
        this.discountCode = discountCode;
        this.discount = discount;
    }

    public DiscountCode(String discountCode)
    {
        this.discountCode = discountCode;
    }

    public float convertToId(){
    return discountId;
    }

    public float getDiscountPercentage()
    {
        return discount;
    }


    public float  getDiscountId()
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

    public float getDiscount()
    {
        return discount;
    }

    public void setDiscount(int discount)
    {
        this.discount = discount;
    }
}
