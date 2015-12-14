package dream.team.assemble.routing.core.topology;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Dijkstra’s Shortest-Path Algorithm (with a sprinkle of Cal Magic)
 * 
 * @author Cal
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
    
    /* Just had to comment all this for the time being as it relied on the old routing table and shortest path algorithm
    public static RoutingTable getRoutingTable(Node startNode)
    {
        RoutingTable routingTable = new RoutingTable();

        // A nice little hashmap to allow updating any existing routes with shorter versions
        // Concurrent to allow removal while iterating
        ConcurrentMap<String, LinkWithWeight> tentitiveList = new ConcurrentHashMap<>();

        // Initial node has no weight
        startNode.setNodeWeight(0);
        routingTable.addEntry(startNode, startNode, 0);
        // Populate the tentitive list with all connections from start node
        for (Link link : startNode.getLinks())
        {
            // Assign their weight to be the links weight (ping)
            link.getConnection(startNode).setNodeWeight(link.getWeight());
            tentitiveList.put(link.getConnection(startNode).getPrettyAddress(), new LinkWithWeight(link, startNode, link.getWeight()));
        }

        while (!tentitiveList.isEmpty())
        {
            LinkWithWeight minimum = null;
            System.out.println("Start Node: " + startNode.getPrettyAddress() + "(" + startNode.getNodeWeight() + ")");
            // Select the smallest weight *node*
            for (Entry entry : tentitiveList.entrySet())
            {
                LinkWithWeight link = (LinkWithWeight) entry.getValue();
                if (minimum == null || link.getNode().getNodeWeight() < minimum.getNode().getNodeWeight())
                {
                    minimum = link;
                }
            }
            //if (!routingTable.contains(minimum.getNode().getAddress()))
            {
                // Add route to table
                System.out.println("Adding to routing table " + minimum.getLink());
                routingTable.addEntry(minimum.getNode(), minimum.getStartNode(), minimum.getNode().getNodeWeight());
            }
            // This node will now be the next start node
            startNode = minimum.getNode();

            // Add any nodes the new start node can see IF they are better than any ones we might already have
            for (Link link : startNode.getLinks())
            {
                if (!routingTable.contains(link.getConnection(startNode).getPrettyAddress())) // If we don't already have a quicker route...
                {
                    System.out.println("Adding to tentitive list " + link.toString() + "(" + startNode.getNodeWeight() + " " + link.getWeight() + ")");
                    
                    LinkWithWeight updateLink = tentitiveList.get(link.getConnection(startNode).getPrettyAddress());
                    if (updateLink == null || updateLink.getWeight() > link.getWeight()) // Do we already have a slower route to this node?
                    {
                        link.getConnection(startNode).setNodeWeight(startNode.getNodeWeight() + link.getWeight());
                        tentitiveList.put(link.getConnection(startNode).getPrettyAddress(), new LinkWithWeight(link, startNode, link.getWeight()));
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
                    tentitiveList.remove(link.getNode().getPrettyAddress());
                }
            }

            // Pretty printing
            System.out.println(printList(tentitiveList));
            System.out.println();
        }
        
        return routingTable;
        
    }

    private static String printList(ConcurrentMap<String, LinkWithWeight> list)
    {
        String result = "";
        for (Entry entry : list.entrySet())
        {
            LinkWithWeight link = (LinkWithWeight) entry.getValue();
            result += "[" + entry.getKey() + "]-" + link.getLink().toString() + "\n";
        }
        return result;
    }
    
    */
}
