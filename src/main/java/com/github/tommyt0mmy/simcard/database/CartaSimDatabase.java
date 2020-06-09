package com.github.tommyt0mmy.simcard.database;

import com.github.tommyt0mmy.simcard.SimCard;
import com.github.tommyt0mmy.simcard.cardmanager.CardData;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class CartaSimDatabase
{
    static SimCard simCardClass = SimCard.getInstance();

    static private String url = "jdbc:sqlite:" + simCardClass.getDataFolder().getAbsolutePath() + "\\database.db";

    static Connection c;
    public static Connection getConn() throws SQLException {
        if(c == null || c.isClosed()) {
            c = DriverManager.getConnection(url);
        }
        return c;
    }

    public CartaSimDatabase()
    {
        setup();
    }

    private void setup()
    {
        // SIM Cards table setup
        String sql = "CREATE TABLE IF NOT EXISTS cards("
                + "id INT NOT NULL PRIMARY KEY,"
                + "type VARCHAR(255) NOT NULL,"
                + "remaining_messages INT NOT NULL"
                + ");";

        executeStatement(sql);

        sql = "CREATE TABLE IF NOT EXISTS sim_owners(" +
                "owner_uuid VARCHAR(255) NOT NULL," +
                "id INT NOT NULL);";

        executeStatement(sql);
    }

    public void addSimOwner(int id, UUID owner_uuid)
    {
        String sql = "INSERT INTO sim_owners(" +
                "owner_uuid, " +
                "id) " +
                "VALUES(?, ?)";

        try(Connection connection = getConn(); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setString(1, owner_uuid.toString());
            pstmt.setInt(2, id);

            pstmt.execute();

            pstmt.close();
        } catch(SQLException e) {simCardClass.console.severe(e.getMessage() + " (addSimOwner)");}

    }

    public void removeSimOwner(UUID owner_uuid)
    {
        String sql = "DELETE FROM sim_owners " +
                "WHERE owner_uuid = ?";

        try (Connection connection = getConn(); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setString(1, owner_uuid.toString());

            pstmt.execute();

            pstmt.close();

        } catch (SQLException e) {simCardClass.console.severe(e.getMessage() + " (removeCard)");}
    }

    /*   UNSTABLE METHOD
    public Optional<UUID> getSimOwner(int id)
    {
        String sql = "SELECT owner_uuid " +
                "FROM sim_owners " +
                "WHERE id = ?";

        try(Connection connection = DriverManager.getConnection(url); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next())
                return Optional.empty();

            String uuid_string = rs.getString("owner_uuid");

            pstmt.close();
            rs.close();

            return Optional.of(UUID.fromString(uuid_string));
        } catch (SQLException e) {simCardClass.console.severe(e.getMessage() + " (getSimOwner)");}
        return Optional.empty();
    }
     */

    public Optional<Integer> getOwnedSimId(UUID uuid)
    {
        String sql = "SELECT id " +
                "FROM sim_owners " +
                "WHERE owner_uuid = ?";

        try(Connection connection = getConn(); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setString(1, uuid.toString());

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next())
                return Optional.empty();

            int id = rs.getInt("id");

            rs.close();
            pstmt.close();

            return Optional.of(id);
        } catch (SQLException e) {simCardClass.console.severe(e.getMessage() + " (getOwnedSim)");}
        return Optional.empty();
    }

    public Optional<CardData> addCard(CardData cardData)
    {
        String sql = "INSERT INTO cards(" +
                "id, " +
                "type, " +
                "remaining_messages) " +
                "VALUES(?, ?, ?)";

        try(Connection connection = getConn(); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            //Generate new id
            String getMaxIdQuery = "SELECT MAX(id) AS id FROM cards";
            Statement stmt2 = connection.createStatement();
            ResultSet maxId_rs = stmt2.executeQuery(getMaxIdQuery);
            int newId = 0;
            if (maxId_rs.next())
                newId = maxId_rs.getInt("id") + 1;

            cardData.setId(newId);

            pstmt.setInt(1, cardData.getId()); //id
            pstmt.setString(2, cardData.getType()); //type
            pstmt.setInt(3, cardData.getRemaining_messages());

            pstmt.execute();

            pstmt.close();
            maxId_rs.close();

            return Optional.of(cardData);
        } catch(SQLException e) {simCardClass.console.severe(e.getMessage() + " (addCard)");}
        return Optional.empty();
    }

    public void updateRemainingMessages(int id, int new_remaining_messages)
    {
        String sql = "UPDATE cards " +
                "SET remaining_messages = ? " +
                "WHERE id = ?";

        try(Connection connection = getConn(); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, new_remaining_messages);
            pstmt.setInt(2, id);

            pstmt.execute();
            pstmt.close();

        } catch(SQLException e) {simCardClass.console.severe(e.getMessage() + " (updateRemainingMessages)");}
    }

    public void removeCard(int id)
    {
        String sql = "DELETE FROM cards " +
                "WHERE id = ?";

        try (Connection connection = getConn(); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, id);

            pstmt.execute();

            pstmt.close();

        } catch (SQLException e) {simCardClass.console.severe(e.getMessage() + " (removeCard)");}
    }

    public Optional<CardData> getCardData(int id)
    {
        String sql = "SELECT type, " +
                "remaining_messages " +
                "FROM cards " +
                "WHERE id = ?";

        try(Connection connection = getConn(); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next())
                return Optional.empty();

            String type = rs.getString("type");
            int remaining_messages = rs.getInt("remaining_messages");

            CardData cd = new CardData(id, type, remaining_messages);

            pstmt.close();

            return Optional.of(cd);
        } catch (SQLException e) {simCardClass.console.severe(e.getMessage() + " (getCardData)");}
        return Optional.empty();
    }

    private void executeStatement(String sql)
    {
        try (Connection connection = getConn(); Statement stmt = connection.createStatement())
        {
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {simCardClass.console.severe(e.getMessage() + " (" + sql + ")");}
    }

    public void closeConnection()
    {
        try
        {
            c.close();
        } catch (SQLException e) {simCardClass.console.severe(e.getMessage());}
    }
}
