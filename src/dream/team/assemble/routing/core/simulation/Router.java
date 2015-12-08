package dream.team.assemble.routing.core.simulation;

import java.util.ArrayList;

/**
 *
 * @author aran
 */
class Router extends dream.team.assemble.routing.core.AbstractRouter {

    private final Simulation parent;
    private final ArrayList<String> visibleIPs;
    
    public Router(Simulation parent, String ip)
    {
        super(ip);
        this.parent = parent;
        this.visibleIPs = new ArrayList<>(); //for coding quick examples, this router will be able to see everything
    }
    
    public void addListener(String ip)
    {
        visibleIPs.add(ip);
    }

    @Override
    public void send(byte[] packet, String dstAddr)
    {
        if(canSee(dstAddr))
            parent.send(this, packet, dstAddr);
    }
    
    public boolean canSee(String dstAddr)
    {
        return visibleIPs.contains(dstAddr);
    }
}