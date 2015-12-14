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

    private Integer broadcastID = 0;
    
    public Router(Simulation parent, String ip)
    {
        super(ip);
        this.parent = parent;
    }

    @Override
    public void send(byte[] packet, String dstAddr)
    {   
        //check for physical possibility of receipt done in parent class
        parent.send(this, packet, dstAddr);         
    }
    


}