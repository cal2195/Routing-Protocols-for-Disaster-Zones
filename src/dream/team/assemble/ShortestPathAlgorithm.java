package dream.team.assemble;

import java.util.ArrayList;

/**
 *
 * @author Cal
 *
 * Dijkstra’s Shortest-Path Algorithm (with a sprinkle of Cal Magic)
 */
public class ShortestPathAlgorithm
{

    public static RoutingTable getRoutingTable(Node startNode)
    {
        RoutingTable routingTable = new RoutingTable();
        int totalWeight = 0;

        ArrayList<Link> tentitiveList = new ArrayList<>();

        routingTable.addEntry(startNode.myIP, startNode);
        
        do
        {
            tentitiveList.addAll(startNode.connections);
            
            Link minimum = new Link(null, null, Integer.MAX_VALUE);
            for (Link link : tentitiveList)
            {
                if (link.weight < minimum.weight && !routingTable.contains(link.getConnection(startNode)))
                {
                    minimum = link;
                }
            }
            
            totalWeight += minimum.weight;
            routingTable.addEntry(minimum.getConnection(startNode).myIP, minimum.getConnection(startNode));
            startNode = minimum.getConnection(startNode);
            
        } while (!tentitiveList.isEmpty());

        return routingTable;
    }
}
