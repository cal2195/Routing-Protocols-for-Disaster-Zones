package dream.team.assemble.simulation;

/**
 *
 * @author aran
 */
class Router extends dream.team.assemble.core.AbstractRouter {

    private final Simulation parent;
    
    public Router(Simulation parent, String ip)
    {
        super(ip);
        this.parent = parent;
    }

    @Override
    public void send(byte[] packet, String dstAddr)
    {
        parent.send(this, packet, dstAddr);
    }
}