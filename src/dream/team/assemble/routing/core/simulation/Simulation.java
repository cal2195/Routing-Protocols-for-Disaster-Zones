package dream.team.assemble.routing.core.simulation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dream.team.assemble.gui.DrawingNode;
import dream.team.assemble.routing.core.RouterPacket;
import dream.team.assemble.routing.core.topology.LinkInformation;
import dream.team.assemble.routing.core.topology.NodeInformation;
import dream.team.assemble.routing.core.topology.Topology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Manages the Nodes in a simulated network
 * @author Dan
 * @author aran
 *
 */
public class Simulation implements Runnable{

    private final BiMap<String, Router> deviceIdMap; // NOTE: Make a standard Map if bi-directionallity is not used.
    private HashMap<String, String> nameToIPMap = new HashMap<>();

    public Simulation() {
        deviceIdMap = HashBiMap.create();
    }

    void addRouter(Router router) {
        deviceIdMap.put(router.getAddress(), router);
    }

    /**
     * Emulates the physical sending of a message between srcNode and dstAddr.
     *
     * @param srcNode
     * @param packet
     * @param dstAddr
     */
    void send(Router srcNode, byte[] packet, String dstAddr) {
        Router dstNode = deviceIdMap.get(dstAddr);
        /* check if target node exists */
        if (dstNode == null) {
            return;
        }
        if (!srcNode.hasNeighbour(dstAddr)) {
            return;
        }
        dstNode.onReceipt(packet);
    }

    /**Creates a Simulation based on a topology.
     * Allows for weighted links.
     * @param topo 
     */
    public Simulation(Topology topo) 
    {
        nameToIPMap = topo.getNameToIPMap();
        deviceIdMap = HashBiMap.create();
        HashMap<String, NodeInformation> topoNodes = topo.getNodes();
        for(String currentKey : topoNodes.keySet())
        {
            NodeInformation topoNode = topoNodes.get(currentKey);
            Router temp = new Router(this, topoNode.getName(), topoNode.getIP());
            temp.addAllNeighbours(topoNode.getLinks());
            deviceIdMap.put(temp.getAddress(), temp);
        }
    }
    
    
    /**
     * Tests whether neighbours can communicate. 
     * No routing tables are built.
     */
    public void runTopoTest() {
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

    /**
     * Tests if a message can be propagated through broadcasts. No routing
     * tables are built.
     */
    public void runBroadcastTest() {
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
    
    
    /**
     * Tests if NodeInformations can be swapped successfully between routers.
     * No routing tables are built.
     */
    public void runNodeInformationSwapTest() {
        for (String name : nameToIPMap.keySet()) {
            String IP = nameToIPMap.get(name);
            Router temp = this.deviceIdMap.get(IP);
            temp.broadcastNodeInformation();
        }

        for (String key : nameToIPMap.keySet()) {
            String IP = nameToIPMap.get(key);
            Router temp = deviceIdMap.get(IP);
            System.out.println("Router " + key + " at " + IP + "\n" + temp.nodeInformationListString());
        }
    }

    /**
     * Tests Distance Vector Routing. 
     * The routers build their routing tables,
     * then users can select a source, message and destination.
     */
    public void runDVRoutingTest() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Building routing tables...");
        for (String name : nameToIPMap.keySet()) {
            String IP = nameToIPMap.get(name);
            Router temp = this.deviceIdMap.get(IP);
            temp.broadcastDVRoutingTable();
        }

        for (String key : nameToIPMap.keySet()) {
            String IP = nameToIPMap.get(key);
            Router temp = deviceIdMap.get(IP);
            System.out.println("Router " + key + " at " + IP + "\n" + temp.getRoutingTableString());
        }

        while (true) {
            String chosenNode;
            boolean validInput = false;
            
            do
            {
                System.out.println("Choose a valid node :");
                chosenNode = scanner.nextLine();
                validInput = (nameToIPMap.containsKey(chosenNode));
            }while(!validInput);
            
            String chosenNodeIP = this.nameToIPMap.get(chosenNode);
            Router routerA = this.deviceIdMap.get(chosenNodeIP);
            System.out.println("Type a message :");
            String message = scanner.nextLine();
            

            System.out.println("Choose a destination :");
            chosenNode = scanner.nextLine();

            chosenNodeIP = this.nameToIPMap.get(chosenNode);
            Router routerB = this.deviceIdMap.get(chosenNodeIP);
            /* wrap this in a RouterPacket destined for RouterB */
            String routerBAddr;
            if(routerB == null)
                routerBAddr = "0.0.0.0";
            else
                routerBAddr = routerB.getAddress();
            
            RouterPacket packet = new RouterPacket(0, routerA.getAddress(), routerBAddr, message.getBytes());
            /* send to routerB */
            routerA.sendWithRouting(packet.toByteArray(), routerBAddr);
        }

    }
    
    public static void buildDVSimFromGui(ArrayList<DrawingNode> GuiNodes)
    {
        Topology topo = new Topology(GuiNodes);
        Simulation sim = new Simulation(topo);
        sim.runDVRoutingTest();
    }
    
    public void run()
    {
        runDVRoutingTest();
    }

    /**
     * Demonstration of direct router communication.
     *
     * NOTE: Remove TEMPORARY from AbstractRouter when changing this!
     */
    public static void main(String[] args) {
        //have added screenshots of these to data folder to make life easier
        String firstNetworkInSpec = "R1 = E1 E2 R3 R2, E1 = R1, E2 = R1, R3 = R1 E3, E3 = R3, R2 = R1 R4, R4 = R2 E4, E4 = R4";
        String testNetwork1 = "A = C E F B G, B = A C E, C = A B D E I J L, D = C E H G, E = A D B C H M, F = A, G = A D H, H = D G E, I = C K, J = C, K = I, L = C, M = E";
        String quickTests = "A = D E G I, B = D H C E F, C = B D F G H K, D = A B C E H F J, E = D A B H, F = C B D G, G = C F A H L, H = B C D E G, I = A M, J = D, K = C, L = G, M = I";
        //Simulation sim = new Simulation(quickTests);

        //sim.runDVRoutingTest();
        //sim.runNodeInformationSwapTest();
    }

}
