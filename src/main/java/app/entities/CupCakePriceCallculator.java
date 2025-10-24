package app.entities;

public class CupCakePriceCallculator {
    int amount;
    int bottomPrice;
    int icingPrice;

    public CupCakePriceCallculator(int amount, int bottomPrice, int icingPrice) {
        this.amount = amount;
        this.bottomPrice = bottomPrice;
        this.icingPrice = icingPrice;
    }

    @Override
    public String toString() {
        return "amount:  " + amount + ", bottomPrice: " + bottomPrice + ", icingPrice: " + icingPrice;
    }
public int getAmount(){
        return amount;
}
public int getBottomPrice(){
        return bottomPrice;
}
public int getIcingPrice(){
        return icingPrice;
}
}
