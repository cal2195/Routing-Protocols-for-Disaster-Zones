package dream.team.assemble.reality;

/**
 *
 * @author aran
 */
class Router extends dream.team.assemble.core.AbstractRouter {

    public Router(String ip)
    {
        super(ip);
    }

    @Override
    public void send(byte[] packet, String dstAddr)
    {
        /* Implement with DatagramSocket */
    }
}
