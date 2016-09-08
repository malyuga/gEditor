package client.graphicElements;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Vector;

/**
 * Created by 1 on 6/22/2015.
 */
public class MyCanvas extends JPanel {

    Vector<Node> nodesToDraw;
    public JPopupMenu popupMenu;
    private String nodeName = null;
    private String info = null;
    private int number = -1;
    private boolean gotActiveNodeInfo = false;
    private Color color;

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setGotActiveNodeInfo(boolean gotActiveNodeInfo) {
        this.gotActiveNodeInfo = gotActiveNodeInfo;
    }
    public boolean gotActiveNodeInfo() {
        return gotActiveNodeInfo;
    }

    public String getActiveNodeName(){
        return nodeName;
    }

    public String getActiveNodeInfo(){
        return info;
    }

    public int getActiveNodeNumber(){
        return number;
    }

    public void setNodesToDraw(Vector<Node> nodesToDraw) {
        this.nodesToDraw = (Vector<Node>)nodesToDraw.clone();
    }

    public void addNodeToDraw(Node node){
        nodesToDraw.add(node);
    }

    public void clearNodesToDraw(){
        nodesToDraw.clear();
    }

    public Vector<Node> getNodesToDraw() {
        return nodesToDraw;
    }

    public MyCanvas() {
        nodesToDraw = new Vector<Node>();
        color = Color.gray;
    }

    public void drawOEdge(Node node1, Node node2, Graphics g){
        g.drawLine( (int)(node1.getLeft() + node1.getD()/2),
                    (int)(node1.getTop() + node1.getD()/2),
                    (int)(node2.getLeft() + node2.getD()/2),
                    (int)(node2.getTop() + node2.getD()/2));

        Graphics2D g2 = (Graphics2D)g;
        g2.draw(getArrowHeadShape(new Line2D.Double(node1.getLeft()+node1.getD()/2, node1.getTop()+node1.getD()/2, node2.getLeft()+node2.getD()/2, node2.getTop()+node2.getD()/2)));
    }
    public static Shape getArrowHeadShape(Line2D line) {

        double x1 = line.getX1(); double y1 = line.getY1();
        double x2 = line.getX2(); double y2 = line.getY2();

        Path2D arrowHead = new Path2D.Double();
        arrowHead.moveTo(6,6);
        arrowHead.lineTo(12, 0);
        arrowHead.lineTo(6,-6);

        double midX = (x1 + x2) / 2.0;
        double midY = (y1 + y2) / 2.0;

        double rotate = Math.atan2(y2 - y1, x2 - x1);

        AffineTransform transform = new AffineTransform();
        transform.translate(midX, midY);
        transform.rotate(rotate);

        return transform.createTransformedShape(arrowHead);
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(getX() - 2, getY() - 2, getWidth() + 4, getHeight() + 4);

        for(Node node : nodesToDraw){
            if(node.isActive()){
                g.setColor(Color.blue);
                g.drawOval((int)node.getLeft() - 2, (int)node.getTop() - 2, node.getD() + 4, node.getD() + 4);
            }
            else{
                g.setColor(Color.black);
            }

            if(node.getNodeName()!=null){
                node.draw(g);
            }
            else{
                node.drawEmpty(g);
            }
        }

        g.setColor(Color.black);
        for(Node node1 : nodesToDraw){
            for(Edge edge : node1.getEdgesTo()){
                drawOEdge(node1, edge.getNode(), g);
            }
        }
    }
}

