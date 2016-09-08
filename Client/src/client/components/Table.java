package client.components;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by 1 on 6/30/2015.
 */
public class Table {
    public ArrayList<String> getHead() {
        return head;
    }

    public ArrayList<ArrayList<Object>> getBody() {
        return body;
    }

    private ArrayList<String> head;
    private ArrayList<ArrayList<Object>> body;
    public static long size;
    public static int columns;

    public Table(ArrayList<String> head, ArrayList<ArrayList<Object>> body){
        this.head = head;
        this.body = body;
        if(body!=null) {
            this.size = body.size();
            this.columns = body.get(0).size();
        }
        else{
            this.size = 0;
            this.columns = 0;
        }
    }

    public Object get(int i, int j){
        return body.get(i).get(j);
    }

    public ArrayList<Object> getColumn(int j){
        ArrayList<Object> column = new ArrayList<Object>();
        for(int i = 0; i < size; ++i){
            column.add(body.get(i).get(j));
        }
        return column;
    }

    public ArrayList<Object> getRow(int i){
        return body.get(i);
    }

    public Table(String json) throws ParseException {
        JSONParser parser = new JSONParser();

        JSONObject object = (JSONObject) parser.parse(json);
        this.head = (ArrayList)(object.get("head"));
        this.body = (ArrayList<ArrayList<Object>>)(object.get("body"));
        this.size = (Long)(object.get("rows"));
        this.columns = head.size();

    }
}
