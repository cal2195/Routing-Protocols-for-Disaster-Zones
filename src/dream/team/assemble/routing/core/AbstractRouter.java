package dream.team.assemble.routing.core;

import dream.team.assemble.routing.core.topology.LinkInformation;
import dream.team.assemble.routing.core.topology.NodeInformation;
import dream.team.assemble.routing.core.topology.RoutingEntry;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import dream.team.assemble.routing.core.topology.RoutingTable;
import dream.team.assemble.routing.core.topology.ShortestPathAlgorithm;
import dream.team.assemble.routing.core.topology.Topology;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Standalone AbstractRouter object.
 *
 * @author aran
 */
public abstract class AbstractRouter
{

    private final String localIP;
    private final ArrayList<String> log;
    private final boolean logToFile = true;
    private PrintWriter logFile = null;
    private final ArrayList<LinkInformation> visibleIPs;

    private final HashMap<String, String> receivedBroadcasts;
    private final int MAX_REMEMBERED = 255;
    private RoutingTable routingTable;

    private final HashMap<NodeInformation, NodeInformation> LSNodeInfo;
    private final String name;
    private final String nameAndIP;
    NodeInformation myInfo;

    public AbstractRouter(String name, String ip)
    {
        this.name = name;
        this.visibleIPs = new ArrayList<>();
        this.localIP = ip;
        this.nameAndIP = name + " " + localIP;
        if (logToFile)
        {
            try
            {
                logFile = new PrintWriter(nameAndIP + "logFile.logFile", "UTF-8");
            } catch (FileNotFoundException | UnsupportedEncodingException e)
            {
            }
        }
        log = new ArrayList<>();

        receivedBroadcasts = new HashMap<>(MAX_REMEMBERED, (float) 1.0);
        LSNodeInfo = new HashMap<>();
        routingTable = new RoutingTable();
        //add self as first entry in table
        myInfo = new NodeInformation(name, ip);
        routingTable.addEntry(myInfo, myInfo, 0);
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
     * @param data the data packet received
     */
    public void onReceipt(byte[] data)
    {
        RouterPacket packet = new RouterPacket(data);
        String logString = packet.toString();

        String dstAddr = packet.getDstAddr();

        //handles broadcasts - checks if it has already received an identical message from the same source, if so ignores, otherwise rebroadcasts
        if (packet.isBroadcast())
        {
            String payload = new String(packet.getPayload());

            String broadcast = packet.getSrcAddr() + " " + payload;

            if (!receivedBroadcasts.containsKey(broadcast))
            {
                //clear memory after 254 unique broadcasts received
                if (receivedBroadcasts.size() >= MAX_REMEMBERED - 1)
                {
                    receivedBroadcasts.clear();
                }

                receivedBroadcasts.put(broadcast, broadcast);

                //if flags == 1 then distance vector routing table
                if (packet.getFlags() == 1)
                {
                    routingTable.updateRoutingTable(packet.getPayload());

                    logString += " comparing routing table with table from " + packet.getSrcAddr() + "\n";
                    logString += routingTable.getUpdatesString();
                    log.add(logString);
                    logFile.write(logString + "\n");
                    logFile.flush();

                    broadcast(1, routingTable.getRoutingTableBytes());
                } //if flags == 2 then NodeInformation for link state routing
                else if (packet.getFlags() == 2)
                {
                    try
                    {
                        ByteArrayInputStream bis = new ByteArrayInputStream(packet.getPayload());
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        NodeInformation receivedNodeInfo = (NodeInformation) ois.readObject();

                        if (!LSNodeInfo.containsKey(receivedNodeInfo))
                        {
                            System.out.println(name + " recieved " + receivedNodeInfo.description());
                            LSNodeInfo.put(receivedNodeInfo, receivedNodeInfo);
                        }

                        broadcast(2, packet.getPayload());
                    } catch (IOException | ClassNotFoundException e)
                    {
                    }
                } else
                {

                    sendToAllVisible(data);
                    logString += " " + new String(packet.getPayload());
                    // TEMPORARY -->
                    System.out.println(nameAndIP + ": " + new String(packet.getPayload()));
                    // TEMPORARY --<   
                }

            }

            //if a previously seen broadcast, no further action
            return;

        }
        /* if addressed for this AbstractRouter then handle it as is appropriate for packet type */
        if (localIP.equals(dstAddr))
        {
            logString += "\n I am the destination, opening packet, message is : \n " + new String(packet.getPayload());
            // packet handling stuff goes here

            // TEMPORARY -->
            System.out.println(nameAndIP + ": " + new String(packet.getPayload()));
            // TEMPORARY --<

        } /* if addressed for another node then pass to address of next hop */ else
        {
            String nextAddr = routingTable.getNextHop(dstAddr);
            System.out.println(nameAndIP + " - routed to " + nextAddr);
            logString += localIP + " - routed to " + nextAddr;
            send(data, nextAddr);
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
        sendToAllVisible(packet.toByteArray());
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
        Topology topology = new Topology(buildTopologyString());
        routingTable = ShortestPathAlgorithm.getRoutingTable(topology.getNodes().get(name));
        System.out.println(routingTable);
    }
    
    public String buildTopologyString()
    {
        String top = "";//myInfo.name + " = " + myInfo.heardByToString();
        HashMap<String, String> seen = new HashMap<>();
        for (NodeInformation info : LSNodeInfo.keySet())
        {
            if (!seen.containsKey(info.name))
            {
                top += ((top.equals("")) ? "" : ", ") + info.name + " =" + info.heardByToString();
                seen.put(info.name, "GO AWAY EVIL INFORMATION");
            }
        }
        System.out.println(top);
        return top;
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
     * Sends this payload to all neighbours.
     *
     * @param packet
     */
    public void sendToAllVisible(byte[] packet)
    {
        for (LinkInformation visible : visibleIPs)
        {
            send(packet, visible.getConnection(localIP).getIP());
        }
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
    public String getNextHop(String dstAddr)
    {
        return this.routingTable.getNextHop(dstAddr);
    }

    /**
     * Sends a message using this router's routing table.
     *
     * @param packet
     * @param dstAddr
     */
    public void sendWithRouting(byte[] packet, String dstAddr)
    {
        String nextHop = this.getNextHop(dstAddr);
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

    public abstract void send(byte[] packet, String dstAddr);
}
