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

        ArrayList<LinkWithWeight> tentitiveList = new ArrayList<>();

        startNode.tempWeight = 0;
        routingTable.addEntry(startNode.getAddress(), startNode, 0);
        for (Link link : startNode.connections)
        {
            link.getConnection(startNode).tempWeight = link.weight;
            tentitiveList.add(new LinkWithWeight(link, startNode, link.weight));
        }

        do
        {
            LinkWithWeight minimum = new LinkWithWeight(new Link(null, null, Integer.MAX_VALUE), null, Integer.MAX_VALUE);
            System.out.println("Start Node: " + startNode.getAddress());
            for (Iterator<LinkWithWeight> it = tentitiveList.iterator(); it.hasNext();)
            {
                LinkWithWeight link = it.next();
                if (link.weight < minimum.weight)
                {
                    minimum = link;
                }
            }
            if (!routingTable.contains(minimum.getNode().getAddress()))
            {
                System.out.println("Adding to routing table " + minimum.getLink());
                routingTable.addEntry(minimum.getNode().getAddress(), minimum.startNode, minimum.getNode().tempWeight);
            }
            startNode = minimum.getNode();

            for (Link link : startNode.connections)
            {
                if (!routingTable.contains(link.getConnection(startNode).getAddress()))
                {
                    System.out.println("Adding to tentitive list " + link.toString());
                    link.getConnection(startNode).tempWeight = startNode.tempWeight + link.weight;
                    tentitiveList.add(new LinkWithWeight(link, startNode, link.weight));
                }
            }

            System.out.println("Removing from tentitive list " + minimum.getLink().toString());
            for (Iterator<LinkWithWeight> it = tentitiveList.iterator(); it.hasNext();)
            {
                LinkWithWeight link = it.next();
                if (link.getLink().toString().equals(minimum.getLink().toString()))
                {
                    it.remove();
                }
            }

            System.out.println(printList(tentitiveList));
//            tentitiveList.addAll(startNode.connections);
//            for (Iterator<Link> it = tentitiveList.iterator(); it.hasNext();)
//            {
//                Link link = it.next();
//                if (routingTable.contains(link.nodes[0].getAddress()) && routingTable.contains(link.nodes[1].getAddress()))
//                {
//                    System.out.println("Removing " + link.toString());
//                    System.out.println("Why? " + link.getConnection(startNode).getAddress() + " " + startNode.getAddress());
//                    it.remove();
//                }
//            }
            System.out.println();
        } while (!tentitiveList.isEmpty());

        return routingTable;
    }

    public static String printList(ArrayList<LinkWithWeight> list)
    {
        String result = "";
        for (LinkWithWeight link : list)
        {
            result += "-" + link.getLink().toString() + "\n";
        }
        return result;
    }
}
