package dream.team.assemble.core.topology;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * A class that represents nodes on the network.
 * 
 * @author Dan
 * @author Cal
 * @see Link
 */
public class Node
{
    private String name;
    private ArrayList<Link> links; //represents physical restrictions of network, not our routing
    private final int port;
    private final String IP;
    private int nodeWeight = 0;

    /**
     * A class to represent a node on the network.
     * 
     * @param name      the name of the node
     * @param myPort    the connection port
     * @param myIP      the connection IP address
     */
    public Node(String name, int myPort, String myIP)
    {
        this.name = name;
        this.links = new ArrayList<>();
        this.port = myPort;
        this.IP = myIP;
    }

    /**
     * Returns the address and name of this node in a pretty way.
     * 
     * @return the address and name of this node
     */
    public String getPrettyAddress()
    {
        return IP + ":" + port + "[" + name + "]";
    }
    
    /**
     * Returns a full description of this node ready for printing.
     * 
     * @return a full description of this node
     */
    public String description()
    {
        String tmp = "";
        tmp += "Name = " + name;
        tmp += ", myPort = " + port;
        tmp += ", heardBy = " + heardByToString();
        tmp += ", weight = " + nodeWeight + "\n";
        return tmp;
    }

    @Override
    public String toString()
    {
        return getPrettyAddress();
    }

    private String heardByToString()
    {
        String tmp = "";
        for (int i = 0; i < links.size(); i++)
        {
            tmp += " " + links.get(i).getConnection(this).name;
        }
        return tmp;
    }
    
        public String heardByIPsToString()
    {
        String tmp = "";
        for (int i = 0; i < links.size(); i++)
        {
            tmp += " " + links.get(i).getConnection(this).getIP();
        }
        return tmp;
    }

    /**
     * Returns the nodes weight (ping).
     * 
     * @return this nodes weight (ping)
     */
    public int getNodeWeight()
    {
        return nodeWeight;
    }

    /**
     * Sets this nodes weight (ping).
     * 
     * @param nodeWeight the weight to set
     */
    public void setNodeWeight(int nodeWeight)
    {
        this.nodeWeight = nodeWeight;
    }

    /**
     * Returns the name of this node.
     * 
     * @return the nodes name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns an ArrayList of this nodes Links (connections) to other nodes.
     * 
     * @return the list of links
     * @see Link
     */
    public ArrayList<Link> getLinks()
    {
        return links;
    }

    /**
     * Returns the port this node should be listening on.
     * 
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Returns the IP address this node should be listening on.
     * 
     * @return the IP address
     */
    public String getIP()
    {
        return IP;
    }

    /**
     * Adds a link to the list of links this node can see.
     * 
     * @param node the node to add
     */
    public void addLink(Node node)
    {
        links.add(generateLink(node));
    }
    
    private Link generateLink(Node node)
    {
        for (Link link : node.links)
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
//        Hashtable<String, Integer> pings = new Hashtable<>();
//        pings.put("AB", 2);
//        pings.put("AG", 6);
//        pings.put("BE", 2);
//        pings.put("EG", 1);
//        pings.put("BC", 7);
//        pings.put("EF", 2);
//        pings.put("GH", 4);
//        pings.put("FC", 3);
//        pings.put("CD", 3);
//        pings.put("DH", 2);
//        pings.put("FH", 2);
//        
//        int ping = pings.getOrDefault(this.name + node.name, -1);
//        if (ping == -1)
//        {
//            ping = pings.getOrDefault(node.name + this.name, -1);
//        }
//        return ping;
        return 1;
    }
}
