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
    private NodeInformation dest;
    private NodeInformation nextHop;
    private int weight;
    
    /**
     * A class to represent a route between nodes.
     * 
     * @param address   the destination address
     * @param nextHop   the nextHop the packet should be forwarded to
     * @param weight    the weight of this route
     */
    public RoutingEntry(NodeInformation dest, NodeInformation nextHop, int weight)
    {
        this.dest = dest;
        this.nextHop = nextHop;
        this.weight = weight;
    }

    public NodeInformation getDest()
    {
        return dest;
    }
    
    public NodeInformation getNextHop()
    {
        return nextHop;
    }
    
    /**
     * Returns the destination address of this route.
     * 
     * @return the IP address
     */
    public int getDestID()
    {
        return dest.getID();
    }
    
    public String getDestName()
    {
        return dest.getName();
    }
    
    public String getNextHopName()
    {
        return nextHop.getName();
    }

    /**
     * Returns the nextHop that any packets for destination address should be forwarded to.
     * 
     * @return the nextHop 
     */
    public int getNextHopID()
    {
        return nextHop.getID();
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

    public NodeInformation getDestInfo()
    {
        return dest;
    }
    
    public NodeInformation getNextHopInfo()
    {
        return nextHop;
    }
    
    @Override
    public int compareTo(RoutingEntry t)
    {
        return this.weight - t.weight;
    }
    
    @Override
    public String toString()
    {
        return this.getDestName() + " " + this.getDestID() + " -(" 
                + this.getWeight() + ")> " 
                + this.getNextHopName() + " " + this.getNextHopID();
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
