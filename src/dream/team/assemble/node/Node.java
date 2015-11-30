package dream.team.assemble.node;

import java.util.HashMap;
import java.util.Map;

/**
 * Standalone Node object.
 * 
 * @author aran
 */
public abstract class Node
{
    private final String localIP;
    
    /**
     * Maps destination address to next hop address.
     */
    private final Map<String, String> routingTable = new HashMap<>(); // NOTE: Consider changing Map to RangeMap, for IP ranged lookup.
    
    public Node(String ip)
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
     * @param packet the data packet received
     */
    public void onReceipt(byte[] packet)
    {
        String dstAddr = null; //<--placeholder value, replace with dstAddr from packet header
        
        /* if addressed for this Node then handle it as is appropriate for packet type */
        if (localIP.equals(dstAddr))
        {
            // packet handling stuff goes here
            return;
        }
        /* if addressed for another node then pass to adderss of next hop */
        else 
        {
            send(packet, dstAddr);
        }
        
    }
    
    public abstract void send(byte[] packet, String dstAddr);
}
