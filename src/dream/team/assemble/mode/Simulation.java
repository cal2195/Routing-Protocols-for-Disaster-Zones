package dream.team.assemble.mode;

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
    private final BiMap<String, Node> deviceIdMap; // NOTE: Make a standard Map if bi-directionallity is not used.
    
    public Simulation()
    {
        deviceIdMap = HashBiMap.create();
    }
    
    /**
     * Implements the send() method of Node for use with Simulation
     */
    private class Node extends dream.team.assemble.node.Node {

        public Node(String ip)
        {
            super(ip);
        }

        @Override
        public void send(byte[] packet, String dstAddr)
        {
            Simulation.this.send(this, packet, dstAddr);
        }
    }
    
    private void send(Node srcNode, byte[] packet, String dstAddr)
    {
        Node dstNode = deviceIdMap.get(dstAddr);
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
