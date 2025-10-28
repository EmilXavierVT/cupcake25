package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;


public class AdminMapper
{
    public static ArrayList<CupcakeInOrder> findDailySales(ConnectionPool connectionPool) throws DatabaseException
    {
        LocalDate date = LocalDate.now();
        ArrayList<CupcakeInOrder> cupcakeList = new ArrayList<>();
        String sql = "SELECT * FROM orders " +
                "JOIN cupcakes_in_a_order ON id = order_id " +
                "JOIN user_defined_cupcakes USING(udc_id) " +
                "JOIN icing on udc_icing = icing_id " +
                "JOIN the_bottoms on udc_bottom = bottom_id " +
                "WHERE orders.date = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
        ps.setDate(1, java.sql.Date.valueOf(date));
        ResultSet rs = ps.executeQuery();

        while(rs.next())
        {
            int amount = rs.getInt("amount");
            int icingId = rs.getInt("icing_id");
            int orderId = rs.getInt("order_id");
            int bottomId = rs.getInt("bottom_id");
            int icingPrice = rs.getInt("icing_price");
            int bottomPrice = rs.getInt("bottom_price");
            String icingName = rs.getString("icing_name");
            String bottomName = rs.getString("bottom_name");

            UserDefinedCupcake cupcakeHolder = new UserDefinedCupcake(amount, new Bottom(bottomId,bottomName,bottomPrice),new Icing(icingId,icingName,icingPrice));
            cupcakeList.add(new CupcakeInOrder(orderId,cupcakeHolder,amount));
        }
        } catch (SQLException e)
        {
            throw new DatabaseException("AdminMapper findDailySales" ,e.getMessage());
        }
        return  cupcakeList;
        }


        public static int calulateDailySales(ArrayList<CupcakeInOrder> priceList)
        {
        int totalDailySales = 0;

        for(CupcakeInOrder c : priceList)
        {
            totalDailySales += (int) ((c.getUdc().getBottom().getBottomPrice() +  c.getUdc().getIcing().getIcingPrice()) * c.getAmount());
        }
        return totalDailySales;
        }


        public static HashMap<Integer, User> findMostActiveUsers(ConnectionPool connectionPool) throws DatabaseException
        {
        int count = 6;
        HashMap<Integer, User> mostActiveUsers = new HashMap<>();
        String sql = "SELECT COUNT(users.id) AS Total_orders_by_user, users.id " +
            "FROM public.orders " +
            "JOIN users ON  users.id = user_id "+
            "GROUP BY users.id " +
            "ORDER BY Total_orders_by_user";

            try (Connection connection = connectionPool.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = ps.executeQuery();

                while(rs.next() && count > 0)
                {
                    int numberOfOrdersByUser = rs.getInt("Total_orders_by_user");
                    int userId = rs.getInt("id");
                    count--;

                    mostActiveUsers.put(numberOfOrdersByUser,UserMapper.getUser(userId));
                }

            } catch (SQLException e)
            {
                throw new DatabaseException("AdminMapper findMostActiveUsers" ,e.getMessage());
            }
            return mostActiveUsers;
        }
    }

