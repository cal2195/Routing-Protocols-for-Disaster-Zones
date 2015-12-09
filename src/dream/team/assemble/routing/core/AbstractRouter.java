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
    
    /**
     * Maps destination address to next hop address.
     */
    private final Map<String, String> routingTable = new HashMap<>(); // NOTE: Consider changing Map to RangeMap, for IP ranged lookup.
    
    public AbstractRouter(String ip)
    {
        this.localIP = ip;
        if(logToFile){
            try{
           logFile = new PrintWriter(localIP + "logFile.logFile", "UTF-8");
            }
            catch(FileNotFoundException|UnsupportedEncodingException e){}
        }

        log = new ArrayList<>();

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
        
        /* if addressed for this AbstractRouter then handle it as is appropriate for packet type */
        if (localIP.equals(dstAddr) || packet.isBroadcast())
        {
            logString += " " + new String(packet.getPayload());
            // packet handling stuff goes here
           
            
            // TEMPORARY -->
            System.out.println(localIP + ": " + new String(packet.getPayload()));
            // TEMPORARY --<
            
            
            //if it's a broadcast packet we should send it to all our neighbours!
            if(packet.isBroadcast())
                sendToAllVisible(data);
            
        }
        /* if addressed for another node then pass to adderss of next hop */
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
    
    public abstract void send(byte[] packet, String dstAddr);
    public abstract void sendToAllVisible(byte[] packet);
}
