package app.entities;

public class Icing
{
    private int icingId;
    private String icingName;
    private float icingPrice;

    public Icing(int icingId, String icingName, float icingPrice)
    {
        this.icingId = icingId;
        this.icingName = icingName;
        this.icingPrice = icingPrice;
    }

    public Icing()
    {
    }

    public int getIcingId()
    {
        return icingId;
    }



    public String getIcingName()
    {
        return icingName;
    }

    public void setIcingName(String icingName)
    {
        this.icingName = icingName;
    }

    public float getIcingPrice()
    {
        return icingPrice;
    }

    public void setIcingPrice(float icingPrice)
    {
        this.icingPrice = icingPrice;
    }
}
