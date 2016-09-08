package client.graphicElements;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Created by 1 on 6/22/2015.
 */
public class Node extends JComponent{


    public Node(long id, long g_id, long number, String nodeName, String info, long x, long y) {
        this.G_id = g_id;
        this.id = id;
        this.number = number;
        this.info = info;
        this.nodeName = nodeName;
        this.x = x;
        this.y = y;
        this.left = (int)x;
        this.top = (int)y;
        edgesTo = new HashSet<Edge>();
        referers = new HashSet<Node>();
        this.visible = true;
        this.color = Color.black;
    }

    private int left;
    private int top;
    private long x;
    private long y;
    private int d = 10;
    private Color color = new Color(50,70,200);
    private boolean visible;
    private boolean active;
    private HashSet<Edge> edgesTo;
    private HashSet<Node> referers;
    private String nodeName;
    private String info;
    private long number;
    private long G_id;
    private long id = 0;

    public void setId(long in_id) {
        this.id = in_id;
    }
    public long getId() {
        return id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String name) {
        this.nodeName = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getLeft() {
        return left;
    }

    public long getTop() {
        return top;
    }
    public void setActive(boolean b){
        this.active = b;
    }

    public boolean isActive(){
        return active;
    }

    public void setEdgeTo(Edge edge){
        edgesTo.add(edge);
    }

    public HashSet<Edge> getEdgesTo(){
        return edgesTo;
    }

    public void setReferer(Node node){
        referers.add(node);
    }

    public HashSet<Node> getReferers(){
        return referers;
    }

    public int getD() {
        return d;
    }

    public void setPosition(int x, int y){
        this.left = x;
        this.top = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Node(int left, int top, int d){
        this.left = left;
        this.top = top;
        this.d = d;
        this.edgesTo = new HashSet<Edge>();
        this.referers = new HashSet<Node>();
        this.x = left;
        this.y = top;
    }

    public Node(Node anotherNode) throws IllegalAccessException {

        this.left = anotherNode.left;
        this.top = anotherNode.top;
        this.x = anotherNode.left;
        this.y = anotherNode.top;

        this.d = anotherNode.d;
        this.color = anotherNode.color;
        this.visible = anotherNode.visible;
        this.active = false;
        this.edgesTo = new HashSet<Edge>();
        for(Edge edge : anotherNode.getEdgesTo()){
            this.edgesTo.add(edge);
        }

        this.referers = new HashSet<Node>();
        for (Node node : anotherNode.getReferers()) {
            this.referers.add(node);
        }

        this.nodeName = anotherNode.getNodeName();
        this.info = anotherNode.getInfo();
        this.number = anotherNode.getNumber();
        this.id = 0;
        this.G_id = anotherNode.G_id;
    }

    public void setVisible(boolean f){
        visible = f;
    }

    public boolean isVisible(){
        return visible;
    }

    public void draw(Graphics g){
        if (visible) {
            g.setColor(color);
            g.fillOval((int) left, (int) top, d, d);
            g.drawString(getNumber() + "." + getNodeName(), (int) getLeft(), (int) getTop() + 23);
        }
    }

    public void drawEmpty(Graphics g){
        if (visible){
            g.setColor(color);
            g.drawOval((int)left, (int)top, d, d);
        }
    }

    public boolean contains(int x, int y){
        return ((x>=left)&&(x<=left+d)&&(y>=top)&&(y<=top+d));
    }
}
