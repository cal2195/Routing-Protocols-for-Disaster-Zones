package dream.team.assemble.routing.core.topology;

import dream.team.assemble.gui.DrawingNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Dan
 */
public class Topology
{

    final int START_PORT = 50000;
    private HashMap<String, NodeInformation> nameToNodeInfo = new HashMap<>();
    private HashMap<String, String> nameToID = new HashMap<>();
    private HashMap<String, String> idToName = new HashMap<>();
    private final boolean useIPs = false;
    private final int STARTING_IP = 1;
    private int currentIP = STARTING_IP;

    /**
     * Creates a topology from an ArrayList of DrawingNodes.
     * @param GuiNodes 
     */
    public Topology(ArrayList<DrawingNode> GuiNodes)
    {
        
        /* create NodeInformations from GUI objects */
        for(DrawingNode GuiNode : GuiNodes)
        {
            int endingByte = currentIP++;
            String IP = "" + 10 + "." + 1 + "." + 6 + "." + endingByte;
            nameToID.put(GuiNode.getLabel(), IP);
            idToName.put(IP, GuiNode.getLabel());
            NodeInformation temp = new NodeInformation(GuiNode.getLabel(), IP);
            nameToNodeInfo.put(GuiNode.getLabel(), temp);   
        }
        
        /* add the correct links */    
        for(DrawingNode GuiNode : GuiNodes)
        {
            NodeInformation realNode = nameToNodeInfo.get(GuiNode.getLabel());
            ArrayList<DrawingNode> GuiLinkedNodes = GuiNode.getLinkedNodes();
            for(DrawingNode link : GuiLinkedNodes)
            {
                realNode.addLink(nameToNodeInfo.get(link.getLabel()));
            }
        }

    }
    
    
    
    /**
     *   Depreciated - Build topolgies from GUI objects now!
     *   Builds a network topology from a String representing adjacency list.
     *         
     *   This function expects a String in this format -
   "node1 = node2 node3, node 2 = node1 node3, node3 = node1 node2"
   Where each node has a unique name, then an equals sign, then the list of nameToInfo that can "hear" that node.
   This constructor will create all the nameToInfo, their ports and the list of nameToInfo that can hear them.
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
            nameToID.put(nodeName, IP);
            idToName.put(IP, nodeName);
            NodeInformation temp = new NodeInformation(nodeName, IP);
            nameToNodeInfo.put(nodeName, temp);
        }

        System.out.println("List of all nodes in system = " + nameToID.toString());

        //adds to each node the list of nameToInfo that can "hear" it
        for (int i = 0; i < split.length; i++)
        {
            Scanner tmpScanner = new Scanner(split[i]);
            NodeInformation tempNode = nameToNodeInfo.get(tmpScanner.next());
            while (tmpScanner.hasNext())
            {
                NodeInformation readPort = nameToNodeInfo.get(tmpScanner.next());
                if (readPort != null)
                {
                    tempNode.addLink(readPort);
                }
            }
        }

    }

    public ArrayList<String> getNeighbourIDs(String nodeId)
    {
        String nodeName = idToName.get(nodeId);
        return nameToNodeInfo.get(nodeName).getNeighbourIDs();
    }
    
    public HashMap<String, NodeInformation> getNodes()
    {
        return nameToNodeInfo;
    }
    
    public HashMap<String, String> getNameToIPMap()
    {
        return nameToID;
    }

    @Override
    public String toString()
    {
        String temp = "";
        for (String currentKey : nameToNodeInfo.keySet())
        {
            temp += nameToNodeInfo.get(currentKey).description();
        }
        return temp;
    }
 
}
