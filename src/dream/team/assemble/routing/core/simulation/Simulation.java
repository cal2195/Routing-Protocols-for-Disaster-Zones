package dream.team.assemble.routing.core.simulation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dream.team.assemble.routing.core.RouterPacket;
import java.util.Scanner;

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
    private final BiMap<String, Router> deviceIdMap; // NOTE: Make a standard Map if bi-directionallity is not used.
    
    public Simulation()
    {
        deviceIdMap = HashBiMap.create();
    }
    
    void addRouter(Router router)
    {
        deviceIdMap.put(router.getAddress(), router);
    }
    
    void send(Router srcNode, byte[] packet, String dstAddr)
    {
        Router dstNode = deviceIdMap.get(dstAddr);
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
    
    /**
     * Demonstration of direct router communication.
     * 
     * NOTE:
     * Remove TEMPORARY from AbstractRouter when changing this!
     */
    public static void main(String[] args)
    {
        Simulation sim = new Simulation();
        
        Router routerA = new Router(sim, "10.42.0.1");
        Router routerB = new Router(sim, "10.42.0.143");
        
        sim.addRouter(routerA);
        sim.addRouter(routerB);
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            /* get input from user at routerA */
            System.out.print(routerA.getAddress() + ": ");
            String message = scanner.nextLine();
            /* wrap this in a RouterPacket destined for RouterB */
            RouterPacket packet = new RouterPacket(0, routerA.getAddress(), routerB.getAddress(), message.getBytes());
            /* send to routerB */
            routerA.send(packet.toByteArray(), routerB.getAddress());
            /* routerB then prints the packet (see AbstractRouter TEMPORARY tag)
             * because it is the destination address. Other possible 
             * functionality is that it should forward the packet on according 
             * to it's routing table.
             */
        }
    }
    
}
