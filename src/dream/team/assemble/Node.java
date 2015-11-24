/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dream.team.assemble;

import java.util.ArrayList;

/**
 *
 * @author Dan
 */
public class Node
{

    String name;
    ArrayList<Link> connections; //represents physical restrictions of network, not our routing
    final int myPort;
    final String myIP;

    public Node(String name, int myPort, String myIP)
    {
        this.name = name;
        this.connections = new ArrayList<>();
        this.myPort = myPort;
        this.myIP = myIP;
    }

    public String getAddress()
    {
        return myIP + ":" + myPort;
    }
    
    public String description()
    {
        String tmp = "";
        tmp += "Name = " + name;
        tmp += ", myPort = " + myPort;
        tmp += ", heardBy = " + heardByToString() + "\n";
        return tmp;
    }

    @Override
    public String toString()
    {
        return "Name = " + name + ", heardBy = " + heardByToString();
    }

    private String heardByToString()
    {
        String tmp = "";
        for (int i = 0; i < connections.size(); i++)
        {
            tmp += " " + connections.get(i).getConnection(this).name;
        }
        return tmp;
    }

    public void addListener(Node node)
    {
        connections.add(generateLink(node));
    }
    
    public Link generateLink(Node node)
    {
        return new Link(this, node, ping(node));
    }
    
    // Returns the ping between this.node and node
    public int ping(Node node)
    {
        return 1;
    }
}
