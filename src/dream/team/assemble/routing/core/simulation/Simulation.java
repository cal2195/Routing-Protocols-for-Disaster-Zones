package dream.team.assemble.routing.core.simulation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dream.team.assemble.routing.core.RouterPacket;
import dream.team.assemble.routing.core.topology.Topology;
import java.util.HashMap;
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
 * 
 */
public class Simulation
{
    private final BiMap<String, Router> deviceIdMap; // NOTE: Make a standard Map if bi-directionallity is not used.
    private HashMap<String, String> nameToIPMap = new HashMap<>();
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
        if (!srcNode.canSee(dstAddr)) //<--placeholder value, replace with topology check
        {
            return;
        }
        dstNode.onReceipt(packet);
    }
    
    
    public Simulation(String topo)
    {
        Topology tempTopo = new Topology(topo);
        String[] routersAndListeners = tempTopo.getNodeAndListenerIPs();
        deviceIdMap = HashBiMap.create();
        nameToIPMap = tempTopo.getNameToIPMap();
        for(int i = 0; i < routersAndListeners.length; i++)
        {
            String[] split = routersAndListeners[i].split(" ");
            Router temp = new Router(this, split[0]);
            for(int j = 1; j < split.length; j++)
            {
                temp.addListener(split[j]);
            }
            deviceIdMap.put(temp.getAddress(), temp);
        }
    }
    
    public void runTopoTest()
    {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose a node :");
            String chosenNode = scanner.nextLine();
            String chosenNodeIP = this.nameToIPMap.get(chosenNode);
            Router routerA = this.deviceIdMap.get(chosenNodeIP);
            System.out.println("Type a message :");
            String message = scanner.nextLine();
            
            System.out.println("Chose destination :");
            chosenNode = scanner.nextLine();
            chosenNodeIP = this.nameToIPMap.get(chosenNode);
            Router routerB = this.deviceIdMap.get(chosenNodeIP);
            /* wrap this in a RouterPacket destined for RouterB */
            RouterPacket packet = new RouterPacket(0, routerA.getAddress(), routerB.getAddress(), message.getBytes());
            /* send to routerB */
            routerA.send(packet.toByteArray(), routerB.getAddress());
        }
    }
    
    public void runBroadcastTest()
    {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose a node :");
            String chosenNode = scanner.nextLine();
            String chosenNodeIP = this.nameToIPMap.get(chosenNode);
            Router routerA = this.deviceIdMap.get(chosenNodeIP);
            System.out.println("Type a message :");
            String message = scanner.nextLine();
            
            //flags to 0 to indicate normal message, automatically sends to its own IP with last byte changed to 255
            routerA.broadcast(0, message.getBytes());
        }
    }    
            
        public void runDVRoutingTest()
    {
        Scanner scanner = new Scanner(System.in);
        
            System.out.println("Building routing tables...");
            for(String name : nameToIPMap.keySet())
            {
                String IP = nameToIPMap.get(name);
                Router temp = this.deviceIdMap.get(IP);
                temp.broadcastRoutingTable();
            }

            for (String key : nameToIPMap.keySet()) {
                String IP = nameToIPMap.get(key);
                Router temp = deviceIdMap.get(IP);
                System.out.println("Router " + key + " at " + IP + "\n" + temp.getRoutingTableString());
            } 
            
            while (true) {
            System.out.println("Choose a node :");
            String chosenNode = scanner.nextLine();
            String chosenNodeIP = this.nameToIPMap.get(chosenNode);
            Router routerA = this.deviceIdMap.get(chosenNodeIP);
            System.out.println("Type a message :");
            String message = scanner.nextLine();
            
            System.out.println("Chose destination :");
            chosenNode = scanner.nextLine();
            chosenNodeIP = this.nameToIPMap.get(chosenNode);
            Router routerB = this.deviceIdMap.get(chosenNodeIP);
            /* wrap this in a RouterPacket destined for RouterB */
            RouterPacket packet = new RouterPacket(0, routerA.getAddress(), routerB.getAddress(), message.getBytes());
            /* send to routerB */
            routerA.sendWithRouting(packet.toByteArray(), routerB.getAddress());
        }   
        
    }
        
    /**
     * Demonstration of direct router communication.
     * 
     * NOTE:
     * Remove TEMPORARY from AbstractRouter when changing this!
     */
    public static void main(String[] args)
    {
        String testTopo = "A = B C D E, B = A, C = A, D = A G F, E = A, F = D, G = D";
        Simulation sim = new Simulation(testTopo);
        
        //simple sending to and from adjacent nodes
        //sim.runTopoTest();
        
        //test broadcasts
        //sim.runBroadcastTest();
        
        sim.runDVRoutingTest();
    }
    
}
