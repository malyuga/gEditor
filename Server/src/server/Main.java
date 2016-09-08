package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 1 on 7/2/2015.
 */
public class Main extends JFrame {
    private JPanel pnlMain;
    private JPanel pnlConnect;
    private JButton btnStart;
    private JButton btnStop;
    private JLabel lblStatus;
    private JPanel pnlEdit;
    private JPanel panel;
    private JTextField edtURL;
    private JTextField edtName;
    private JTextField edtPass;
    private JTextField edtDriver;
    private JTextField edtPort;
    private JTextField edtHost;
    private JLabel fullUrl;
    private JLabel lblName;
    private JLabel lblPass;
    private JLabel lblDriver;
    private JLabel lblPort;
    private JLabel lblHost;
    private JButton btnTest;
    private JButton btnApply;
    private JButton btnDefault;
    private JLabel lblStatusText;

    private Server server;

    public Main() {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();

        fillEditFields();
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Server.driverName = Server.defaultDriverName;
        Server.host = Server.defaultHost;
        Server.name = Server.defaultName;
        Server.url = Server.defaultUrl;
        Server.password = Server.defaultPassword;
        Server.port = Server.defaultPort;

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        setSize(screenWidth * 5 / 16, screenHeight * 5 / 16);
        setLocation(screenWidth * 3 / 16, screenHeight * 3 / 16);

        add($$$getRootComponent$$$());
        btnTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.setDBConn(Server.url, Server.name, Server.password, Server.driverName);
                try {
                    Statement statement = Server.getConnection().createStatement();
                    statement.execute("SELECT * FROM Dummy");
                    JOptionPane.showMessageDialog(btnTest, "Успешное соедиенение", "Тест", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(btnTest, "Не удалось подключиться к базе", "Тест", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Server server = new Server(Server.url, Server.name, Server.password, Server.driverName, Server.port, Server.host);
                    lblStatusText.setText("Сервер запущен");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    lblStatusText.setText("Сервер остановлен");
                }
            }
        });

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.servSocket = null;
                Server.connection = null;
                lblStatusText.setText("Сервер остановлен");
            }
        });

        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.driverName = edtDriver.getText();
                Server.host = edtDriver.getText();
                Server.name = edtDriver.getText();
                Server.url = edtDriver.getText();
                Server.password = edtDriver.getText();
                Server.port = Integer.parseInt(edtDriver.getText());
            }
        });

        btnDefault.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillEditFields();
            }
        });

    }


    private void fillEditFields() {
        edtDriver.setText(Server.defaultDriverName);
        edtHost.setText(Server.defaultHost);
        edtName.setText(Server.defaultName);
        edtPass.setText(Server.defaultPassword);
        edtPort.setText(Integer.toString(Server.defaultPort));
        edtURL.setText(Server.defaultUrl);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout(0, 0));
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        pnlMain.add(panel, BorderLayout.SOUTH);
        pnlConnect = new JPanel();
        pnlConnect.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.add(pnlConnect, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        pnlConnect.add(spacer1);
        btnStart = new JButton();
        btnStart.setText("Запуск сервера");
        pnlConnect.add(btnStart);
        btnStop = new JButton();
        btnStop.setText("Остановка сервера");
        pnlConnect.add(btnStop);
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        pnlConnect.add(spacer2);
        lblStatus = new JLabel();
        lblStatus.setText("Статус: ");
        panel.add(lblStatus, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblStatusText = new JLabel();
        lblStatusText.setText("");
        panel.add(lblStatusText, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pnlEdit = new JPanel();
        pnlEdit.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        pnlMain.add(pnlEdit, BorderLayout.CENTER);
        fullUrl = new JLabel();
        fullUrl.setText("Полный URL:");
        pnlEdit.add(fullUrl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edtURL = new JTextField();
        pnlEdit.add(edtURL, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lblName = new JLabel();
        lblName.setText("Имя пользователя базы");
        pnlEdit.add(lblName, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edtName = new JTextField();
        pnlEdit.add(edtName, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lblPass = new JLabel();
        lblPass.setText("Пароль");
        pnlEdit.add(lblPass, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edtPass = new JTextField();
        pnlEdit.add(edtPass, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lblDriver = new JLabel();
        lblDriver.setText("jdbc Драйввер");
        pnlEdit.add(lblDriver, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edtDriver = new JTextField();
        pnlEdit.add(edtDriver, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pnlEdit.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnTest = new JButton();
        btnTest.setText("Тест");
        panel1.add(btnTest);
        btnApply = new JButton();
        btnApply.setText("Применить");
        panel1.add(btnApply);
        btnDefault = new JButton();
        btnDefault.setText("По умолчанию");
        panel1.add(btnDefault);
        edtPort = new JTextField();
        pnlEdit.add(edtPort, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        edtHost = new JTextField();
        pnlEdit.add(edtHost, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lblPort = new JLabel();
        lblPort.setText("Порт");
        pnlEdit.add(lblPort, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(28, -1), null, 0, false));
        lblHost = new JLabel();
        lblHost.setText("Имя хоста");
        pnlEdit.add(lblHost, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(56, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return pnlMain;
    }
}


