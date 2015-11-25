package dream.team.assemble;

import java.util.ArrayList;

/**
 * A class to represent a routing table.
 * 
 * @author Cal
 * @see RoutingEntry
 * @see Node
 */
public class RoutingTable
{
    private ArrayList<RoutingEntry> table = new ArrayList<>();
    private Node defaultNode; // Node which represents the IP '*.*.*.*'

    /**
     * Set the default route. Any packets with a destination not in the table
     * will be forwarded to this node.
     * 
     * @param node the default node
     */
    public void setDefault(Node node)
    {
        defaultNode = node;
    }

    /**
     * Adds an entry to this routing table.
     * 
     * @param IP        the destination IP address and port
     * @param node      the node to forward packets to
     * @param weight    the weight of this route
     */
    public void addEntry(String IP, Node node, int weight)
    {
        table.add(new RoutingEntry(IP, node, weight));
    }

    /**
     * Returns the node that packets bound for IP should be forwarded to.
     * 
     * @param IP the destination IP address and port
     * @return the node the packet should be forwarded to
     */
    public Node getNextHop(String IP)
    {
        for (RoutingEntry routingEntry : table)
        {
            if (routingEntry.getAddress().equals(IP))
            {
                return routingEntry.getNode();
            }
        }
        return defaultNode;
    }

    /**
     * Returns whether the routing table contains a route to an address.
     * 
     * @param address the address to check
     * @return true if the routing table contains address, false otherwise
     */
    public boolean contains(String address)
    {
        return table.stream().anyMatch((routingEntry) -> (routingEntry.getAddress().equals(address)));
    }

    @Override
    public String toString()
    {
        String result = "";
        for (RoutingEntry routingEntry : table)
        {
            result += routingEntry.getAddress() + " -(" + routingEntry.getWeight() + ")> " + routingEntry.getNode().getPrettyAddress()+ "\n";
        }
        return result;
    }
}
