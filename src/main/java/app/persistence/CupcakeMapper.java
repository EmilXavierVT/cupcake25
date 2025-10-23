package app.persistence;

import app.entities.Bottom;
import app.entities.CupcakeInOrder;
import app.entities.Icing;
import app.entities.UserDefinedCupcake;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {
    private ConnectionPool connectionPool;

    private final String GET_ALL_BOTTOMS = "SELECT * FROM the_bottoms";
    private final String GET_ALL_ICINGS = "SELECT * FROM icing";



    public List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        List<Bottom> bottomList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_ALL_BOTTOMS)) {

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int bottomId = rs.getInt("bottom_id");
                    String bottomName = rs.getString("bottom_name");
                    float bottomPrice = rs.getFloat("bottom_price");
                    bottomList.add(new Bottom(bottomId, bottomName, bottomPrice));
                }

        } catch (SQLException e) {
            throw new DatabaseException("something database", e.getMessage());
        }
        return bottomList;
    }

    public List<Icing> getAllIcings(ConnectionPool connectionPool) throws DatabaseException {
        List<Icing> icingList = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_ALL_ICINGS)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int icingId = rs.getInt("icing_id");
                    String icingName = rs.getString("icing_name");
                    float icingPrice = rs.getFloat("icing_price");
                    icingList.add(new Icing(icingId, icingName, icingPrice));
                }
            } catch (SQLException e) {
            throw new DatabaseException("something database", e.getMessage());
    }
        return icingList;
    }

    public Bottom getBottomById(int id, ConnectionPool connectionPool) throws SQLException {
        Bottom bottom = null;
        String sql = "SELECT * FROM the_bottoms WHERE bottom_id = ?";

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String bottomName = rs.getString("bottom_name");
                    float bottomPrice = rs.getFloat("bottom_price");
                    bottom = new Bottom(id, bottomName, bottomPrice);
                }
            }
        return bottom;
    }

    public Icing getIcingById(int id, ConnectionPool connectionPool) throws SQLException {
        Icing icing = null;
        String sql = "SELECT * FROM icing WHERE icing_id = ?";

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String icingName = rs.getString("icing_name");
                    float icingPrice = rs.getFloat("icing_price");
                    icing = new Icing(id, icingName, icingPrice);
                }
            }
        return icing;
    }
    public UserDefinedCupcake saveUserDefinedCupcake(int bottom, int icing, ConnectionPool connectionPool) throws DatabaseException {

        UserDefinedCupcake userDefinedCupcake = null;
        String sql = "INSERT INTO user_defined_cupcakes (udc_bottom, udc_icing) VALUES (?, ?) RETURNING udc_id";

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, bottom);
                ps.setInt(2, icing);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("udc_id");
                    Bottom bottomId = getBottomById(bottom, connectionPool);
                    Icing icingId = getIcingById(icing, connectionPool);

                    userDefinedCupcake = new UserDefinedCupcake(id, bottomId, icingId);
                }
        } catch (SQLException e) {
            throw new DatabaseException("Something something database",e.getMessage());
        }
        return userDefinedCupcake;
    }
    public UserDefinedCupcake getUserDefinedCupcakeById(int id, ConnectionPool connectionPool) throws DatabaseException, SQLException {
        UserDefinedCupcake userDefinedCupcake = null;
        String sql = "SELECT * FROM user_defined_cupcakes WHERE udc_id = ?";

        try(Connection connection = connectionPool.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int bottomId = rs.getInt("udc_bottom");
                int icingId = rs.getInt("udc_icing");
                Bottom bottom = getBottomById(bottomId, connectionPool);
                Icing icing = getIcingById(icingId, connectionPool);
                userDefinedCupcake = new UserDefinedCupcake(id, bottom, icing);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Something something database",e.getMessage());
        }
        return userDefinedCupcake;
    }


    public void saveCupcakeInOrder(int order, int udcId, int amount, ConnectionPool connectionPool) throws DatabaseException {

        CupcakeInOrder cupcakeInOrder = null;
        String sql = "INSERT INTO cupcakes_in_a_order(order_id, udc_id, amount) VALUES (?, ?, ?) RETURNING order_id";

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, order);
                ps.setInt(2, udcId);
                ps.setInt(3, amount);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("order_id");
                    Bottom bottomId = getUserDefinedCupcakeById(udcId,connectionPool).getBottom();
                    Icing icingId = getUserDefinedCupcakeById(udcId,connectionPool).getIcing();

                    cupcakeInOrder = new CupcakeInOrder(order, new UserDefinedCupcake(id, bottomId, icingId), amount);
                    System.out.println(cupcakeInOrder);
                }
        } catch (SQLException e) {
            throw new DatabaseException("Something something database",e.getMessage());
        }
    }


}
