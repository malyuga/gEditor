package server;
/**
 * Created by "1" on 07.03.2015.
 * makes connect to DB in SetDBConn and set up server socket to listening
 * clients' input connections in SetSocket
 */

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread{

    public    static  ServerSocket    servSocket  = null;
    public    static  Connection      connection  = null;

    protected static String defaultUrl = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";
    protected static String defaultName = "sys as sysdba";
    protected static String defaultPassword = "123";
    protected static String defaultDriverName = "oracle.jdbc.driver.OracleDriver";
    protected static int defaultPort = 3128;
    protected static String defaultHost = "localhost";

    protected static String url;
    protected static String name;
    protected static String password;
    protected static String driverName;
    protected static int port;
    protected static String host;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            Main fmMain = new Main();
        }
        catch (Exception ex){
            Main fmMain = new Main();
            fmMain.setDefaultLookAndFeelDecorated(false);
        }
    }

    public Server(){
        setDBConn(defaultUrl, defaultName, defaultPassword, defaultDriverName);
        setSocket(defaultPort, defaultHost);
    }

    public Server(String Url, String Name, String Password, String DriverName, int Port, String Host){
        setDBConn(Url, Name, Password, DriverName);
        setSocket(Port, Host);
    }

    public void setDBConn(String url,String name,String pass,String driverName){
        try {
            //Class.forName(driverName);
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("Драйвер подключен");

            connection = DriverManager.getConnection(url, name, pass);
            System.out.println("Соединение установлено");

        }
        catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSocket(int port, String host){
        try {
            int i = 0;
            servSocket = new ServerSocket(port);
            System.out.print("Server started\n");
            while (true){
                i++;
                new ServerThread(i, servSocket.accept());

                System.out.println(i + " connection");
            }
        }
        catch(Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            if (servSocket != null)
                try {servSocket.close();}
                catch (Exception ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}

