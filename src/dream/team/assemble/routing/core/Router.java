package dream.team.assemble.routing.core;

import dream.team.assemble.routing.core.topology.LinkInformation;
import dream.team.assemble.routing.core.topology.NodeInformation;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import dream.team.assemble.routing.core.topology.RoutingTable;
import dream.team.assemble.routing.core.topology.ShortestPathAlgorithm;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aran
 */
public class Router
{
    public static final int PACKET_MTU = 65535;
    public static final int DEFAULT_PORT = 54000;

    private DatagramSocket socket;
    private final ExecutorService executor;
    
    private final String localIP;
    private final ArrayList<String> log;
    private final boolean logToFile = true;
    private PrintWriter logFile = null;
    private final ArrayList<LinkInformation> visibleIPs;

    private final HashMap<String, String> receivedBroadcasts;
    private final int MAX_REMEMBERED = 255;
    private RoutingTable routingTable;

    private final ConcurrentHashMap<NodeInformation, NodeInformation> LSNodeInfo;
    private final String name;
    private final String nameAndIP;
    NodeInformation myInfo;

    /* Designates the routing protocol to be used by this router. */
    public static enum ROUTING { DISTANCE_VECTOR, LINK_STATE };
    public final ROUTING routingType;    
    
    public Router(ROUTING routingType, int port, String name, String ip)
    {
        this.routingType = routingType;

        this.name = name;
        this.visibleIPs = new ArrayList<>();
        this.localIP = ip;
        this.nameAndIP = name + " " + localIP;
        if (logToFile)
        {
            try {
                logFile = new PrintWriter(nameAndIP + "logFile.logFile", "UTF-8");
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        log = new ArrayList<>();

        receivedBroadcasts = new HashMap<>(MAX_REMEMBERED, (float) 1.0);
        LSNodeInfo = new ConcurrentHashMap<>();
        routingTable = new RoutingTable();
        //add self as first entry in table
        myInfo = new NodeInformation(name, ip);
        //LSNodeInfo.put(myInfo, myInfo);
        routingTable.addEntry(myInfo, myInfo, 0);
        
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* Listener for incoming packets */
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> listen());        
    }
    
    /**
     * Listen for incoming packets.
     * 
     */
    private void listen() {
        while (true)
        {
            byte[] buffer = new byte[PACKET_MTU];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                onReceipt(packet);
            } catch (IOException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String getLog()
    {
        String result = "";
        for (String message : log)
        {
            result += message + "\n";
        }
        return result;
    }

    public String getAddress()
    {
        return localIP;
    }

    /**
     * Action to take upon receiving a data packet.
     *
     * @param datagram the datagram packet received
     */
    public void onReceipt(DatagramPacket datagram)
    {
        byte[] datagramPayload = Arrays.copyOf(datagram.getData(), datagram.getLength());
        
        /* Interpret datagram payload as 'link packet'. */
        RouterPacket linkPacket = new RouterPacket(datagramPayload);
        // Any 'link layer' precessing is done using the data from this packet.        
        
        /* Interpret link packet payload as network packet. */
        RouterPacket networkPacket = new RouterPacket(linkPacket.getPayload());
        
        String logString = networkPacket.toString();

        String dstAddr = networkPacket.getDstAddr();

        //handles broadcasts - checks if it has already received an identical message from the same source, if so ignores, otherwise rebroadcasts
        if (networkPacket.isBroadcast())
        {
            String payload = new String(networkPacket.getPayload());

            String broadcast = networkPacket.getSrcAddr() + " " + payload;

            if (!receivedBroadcasts.containsKey(broadcast))
            {
                //clear memory after 254 unique broadcasts received
                if (receivedBroadcasts.size() >= MAX_REMEMBERED - 1)
                {
                    receivedBroadcasts.clear();
                }

                receivedBroadcasts.put(broadcast, broadcast);

                //if flags == 1 then distance vector routing table
                if (networkPacket.getFlags() == 1)
                {
                    routingTable.updateRoutingTable(networkPacket.getPayload());

                    logString += " comparing routing table with table from " + networkPacket.getSrcAddr() + "\n";
                    logString += routingTable.getUpdatesString();
                    log.add(logString);
                    logFile.write(logString + "\n");
                    logFile.flush();

                    broadcast(1, routingTable.getRoutingTableBytes());
                } //if flags == 2 then NodeInformation for link state routing
                else if (networkPacket.getFlags() == 2)
                {
                    try
                    {
                        ByteArrayInputStream bis = new ByteArrayInputStream(networkPacket.getPayload());
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        NodeInformation receivedNodeInfo = (NodeInformation) ois.readObject();

                        if (!LSNodeInfo.containsKey(receivedNodeInfo))
                        {
                            if(dream.team.assemble.RoutingProtocolsForDisasterZones.verboseDebugPrintouts)
                                System.out.println(name + " recieved " + receivedNodeInfo.description());
                            LSNodeInfo.put(receivedNodeInfo, receivedNodeInfo);
                        }

                        broadcast(2, networkPacket.getPayload());
                    } catch (IOException | ClassNotFoundException e)
                    {
                    }
                } else
                {

                    sendToAllNeighbours(networkPacket);
                    logString += " " + new String(networkPacket.getPayload());
                    if(dream.team.assemble.RoutingProtocolsForDisasterZones.verboseDebugPrintouts)
                        System.out.println(nameAndIP + ": " + new String(networkPacket.getPayload()));

                }

            }

            //if a previously seen broadcast, no further action
            return;

        }
        /* if addressed for this AbstractRouter then handle it as is appropriate for packet type */
        if (localIP.equals(dstAddr))
        {
            logString += "\n I am the destination, opening packet, message is : \n " + new String(networkPacket.getPayload());
            // packet handling stuff goes here
            System.out.println(nameAndIP + ": " + new String(networkPacket.getPayload()));


        } /* if addressed for another node then pass to address of next hop */ else
        {
            boolean isDVR = routingType == ROUTING.DISTANCE_VECTOR;
            String nextAddr = routingTable.getNextHop(dstAddr, isDVR);
            System.out.println(nameAndIP + " - routed to " + nextAddr);
            logString += localIP + " - routed to " + nextAddr;
            send(networkPacket, nextAddr);
        }

        log.add(logString);
        if (logToFile)
        {
            logFile.write(logString + "\n");
            logFile.flush();
        }

    }

    /**
     * Broadcast this payload with the given flags. All routers will parse the
     * payload appropriately and retransmit. flags == 1 for Distance Vector
     * Routing Table payload
     *
     * @param flags
     * @param payload
     */
    public void broadcast(int flags, byte[] payload)
    {
        String[] IPSplit = this.getAddress().split("\\.");
        IPSplit[3] = "255";
        String broadcast = "" + IPSplit[0] + "." + IPSplit[1] + "." + IPSplit[2] + "." + IPSplit[3];
        RouterPacket packet = new RouterPacket(flags, this.getAddress(), broadcast, payload);
        sendToAllNeighbours(packet);
    }

    /**
     * Sends this payload to all neighbours.
     *
     * @param packet
     */
    public void sendToAllNeighbours(RouterPacket packet)
    {
        send(packet, "0.0.0.255");
    }

    public String nodeInformationListString()
    {
        String nodeInfoString = "";
        for (NodeInformation nodeInfo : LSNodeInfo.keySet())
        {
            nodeInfoString += nodeInfo.description() + "\n";
        }
        return nodeInfoString;

    }

    public void buildLSRoutingTable()
    {
        mergeLSInformation();
        routingTable = ShortestPathAlgorithm.getRoutingTable(myInfo);
        if(dream.team.assemble.RoutingProtocolsForDisasterZones.debugPrintouts)
            System.out.println(routingTable);
    }

    public void mergeLSInformation()
    {
        HashMap<NodeInformation, NodeInformation> knownGraph = new HashMap<>();
        for (NodeInformation info : LSNodeInfo.keySet())
        {
            NodeInformation newInfo = new NodeInformation(info.name, info.getIP());
            knownGraph.put(newInfo, newInfo);
            if (newInfo.equals(myInfo))
            {
                myInfo = newInfo;
            }
        }
        for (NodeInformation info : LSNodeInfo.keySet())
        {
            NodeInformation newInfo = knownGraph.get(info);
            for (LinkInformation link : info.getLinks())
            {
                NodeInformation newOther = knownGraph.get(link.getConnection(info));
                if (newOther == null)
                {
                    newOther = new NodeInformation(link.getConnection(info).name, link.getConnection(info).getIP());
                    knownGraph.put(newOther, newOther);
                }
                if (!newInfo.equals(newOther))
                {
                    newInfo.addLink(newOther);
                    newOther.addLink(newInfo);
                }
            }
        }
    }

    /**
     * Adds a neighbour to a router/endpoint. Allows "physical" communication
     * between adjacent elements of the network.
     *
     * @param ip
     */
    public void addNeighbour(LinkInformation link)
    {
        visibleIPs.add(link);
    }

    /**
     * Adds all neighbours to a router/endpoint. Allows "physical" communication
     * between adjacent elements of the network.
     *
     * @param ip
     */
    public void addAllNeighbours(ArrayList<LinkInformation> links)
    {
        visibleIPs.addAll(links);
        myInfo.addAllLinks(links);
    }

    /**
     * Whether this router can "physically" talk to a given address.
     *
     * @param dstAddr
     * @return
     */
    public boolean hasNeighbour(String dstAddr)
    {
        for (LinkInformation link : visibleIPs)
        {
            if (link.contains(dstAddr))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Broadcasts this Router's Distance Vector table.
     *
     */
    public void broadcastDVRoutingTable()
    {
        broadcast(1, routingTable.getRoutingTableBytes());
    }

    /**
     * Broadcasts this Router's NodeInformation. Used for link state routing.
     */
    public void broadcastNodeInformation()
    {
        broadcast(2, myInfo.getByteArr());
    }

    /**
     * Returns a text representation of this routers RoutingTable.
     *
     * @return
     */
    public String getRoutingTableString()
    {
        return routingTable.toString();
    }

    public RoutingTable getRoutingTable()
    {
        return routingTable;
    }

    /**
     * Returns the IP of the neighbour with the shortest route to dstAddr.
     *
     * @param dstAddr
     * @return
     */
    public String getNextHop(String dstAddr, boolean DVR)
    {
        return this.routingTable.getNextHop(dstAddr, DVR);
    }

    /**
     * Sends a message using this router's routing table.
     *
     * @param packet
     * @param dstAddr
     */
    public void sendWithRouting(RouterPacket packet, String dstAddr, boolean DVR)
    {
        String nextHop = this.getNextHop(dstAddr, DVR);
        if (nextHop.equals("err"))
        {
            System.err.println("No routing entry to " + nextHop);
        } else
        {
            String logString = "Sent packet to " + dstAddr + " via " + nextHop;
            log.add(logString);
            if (logToFile)
            {
                logFile.write(logString + "\n");
                logFile.flush();
            }
            send(packet, nextHop);
        }
    }

    public void send(RouterPacket packet, String dstAddr)
    {   
        /* Encapsulate the data in a 'link packet' to send to next-hop */
        RouterPacket linkPacket = new RouterPacket(0, localIP, dstAddr, packet.toByteArray());
        byte[] data = linkPacket.toByteArray();
        
        /* Send all datagrams to localhost for simulation.
         * In reality, the IPs of target devices would be stored and used.
         * DEFAULT_PORT is the well known port which is the entry point into any
         * device using our protocol.
         */
        SocketAddress addr = new InetSocketAddress("localhost", DEFAULT_PORT);
        DatagramPacket datagram = new DatagramPacket(data, data.length, addr);
        try {
            Thread.sleep((long) (100 * Math.random()));
            socket.send(datagram);
        } catch (IOException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
