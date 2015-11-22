/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routingprotocols;

import java.util.ArrayList;

/**
 *
 * @author Dan
 */
public class Node {
    String name;
    ArrayList<Integer> heardBy;
    ArrayList<Node> heardBy2;
    final int myPort;
    
    public Node(String name, int myPort){
        this.name = name;
        this.heardBy = new ArrayList<>();
        this.myPort = myPort;
    }
    
    @Override
    public String toString()
    {
        String tmp = "";
        tmp += "Name = " + name;
        tmp += ", myPort = " + myPort;
        tmp += ", heardBy = " + heardBy.toString() + "\n";
        return tmp;
    }
    
    public String shortToString()
    {
        return name;
    }
    public void addListener(int port){
        heardBy.add(port);
    }
}
