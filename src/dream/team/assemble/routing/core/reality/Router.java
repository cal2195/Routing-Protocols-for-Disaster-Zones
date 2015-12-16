package dream.team.assemble.routing.core.reality;

/**
 *
 * @author aran
 */
class Router extends dream.team.assemble.routing.core.AbstractRouter {

    public Router(String name, String ip)
    {
        super(name, ip);
    }

    @Override
    public void send(byte[] packet, String dstAddr)
    {
        /* Implement with DatagramSocket */
    }

}
