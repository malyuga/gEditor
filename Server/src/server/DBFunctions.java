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
        statement.setLong(1, (Long) args.get(0));
        statement.setString(2, (String) args.get(1));
        statement.setDate(3, new Date((Long)args.get(2)));
        statement.setString(4, (String) args.get(3));
        statement.registerOutParameter(1, Types.BIGINT);
        statement.executeUpdate();
        long retVal = statement.getLong(1);
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
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Node WHERE G_ID = ?");
        statement.setLong(1, (Long) args.get(0));
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }
////////////////////////              ///////////////////////////////////////
    public ResultSet get_edges(ArrayList<Object> args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM EDGE WHERE G_ID = ? AND S_ID = ?");
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
        statement.setLong(1, (Long) args.get(0));
        statement.setLong(2, (Long) args.get(1));
        statement.setLong(3, (Long) args.get(2));
        statement.setString(4, (String) args.get(3));
        statement.setString(5, (String) args.get(4));
        try{
            statement.setDouble(6, (Double) args.get(5));
        }
        catch (Exception ex){
            statement.setLong(6, (Long) args.get(5));
        }
        try{
            statement.setDouble(7, (Double) args.get(5));
        }
        catch (Exception ex){
            statement.setLong(7, (Long) args.get(6));
        }
        statement.registerOutParameter(1, Types.BIGINT);
        statement.executeUpdate();
        long retVal = statement.getLong(1);
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

