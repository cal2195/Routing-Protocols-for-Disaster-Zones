/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dream.team.assemble;

import java.util.ArrayList;
import java.util.Hashtable;

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
    int tempWeight = 0;

    public Node(String name, int myPort, String myIP)
    {
        this.name = name;
        this.connections = new ArrayList<>();
        this.myPort = myPort;
        this.myIP = myIP;
    }

    public String getAddress()
    {
        return myIP + ":" + myPort + "[" + name + "]";
    }
    
    public String description()
    {
        String tmp = "";
        tmp += "Name = " + name;
        tmp += ", myPort = " + myPort;
        tmp += ", heardBy = " + heardByToString();
        tmp += ", weight = " + tempWeight + "\n";
        return tmp;
    }

    @Override
    public String toString()
    {
        return getAddress();
        //return "Name = " + name + ", heardBy = " + heardByToString();
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
        for (Link link : node.connections)
        {
            if (link.getConnection(node) == this)
            {
                return link;
            }
        }
        return new Link(this, node, ping(node));
    }
    
    // Returns the ping between this.node and node
    public int ping(Node node)
    {
        Hashtable<String, Integer> pings = new Hashtable<>();
        pings.put("AB", 2);
        pings.put("AG", 6);
        pings.put("BE", 2);
        pings.put("EG", 1);
        pings.put("BC", 7);
        pings.put("EF", 2);
        pings.put("GH", 4);
        pings.put("FC", 3);
        pings.put("CD", 3);
        pings.put("DH", 2);
        pings.put("FH", 2);
        
        int ping = pings.getOrDefault(this.name + node.name, -1);
        if (ping == -1)
        {
            ping = pings.getOrDefault(node.name + this.name, -1);
        }
        return ping;
    }
}
