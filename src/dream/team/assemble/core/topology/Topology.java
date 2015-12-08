package dream.team.assemble.core.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Dan
 */
public class Topology
{

    final int START_PORT = 50000;
    private HashMap<String, Node> nodes = new HashMap<>();
    private HashMap<String, String> nameToIPMap = new HashMap<>();

    public Topology(String topo)
    {

        /* 
        *   This function expects a String in this format -
        *   "node1 = node2 node3, node 2 = node1 node3, node3 = node1 node2"
        *   Where each node has a unique name, then an equals sign, then the list of nodes that can "hear" that node.
        *   This constructor will create all the nodes, their ports and the list of nodes that can hear them.
         */
        //Scans through the start of each node description and assigns a real port to each node 
        Random IPgen = new Random(100);
        String[] split = topo.split(",");
        for (int i = 0; i < split.length; i++)
        {
            Scanner tmpScanner = new Scanner(split[i]);
            String nodeName = tmpScanner.next();
            String IP = "" + IPgen.nextInt(255) + "." + IPgen.nextInt(255) + "." + IPgen.nextInt(255) + "." + IPgen.nextInt(255);
            nameToIPMap.put(nodeName, IP);
            Node temp = new Node(nodeName, START_PORT + i, IP);
            nodes.put(nodeName, temp);
        }

        System.out.println("List of all nodes in system = " + nameToIPMap.toString());

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

    public HashMap<String, Node> getNodes()
    {
        return nodes;
    }
    
    public HashMap<String, String> getNameToIPMap()
    {
        return nameToIPMap;
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

    public String[] getNodeAndListenerIPs()
    {
        String[] nodesAndListeners = new String[nodes.keySet().size()];
        int i = 0;
        for (String currentKey : nodes.keySet())
        {
            
            Node temp = nodes.get(currentKey);
            String thisRouter = nodes.get(currentKey).getIP();
            thisRouter += temp.heardByIPsToString();
            nodesAndListeners[i] = thisRouter;
            i++;
            System.out.println(thisRouter);
        }
        return nodesAndListeners;
    }
    
    public static void main (String[] args)
    {
        Topology temp = new Topology("A = B C E H, B = A D G, C = A, D = B F, E = A, F = D, G = B, H = A");
        temp.getNodeAndListenerIPs();
    }
}
