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
        this.visibleIPs = new ArrayList<>();
    }
    
    public void addListener(String ip)
    {
        visibleIPs.add(ip);
    }

    @Override
    public void send(byte[] packet, String dstAddr)
    {   
        //check for physical possibility of receipt done in parent class
        parent.send(this, packet, dstAddr);         
    }
    
    public void sendToAllVisible(byte[] packet)
    {
        for(String visible : visibleIPs)
            parent.send(this, packet, visible);
    }
    
    public boolean canSee(String dstAddr)
    {
        return visibleIPs.contains(dstAddr);
    }
}