package dream.team.assemble;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author Cal
 *
 * Dijkstra’s Shortest-Path Algorithm (with a sprinkle of Cal Magic)
 */
public class ShortestPathAlgorithm
{

    /**
     * Returns a full RoutingTable with the shortest route to each node in the 
     * networks Topology.
     * 
     * @param   startNode   the node you want to generate the RoutingTable for.
     * @return              the RoutingTable for the given startNode.
     */
    public static RoutingTable getRoutingTable(Node startNode)
    {
        RoutingTable routingTable = new RoutingTable();

        // A nice little hashmap to allow updating any existing routes with shorter versions
        // Concurrent to allow removal while iterating
        ConcurrentMap<String, LinkWithWeight> tentitiveList = new ConcurrentHashMap<>();

        // Initial node has no weight
        startNode.tempWeight = 0;
        routingTable.addEntry(startNode.getAddress(), startNode, 0);
        // Populate the tentitive list with all connections from start node
        for (Link link : startNode.connections)
        {
            // Assign their weight to be the links weight (ping)
            link.getConnection(startNode).tempWeight = link.weight;
            tentitiveList.put(link.getConnection(startNode).getAddress(), new LinkWithWeight(link, startNode, link.weight));
        }

        while (!tentitiveList.isEmpty())
        {
            LinkWithWeight minimum = null;
            System.out.println("Start Node: " + startNode.getAddress() + "(" + startNode.tempWeight + ")");
            // Select the smallest weight *node*
            for (Entry entry : tentitiveList.entrySet())
            {
                LinkWithWeight link = (LinkWithWeight) entry.getValue();
                if (minimum == null || link.getNode().tempWeight < minimum.getNode().tempWeight)
                {
                    minimum = link;
                }
            }
            //if (!routingTable.contains(minimum.getNode().getAddress()))
            {
                // Add route to table
                System.out.println("Adding to routing table " + minimum.getLink());
                routingTable.addEntry(minimum.getNode().getAddress(), minimum.getStartNode(), minimum.getNode().tempWeight);
            }
            // This node will now be the next start node
            startNode = minimum.getNode();

            // Add any nodes the new start node can see IF they are better than any ones we might already have
            for (Link link : startNode.connections)
            {
                if (!routingTable.contains(link.getConnection(startNode).getAddress())) // If we don't already have a quicker route...
                {
                    System.out.println("Adding to tentitive list " + link.toString() + "(" + startNode.tempWeight + " " + link.weight + ")");
                    
                    LinkWithWeight updateLink = tentitiveList.get(link.getConnection(startNode).getAddress());
                    if (updateLink == null || updateLink.getWeight() > link.weight) // Do we already have a slower route to this node?
                    {
                        link.getConnection(startNode).tempWeight = startNode.tempWeight + link.weight;
                        tentitiveList.put(link.getConnection(startNode).getAddress(), new LinkWithWeight(link, startNode, link.weight));
                    }
                }
            }

            // Remove the minimum (new start node) from the list
            System.out.println("Removing from tentitive list " + minimum.getLink().toString());
            for (Entry entry : tentitiveList.entrySet())
            {
                LinkWithWeight link = (LinkWithWeight) entry.getValue();
                if (link.getLink() == minimum.getLink())
                {
                    tentitiveList.remove(link.getNode().getAddress());
                }
            }

            // Pretty printing
            System.out.println(printList(tentitiveList));
            System.out.println();
        }
        
        return routingTable;
    }

    public static String printList(ConcurrentMap<String, LinkWithWeight> list)
    {
        String result = "";
        for (Entry entry : list.entrySet())
        {
            LinkWithWeight link = (LinkWithWeight) entry.getValue();
            result += "[" + entry.getKey() + "]-" + link.getLink().toString() + "\n";
        }
        return result;
    }
}
