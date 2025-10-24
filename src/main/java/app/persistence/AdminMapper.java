package app.persistence;

import app.entities.CupCakePriceCallculator;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class AdminMapper {



    public static ArrayList<CupCakePriceCallculator> findDailySales(ConnectionPool connectionPool) throws SQLException {
        LocalDate date = LocalDate.now();
        ArrayList<CupCakePriceCallculator> priceList = new ArrayList<CupCakePriceCallculator>();

        String sql = "SELECT amount,bottom_price,icing_price FROM orders " +
                "JOIN cupcakes_in_a_order ON id = order_id " +
                "JOIN user_defined_cupcakes USING(udc_id) " +
                "JOIN icing on udc_icing = icing_id " +
                "JOIN the_bottoms on udc_bottom = bottom_id " +
                "WHERE orders.date = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

        ps.setDate(1, java.sql.Date.valueOf(date));
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int amount = rs.getInt("amount");
            int bottomPrice = rs.getInt("bottom_price");
            int icingPrice = rs.getInt("icing_price");

            priceList.add(new CupCakePriceCallculator(amount, bottomPrice, icingPrice));
        }
        }


        return  priceList;

        }

        public static int calulateDailySales(ArrayList<CupCakePriceCallculator> priceList){

        int totalDailySales = 0;
        for(CupCakePriceCallculator c : priceList){
            totalDailySales += c.getAmount() * (c.getBottomPrice() + c.getIcingPrice());
        }
        return totalDailySales;
        }
    }

