package client.graphicElements;

import javax.swing.*;

/**
 * Created by 1 on 6/26/2015.
 */
public class Edge extends JComponent {

    private Node node;
    private double weight;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Node getNode() {
        return node;
    }

    public Edge(Node node, double weight){
        this.node = node;
        this.weight = weight;
    }

    public String toString(){
        return node.getNodeName() + ":" + Double.toString(weight);
    }

}
