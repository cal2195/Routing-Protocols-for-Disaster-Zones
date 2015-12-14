package dream.team.assemble.routing.core.topology;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public void addEntry(String dest, String node, int weight)
    {
        table.add(new RoutingEntry(dest, node, weight));
    }

    /**
     * Returns the node that packets bound for IP should be forwarded to.
     * 
     * @param IP the destination IP address and port
     * @return the node the packet should be forwarded to
     */
    public String getNextHop(String IP)
    {
        for (RoutingEntry routingEntry : table)
        {
            if (routingEntry.getDest().equals(IP))
            {
                return routingEntry.getNode();
            }
        }
        return "err";
    }

    public ArrayList<RoutingEntry> getTable()
    {
        return table;
    }

    /**
     * Returns whether the routing table contains a route to an address.
     * 
     * @param address the address to check
     * @return true if the routing table contains address, false otherwise
     */
    public boolean contains(String address)
    {
        return table.stream().anyMatch((routingEntry) -> (routingEntry.getDest().equals(address)));
    }

    @Override
    public String toString()
    {
        String result = "";
        for (RoutingEntry routingEntry : table)
        {
            result += routingEntry.getDest() + " -(" + routingEntry.getWeight() + ")> " + routingEntry.getNode() + "\n";
        }
        return result;
    }
    
    private byte[] getRoutingTableBytes() throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(table);
        byte[] temp = bos.toByteArray();
        return temp;
    }
    
    private void updateRoutingTable(byte[] receivedTable) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(receivedTable);
        ObjectInputStream ois = new ObjectInputStream(bis);
        RoutingTable received = (RoutingTable) ois.readObject();
        received.incrementAll();
        //addAndUpdateEntries(received);
    }
    
    private void incrementAll(){
        for(int i = 0; i < table.size(); i++)
            table.get(i).increment();
    }
    
    public int getSize()
    {
        return table.size();
    }

}
