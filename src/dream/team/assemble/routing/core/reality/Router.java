package dream.team.assemble.routing.core.reality;

/**
 *
 * @author aran
 */
class Router extends dream.team.assemble.routing.core.AbstractRouter {

    public Router(String ip)
    {
        super(ip);
    }

    @Override
    public void send(byte[] packet, String dstAddr)
    {
        /* Implement with DatagramSocket */
    }

    @Override
    public void sendToAllVisible(byte[] packet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
