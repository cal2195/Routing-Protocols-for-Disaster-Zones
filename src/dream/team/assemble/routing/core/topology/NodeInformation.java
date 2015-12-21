package dream.team.assemble.routing.core.topology;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * A class that represents nodes on the network.
 * 
 * @author Dan
 * @author Cal
 * @see LinkInformation
 */
public class NodeInformation implements Serializable
{
    private final String IP;
    private String name;
    private ArrayList<LinkInformation> links;
    
    //Used in calculation of Link State Routing
    private int nodeWeight = 0;
    

    /**
     * A class to represent information held on a network node.
     * 
     * @param name      the name of the node
     * @param myIP      the connection IP address
     */
    public NodeInformation(String name, String myIP)
    {
        this.name = name;
        this.IP = myIP;
        this.links = new ArrayList<>();
    }

    /**
     * Returns the address and name of this node in a pretty way.
     * 
     * @return the address and name of this node
     */
    public String getPrettyAddress()
    {
        return IP + "[" + name + "]";
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
     * @see LinkInformation
     */
    public ArrayList<LinkInformation> getLinks()
    {
        return links;
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
    public void addLink(NodeInformation node)
    {
        links.add(generateLink(node));
    }
    
    private LinkInformation generateLink(NodeInformation node)
    {
        for (LinkInformation link : node.links)
        {
            if (link.getConnection(node) == this)
            {
                return link;
            }
        }
        return new LinkInformation(this, node, ping(node));
    }
    
    // Returns the ping between this.node and node
    public int ping(NodeInformation node)
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
    
    public byte[] getByteArr()
    {
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            byte[] temp = bos.toByteArray();
        return temp;
        }
        catch (IOException e){
            System.err.println("Something bad happened serialising a NodeInformation");
        }
        return null;
    }
    
    /**
     * Returns if address and name are equal.
     * As part of specification we assume links never change.
     * @param b
     * @return 
     */
    @Override
    public boolean equals(Object b)
    {
        NodeInformation toCompare = (NodeInformation) b;
        return (this.getPrettyAddress().equals(toCompare.getPrettyAddress()));
    }
    
    private void writeObject(java.io.ObjectOutputStream out)throws IOException
     {
         out.defaultWriteObject();
     }
     
     private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
     {
         in.defaultReadObject();
     }
     
    private void readObjectNoData() throws ObjectStreamException
    {
        
    }
}
