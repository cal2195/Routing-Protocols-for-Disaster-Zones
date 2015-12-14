package dream.team.assemble.routing.core.topology;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * A class to represent a route between nodes.
 * 
 * @author Cal
 * @see RoutingTable
 * @see Node
 */
public class RoutingEntry implements Serializable, Comparable<RoutingEntry>  
{
    private String dest;
    private String node;
    private int weight;
    
    /**
     * A class to represent a route between nodes.
     * 
     * @param address   the destination address
     * @param node      the node the packet should be forwarded to
     * @param weight    the weight of this route
     */
    public RoutingEntry(String dest, String node, int weight)
    {
        this.dest = dest;
        this.node = node;
        this.weight = weight;
    }

    /**
     * Returns the destination address of this route.
     * 
     * @return the IP address
     */
    public String getDest()
    {
        return dest;
    }

    /**
     * Returns the node that any packets for destination address should be
     * forwarded to.
     * 
     * @return the node 
     */
    public String getNode()
    {
        return node;
    }

    /**
     * The weight of this route.
     * 
     * @return the weight (ping)
     */
    public int getWeight()
    {
        return weight;
    }

    @Override
    public int compareTo(RoutingEntry t)
    {
        return this.weight - t.weight;
    }
    
    public void increment()
    {
        this.weight++;
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
