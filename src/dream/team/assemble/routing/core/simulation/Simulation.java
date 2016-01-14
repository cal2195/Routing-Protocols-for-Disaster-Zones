package dream.team.assemble.routing.core.simulation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dream.team.assemble.routing.core.RouterPacket;
import dream.team.assemble.routing.core.topology.NodeInformation;
import dream.team.assemble.routing.core.topology.RoutingTable;
import dream.team.assemble.routing.core.topology.Topology;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Manages the Nodes in a simulated network
 *
 * @author Dan
 * @author aran
 *
 */
public class Simulation implements Runnable
{

    private final BiMap<String, Router> deviceIdMap; // NOTE: Make a standard Map if bi-directionallity is not used.
    private HashMap<String, String> nameToIPMap = new HashMap<>();

    public Simulation()
    {
        deviceIdMap = HashBiMap.create();
    }

    public String[] getAllNames()
    {
        return (String[]) nameToIPMap.keySet().toArray(new String[0]);
    }

    public String nameToIP(String name)
    {
        return nameToIPMap.get(name);
    }
    
    public RoutingTable getNodeRoutingTable(String label)
    {
        return getRouterByName(label).getRoutingTable();
    }

    public void sendMessage(String source, String destination, String payload)
    {
        String chosenNodeIP = this.nameToIPMap.get(source);
        Router routerA = this.deviceIdMap.get(chosenNodeIP);

        chosenNodeIP = this.nameToIPMap.get(destination);
        Router routerB = this.deviceIdMap.get(chosenNodeIP);
        /* wrap this in a RouterPacket destined for RouterB */
        String routerBAddr;
        if (routerB == null)
        {
            routerBAddr = "0.0.0.0";
        } else
        {
            routerBAddr = routerB.getAddress();
        }

        RouterPacket packet = new RouterPacket(0, routerA.getAddress(), routerBAddr, payload.getBytes());
        System.out.println(packet);
        /* send to routerB */
        routerA.sendWithRouting(packet.toByteArray(), routerBAddr);
    }

    public Router getRouterByName(String name)
    {
        return deviceIdMap.get(nameToIPMap.get(name));
    }

    void addRouter(Router router)
    {
        deviceIdMap.put(router.getAddress(), router);
    }

    /**
     * Emulates the physical sending of a message between srcNode and dstAddr.
     *
     * @param srcNode
     * @param packet
     * @param dstAddr
     */
    void send(Router srcNode, byte[] packet, String dstAddr)
    {
        Router dstNode = deviceIdMap.get(dstAddr);
        /* check if target node exists */
        if (dstNode == null)
        {
            return;
        }
        if (!srcNode.hasNeighbour(dstAddr))
        {
            return;
        }
        dstNode.onReceipt(packet);
    }

    /**
     * Creates a Simulation based on a topology. Allows for weighted links.
     *
     * @param topo
     */
    public Simulation(Topology topo)
    {
        nameToIPMap = topo.getNameToIPMap();
        deviceIdMap = HashBiMap.create();
        HashMap<String, NodeInformation> topoNodes = topo.getNodes();
        for (String currentKey : topoNodes.keySet())
        {
            NodeInformation topoNode = topoNodes.get(currentKey);
            Router temp = new Router(this, topoNode.getName(), topoNode.getIP());
            temp.addAllNeighbours(topoNode.getLinks());
            deviceIdMap.put(temp.getAddress(), temp);
        }
    }

    /**
     * Tests whether neighbours can communicate. No routing tables are built.
     */
    public void runTopoTest()
    {
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
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
    public void runBroadcastTest()
    {
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
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
     * Each router transmits it's view of the network at present and then calculates the shortest path
     * to each other node using Dijkstra's shortest path algorithm.
     */
    public void runLSRouting()
    {
        for (String name : nameToIPMap.keySet())
        {
            String IP = nameToIPMap.get(name);
            Router temp = this.deviceIdMap.get(IP);
            temp.broadcastNodeInformation();
        }
        //this code prints every router's view of the network (ie, each node it's connected to and that node's links)
        
        for (String key : nameToIPMap.keySet())
        {
            String IP = nameToIPMap.get(key);
            Router temp = deviceIdMap.get(IP);
            System.out.println("Router " + key + " at " + IP + "\n" + temp.nodeInformationListString());
        }
        
      
        for (String name : nameToIPMap.keySet())
        {
            String IP = nameToIPMap.get(name);
            Router temp = this.deviceIdMap.get(IP);
            temp.buildLSRoutingTable();
        }
    }

    /**
     * Runs Distance Vector Routing. The routers build their routing tables.
     */
    public void runDVRouting()
    {
        System.out.println("Building routing tables...");
        for (String name : nameToIPMap.keySet())
        {
            String IP = nameToIPMap.get(name);
            Router temp = this.deviceIdMap.get(IP);
            temp.broadcastDVRoutingTable();
        }
        //this code prints every router's routing table
        /*
        for (String key : nameToIPMap.keySet())
        {
            String IP = nameToIPMap.get(key);
            Router temp = deviceIdMap.get(IP);
            System.out.println("Router " + key + " at " + IP + "\n" + temp.getRoutingTableString());
        }
        */
    }
    
    public void run()
    {
        runDVRouting();
        //runLSRouting();
    }

}
