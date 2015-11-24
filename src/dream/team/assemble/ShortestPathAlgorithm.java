package dream.team.assemble;

import java.util.ArrayList;
import java.util.Iterator;

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

        routingTable.addEntry(startNode.getAddress(), startNode);
        tentitiveList.addAll(startNode.connections);

        do
        {
            Link minimum = new Link(null, null, Integer.MAX_VALUE);
            for (Iterator<Link> it = tentitiveList.iterator(); it.hasNext();)
            {
                Link link = it.next();
                if (link.weight < minimum.weight)
                {
                    minimum = link;
                }
            }
            totalWeight += minimum.weight;
            System.out.println("Adding " + minimum.getConnection(startNode).getAddress());
            routingTable.addEntry(minimum.getConnection(startNode).getAddress(), startNode);
            startNode = minimum.getConnection(startNode);

            tentitiveList.remove(minimum);
            tentitiveList.addAll(startNode.connections);
            for (Iterator<Link> it = tentitiveList.iterator(); it.hasNext();)
            {
                Link link = it.next();
                if (routingTable.contains(link.getConnection(startNode)))
                {
                    it.remove();
                }
            }
        } while (!tentitiveList.isEmpty());

        return routingTable;
    }
}
