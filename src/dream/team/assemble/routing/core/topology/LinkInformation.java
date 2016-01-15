package dream.team.assemble.routing.core.topology;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * A class to represent a directionless Link between two Nodes.
 * 
 * @author Cal
 * @see NodeInformation
 */
public class LinkInformation implements Serializable
{
    private final NodeInformation[] nodes = new NodeInformation[2];
    private final int weight;
    
    /**
     * A class to represent a directionless Link between two Nodes.
     * 
     * @param connectFrom   the node to connect from
     * @param connectTo     the node to connect to
     * @param weight        the weight (ping) of the link
     */
    public LinkInformation(NodeInformation connectFrom, NodeInformation connectTo, int weight)
    {
        nodes[0] = connectFrom;
        nodes[1] = connectTo;
        this.weight = weight;
    }
    
     /**
     * This constructor gives everything a default weight of 1.
     * @param connectFrom   the node to connect from
     * @param connectTo     the node to connect to
     * @param weight        the weight (ping) of the link
     */
    public LinkInformation(NodeInformation connectFrom, NodeInformation connectTo)
    {
        nodes[0] = connectFrom;
        nodes[1] = connectTo;
        this.weight = 1;
    }
     
    /**
     * Returns the node that this link connects connectFrom to.
     * 
     * @param connectFrom   one node in the link
     * @return the other node in the link
     */
    public NodeInformation getConnection(NodeInformation connectFrom)
    {
        return ((nodes[0] == connectFrom) ? nodes[1] : nodes[0]);
    }
    
    
     /**
     * Returns the node that this link connects the node with the IP connectFrom to.
     * 
     * @param connectFrom   one node in the link
     * @return the other node in the link
     */
    public NodeInformation getConnection(int connectFrom)
    {
        return ((nodes[0].getID() == connectFrom) ? nodes[1] : nodes[0]);
    }
    
    /**
     * Returns whether node is part of this link.
     * 
     * @param node  the node to check
     * @return true if the node is part of this link, false otherwise
     */
    public boolean contains(NodeInformation node)
    {
        return (nodes[0] == node || nodes[1] == node);
    }
    
     /**
     * Returns whether nodeIP IP is part of this link.
     * 
     * @param nodeIP  the nodeIP to check
     * @return true if the nodeIP is part of this link, false otherwise
     */
    public boolean contains(String nodeIP)
    {
        return (nodes[0].getID().equals(nodeIP) || nodes[1].getID().equals(nodeIP));
    }
    
    

    /**
     * Returns the weight (ping) of this link.
     * 
     * @return the weight (ping) of this link
     */
    public int getWeight()
    {
        return weight;
    }
    
    @Override
    public String toString()
    {
        return nodes[0].getPrettyAddress()+ " <-> " + nodes[1].getPrettyAddress() + " weight = " + weight;
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
        System.out.println("Something bad happened serialising a routingEntry!");
    }
}
