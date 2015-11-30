package dream.team.assemble.mode;

/**
 * Manages a Node to allow it to send to and receive from another machine.
 * 
 * TODO:
 * Implement
 * 
 * @author aran
 */
public class Reality
{

    private class Node extends dream.team.assemble.node.Node {

        public Node(String ip)
        {
            super(ip);
        }

        @Override
        public void send(byte[] packet, String dstAddr)
        {
            /* Implement with DatagramSocket */
        }
        
    }
}
