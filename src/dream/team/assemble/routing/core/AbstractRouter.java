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

/**
 * Standalone AbstractRouter object.
 *
 * @author aran
 */
public abstract class AbstractRouter
{

    private final int localID;
    private final ArrayList<String> log;
    private final boolean logToFile = true;
    private PrintWriter logFile = null;
    private final ArrayList<LinkInformation> visibleDevices;

    private final HashMap<String, String> receivedBroadcasts;
    private final int MAX_REMEMBERED = 255;
    private RoutingTable routingTable;

    private final HashMap<NodeInformation, NodeInformation> LSNodeInfo;
    private final String name;
    private final String nameAndIP;
    NodeInformation myInfo;

    public AbstractRouter(String name, int id)
    {
        this.name = name;
        this.visibleDevices = new ArrayList<>();
        this.localID = id;
        this.nameAndIP = name + " " + localID;
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
        myInfo = new NodeInformation(name, id);
        //LSNodeInfo.put(myInfo, myInfo);
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

    public int getID()
    {
        return localID;
    }

    /**
     * Action to take upon receiving a data packet.
     *
     * @param data the data packet received
     */
    public void onReceipt(byte[] data, boolean DVR)
    {
        RouterPacket packet = new RouterPacket(data);
        String logString = packet.toString();

        int dstAddr = packet.getDstAddr();

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
        if (localID == dstAddr)
        {
            logString += "\n I am the destination, opening packet, message is : \n " + new String(packet.getPayload());
            // packet handling stuff goes here

            // TEMPORARY -->
            System.out.println(nameAndIP + ": " + new String(packet.getPayload()));
            // TEMPORARY --<

        } /* if addressed for another node then pass to address of next hop */ else
        {
            int nextAddr = routingTable.getNextHop(dstAddr, DVR);
            System.out.println(nameAndIP + " - routed to " + nextAddr);
            logString += localID + " - routed to " + nextAddr;
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
        RouterPacket packet = new RouterPacket(flags, this.getID(), -1, payload);
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
        mergeLSInformation();
        routingTable = ShortestPathAlgorithm.getRoutingTable(myInfo);
        System.out.println(routingTable);
    }

    public void mergeLSInformation()
    {
        HashMap<NodeInformation, NodeInformation> knownGraph = new HashMap<>();
//        for (NodeInformation info : LSNodeInfo.keySet())
//        {
//            NodeInformation newInfo;
//            if (!knownGraph.containsKey(info))
//            {
//                //#calsearch
//                newInfo = new NodeInformation(info.name, info.getID());
//                knownGraph.put(newInfo, newInfo);
//                if (newInfo.equals(myInfo))
//                {
//                    myInfo = newInfo;
//                }
//            } else
//            {
//                newInfo = knownGraph.get(info);
//            }
//            for (LinkInformation link : info.getLinks())
//            {
//                NodeInformation other = link.getConnection(info);
//                NodeInformation newOther;
//                if (!knownGraph.containsKey(other))
//                {
//                    newOther = new NodeInformation(other.name, other.getID());
//
//                    newInfo.addLink(newOther);
//                    newOther.addLink(newInfo);
//
//                    System.out.println(myInfo.name + ")" + newInfo + " linked to " + newOther);
//                    
//                    knownGraph.put(newOther, newOther);
//                }
//                else
//                {
//                    newOther = knownGraph.get(other);
//                    
//                    newInfo.addLink(newOther);
//                    newOther.addLink(newInfo);
//                    
//                    System.out.println(myInfo.name + ")" + newInfo + " linked to " + newOther);
//                }
//            }
//        }
        for (NodeInformation info : LSNodeInfo.keySet())
        {
            NodeInformation newInfo = new NodeInformation(info.name, info.getID());
            knownGraph.put(newInfo, newInfo);
            System.out.println(myInfo.name + ") Adding " + newInfo);
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
                if (!newInfo.equals(newOther))
                {
                    newInfo.addLink(newOther);
                    newOther.addLink(newInfo);
                    System.out.println(myInfo.name + ") Linking " + newInfo + " and " + newOther);
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
        visibleDevices.add(link);
    }

    /**
     * Adds all neighbours to a router/endpoint. Allows "physical" communication
     * between adjacent elements of the network.
     *
     * @param ip
     */
    public void addAllNeighbours(ArrayList<LinkInformation> links)
    {
        visibleDevices.addAll(links);
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
        for (LinkInformation link : visibleDevices)
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
        for (LinkInformation visible : visibleDevices)
        {
            send(packet, visible.getConnection(localID).getID());
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
    public int getNextHop(int dstAddr, boolean DVR)
    {
        return this.routingTable.getNextHop(dstAddr, DVR);
    }

    /**
     * Sends a message using this router's routing table.
     *
     * @param packet
     * @param dstAddr
     */
    public void sendWithRouting(byte[] packet, int dstAddr, boolean DVR)
    {
        int nextHop = this.getNextHop(dstAddr, DVR);
        if (nextHop == -2)
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

    public abstract void send(byte[] packet, int dstAddr);
}
