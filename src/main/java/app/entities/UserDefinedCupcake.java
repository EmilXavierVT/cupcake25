package app.entities;

public class UserDefinedCupcake {
    private Bottom bottom;
    private Icing icing;
    private int id;

    public UserDefinedCupcake(int id, Bottom bottom, Icing icing) {
        this.bottom = bottom;
        this.icing = icing;
        this.id =id;
    }


    public UserDefinedCupcake(Bottom bottom, Icing icing) {
        this.bottom = bottom;
        this.icing = icing;
    }

    public Bottom getBottom() {
        return bottom;
    }

    public void setBottom(Bottom bottom) {
        this.bottom = bottom;
    }

    public Icing getIcing() {
        return icing;
    }

    public void setIcing(Icing icing) {
        this.icing = icing;
    }
    public int getId() {
        return id;
    }
}
