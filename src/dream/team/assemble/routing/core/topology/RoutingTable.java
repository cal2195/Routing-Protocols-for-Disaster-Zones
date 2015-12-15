package dream.team.assemble.routing.core.topology;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
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

    public RoutingTable(ArrayList<RoutingEntry> t)
    {
        this.table = t;
    }
    
    public RoutingTable()
    {
    }
    
    
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
        for(RoutingEntry e : table)
        {
            if(e.getDest().equals(address))
             return true;
        }
        return false;
        //return table.stream().anyMatch((routingEntry) -> (routingEntry.getDest().equals(address)));
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
    
    public byte[] getRoutingTableBytes()
    {
        try
        {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(table);
        byte[] temp = bos.toByteArray();
        return temp;
        }
        catch (IOException e){}
        return null;
    }
    
    public void updateRoutingTable(byte[] receivedTable, String srcAddr)
    {       
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(receivedTable);
            ObjectInputStream ois = new ObjectInputStream(bis);
            ArrayList receivedList = (ArrayList<RoutingEntry>) ois.readObject();
            RoutingTable received = new RoutingTable (receivedList);
            this.addAndUpdateEntries(received, srcAddr);
        }
        catch(IOException | ClassNotFoundException e){}
    }
    
    private void addAndUpdateEntries(RoutingTable received, String srcAddr)
    { 
        received.incrementAll();
        for(RoutingEntry receivedEntry : received.table)
        {
            boolean found = false;
            for(RoutingEntry oldEntry : table)
            {
                //address already exists in routing table
                if(oldEntry.getDest().equals(receivedEntry.getDest()))
                {
                    found = true;
                    //replace if lighter weight
                    if(receivedEntry.compareTo(oldEntry) < 0)
                    {
                        RoutingEntry tmp = new RoutingEntry(receivedEntry.getDest(), received.table.get(0).getDest(), receivedEntry.getWeight());
                        //table.add(tmp);
                    } 
                }
            }
            //new destination? add to table
            if(!found)
            {
                RoutingEntry tmp = new RoutingEntry(receivedEntry.getDest(), received.table.get(0).getDest(), receivedEntry.getWeight());
                table.add(tmp);
            }
            
        }
        
    }
    
    private void incrementAll(){
        for(int i = 0; i < table.size(); i++)
            table.get(i).increment();
    }
    
    public int getSize()
    {
        return table.size();
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        RoutingTable tmp = new RoutingTable();
        tmp.addEntry("1.1.1.1", "1.1.1.1", 0);
        byte[] tmpBytes = tmp.getRoutingTableBytes();
        ByteArrayInputStream bis = new ByteArrayInputStream(tmpBytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        RoutingTable received = new RoutingTable((ArrayList<RoutingEntry>) ois.readObject());
        System.out.println(received.toString());
    }

}
