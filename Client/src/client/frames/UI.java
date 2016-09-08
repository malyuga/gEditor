package client.frames;

import client.components.ClientSocket;
import client.components.Table;
import client.graphicElements.Edge;
import client.graphicElements.MyCanvas;
import client.graphicElements.Node;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ConnectException;
import java.util.*;

/**
 * Created by 1 on 6/17/2015.
 */
public class UI extends JFrame {
    private JPanel panel;
    private JPanel pnlMenus;
    private JMenuBar menuBar;
    private JMenuItem miNew;
    private JMenuItem miOpen;
    private JMenuItem miSave;
    private JMenuItem miDelete;
    private JSeparator separator1;
    private JMenuItem miUndo;
    private JMenuItem miRedo;
    private JToolBar toolBar;
    private JMenuItem miMinCicle;
    private JMenuItem miShortWay;
    private JMenuItem miMinTree;
    private JPanel main_panel;
    private JPanel pnlButtons;
    private JButton btnNode;
    private JButton btnOEdge;
    private JButton btnNOEdge;
    private JButton btnDeleteGraphElem;
    private JButton btnCursor;
    private JTabbedPane tabEditor;
    private JPanel mainTab;
    private JProgressBar progressBar1;
    private JLabel lblCoords;
    private JLabel lblCurTaskCaption;
    private JLabel lblCurTask;
    private JLabel lblStatus;
    private JPanel statusBar;

    int windowWidth;
    int windowHeight;
    int X;
    int Y;

    Vector<Node> nodes = new Vector<Node>();

    ClientSocket clientSocket = new ClientSocket();

    MyCanvas canvas;
    long activeModel = -1;
    Node activeNode;
    int activeNumber = 1;
    JFrame fmMain;
    byte viewState = -1;
    int[][] graph; // матрица смежности

    public long isActiveModel() {
        return activeModel;
    }

    private void createUIComponents() {

        miNew = new JMenuItem(new ImageIcon("Client/res/imgs/new.png"));
        miOpen = new JMenuItem(new ImageIcon("Client/res/imgs/open.png"));
        miSave = new JMenuItem(new ImageIcon("Client/res/imgs/save.png"));
        miDelete = new JMenuItem(new ImageIcon("Client/res/imgs/del.png"));
        miUndo = new JMenuItem(new ImageIcon("Client/res/imgs/undo.png"));
        miRedo = new JMenuItem(new ImageIcon("Client/res/imgs/redo.png"));
        btnNode = new JButton(new ImageIcon("Client/res/imgs/new node.png"));
        btnOEdge = new JButton(new ImageIcon("Client/res/imgs/new O edge.png"));
        btnNOEdge = new JButton(new ImageIcon("Client/res/imgs/new NO edge.png"));
        btnDeleteGraphElem = new JButton(new ImageIcon("Client/res/imgs/Del elem.png"));
        btnCursor = new JButton(new ImageIcon("Client/res/imgs/cur.png"));

    }

