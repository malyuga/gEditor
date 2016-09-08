package client.components;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 1 on 6/17/2015.
 */
public class DBFunctionsCl {
    private class Function {
        String name;
        ArrayList<String> params; //<Type>
        String retValType;

        public Function() {

        }

        public Function(String name, ArrayList<String> params, String retValType) {
            this.retValType = retValType;
            this.name = name;
            this.params = params;
        }
    }

    String NAME = "name";
    String PARAMS = "params";
    String RET_VAL_TYPE = "retValType";

    static Socket socket;
    HashMap<Integer, Function> functions;

    public DBFunctionsCl() {
        try {
            this.socket = new Socket("localhost", 3128);
            getInputData(this.getSocket());
            socket.setKeepAlive(true);
        }
        catch(ConnectException ex){
            JOptionPane.showMessageDialog(new JLabel(), "Нет соединения с сервером", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getInputData(Socket socket) throws IOException, ParseException {

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF("dbFunctionsAsJSON");

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String jsonString = dis.readUTF();

        functions = new HashMap<Integer, Function>();
        JSONParser parser = new JSONParser();
        JSONObject jsonFunctions = (JSONObject) parser.parse(jsonString);
        JSONObject jsonFunction;
        Function function = null;
        //HashMap<String, String> params = new HashMap<String, String>();

        for (int i = 0; i < jsonFunctions.size(); ++i) {
            jsonFunction = (JSONObject) jsonFunctions.get(Integer.toString(i));
            function = new Function();
            function.name = jsonFunction.get(NAME).toString();
            try {
                function.params = (ArrayList<String>) jsonFunction.get(PARAMS);
            } catch (Exception ex) {
                function.params = null;
            }
            function.retValType = jsonFunction.get(RET_VAL_TYPE).toString();

            functions.put(i, function);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    private JSONObject findFunction(String funcName, ArrayList<Object> params) {
        JSONObject jsonObject = new JSONObject();
        for (int it : functions.keySet()) {
            if (functions.get(it).name.equals(funcName)) {
                jsonObject.put(it, params);
            }
        }
        return jsonObject;
    }

    public Table get_edges(long G_id, long s_id) throws IOException, ParseException {
        this.socket = new Socket("localhost", 3128);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(G_id);
        params.add(s_id);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(findFunction("get_edges", params).toJSONString());

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String response = dis.readUTF();

        Table retVal = new Table(response);
        return retVal;
    }

    public Table get_node(long G_id) throws IOException, ParseException {
        this.socket = new Socket("localhost", 3128);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(G_id);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(findFunction("get_node", params).toJSONString());

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String response = dis.readUTF();

        Table retVal = new Table(response);
        return retVal;
    }

    public Table get_graphs() throws IOException, ParseException {
        this.socket = new Socket("localhost", 3128);
        if (socket.isConnected()) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(findFunction("get_graph", null).toJSONString());

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String response = dis.readUTF();

            Table retVal = new Table(response);
            return retVal;
        } else {
            JOptionPane.showMessageDialog(new JLabel(), "Ошибка подключения к серверу");
            return null;
        }
    }

    public long new_graph(long in_id, String in_name, long date, String info) throws IOException, ParseException {
        this.socket = new Socket("localhost", 3128);
        if (socket.isConnected()) {
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(in_id);
            params.add(in_name);
            params.add(date);
            params.add(info);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(findFunction("new_graph", params).toJSONString());

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String response = dis.readUTF();
            JSONParser parser = new JSONParser();
            HashMap<String, Long> object = (HashMap) parser.parse(response);

            long retVal = object.get("Long");
            return retVal;
        } else {
            JOptionPane.showMessageDialog(new JLabel(), "Ошибка подключения к серверу");
            return -1;
        }
    }

    public void new_edge(long s_id, long f_id, long G_id, double weight) throws IOException {
        this.socket = new Socket("localhost", 3128);
        if (socket.isConnected()) {
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(s_id);
            params.add(f_id);
            params.add(G_id);
            params.add(weight);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(findFunction("new_edge", params).toJSONString());
        }
    }

    public void delete_graph(long g_id) throws IOException {
        this.socket = new Socket("localhost", 3128);
        if (socket.isConnected()) {
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(g_id);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(findFunction("delete_graph", params).toJSONString());
        }
    }

    public Long new_node(long id, long G_id, long numb, String name, String info, long x, long y) throws IOException, ParseException {
        this.socket = new Socket("localhost", 3128);
        if (socket.isConnected()) {
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(id);
            params.add(G_id);
            params.add(numb);
            params.add(name);
            params.add(info);
            params.add(x);
            params.add(y);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(findFunction("new_node", params).toJSONString());

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String response = dis.readUTF();
            JSONParser parser = new JSONParser();
            HashMap<String, Long> object = (HashMap) parser.parse(response);

            long retVal = object.get("Long");
            return retVal;
        } else {
            JOptionPane.showMessageDialog(new JLabel(), "Ошибка подключения к серверу");
            return (long)-1;
        }
    }
}
    /*
    //call's "get_node" procedure from server and parse and print's response
    public static ResultSet get_node(long G_id) throws IOException, ParseException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(new String("method"), new String("get_node"));
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put("0", G_id);
        jsonObject.put("params", params);
        jsonObject.put("retVal", "ResultSet");


        //sending request
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(jsonObject.toJSONString());
        dos.flush();

        //getting response
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String response = dis.readUTF();

        //parse response and print it out
        JSONParser parser = new JSONParser();
        Map<String, Object> objResponse = (Map)parser.parse(response);
        Map<String, Object> head = (Map)objResponse.get("head");
        Map<String, Object> body = (Map)objResponse.get("body");
        long count = (Long)objResponse.get("rows");
        JSONArray iter;
        Object iter2;

        for (int i = 1; i <= count; ++i) {
            iter = (JSONArray) body.get(Integer.toString(i));
            for (int j = 0; j < iter.size(); ++j) {
                iter2 = iter.get(j);
                if (iter2 != null) {
                    System.out.print(iter2.toString() + " \t");
                } else {
                    System.out.print("null \t");
                }
            }
            System.out.println();
        }
        return null;
    }

    //call's "new_graph" procedure from server
    public static void new_graph(long in_id, long year, long month, long day, String info) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(new String("method"), new String("new_graph"));
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        params.put("0", in_id);
        params.put("1", year);
        params.put("2", month);
        params.put("3", day);
        params.put("4", info);
        jsonObject.put("params", params);
        jsonObject.put("retVal", "void");

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(jsonObject.toJSONString());
    }

    public static ResultSet get_graphs() throws IOException, ParseException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(new String("method"), new String("get_graphs"));
        jsonObject.put("retVal", "ResultSet");

        //sending request
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(jsonObject.toJSONString());
        dos.flush();

        //getting response
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String response = dis.readUTF();

        //parse response and print it out
        JSONParser parser = new JSONParser();
        Map<String, Object> objResponse = (Map)parser.parse(response);
        Map<String, Object> head = (Map)objResponse.get("head");
        Map<String, Object> body = (Map)objResponse.get("body");
        long count = (Long)objResponse.get("rows");
        JSONArray iter;
        Object iter2;

        for (int i = 1; i <= count; ++i) {
            iter = (JSONArray) body.get(Integer.toString(i));
            for (int j = 0; j < iter.size(); ++j) {
                iter2 = iter.get(j);
                if (iter2 != null) {
                    System.out.print(iter2.toString() + " \t");
                } else {
                    System.out.print("null \t");
                }
            }
            System.out.println();
        }
        return null;
    }*/

