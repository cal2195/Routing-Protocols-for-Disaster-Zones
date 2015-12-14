package dream.team.assemble.routing.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    
    //TODO - change to adding an ID int at the end of each payload and remembering that instead!
    private final HashMap<String, String> receivedBroadcasts;  
    private final int MAX_REMEMBERED = 256;
    
    
    /**
     * Maps destination address to next hop address.
     */
    private final Map<String, String> routingTable = new HashMap<>(); // NOTE: Consider changing Map to RangeMap, for IP ranged lookup.
    
    public AbstractRouter(String ip)
    {
        this.visibleIPs = new ArrayList<>();
        this.localIP = ip;
        if(logToFile){
            try{
           logFile = new PrintWriter(localIP + "logFile.logFile", "UTF-8");
            }
            catch(FileNotFoundException|UnsupportedEncodingException e){}
        }
        log = new ArrayList<>();

        receivedBroadcasts = new HashMap<>(MAX_REMEMBERED, (float) 1.0);         
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
                sendToAllVisible(data);
                logString += " " + new String(packet.getPayload());
            
                // TEMPORARY -->
                System.out.println(localIP + ": " + new String(packet.getPayload()));
                // TEMPORARY --<
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
            System.out.println(localIP + ": " + new String(packet.getPayload()));
            // TEMPORARY --<
            
        }
        /* if addressed for another node then pass to address of next hop */
        else 
        {
            String nextAddr = routingTable.get(dstAddr);
            logString += " - routed to " + nextAddr;
            send(data, nextAddr);
        }
        
        log.add(logString);
        if(logToFile)
        {
            logFile.write(logString + "\n"); 
            logFile.flush();
        }
        
    }
    
    
    //directly broadcast a byte payload with the appropriate flags
    public void broadcast(int flags, byte[] payload)
    {
        String[] IPSplit = this.getAddress().split("\\.");
        IPSplit[3] = "255";
        String broadcast = "" + IPSplit[0] + "." + IPSplit[1] + "." + IPSplit[2] + "." + IPSplit[3];
        RouterPacket packet = new RouterPacket(flags, this.getAddress(), broadcast, payload);
        sendToAllVisible(packet.toByteArray());
    }
    
    public boolean canSee(String dstAddr)
    {
        return visibleIPs.contains(dstAddr);
    }
    
        
    public void addListener(String ip)
    {
        visibleIPs.add(ip);
    }

      public void sendToAllVisible(byte[] packet)
    {
        for(String visible : visibleIPs)
            send(packet, visible);
    }
      
    public abstract void send(byte[] packet, String dstAddr);
}
