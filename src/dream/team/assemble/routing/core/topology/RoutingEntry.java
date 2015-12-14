package dream.team.assemble.routing.core.topology;

/**
 * A class to represent a route between nodes.
 * 
 * @author Cal
 * @see RoutingTable
 * @see Node
 */
public class RoutingEntry implements Comparable<RoutingEntry>
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
     * @return the IP address and port (and currently name too!)
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
}
