package dream.team.assemble.core.topology;

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
    public void addEntry(Node dest, Node node, int weight)
    {
        table.add(new RoutingEntry(dest, node, weight));
    }

    /**
     * Returns the node that packets bound for IP should be forwarded to.
     * 
     * @param IP the destination IP address and port
     * @return the node the packet should be forwarded to
     */
    public Node getNextHop(Node IP)
    {
        for (RoutingEntry routingEntry : table)
        {
            if (routingEntry.getDest().getPrettyAddress().equals(IP.getPrettyAddress()))
            {
                return routingEntry.getNode();
            }
        }
        return defaultNode;
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
        return table.stream().anyMatch((routingEntry) -> (routingEntry.getDest().getPrettyAddress().equals(address)));
    }

    @Override
    public String toString()
    {
        String result = "";
        for (RoutingEntry routingEntry : table)
        {
            result += routingEntry.getDest().getPrettyAddress()+ " -(" + routingEntry.getWeight() + ")> " + routingEntry.getNode().getPrettyAddress()+ "\n";
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
    
    
    //CURRENTLY BROKEN - need to know source of new table to add entries correctly
//    private void addAndUpdateEntries(RoutingTable newInfo)
//    {
//        for(int i = 0; i < newInfo.getSize() ;i++)
//        {  
//            RoutingEntry currentNew = newInfo.table.get(i);
//            boolean duplicate = false;
//            
//            for(int j = 0; j < table.size() && !duplicate; j++)
//            {
//                RoutingEntry currentOld = table.get(i);
//                if(currentOld.getAddress().equals(currentNew.getAddress()))
//                {
//                    duplicate = true;
//                    if(currentNew.getWeight() < currentOld.getWeight())
//                        currentOld = currentNew;                    //ADDS INCORRECT INFO!
//                }
//            }
//            if(!duplicate)
//                table.add(currentNew);
//        }
//    }
    
    public int getSize()
    {
        return table.size();
    }

}
