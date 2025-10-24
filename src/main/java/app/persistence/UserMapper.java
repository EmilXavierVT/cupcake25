package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;

public class UserMapper
{
    public static User createUser(String email, String password) throws DatabaseException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        String sql = "INSERT INTO users (email, password) " +
                "VALUES (?, ?) RETURNING id";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getUser(rs.getInt(1));
            } else {
                throw new DatabaseException("Failed to create new user");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error creating user", e.getMessage());
        }
    }

    public static User getUser(int id) throws DatabaseException
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if ( rs.next() )
            {
                return new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("zip_code"),
                        rs.getString("street_name"),
                        (Integer) rs.getObject("house_number"),
                        rs.getString("floor"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getFloat("wallet")
                );
            } else
            {
                throw new DatabaseException("No user found with ID: " + id);
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Error retrieving user", e.getMessage());
        }
    }

    public static void updateUser(int id, String firstName, String lastName, int zipCode,
                                  String streetName, Integer houseNumber, String floor
                                  ) throws DatabaseException
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        String sql = "UPDATE users SET first_name=?, last_name=?, zip_code=?, street_name=?, " +
                "house_number=?, floor=? WHERE id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setInt(3, zipCode);
            ps.setString(4, streetName);
            ps.setObject(5, houseNumber);
            ps.setString(6, floor);


            int rowsAffected = ps.executeUpdate();
            if ( rowsAffected != 1 )
            {
                throw new DatabaseException("Failed to update user with ID: " + id);
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Error updating user", e.getMessage());
        }
    }

    public static void deleteUser(int id) throws DatabaseException
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        String sql = "DELETE FROM users WHERE id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Failed to delete user with ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user", e.getMessage());
        }
    }

    public static User login(String email, String password) throws DatabaseException
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        String sql = "select id from users where email=? and password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if ( rs.next() )
            {
                int id = rs.getInt("id");

                return getUser(id);
            } else
            {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }


    public static int checkIfAdmin(User user) throws DatabaseException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        String sql = "SELECT * FROM roles WHERE user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {

                    int role = rs.getInt("roles");
                    return role;

            }
        } catch (SQLException e) {
            throw new DatabaseException("something admin login", e.getMessage());
        }
        return 0;
    }

      public static void insertMoney (String email, float amount) throws DatabaseException
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        String sql = "UPDATE users SET wallet = wallet + ? WHERE email = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, email);
            ps.setFloat(2, amount);

            int rowsAffected = ps.executeUpdate();
            if ( rowsAffected != 1 )
            {
                throw new DatabaseException("Failed to update user with ID: " + email);
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Error updating user", e.getMessage());
        }
    }

}