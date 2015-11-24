package dream.team.assemble;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

    public static RoutingTable getRoutingTable(Node startNode)
    {
        RoutingTable routingTable = new RoutingTable();

        ConcurrentMap<String, LinkWithWeight> tentitiveList = new ConcurrentHashMap<>();

        startNode.tempWeight = 0;
        routingTable.addEntry(startNode.getAddress(), startNode, 0);
        for (Link link : startNode.connections)
        {
            link.getConnection(startNode).tempWeight = link.weight;
            tentitiveList.put(link.getConnection(startNode).getAddress(), new LinkWithWeight(link, startNode, link.weight));
        }

        do
        {
            LinkWithWeight minimum = new LinkWithWeight(new Link(null, null, Integer.MAX_VALUE), null, Integer.MAX_VALUE);
            System.out.println("Start Node: " + startNode.getAddress() + "(" + startNode.tempWeight + ")");
            for (Entry entry : tentitiveList.entrySet())
            {
                LinkWithWeight link = (LinkWithWeight) entry.getValue();
                if (link.weight < minimum.weight)
                {
                    minimum = link;
                }
            }
            //if (!routingTable.contains(minimum.getNode().getAddress()))
            {
                System.out.println("Adding to routing table " + minimum.getLink());
                routingTable.addEntry(minimum.getNode().getAddress(), minimum.startNode, minimum.getNode().tempWeight);
            }
            startNode = minimum.getNode();

            for (Link link : startNode.connections)
            {
                if (!routingTable.contains(link.getConnection(startNode).getAddress()))
                {
                    System.out.println("Adding to tentitive list " + link.toString() + "(" + startNode.tempWeight + " " + link.weight + ")");
                    
                    LinkWithWeight updateLink = tentitiveList.get(link.getConnection(startNode).getAddress());
                    if (updateLink == null || updateLink.weight > link.weight)
                    {
                        link.getConnection(startNode).tempWeight = startNode.tempWeight + link.weight;
                        tentitiveList.put(link.getConnection(startNode).getAddress(), new LinkWithWeight(link, startNode, link.weight));
                    }
                }
            }

            System.out.println("Removing from tentitive list " + minimum.getLink().toString());
            for (Entry entry : tentitiveList.entrySet())
            {
                LinkWithWeight link = (LinkWithWeight) entry.getValue();
                if (link.getLink().toString().equals(minimum.getLink().toString()))
                {
                    tentitiveList.remove(link.getNode().getAddress());
                }
            }

            System.out.println(printList(tentitiveList));
//            tentitiveList.addAll(startNode.connections);
//            for (Iterator<LinkWithWeight> it = tentitiveList.iterator(); it.hasNext();)
//            {
//                LinkWithWeight link = it.next();
//                
//            }
            System.out.println();
        } while (!tentitiveList.isEmpty());
        
        routingTable.table.sort(null);
        
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
