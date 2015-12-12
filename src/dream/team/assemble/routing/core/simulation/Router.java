package dream.team.assemble.routing.core.simulation;

import dream.team.assemble.routing.core.RouterPacket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author aran
 */
class Router extends dream.team.assemble.routing.core.AbstractRouter {

    private final Simulation parent;
    private final ArrayList<String> visibleIPs;

    private Integer broadcastID = 0;
    
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
    
    //directly broadcast a byte payload with the appropriate flags
    public void broadcast(int flags, byte[] payload)
    {
        String[] IPSplit = this.getAddress().split("\\.");
        IPSplit[3] = "255";
        String broadcast = "" + IPSplit[0] + "." + IPSplit[1] + "." + IPSplit[2] + "." + IPSplit[3];
        RouterPacket packet = new RouterPacket(flags, this.getAddress(), broadcast, payload);
        sendToAllVisible(packet.toByteArray());
    }
    
    public boolean canSee(String dstAddr)
    {
        return visibleIPs.contains(dstAddr);
    }
}