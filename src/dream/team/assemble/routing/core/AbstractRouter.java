package dream.team.assemble.routing.core;

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
    
    /**
     * Maps destination address to next hop address.
     */
    private final Map<String, String> routingTable = new HashMap<>(); // NOTE: Consider changing Map to RangeMap, for IP ranged lookup.
    
    public AbstractRouter(String ip)
    {
        this.localIP = ip;
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
        
        String dstAddr = packet.getDstAddr();
        
        /* if addressed for this AbstractRouter then handle it as is appropriate for packet type */
        if (localIP.equals(dstAddr))
        {
            // packet handling stuff goes here
            
            // TEMPORARY -->
            System.out.println(localIP + ": " + new String(packet.getPayload()));
            // TEMPORARY --<
        }
        /* if addressed for another node then pass to adderss of next hop */
        else 
        {
            String nextAddr = routingTable.get(dstAddr);
            send(data, nextAddr);
        }
        
    }
    
    public abstract void send(byte[] packet, String dstAddr);
}
