package dream.team.assemble.routing.core.topology;

import dream.team.assemble.gui.DrawingNode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Dan
 */
public class Topology
{
    private HashMap<Integer, NodeInformation> nodes = new HashMap<>();
    private final int STARTING_ID = 1;
    private int currentID = STARTING_ID;

    /**
     * Creates a topology from an ArrayList of DrawingNodes.
     * @param GuiNodes 
     */
    public Topology(ArrayList<DrawingNode> GuiNodes)
    {
        
        /* create NodeInformations from GUI objects */
        for(DrawingNode GuiNode : GuiNodes)
        {
            /* Creat nodes from 1 to n for each DrawingNode */
            NodeInformation temp = new NodeInformation(GuiNode.getLabel(), currentID);
            nodes.put(currentID, temp);
            currentID++;
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
     *   Depreciated - Build topolgies from GUI objects now!
     *   Builds a network topology from a String representing adjacency list.
     *         
     *   This function expects a String in this format -
     *   "node1 = node2 node3, node 2 = node1 node3, node3 = node1 node2"
     *   Where each node has a unique name, then an equals sign, then the list of nodes that can "hear" that node.
     *   This constructor will create all the nodes, their ports and the list of nodes that can hear them.
     * @param topo 
     */
    /*
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
    */

    public HashMap<Integer, NodeInformation> getNodes()
    {
        return nodes;
    }

    @Override
    public String toString()
    {
        String temp = "";
        for (Integer currentKey : nodes.keySet())
        {
            temp += nodes.get(currentKey).description();
        }
        return temp;
    }
 
}
