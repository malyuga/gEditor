package server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 1 on 6/2/2015.
 */

//creates new socket to data transfer between client and server

public class ServerThread extends Thread {
    private Socket socket;
    private int num;

    public ServerThread(int num, Socket socket) {
        this.num = num;
        this.socket = socket;

        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    private JSONObject dbFunctionsAsJSON(DBFunctions dbFunctions){
        Method[] methods = dbFunctions.getClass().getDeclaredMethods();
        String name = null;
        String retValType = null;
        String[] params = null;
        JSONObject json = null;
        JSONObject jsonFuntions = new JSONObject();
        JSONArray paramsArr = null;
        int j = 0;
        for(Method method : methods) {
            name = method.getName();
            retValType = method.getReturnType().getName();
            if(method.getParameters().length>0){
                paramsArr = new JSONArray();
                params = new String[method.getParameters().length];
                for (int i = 0; i < method.getParameters().length; ++i) {
                    params[i] = new String(method.getParameters()[i] != null ? method.getParameters()[i].getType().getName() : "null");
                    paramsArr.add(params[i]);
                }
            }
            else{
                params = null;
            }

            json = new JSONObject();
            json.put("name", name);
            if(params==null){
                json.put( "params", null);
            }
            else{
                json.put("params", paramsArr);
            }
            json.put("retValType", retValType);
            jsonFuntions.put(Integer.toString(j), json);
            ++j;
        }
        return jsonFuntions;
    }


    //makes data transfer between client and server

    public void run() {  ////////socket data transfer process
        try {
            DBFunctions dbFunctions = new DBFunctions();

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String input = dis.readUTF();

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            if(input.equals("dbFunctionsAsJSON")) {
                dos.writeUTF(dbFunctionsAsJSON(dbFunctions).toJSONString());
                dos.flush();
                return;
            }

            JSONParser jsonParser = new JSONParser();
            Map<String, Object> map = (Map) jsonParser.parse(input);
            ArrayList<Object> params = new ArrayList<Object>();
            //System.out.println(input);
            String name = "";
            String retValType = "";
            for (String key : map.keySet()) {
                params = (ArrayList<Object>) map.get(key);
                name = dbFunctions.getClass().getDeclaredMethods()[Integer.parseInt(key)].getName();
                retValType = dbFunctions.getClass().getDeclaredMethods()[Integer.parseInt(key)].getReturnType().getName();
                break;
            }

            Method method = params != null ? dbFunctions.getClass().getMethod(name,  params.getClass()) : dbFunctions.getClass().getMethod(name, null);
            if (retValType.equals("void")) {
                method.invoke(dbFunctions, params);
            } else {
                Object retVal = params != null ? method.invoke(dbFunctions, params) :  method.invoke(dbFunctions);
                dos = new DataOutputStream(socket.getOutputStream());
                JSONObject jsonObject = toJSONObject(retValType, retVal);
                dos.writeUTF(jsonObject.toJSONString());
                dos.flush();
            }

        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error. Connection " + num + " closed.");
        }
        finally {
            try {
                socket.close();
                System.out.println("Connection " + num + " closed.");
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //converts input Object into JSONObject. Works with ResultSet and Integer Objects

    private JSONObject toJSONObject(String type, Object someObject) throws SQLException {
        if (type.equals(ResultSet.class.getTypeName())) {
            try {
                ResultSet resultSet = (ResultSet) someObject;

                ResultSetMetaData meta = resultSet.getMetaData();
                JSONObject jsonObject = new JSONObject();
                ArrayList<String> head = new ArrayList<String>();

                for (int i = 1; i <= meta.getColumnCount(); ++i) {
                    head.add(meta.getColumnName(i));
                }
                jsonObject.put("head", head);

                ArrayList<Object> row = null;
                ArrayList<ArrayList<Object>> body = new ArrayList<ArrayList<Object>>();
                Date date = null;
                Object value = null;
                while (resultSet.next()) {
                    row = new ArrayList<Object>();
                    for (int i = 1; i <= meta.getColumnCount(); ++i) {

                        try{
                            date = resultSet.getDate(i);
                            String dt = date.toString().replace('-','.');
                            row.add(dt);
                        }
                        catch (Exception ex){
                            value = resultSet.getObject(i);
                            row.add(value);
                        }
                    }
                    body.add(row);
                }

                //resultSet.last();
                long rowCount = resultSet.getRow();
                jsonObject.put("body", body);
                jsonObject.put("rows", rowCount);
                return jsonObject;
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

        if (type.equals(Integer.class.getTypeName())) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Integer", (Integer)someObject);
                return jsonObject;
            }
            catch(Exception ex){
                return null;
            }
        }

        if (type.equals(Long.class.getTypeName())) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Long", (Long)someObject);
                return jsonObject;
            }
            catch(Exception ex){
                return null;
            }
        }

        if(type.equals(JSONObject.class.getTypeName())){
            return (JSONObject) someObject;
        }
        return null;
    }
}
