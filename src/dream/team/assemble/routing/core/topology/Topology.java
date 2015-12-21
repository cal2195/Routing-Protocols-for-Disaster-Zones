package dream.team.assemble.routing.core.topology;

import dream.team.assemble.gui.DrawingNode;
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
    private HashMap<String, NodeInformation> nodes = new HashMap<>();
    private HashMap<String, String> nameToIPMap = new HashMap<>();
    private final boolean useIPs = false;
    private final int STARTING_IP = 1;
    private int currentIP = STARTING_IP;

    
    public Topology(ArrayList<DrawingNode> GuiNodes)
    {
        
        /* create NodeInformations from GUI objects */
        for(DrawingNode GuiNode : GuiNodes)
        {
            int endingByte = currentIP++;
            String IP = "" + 10 + "." + 1 + "." + 6 + "." + endingByte;
            nameToIPMap.put(GuiNode.getLabel(), IP);
            NodeInformation temp = new NodeInformation(GuiNode.getLabel(), IP);
            nodes.put(GuiNode.getLabel(), temp);   
        }
        
        /* add the correct links */    
        for(DrawingNode GuiNode : GuiNodes)
        {
            NodeInformation realNode = nodes.get(GuiNode.getLabel());
            ArrayList<DrawingNode> GuiLinkedNodes = GuiNode.getLinkedNodes();
            for(DrawingNode link : GuiLinkedNodes)
            {
                realNode.addLink(nodes.get(link.getLabel()));
            }
        }

    }
    
    
    
    /**
     * Builds a network topology from a String representing adjacency list.
     *         
     *   This function expects a String in this format -
     *   "node1 = node2 node3, node 2 = node1 node3, node3 = node1 node2"
     *   Where each node has a unique name, then an equals sign, then the list of nodes that can "hear" that node.
     *   This constructor will create all the nodes, their ports and the list of nodes that can hear them.
     * @param topo 
     */
    
    public Topology(String topo)
    {

        String[] split = topo.split(",");
        for (int i = 0; i < split.length; i++)
        {
            Scanner tmpScanner = new Scanner(split[i]);
            String nodeName = tmpScanner.next();
            int endingByte = currentIP++;
            String IP = "" + 10 + "." + 1 + "." + 6 + "." + endingByte;
            nameToIPMap.put(nodeName, IP);
            NodeInformation temp = new NodeInformation(nodeName, IP);
            nodes.put(nodeName, temp);
        }

        System.out.println("List of all nodes in system = " + nameToIPMap.toString());

        //adds to each node the list of nodes that can "hear" it
        for (int i = 0; i < split.length; i++)
        {
            Scanner tmpScanner = new Scanner(split[i]);
            NodeInformation tempNode = nodes.get(tmpScanner.next());
            while (tmpScanner.hasNext())
            {
                NodeInformation readPort = nodes.get(tmpScanner.next());
                if (readPort != null)
                {
                    tempNode.addLink(readPort);
                }
            }
        }

    }

    public HashMap<String, NodeInformation> getNodes()
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
            
            NodeInformation temp = nodes.get(currentKey);
            String thisRouter = temp.getName() + " " + temp.getIP();
            thisRouter += temp.heardByIPsToString();
            nodesAndListeners[i] = thisRouter;
            i++;
            //System.out.println(thisRouter);
        }
        return nodesAndListeners;
    }
    
    public static void main (String[] args)
    {
        Topology temp = new Topology("A = B C E H, B = A D G, C = A, D = B F, E = A, F = D, G = B, H = A");
        temp.getNodeAndListenerIPs();
    }
}
