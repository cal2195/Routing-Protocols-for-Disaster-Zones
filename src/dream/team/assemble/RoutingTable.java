package dream.team.assemble;

import java.util.Hashtable;

/**
 *
 * @author Cal
 */
public class RoutingTable
{
    Hashtable<String, Node> table;
    Node defaultNode; // Node which represents the IP '*.*.*.*'
    
    public void setDefault(Node node)
    {
        defaultNode = node;
    }
    
    public void addEntry(String IP, Node node)
    {
        table.put(IP, node);
    }
    
    public Node getNextHop(String IP)
    {
        return table.getOrDefault(IP, defaultNode);
    }
}
