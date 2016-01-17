package dream.team.assemble.routing.core.simulation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dream.team.assemble.routing.core.Router;
import static dream.team.assemble.routing.core.Router.DEFAULT_PORT;
import static dream.team.assemble.routing.core.Router.PACKET_MTU;
import dream.team.assemble.routing.core.Router.ROUTING;
import dream.team.assemble.routing.core.RouterPacket;
import dream.team.assemble.routing.core.topology.NodeInformation;
import dream.team.assemble.routing.core.topology.RoutingTable;
import dream.team.assemble.routing.core.topology.Topology;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;
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
    /* Port numbers assigned to routers created as part of simulation */
    public static final int INITIAL_PORT = DEFAULT_PORT + 1;
    private int currentPort = INITIAL_PORT;
    
    /* Entire topology of simulated network. */
    private final Topology topo;
    
    /**
     * Maps between ad-hoc network ID and datagram socket addresses.
     * 
     * Used for interfacing between UDP and our protocol.
     */
    protected final HashMap<String, SocketAddress> routerSockets;
    
    /* Used for incoming packet listener. */
    private DatagramSocket socket;
    private final ExecutorService executor;
    
    /* Ad-hoc network ID to router object map. */
    private final BiMap<String, Router> deviceIdMap;
    
    /* Human readable alias to network ID. */
    private HashMap<String, String> nameToIPMap = new HashMap<>();
    
    /* Designates the routing protocol to be used by the current simulation. */
    public final ROUTING routingType;

    /**
     * Creates a Simulation based on a topology. Allows for weighted links.
     *
     * @param routingType
     * @param topo
     */
    public Simulation(ROUTING routingType, Topology topo)
    {
        this.topo = topo;
        try {
            socket = new DatagramSocket(DEFAULT_PORT);
        } catch (SocketException ex) {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        routerSockets = new HashMap<>();
        
        /* Listener for incoming packets */
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> listen());
        
        this.routingType = routingType;
        nameToIPMap = topo.getNameToIPMap();
        deviceIdMap = HashBiMap.create();
        HashMap<String, NodeInformation> topoNodes = topo.getNodes();
        /* Create a router for each node in topology */
        for (String currentKey : topoNodes.keySet())
        {
            NodeInformation topoNode = topoNodes.get(currentKey);
            
            /* Create a router on a non-default port */
            Router router = new Router(routingType, currentPort, topoNode.getName(), topoNode.getIP());
            SocketAddress addr = new InetSocketAddress("localhost", currentPort);
            routerSockets.put(router.getAddress(), addr);
            currentPort++;
            
            router.addAllNeighbours(topoNode.getLinks());
            deviceIdMap.put(router.getAddress(), router);
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

    public void sendMessage(String srcName, String destName, String payload)
    {
        String chosenNodeIP = this.nameToIPMap.get(srcName);
        Router routerA = this.deviceIdMap.get(chosenNodeIP);

        chosenNodeIP = this.nameToIPMap.get(destName);
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
        routerA.sendWithRouting(packet, routerBAddr, (routingType == ROUTING.DISTANCE_VECTOR));
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
     * Listen for incoming packets.
     * 
     */
    private void listen()
    {
        while (true)
        {
            byte[] buffer = new byte[PACKET_MTU];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                onReceipt(packet);
            } catch (IOException ex) {
                Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    /**
     * Action to take on receipt of a packet.
     * 
     * @param packet 
     */
    private void onReceipt(DatagramPacket datagram)
    {
        byte[] data = Arrays.copyOf(datagram.getData(), datagram.getLength());
        RouterPacket packet = new RouterPacket(data);
        
        /* List of IDs of devices that neighbour the source device. */
        List<String> neighbours = topo.getNeighbourIDs(packet.getSrcAddr());
        
        /* forward packet on to each neighbour if packet is a broadcast */
        if (packet.isBroadcast()) {
            for (String neighbour : neighbours) {
                SocketAddress addr = routerSockets.get(neighbour);
                datagram.setSocketAddress(addr);
                try {
                    socket.send(datagram);
                } catch (IOException ex) {
                    Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else /* otherwise forward packet on to target device if possibles */
        {
            String destAddr = packet.getDstAddr();
            if (neighbours.contains(destAddr)) {
                SocketAddress addr = routerSockets.get(destAddr);
                datagram.setSocketAddress(addr);
                try {
                    socket.send(datagram);
                } catch (IOException ex) {
                    Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
        
        try
        {
            Thread.sleep(3000);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
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
