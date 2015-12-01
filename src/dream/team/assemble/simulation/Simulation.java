package dream.team.assemble.simulation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Manages the Nodes in a simulated network 
 * 
 * TODO:
 * Finish implementing
 * Integrate with rest of project
 * Improve documentation
 * 
 * @author aran
 */
public class Simulation
{
    private final BiMap<String, Router> deviceIdMap; // NOTE: Make a standard Map if bi-directionallity is not used.
    
    public Simulation()
    {
        deviceIdMap = HashBiMap.create();
    }
    
    void send(Router srcNode, byte[] packet, String dstAddr)
    {
        Router dstNode = deviceIdMap.get(dstAddr);
        /* check if target node exists */
        if (dstNode == null)
        {
            return;
        }
        /* check topology to see if target node can see sender node */
        if (false) //<--placeholder value, replace with topology check
        {
            return;
        }
        dstNode.onReceipt(packet);
    }
}
