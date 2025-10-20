package app.entities;

public class Bottoms
{
    private int bottomId;
    private String bottomName;
    private float bottomPrice;

    public Bottoms()
    {}

    public Bottoms(int bottomId, String bottomName, float bottomPrice) {
        this.bottomId = bottomId;
        this.bottomName = bottomName;
        this.bottomPrice = bottomPrice;
    }

    public int getBottomId()
    {
        return bottomId;
    }

    public void setBottomId(int bottomId)
    {
        this.bottomId = bottomId;
    }

    public String getBottomName()
    {
        return bottomName;
    }

    public void setBottomName(String bottomName)
    {
        this.bottomName = bottomName;
    }

    public float getBottomPrice()
    {
        return bottomPrice;
    }

    public void setBottomPrice(float bottomPrice)
    {
        this.bottomPrice = bottomPrice;
    }
}
