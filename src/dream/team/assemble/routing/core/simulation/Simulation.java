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
        if (!srcNode.canSee(dstAddr))
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
        String firstNetworkInSpec = "R1 = E1 E2 R3 R2, E1 = R1, E2 = R1, R3 = R1 E3, E3 = R3, R2 = R1 R4, R4 = R2 E4, E4 = R4";
        String testTopo = "A = C B F H, B = A D, C = A D E G, D = B C, E = C, F = A I, G = C, H = A, I = F";
        String biggerNetwork =  "A = C D G E, B = C D E F, C = A B E G, D = A B F G J L, E = B C A F I, F = B E D G H K, G = A C F D, H = F, I = E, J = D, K = F, L = D";
        String biggest = "A = G H I L P, B = C H I J D O, C = B E K F J, D = B G E F, E = C D I H, F = C D G H K, G = A D F H J K M, H = A B G E F K, I = A B E J, J = B G I C N, K = C G F H, L = A, M = G, N = J, O = B, P = A";
        Simulation sim = new Simulation(testTopo);
        
        //simple sending to and from adjacent nodes
        //sim.runTopoTest();
        
        //test broadcasts
        //sim.runBroadcastTest();
        
        sim.runDVRoutingTest();
    }
    
}
