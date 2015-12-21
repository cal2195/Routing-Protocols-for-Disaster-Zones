package dream.team.assemble.routing.core.simulation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dream.team.assemble.gui.DrawingNode;
import dream.team.assemble.routing.core.RouterPacket;
import dream.team.assemble.routing.core.topology.Topology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Manages the Nodes in a simulated network
 *
 * TODO: Finish implementing Integrate with rest of project Improve
 * documentation
 *
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

    /**
     * Builds a simulated network from an input string of adjacency lists.
     * Expects a string[] where the each string is in the format routerName
     * routerIP neighbourIP neighbourIP2 neighbourIP3...
     *
     * @param topo
     */
    public Simulation(String topo) {
        Topology tempTopo = new Topology(topo);
        String[] routersAndListeners = tempTopo.getNodeAndListenerIPs();
        deviceIdMap = HashBiMap.create();
        nameToIPMap = tempTopo.getNameToIPMap();
        for (int i = 0; i < routersAndListeners.length; i++) {
            String[] split = routersAndListeners[i].split(" ");
            Router temp = new Router(this, split[0], split[1]);
            for (int j = 1; j < split.length; j++) {
                temp.addNeighbour(split[j]);
            }
            deviceIdMap.put(temp.getAddress(), temp);
        }

    }

    /**Hacky temp job!
     * TODO : Fix this shit.
     * @param topo 
     */
    public Simulation(Topology topo) 
    {
        String[] routersAndListeners = topo.getNodeAndListenerIPs();
        deviceIdMap = HashBiMap.create();
        nameToIPMap = topo.getNameToIPMap();
        for (int i = 0; i < routersAndListeners.length; i++) {
            String[] split = routersAndListeners[i].split(" ");
            Router temp = new Router(this, split[0], split[1]);
            for (int j = 1; j < split.length; j++) {
                temp.addNeighbour(split[j]);
            }
            deviceIdMap.put(temp.getAddress(), temp);
        }
    }
    
    
    /**
     * Tests whether neighbours can communicate. No routing tables are built.
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
     * Tests if a message can be propogated through broadcasts. No routing
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
     * Tests Distance Vector Routing. The routers build their routing tables,
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
        Simulation sim = new Simulation(quickTests);

        sim.runDVRoutingTest();
        //sim.runNodeInformationSwapTest();
    }

}
