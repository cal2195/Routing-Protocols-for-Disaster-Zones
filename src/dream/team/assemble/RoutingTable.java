package dream.team.assemble;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Cal
 */
public class RoutingTable
{

    ArrayList<RoutingEntry> table = new ArrayList<>();
    Node defaultNode; // Node which represents the IP '*.*.*.*'

    public void setDefault(Node node)
    {
        defaultNode = node;
    }

    public void addEntry(String IP, Node node, int weight)
    {
        table.add(new RoutingEntry(IP, node, weight));
    }

    public Node getNextHop(String IP)
    {
        for (RoutingEntry routingEntry : table)
        {
            if (routingEntry.address.equals(IP))
            {
                return routingEntry.node;
            }
        }
        return defaultNode;
    }

    public boolean contains(String IP)
    {
        for (RoutingEntry routingEntry : table)
        {
            if (routingEntry.address.equals(IP))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        String result = "";
        for (RoutingEntry routingEntry : table)
        {
            result += routingEntry.address + " -(" + routingEntry.weight + ")> " + routingEntry.node.getAddress() + "\n";
        }
        return result;
    }
}
