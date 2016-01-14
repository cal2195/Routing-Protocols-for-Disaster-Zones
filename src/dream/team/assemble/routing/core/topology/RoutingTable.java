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
 * @see NodeInformation
 */
public class RoutingTable
{
    private ArrayList<RoutingEntry> table = new ArrayList<>();
    private String lastUpdates = "No updates.\n";

    public RoutingTable(ArrayList<RoutingEntry> t)
    {
        this.table = t;
    }
    
    public RoutingTable()
    {
    }

    /**
     * Adds an entry to this routing table.
     * 
     * @param IP        the destination IP address and port
     * @param node      the node to forward packets to
     * @param weight    the weight of this route
     */
    public void addEntry(NodeInformation dest, NodeInformation node, int weight)
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
            if (routingEntry.getDestIP().equals(IP))
            {
                return routingEntry.getNextHopIP();
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
            if(e.getDestIP().equals(address))
             return true;
        }
        return false;
        //return table.stream().anyMatch((routingEntry) -> (routingEntry.getDest().equals(address)));
    }

    @Override
    public String toString()
    {
        table.sort(null);
        String result = "";
        for (RoutingEntry routingEntry : table)
        {
            result += routingEntry.getDestName() + " " + routingEntry.getDestIP() + " -(" 
                    + routingEntry.getWeight() + ")> " 
                    + routingEntry.getNextHopName() + " " + routingEntry.getNextHopIP()+ "\n";
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
        catch (IOException e){
            System.err.println("Something bad happened serialising a routing table");
        }
        return null;
    }
    
    /**
     * Deserialises routing table, then adds and updates it's own table.
     * @param receivedTable
     */
    public void updateRoutingTable(byte[] receivedTable)
    {       
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(receivedTable);
            ObjectInputStream ois = new ObjectInputStream(bis);
            ArrayList receivedList = (ArrayList<RoutingEntry>) ois.readObject();
            RoutingTable received = new RoutingTable (receivedList);
            this.addAndUpdateEntries(received);
        }
        catch(IOException | ClassNotFoundException e){}
    }
    
    /**
     * Adds new routing entries, updates entries with shorter paths available.
     * Creates a String summarising updates for logfiles.
     * @param received 
     */
    private void addAndUpdateEntries(RoutingTable received)
    { 
        lastUpdates = "";
        received.incrementAll();
        for(RoutingEntry receivedEntry : received.table)
        {
            RoutingEntry tmp = null;
            RoutingEntry removePointer = null;
            boolean found = false;
            for(RoutingEntry oldEntry : table)
            {
                //address already exists in routing table
                if(oldEntry.getDestIP().equals(receivedEntry.getDestIP()))
                {
                    found = true;
                    //replace if lighter weight
                    if(receivedEntry.compareTo(oldEntry) < 0)
                    {
                        removePointer = oldEntry;
                        tmp = new RoutingEntry(receivedEntry.getDestInfo(), received.table.get(0).getDestInfo(), receivedEntry.getWeight());
                    } 
                }
            }
            //new destination? add to table
            if(!found)
            {
                tmp = new RoutingEntry(receivedEntry.getDestInfo(), received.table.get(0).getDestInfo(), receivedEntry.getWeight());
                table.add(tmp);
                lastUpdates += "Added " + tmp.toString() + "\n";
            }
            //if found lighter way to same node, replace
            else if(tmp != null)
            {
                lastUpdates += "" + removePointer.toString() + " replaced with " + tmp.toString() + "\n";
                table.remove(removePointer);
                table.add(tmp);
            }
        }
        
    }
    
    /**
     * Returns a string representation of the last updates made to this routing table.
     * @return 
     */
    public String getUpdatesString()
    {
        return lastUpdates;
    }
    
    /**
     * Increments all weights in this routing table.
     */
    private void incrementAll(){
        for(int i = 0; i < table.size(); i++)
            table.get(i).increment();
    }
    
    public int getSize()
    {
        return table.size();
    }

}
