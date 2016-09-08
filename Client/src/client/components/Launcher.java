package client.components;

import client.frames.UI;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;

/**
 * Created by 1 on 6/17/2015.
 */
public class Launcher {
    public static void main(String[] args){

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new WindowsLookAndFeel());
                    UI ui = new UI();
                }
                catch (Exception ex){
                    UI ui = new UI();
                    ui.setDefaultLookAndFeelDecorated(false);
                }
            }
        });

    }
}
