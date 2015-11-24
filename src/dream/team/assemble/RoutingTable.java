package dream.team.assemble;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Cal
 */
public class RoutingTable
{
    Hashtable<String, Node> table = new Hashtable<>();
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
    
    public boolean contains(Node node)
    {
        return table.containsValue(node);
    }
    
    @Override
    public String toString()
    {
        String result = "";
        for (Map.Entry<String, Node> entry : table.entrySet())
        {
            result += entry.getKey() + " -> " + entry.getValue().myIP + "(" + entry.getValue().name + ")";
        }
        return result;
    }
}
