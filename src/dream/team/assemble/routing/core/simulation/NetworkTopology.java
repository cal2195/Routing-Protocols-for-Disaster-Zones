package dream.team.assemble.routing.core.simulation;

import dream.team.assemble.routing.core.topology.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Dan
 */
public class NetworkTopology
{

    final int START_PORT = 50000;
    private HashMap<String, Node> nodes = new HashMap<>();

    public NetworkTopology(String topo)
    {

        /* 
        *   This function expects a String in this format -
        *   "node1 = node2 node3, node 2 = node1 node3, node3 = node1 node2"
        *   Where each node has a unique name, then an equals sign, then the list of nodes that can "hear" that node.
        *   This constructor will create all the nodes, their ports and the list of nodes that can hear them.
         */
        //Scans through the start of each node description and assigns a real port to each node 
        HashMap<String, Integer> namePorts = new HashMap<>();
        String[] split = topo.split(",");
        for (int i = 0; i < split.length; i++)
        {
            Scanner tmpScanner = new Scanner(split[i]);
            String nodeName = tmpScanner.next();
            namePorts.put(nodeName, START_PORT + i);
            Node temp = new Node(nodeName, START_PORT + i, nodeName);
            nodes.put(nodeName, temp);
        }

        System.out.println("List of all nodes in system = " + namePorts.toString());

        //adds to each node the list of nodes that can "hear" it
        for (int i = 0; i < split.length; i++)
        {
            Scanner tmpScanner = new Scanner(split[i]);
            Node tempNode = nodes.get(tmpScanner.next());
            while (tmpScanner.hasNext())
            {
                Node readPort = nodes.get(tmpScanner.next());
                if (readPort != null)
                {
                    tempNode.addLink(readPort);
                }
            }
        }

    }

    public boolean connectionExists(String routerA, String routerB)
    {
        Node node = nodes.get(routerA);
        
        if (node == null)
        {
            return false;
        }
        
        for (Link link : node.getLinks())
        {
            if (link.getConnection(node).getIP().equals(routerB))
            {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, Node> getNodes()
    {
        return nodes;
    }

    @Override
    public String toString()
    {
        String temp = "";
        for (String currentKey : nodes.keySet())
        {
            temp += nodes.get(currentKey).description();
        }
        return temp;
    }

}
