package dream.team.assemble.routing.core;

import dream.team.assemble.routing.core.topology.NodeInformation;
import dream.team.assemble.routing.core.topology.RoutingEntry;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import dream.team.assemble.routing.core.topology.RoutingTable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Standalone AbstractRouter object.
 * 
 * @author aran
 */
public abstract class AbstractRouter
{
    private final String localIP;
    private ArrayList<String> log;
    private final boolean logToFile = true;
    private PrintWriter logFile = null;
    private final ArrayList<String> visibleIPs;
    private final HashMap<String, String> receivedBroadcasts;  
    private final int MAX_REMEMBERED = 255;
    private final RoutingTable routingTable;
    private final ArrayList<NodeInformation> LSNodeInfo;
    private final String name;
    private final String nameAndIP;
    
    public AbstractRouter(String name, String ip)
    {
        this.name = name;
        this.visibleIPs = new ArrayList<>();
        this.localIP = ip;
        this.nameAndIP = name + " " + localIP;
        if(logToFile){
            try{
           logFile = new PrintWriter(nameAndIP + "logFile.logFile", "UTF-8");
            }
            catch(FileNotFoundException|UnsupportedEncodingException e){}
        }
        log = new ArrayList<>();

        receivedBroadcasts = new HashMap<>(MAX_REMEMBERED, (float) 1.0);  
        LSNodeInfo = new ArrayList<>();
        routingTable = new RoutingTable();
        //add self as first entry in table
        NodeInformation myInfo = new NodeInformation(name, ip);
        routingTable.addEntry(myInfo, myInfo, 0);
    }
    
    public String getAddress()
    {
        return localIP;
    }
    
    /**
     * Action to take upon receiving a data packet.
     * 
     * @param data the data packet received
     */
    public void onReceipt(byte[] data)
    {
        RouterPacket packet = new RouterPacket(data);
        String logString = packet.toString();
        
        String dstAddr = packet.getDstAddr();
        
        //handles broadcasts - checks if it has already received an identical message from the same source, if so ignores, otherwise rebroadcasts
        if(packet.isBroadcast())
        {
            String payload = new String(packet.getPayload());
            
            String broadcast = packet.getSrcAddr() + " " + payload;
            
            if(!receivedBroadcasts.containsKey(broadcast))
            {
                //clear memory after 254 unique broadcasts received
                if(receivedBroadcasts.size() >= MAX_REMEMBERED - 1)
                    receivedBroadcasts.clear();
                
                receivedBroadcasts.put(broadcast, broadcast);
                
                
            
                //if flags == 1 then distance vector routing table
                if(packet.getFlags() == 1)
                {
                    routingTable.updateRoutingTable(packet.getPayload());
                    
                    logString += " comparing routing table with table from " + packet.getSrcAddr() + "\n";
                    logString += routingTable.getUpdatesString();
                    log.add(logString);
                    logFile.write(logString + "\n"); 
                    logFile.flush();
                    
                    broadcast(1, routingTable.getRoutingTableBytes());
                }
                //if flags == 2 then NodeInformation for link state routing
                else if(packet.getFlags() == 2)
                {
                    try{
                        ByteArrayInputStream bis = new ByteArrayInputStream(packet.getPayload());
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        NodeInformation receivedNodeInfo = (NodeInformation) ois.readObject();
                        System.out.println(receivedNodeInfo.toString());
                        LSNodeInfo.add(receivedNodeInfo);
                    }
                    catch(IOException | ClassNotFoundException e){}           
                }
                else
                {
                
                sendToAllVisible(data);
                logString += " " + new String(packet.getPayload());
                // TEMPORARY -->
                System.out.println(nameAndIP + ": " + new String(packet.getPayload()));
                // TEMPORARY --<   
                }

            }
            
            //if a previously seen broadcast, no further action
            return;

        }
        /* if addressed for this AbstractRouter then handle it as is appropriate for packet type */
        if (localIP.equals(dstAddr))
        {
            logString += " " + new String(packet.getPayload());
            // packet handling stuff goes here
           
            
            // TEMPORARY -->
            System.out.println(nameAndIP + ": " + new String(packet.getPayload()));
            // TEMPORARY --<
            
        }
        /* if addressed for another node then pass to address of next hop */
        else 
        {
            String nextAddr = routingTable.getNextHop(dstAddr);
            System.out.println(nameAndIP + " - routed to " + nextAddr);
            logString += localIP + " - routed to " + nextAddr;
            send(data, nextAddr);
        }
        
        log.add(logString);
        if(logToFile)
        {
            logFile.write(logString + "\n"); 
            logFile.flush();
        }
        
    }
    
    
    /**
     * Broadcast this payload with the given flags.
     * All routers will parse the payload appropriately and retransmit.
     * flags == 1 for Distance Vector Routing Table payload
     * @param flags
     * @param payload 
     */
    public void broadcast(int flags, byte[] payload)
    {
        String[] IPSplit = this.getAddress().split("\\.");
        IPSplit[3] = "255";
        String broadcast = "" + IPSplit[0] + "." + IPSplit[1] + "." + IPSplit[2] + "." + IPSplit[3];
        RouterPacket packet = new RouterPacket(flags, this.getAddress(), broadcast, payload);
        sendToAllVisible(packet.toByteArray());
    }
    

    
    /**
     * Adds a neighbour to a router/endpoint.
     * Allows "physical" communication between adjacent elements of the network.
     * @param ip 
     */   
    public void addNeighbour(String ip)
    {
        visibleIPs.add(ip);
    }

    /**
     * Whether this router can "physically" talk to a given address.
     * @param dstAddr
     * @return 
     */
    public boolean hasNeighbour(String dstAddr)
    {
        return visibleIPs.contains(dstAddr);
    }
    
    /**
     * Sends this payload to all neighbours.
     * @param packet 
     */
      public void sendToAllVisible(byte[] packet)
     {
        for(String visible : visibleIPs)
            send(packet, visible);
     }
      
    /**
     * Broadcasts this Router's Distance Vector table.
     */
      public void broadcastDVRoutingTable()
      {
        broadcast(1, routingTable.getRoutingTableBytes());
      }
      
      
      /**
       * Returns a text representation of this routers RoutingTable.
       * @return 
       */
      public String getRoutingTableString()
      {
          return routingTable.toString();
      }
      
      /**
       * Returns the IP of the neighbour with the shortest route to dstAddr.
       * @param dstAddr
       * @return 
       */
      public String getNextHop(String dstAddr)
      {
          return this.routingTable.getNextHop(dstAddr);
      }
      
      /**
       * Sends a message using this router's routing table.
       * @param packet
       * @param dstAddr 
       */
      public void sendWithRouting(byte[] packet, String dstAddr)
      {
          String nextHop = this.getNextHop(dstAddr);
          if(nextHop.equals("err"))
              System.err.println("No routing entry to " + nextHop);
          else
              send(packet, nextHop);
      }
      
    public abstract void send(byte[] packet, String dstAddr);
}
