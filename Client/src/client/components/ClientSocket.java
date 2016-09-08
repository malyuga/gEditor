package client.components;


import java.net.Socket;

/**
 * Created by 1 on 4/20/2015.
 */
public class ClientSocket {
    public Socket socket;
    public DBFunctionsCl dbFunctionsCl;

    public ClientSocket(){
        try{
            this.dbFunctionsCl = new DBFunctionsCl();
            this.socket = dbFunctionsCl.getSocket();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
