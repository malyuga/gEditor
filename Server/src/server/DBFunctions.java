package server;

import org.json.simple.JSONArray;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;

public class DBFunctions{


    Connection connection = Server.getConnection();
/////////////////////////                  /////////////////////////////
    public Long new_graph(JSONArray args) throws SQLException, ParseException {
        return new_graph((ArrayList<Object>) args);
    }

    public Long new_graph(ArrayList<Object> args) throws SQLException, ParseException {
        CallableStatement statement = connection.prepareCall("{call NEW_GRAPH(?, ?, ?, ?)}");
        statement.setLong("OUT_ID", (Long) args.get(0));
        statement.setString("IN_NAME", (String) args.get(1));
        statement.setDate("IN_LAST_CHANGE", new Date((Long)args.get(2)));
        statement.setString("IN_INFO", (String) args.get(3));
        statement.registerOutParameter("OUT_ID", Types.BIGINT);
        statement.executeUpdate();
        long retVal = statement.getLong("OUT_ID");
        return retVal;
    }

////////////////////////                  ////////////////////////////////
    public ResultSet get_node(JSONArray args) throws SQLException {
        return get_node((ArrayList<Object>) args);
    }

    //////////////////////             ////////////////////////////////////
    public ResultSet get_edges(JSONArray args) throws SQLException {
        return get_edges((ArrayList<Object>) args);
    }

/////////////////////////                ////////////////////////////////
    public ResultSet get_node(ArrayList<Object> args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Nodes WHERE G_ID = ?");
        statement.setLong(1, (Long) args.get(0));
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }
////////////////////////              ///////////////////////////////////////
    public ResultSet get_edges(ArrayList<Object> args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM EDGE WHERE G_ID = ? AND START_NODE_ID = ?");
        statement.setLong(1, (Long) args.get(0));
        statement.setLong(2, (Long) args.get(1));
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

///////////////////////                //////////////////////////////////////
    public ResultSet get_graph() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Graph");
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public void new_edge(JSONArray args) throws SQLException {
        new_edge((ArrayList<Object>) args);
    }
    public void new_edge(ArrayList<Object> args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO EDGE VALUES(?, ?, ?, ?)");
        statement.setLong(1, (Long) args.get(0));
        statement.setLong(2, (Long) args.get(1));
        statement.setLong(3, (Long) args.get(2));
        statement.setDouble(4, (Double) args.get(3));
        statement.executeUpdate();
    }

    public Long new_node(JSONArray args) throws SQLException {
        return new_node((ArrayList)args);
    }
    public Long new_node(ArrayList<Object> args) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call NEW_NODE(?, ?, ?, ?, ?, ?, ?)}");
        statement.setLong("IN_ID", (Long) args.get(0));
        statement.setLong("IN_G_ID", (Long) args.get(1));
        statement.setLong("IN_NUMB", (Long) args.get(2));
        statement.setString("IN_NAME", (String) args.get(3));
        statement.setString("IN_INFO", (String) args.get(4));
        try{
            statement.setDouble("IN_X", (Double) args.get(5));
        }
        catch (Exception ex){
            statement.setLong("IN_X", (Long) args.get(5));
        }
        try{
            statement.setDouble("IN_Y", (Double) args.get(5));
        }
        catch (Exception ex){
            statement.setLong("IN_Y", (Long) args.get(6));
        }
        statement.registerOutParameter("IN_ID", Types.BIGINT);
        statement.executeUpdate();
        long retVal = statement.getLong("IN_ID");
        return retVal;
    }

    public void delete_graph(JSONArray args) throws SQLException {
        delete_graph((ArrayList)args);
    }
    public void delete_graph(ArrayList<Object> args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM GRAPH WHERE ID = ?");
        statement.setLong(1, (Long) args.get(0));
        statement.executeUpdate();
    }

}

