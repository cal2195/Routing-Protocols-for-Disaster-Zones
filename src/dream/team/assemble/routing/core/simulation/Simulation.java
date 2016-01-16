package dream.team.assemble.routing.core.simulation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import ToDo.Packet;
import dream.team.assemble.routing.core.Router;
import dream.team.assemble.routing.core.RouterPacket;
import dream.team.assemble.routing.core.topology.NodeInformation;
import dream.team.assemble.routing.core.topology.RoutingTable;
import dream.team.assemble.routing.core.topology.Topology;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the Nodes in a simulated network
 *
 * @author Dan
 * @author aran
 *
 */
public class Simulation implements Runnable
{
    // post -----------------------<
    public static final int PACKET_MTU = 65535;
    public static final int DEFAULT_PORT = 54321;
    public static final String BROADCAST_ADDR = "255.255.255.255";
    
    private final Topology topo;
    
    /**
     * Maps between ad-hoc network ID and datagram socket addresses.
     * 
     * Used for interfacing between UDP and our protocol.
     * 
     * Connections are to each router that is part of the simulation.
     */
    protected final HashMap<Integer, SocketAddress> connections;
    
    /* Used for incoming packet listener */
    private DatagramSocket socket;
    private final ExecutorService executor;
    
    // pre ------------------------<
    private final BiMap<String, Router> deviceIdMap; // NOTE: Make a standard Map if bi-directionallity is not used.
    private HashMap<String, String> nameToIPMap = new HashMap<>();
    public static enum ROUTING { DISTANCE_VECTOR, LINK_STATE };
    public final ROUTING routingType;

    /**
     * Creates a Simulation based on a topology. Allows for weighted links.
     *
     * @param routingType
     * @param topo
     */
    public Simulation(ROUTING routingType, Topology topo)
    {
        // post -----------------------<
        this.topo = topo;
        try {
            socket = new DatagramSocket(DEFAULT_PORT);
        } catch (SocketException ex) {
            Logger.getLogger(ToDo.WIPRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        connections = new HashMap<>();
        
        /* Listener for incoming packets */
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> listen());
        
        
        
        // pre ------------------------<
        this.routingType = routingType;
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
        routerA.sendWithRouting(packet.toByteArray(), routerBAddr, (routingType == ROUTING.DISTANCE_VECTOR));
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
    public void send(Router srcNode, byte[] packet, String dstAddr)
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
        dstNode.onReceipt(packet, (routingType == ROUTING.DISTANCE_VECTOR));
    }
    
    /**
     * Listen for incoming packets.
     * 
     */
    private void listen() {
        byte[] buffer = new byte[PACKET_MTU];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
            onReceipt(packet);
        } catch (IOException ex) {
            Logger.getLogger(ToDo.WIPRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Action to take on receipt of a packet.
     * 
     * @param packet 
     */
    private void onReceipt(DatagramPacket datagram) {
                
        Packet packet = new Packet(datagram);
        
        List<Integer> neighbours = getNeighbours(packet.getSrcID());
        
        if (packet.isBroadcast()) {
            /* forward packet on to each neighbour. */
            for (int neighbour : neighbours) {
                SocketAddress addr = connections.get(neighbour);
                datagram.setSocketAddress(addr);
                try {
                    socket.send(datagram);
                } catch (IOException ex) {
                    Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } 
        else {
            int dstID = packet.getDstID();
            if (neighbours.contains(dstID)) {
                /* send packet to targeted neighbour. */
                SocketAddress addr = connections.get(dstID);
                datagram.setSocketAddress(addr);
                try {
                    socket.send(datagram);
                } catch (IOException ex) {
                    Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    } 
    /* placeholder until method created in Topology for this */
    private List<Integer> getNeighbours(int srcID) {
        return null;
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
            WIPRouter temp = deviceIdMap.get(IP);
            System.out.println("WIPRouter " + key + " at " + IP + "\n" + temp.getRoutingTableString());
        }
        */
    }
    
    @Override
    public void run()
    {
        if (routingType == ROUTING.DISTANCE_VECTOR) runDVRouting(); else runLSRouting();
    }

}