    public UI() {
        $$$setupUI$$$();
        fmMain = this;
        activeModel = -1;

        setTitle("Графический редактор графовых структур");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        windowWidth = screenWidth * 5 / 8;
        windowHeight = screenHeight * 5 / 8;
        X = screenWidth * 3 / 16;
        Y = screenHeight * 3 / 16;

        setSize(windowWidth, windowHeight);
        setLocation(X, Y);

        activeNode = new Node(0, 0, 10);
        setVisible(false);
        canvas = new MyCanvas();
        canvas.popupMenu = new JPopupMenu();
        ActionListener editNodePopupAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEditNode();
            }
        };
        JMenuItem itemEditNode = new JMenuItem("Редактировать данные о вершине");
        itemEditNode.addActionListener(editNodePopupAction);

        ActionListener editEdgePopupAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEditEdge();
            }
        };
        JMenuItem itemEditEdge = new JMenuItem("Редактировать данные о ребре");
        itemEditEdge.addActionListener(editEdgePopupAction);

        canvas.popupMenu.add(itemEditNode);
        canvas.popupMenu.add(itemEditEdge);
        mainTab.add(canvas);
        add($$$getRootComponent$$$());
        addListeners();
        setBlockToEditor(true);
        setVisible(true);
        lblCurTask.setText("Нет активных задач");

    }

    private void onEditEdge() {
        EdgeEdit fmEdgeEdit;
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            fmEdgeEdit = new EdgeEdit(activeNode.getEdgesTo());
        } catch (UnsupportedLookAndFeelException ex) {
            fmEdgeEdit = new EdgeEdit(activeNode.getEdgesTo());
            fmEdgeEdit.setDefaultLookAndFeelDecorated(false);
        }
        fmEdgeEdit.setSize(getWidth() / 2, getHeight());
        fmEdgeEdit.setLocation((int) activeNode.getLeft(), (int) activeNode.getTop());
        fmEdgeEdit.setLocationRelativeTo(activeNode);
        fmEdgeEdit.setVisible(true);

    }

    private boolean onEditNode() {
        NodeEdit fmNodeEdit;
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            fmNodeEdit = new NodeEdit();
        } catch (Exception ex) {
            fmNodeEdit = new NodeEdit();
            fmNodeEdit.setDefaultLookAndFeelDecorated(false);
        }
        fmNodeEdit.setSize(getWidth() / 2, getHeight() / 3);
        fmNodeEdit.setLocation((int) activeNode.getLeft(), (int) activeNode.getTop());
        fmNodeEdit.setLocationRelativeTo(activeNode);
        fmNodeEdit.edtName.setText(activeNode.getNodeName());
        fmNodeEdit.edtInfo.setText(activeNode.getInfo());
        fmNodeEdit.setVisible(true);

        boolean retVal = false;

        if (fmNodeEdit.modalResult) {
            try {
                /*activeNode.setNumber(Integer.parseInt(fmNodeEdit.edtNumber.getText()));
                if (activeNode.getNumber() < 0) {
                    throw new Exception();
                }*/

                activeNode.setNumber(activeNumber);

                if ((fmNodeEdit.edtName.getText().length() <= 255) && (fmNodeEdit.edtName.getText().length() != 0)) {
                    activeNode.setNodeName(fmNodeEdit.edtName.getText());
                } else {
                    throw new Exception();
                }

                if (fmNodeEdit.edtInfo.getText().length() <= 255) {
                    activeNode.setInfo(fmNodeEdit.edtInfo.getText());
                } else {
                    throw new Exception();
                }
                retVal = true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(fmNodeEdit, "Ошибка ввода", "Ошибка!", JOptionPane.ERROR_MESSAGE);
            }

        }
        return retVal;
    }

    private void setBlockToEditor(boolean blockToEditor) {
        btnCursor.setEnabled(!blockToEditor);
        btnDeleteGraphElem.setEnabled(!blockToEditor);
        btnNode.setEnabled(!blockToEditor);
        btnNOEdge.setEnabled(!blockToEditor);
        btnOEdge.setEnabled(!blockToEditor);
        canvas.setEnabled(!blockToEditor);
    }

    private void onOpen(OpenDialog fmOpenDialog) throws IOException, ParseException {
        activeModel = fmOpenDialog.getSelectedGraph();
        lblCurTask.setText("Редактирование графа");
        setBlockToEditor(false);
        Table tblNodes = clientSocket.dbFunctionsCl.get_node(fmOpenDialog.getSelectedGraph());
        ArrayList<Object> node_id = tblNodes.getColumn(0);
        nodes.clear();
        canvas.getNodesToDraw().clear();
        ArrayList l = null;
        Node newNode = null;
        for (int i = 0; i < tblNodes.size; ++i) {
            l = tblNodes.getRow(i);
            newNode = new Node((Long) l.get(0), (Long) l.get(1), (Long) l.get(2), (String) l.get(3), (String) l.get(4), (Long) l.get(5), (Long) l.get(6));
            nodes.add(newNode);
        }

        long tmp = 0;
        for (Node curNode : nodes) {
            Table tblEdges = clientSocket.dbFunctionsCl.get_edges(fmOpenDialog.getSelectedGraph(), curNode.getId());
            for (ArrayList row : tblEdges.getBody()) {
                for (Node node : nodes) {
                    tmp = (Long) row.get(1);
                    if (tmp == node.getId()) {
                        curNode.setEdgeTo(new Edge(node, Double.parseDouble(row.get(3).toString())));
                        node.setReferer(curNode);
                    }
                }
            }
            canvas.addNodeToDraw(curNode);
        }

        activeNumber = nodes.size() + 1;
        viewState = 0;
        canvas.repaint();
    }

    private void onSave() throws IOException {
        SaveGraph fmSaveGraph = new SaveGraph(clientSocket.dbFunctionsCl, fmMain);
        fmSaveGraph.setVisible(true);
        if (fmSaveGraph.isModalResult()) {
            try {
                long date = fmSaveGraph.getGraphLastChange();
                long g_id = clientSocket.dbFunctionsCl.new_graph(
                        fmSaveGraph.getSelectedGraph(),
                        fmSaveGraph.getGraphName(),
                        date,
                        fmSaveGraph.getGraphInfo()
                );

                for (Node node : nodes) {
                    node.setId(clientSocket.dbFunctionsCl.new_node(node.getId(), g_id, node.getNumber(), node.getNodeName(), node.getInfo(), node.getLeft(), node.getTop()));
                }

                for (Node node : nodes) {
                    for (Edge edge : node.getEdgesTo()) {
                        clientSocket.dbFunctionsCl.new_edge(node.getId(), edge.getNode().getId(), g_id, edge.getWeight());
                    }
                }

                activeNumber = nodes.size() + 1;
                viewState = 0;
                canvas.repaint();

            } catch (ParseException e) {
                JOptionPane.showMessageDialog(fmMain, "Ошибка сохранения", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        }
    }


    private void addListeners() {
        final JMenuItem[] activeItem = {null};

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (activeNode != null) {
                    activeNode.setPosition(e.getX() - activeNode.getD() / 2, e.getY() - activeNode.getD() / 2);
                    activeNode.setVisible(true);
                    canvas.addNodeToDraw(activeNode);
                    canvas.repaint();
                }
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                lblCoords.setText(Integer.toString(e.getX()) + ":" + Integer.toString(e.getY()));

                switch (viewState) {
                    case 1: {
                        activeNode.setPosition(e.getX() - activeNode.getD() / 2, e.getY() - activeNode.getD() / 2);
                    }
                    case 0: {
                        //activeNode.setPosition(e.getX() - activeNode.getD() / 2, e.getY() - activeNode.getD() / 2);
                    }
                }

                canvas.repaint();
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                switch (viewState) {
                    case 0: {
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        activeNode = null;
                        for (Node node : canvas.getNodesToDraw()) {
                            if (node.contains(e.getX(), e.getY())) {
                                activeNode = node;
                                activeNode.setVisible(true);
                                activeNode.setActive(true);
                            } else {
                                node.setActive(false);
                            }
                        }
                        if ((e.isPopupTrigger()) && (activeNode != null)) {
                            canvas.setComponentPopupMenu(canvas.popupMenu);
                        }
                    }
                    break;
                    case 1: {

                        if (onEditNode()) {
                            Node newNode = null;
                            try {
                                newNode = new Node(activeNode);
                            } catch (IllegalAccessException e1) {
                                e1.printStackTrace();
                            }
                            ++activeNumber;
                            newNode.setColor(new Color(0, 0, 0));
                            nodes.add(newNode);
                        }

                        canvas.clearNodesToDraw();
                        canvas.setNodesToDraw(nodes);
                        activeNode = null;
                        viewState = 0;
                    }
                    break;
                    case 2: {
                        if (activeNode == null) {
                            for (Node node : nodes) {
                                if (node.contains(e.getX(), e.getY())) {
                                    activeNode = node;
                                    activeNode.setVisible(true);
                                    activeNode.setActive(true);
                                }
                            }
                        } else {
                            for (Node node : nodes) {
                                if (node.contains(e.getX(), e.getY())) {
                                    try {
                                        activeNode.setEdgeTo(new Edge(node, Double.parseDouble(JOptionPane.showInputDialog("Введите вес ребра:"))));
                                        node.setReferer(activeNode);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                node.setActive(false);
                            }
                            activeNode = null;
                            viewState = 0;
                            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                    break;
                    case 3: {
                        if (activeNode == null) {
                            for (Node node : nodes) {
                                if (node.contains(e.getX(), e.getY())) {
                                    activeNode = node;
                                    activeNode.setVisible(true);
                                    activeNode.setActive(true);
                                }
                            }
                        } else {
                            for (Node node : nodes) {
                                if (node.contains(e.getX(), e.getY())) {
                                    try {
                                        double weight = Double.parseDouble(JOptionPane.showInputDialog("Введите вес ребра:"));
                                        activeNode.setEdgeTo(new Edge(node, weight));
                                        node.setEdgeTo(new Edge(activeNode, weight));
                                        activeNode.setReferer(node);
                                        node.setReferer(activeNode);
                                    } catch (Exception ex) {
                                        //JOptionPane.showMessageDialog(activeNode, "Ошибка веса ребра!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                                        ex.printStackTrace();
                                    }

                                }
                                node.setActive(false);
                            }
                            activeNode = null;
                            viewState = 0;
                            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                    break;
                    case 4: {
                        Vector<Node> nodesToDraw = canvas.getNodesToDraw();
                        Node node;
                        Node referer;
                        Edge refEdge;
                        Iterator refIterator;
                        Iterator refEdgesIterator;
                        int number = Integer.MAX_VALUE;

                        Iterator iterator = nodesToDraw.iterator();
                        while (iterator.hasNext()) {
                            node = (Node) iterator.next();

                            if (node.contains(e.getX(), e.getY())) {
                                number = (int) node.getNumber();
                                activeNumber--;
                                refIterator = node.getReferers().iterator();
                                while (refIterator.hasNext()) {
                                    referer = (Node) refIterator.next();

                                    refEdgesIterator = referer.getEdgesTo().iterator();
                                    while (refEdgesIterator.hasNext()) {
                                        refEdge = (Edge) refEdgesIterator.next();
                                        if (refEdge.getNode().equals(node)) {
                                            refEdgesIterator.remove();
                                        }
                                    }
                                }
                                iterator.remove();
                            } else {
                                if (node.getNumber() > number) {
                                    node.setNumber((int) node.getNumber() - 1);
                                }

                            }
                        }

                        iterator = nodes.iterator();
                        while (iterator.hasNext()) {
                            node = (Node) iterator.next();
                            if (node.contains(e.getX(), e.getY())) {
                                refIterator = node.getReferers().iterator();
                                while (refIterator.hasNext()) {
                                    referer = (Node) refIterator.next();
                                    referer.getEdgesTo().remove(node);
                                }
                                iterator.remove();
                            }
                        }

                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        viewState = 0;
                    }

                }
                repaint();
            }
        });

        fmMain.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                miNew.setArmed(false);
                miOpen.setArmed(false);
                miSave.setArmed(false);
                miDelete.setArmed(false);
                miUndo.setArmed(false);
                miRedo.setArmed(false);
                if (activeItem[0] != null) {
                    activeItem[0].setArmed(true);
                }
                activeItem[0] = null;
            }
        });

        miOpen.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                activeItem[0] = miOpen;
                fmMain.getMouseMotionListeners()[0].mouseMoved(e);
            }
        });

        miSave.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                activeItem[0] = miSave;
                fmMain.getMouseMotionListeners()[0].mouseMoved(e);
            }
        });

        miDelete.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                activeItem[0] = miDelete;
                fmMain.getMouseMotionListeners()[0].mouseMoved(e);
            }
        });

        miRedo.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                activeItem[0] = miRedo;
                fmMain.getMouseMotionListeners()[0].mouseMoved(e);
            }
        });

        miUndo.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                activeItem[0] = miUndo;
                fmMain.getMouseMotionListeners()[0].mouseMoved(e);
            }
        });

        miNew.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                activeItem[0] = miNew;
                fmMain.getMouseMotionListeners()[0].mouseMoved(e);
            }
        });

        lblCoords.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                lblCoords.setText(Integer.toString(e.getX()) + Integer.toString(e.getY()));
            }
        });

        miNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (activeModel != -1) {
                    int saveDialog = JOptionPane.showConfirmDialog(fmMain, "Сохранить текущюю модель?");
                    if (saveDialog == JOptionPane.YES_OPTION) {
                        try {
                            onSave();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (saveDialog == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }
                activeModel = 0;
                nodes.clear();
                canvas.getNodesToDraw().clear();
                setBlockToEditor(false);
                lblCurTask.setText("Редактирование графа");
                canvas.setColor(Color.white);
                canvas.repaint();
            }
        });

        miOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenDialog fmOpenDialog = null;
                try {
                    fmOpenDialog = new OpenDialog(clientSocket.dbFunctionsCl, fmMain);
                    fmOpenDialog.setVisible(true);

                    if (fmOpenDialog.isModalResult()) {
                        if (activeModel != -1) {
                            int saveDialog = JOptionPane.showConfirmDialog(fmMain, "Сохранить текущюю модель?");
                            if (saveDialog == JOptionPane.YES_OPTION) {
                                onSave();
                            }
                            if (saveDialog == JOptionPane.CANCEL_OPTION) {
                                return;
                            }
                        }
                        fmOpenDialog.getSelectedGraph();
                        lblCurTask.setText("Редактирование графа");
                        onOpen(fmOpenDialog);
                        canvas.setColor(Color.white);
                        canvas.repaint();
                    }

                } catch (ConnectException ex) {
                    JOptionPane.showMessageDialog(fmMain, "Нет соединения с сервером", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(fmOpenDialog, "Ошибка открытия файла", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        miSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (activeModel != -1) {
                    try {
                        onSave();
                    } catch (ConnectException ex) {
                        JOptionPane.showMessageDialog(fmMain, "Нет соединения с сервером", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(fmMain, "Ошибка сохранения", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        miDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenDialog fmOpenDialog = null;
                try {
                    fmOpenDialog = new OpenDialog(clientSocket.dbFunctionsCl, fmMain);
                    fmOpenDialog.setVisible(true);

                    if (fmOpenDialog.isModalResult()) {
                        int saveDialog = JOptionPane.showConfirmDialog(fmMain, "Удалить выбраный граф?");
                        if (saveDialog == JOptionPane.YES_OPTION) {
                            clientSocket.dbFunctionsCl.delete_graph(fmOpenDialog.getSelectedGraph());
                            if (activeModel == fmOpenDialog.getSelectedGraph()) {
                                lblCurTask.setText("Нет активных задач");
                                canvas.setColor(Color.gray);
                                nodes.clear();
                                canvas.getNodesToDraw().clear();
                                canvas.repaint();
                            }
                        }
                    }

                } catch (ConnectException ex) {
                    JOptionPane.showMessageDialog(fmMain, "Нет соединения с сервером", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(fmOpenDialog, "Ошибка открытия файла", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });


        miUndo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        miRedo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        miMinCicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        miShortWay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //graph = new int[activeNumber][activeNumber];
                //dijkstra(activeNode.getNumber(), graph);
            }
        });


        miMinTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        btnNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Node node : nodes) {
                    node.setActive(false);
                }
                activeNode = new Node(0, 0, 10);
                activeNode.setVisible(true);
                canvas.addNodeToDraw(activeNode);
                viewState = 1;
                canvas.repaint();
            }
        });

        btnOEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                viewState = 2;
                for (Node node : nodes) {
                    node.setActive(false);
                }
                for (Node node : canvas.getNodesToDraw()) {
                    node.setActive(false);
                }

                activeNode = null;
                canvas.repaint();
            }

        });

        btnNOEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                viewState = 3;
                for (Node node : nodes) {
                    node.setActive(false);
                }
                for (Node node : canvas.getNodesToDraw()) {
                    node.setActive(false);
                }

                activeNode = null;
                canvas.repaint();
            }
        });

        btnDeleteGraphElem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                viewState = 4;
            }
        });

        btnCursor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                viewState = 0;
            }
        });
    }

    /* Алгоритм Дейкстры за O(V^2) */
    private double dijkstra(int start, int fin, int[][] graph) {

        double INF = Double.POSITIVE_INFINITY; // "Бесконечность"
        int vNum = activeNumber; // количество вершин
        boolean[] used = new boolean[vNum]; // массив пометок
        double[] dist = new double[vNum]; // массив расстояния. dist[v] = минимальное_расстояние(start, v)

        for (double i : dist) {
            i = INF;
        }
        dist[start] = 0; // для начальной вершины положим 0

        for (; ; ) {
            int v = -1;
            for (int nv = 0; nv < vNum; nv++) // перебираем вершины
                if (!used[nv] && dist[nv] < INF && (v == -1 || dist[v] > dist[nv])) // выбираем самую близкую непомеченную вершину
                    v = nv;
            if (v == -1) break; // ближайшая вершина не найдена
            used[v] = true; // помечаем ее
            for (int nv = 0; nv < vNum; nv++)
                if (!used[nv] && graph[v][nv] < INF) // для всех непомеченных смежных
                    dist[nv] = Math.min(dist[nv], dist[v] + graph[v][nv]); // улучшаем оценку расстояния (релаксация)
        }
        return dist[fin];
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        main_panel = new JPanel();
        main_panel.setLayout(new BorderLayout(0, 0));
        panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        main_panel.add(panel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        pnlMenus = new JPanel();
        pnlMenus.setLayout(new BorderLayout(0, 0));
        panel.add(pnlMenus, BorderLayout.NORTH);
        menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        menuBar.setMargin(new Insets(0, 0, 0, 0));
        pnlMenus.add(menuBar, BorderLayout.NORTH);
        miNew.setArmed(false);
        miNew.setHideActionText(false);
        miNew.setHorizontalAlignment(2);
        miNew.setHorizontalTextPosition(11);
        miNew.setSelected(false);
        miNew.setText("");
        menuBar.add(miNew);
        miNew.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        miOpen.setArmed(false);
        miOpen.setHideActionText(false);
        miOpen.setHorizontalAlignment(2);
        miOpen.setText("");
        menuBar.add(miOpen);
        miOpen.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        miSave.setArmed(false);
        miSave.setHideActionText(true);
        miSave.setHorizontalAlignment(2);
        miSave.setText("");
        menuBar.add(miSave);
        miSave.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        miDelete.setArmed(false);
        miDelete.setHideActionText(true);
        miDelete.setHorizontalAlignment(2);
        menuBar.add(miDelete);
        miDelete.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        separator1 = new JSeparator();
        separator1.setEnabled(true);
        menuBar.add(separator1);
        miUndo.setArmed(false);
        miUndo.setHideActionText(true);
        miUndo.setHorizontalAlignment(2);
        miUndo.setText("");
        miUndo.setVisible(false);
        menuBar.add(miUndo);
        miUndo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        miRedo.setArmed(false);
        miRedo.setHideActionText(true);
        miRedo.setHorizontalAlignment(2);
        miRedo.setText("");
        miRedo.setVisible(false);
        menuBar.add(miRedo);
        miRedo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        toolBar = new JToolBar();
        toolBar.setVisible(false);
        pnlMenus.add(toolBar, BorderLayout.CENTER);
        toolBar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(toolBar.getFont().getName(), toolBar.getFont().getStyle(), toolBar.getFont().getSize())));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        toolBar.add(panel1);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        miMinCicle = new JMenuItem();
        miMinCicle.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
        miMinCicle.setHorizontalAlignment(2);
        miMinCicle.setHorizontalTextPosition(4);
        miMinCicle.setSelected(true);
        miMinCicle.setText("Цикл минимальной длины");
        panel1.add(miMinCicle, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        toolBar.add(panel2);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        miShortWay = new JMenuItem();
        miShortWay.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
        miShortWay.setText("Кратчайший путь");
        panel2.add(miShortWay, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        toolBar.add(panel3);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        miMinTree = new JMenuItem();
        miMinTree.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
        miMinTree.setText("Минимальное остовное дерево");
        panel3.add(miMinTree, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pnlButtons = new JPanel();
        pnlButtons.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(pnlButtons, BorderLayout.WEST);
        btnNode.setText("");
        pnlButtons.add(btnNode, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnOEdge.setText("");
        pnlButtons.add(btnOEdge, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNOEdge.setText("");
        pnlButtons.add(btnNOEdge, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDeleteGraphElem.setText("");
        pnlButtons.add(btnDeleteGraphElem, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCursor.setText("");
        pnlButtons.add(btnCursor, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        pnlButtons.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        pnlButtons.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 50), null, null, 0, false));
        tabEditor = new JTabbedPane();
        tabEditor.setTabPlacement(1);
        panel.add(tabEditor, BorderLayout.CENTER);
        mainTab = new JPanel();
        mainTab.setLayout(new BorderLayout(0, 0));
        tabEditor.addTab("Окно графического редактирования", mainTab);
        mainTab.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        statusBar = new JPanel();
        statusBar.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(statusBar, BorderLayout.SOUTH);
        statusBar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        statusBar.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
        lblCoords = new JLabel();
        lblCoords.setHorizontalAlignment(2);
        lblCoords.setHorizontalTextPosition(2);
        lblCoords.setText("");
        panel4.add(lblCoords, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(45, 15), null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        statusBar.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
        lblCurTask = new JLabel();
        lblCurTask.setText("");
        panel5.add(lblCurTask, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        statusBar.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
        lblCurTaskCaption = new JLabel();
        lblCurTaskCaption.setHorizontalAlignment(0);
        lblCurTaskCaption.setHorizontalTextPosition(0);
        lblCurTaskCaption.setText("Текуща задача");
        panel6.add(lblCurTaskCaption, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel6.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 1, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        statusBar.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        progressBar1 = new JProgressBar();
        progressBar1.setString("0");
        progressBar1.setValue(0);
        progressBar1.setVisible(false);
        panel7.add(progressBar1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        statusBar.add(panel8, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblStatus = new JLabel();
        lblStatus.setHorizontalAlignment(0);
        lblStatus.setHorizontalTextPosition(0);
        lblStatus.setText("Статус выполнения текущей задачи");
        lblStatus.setVisible(false);
        panel8.add(lblStatus, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel8.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 1, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main_panel;
    }
}
