package dream.team.assemble.routing.core.topology;

import dream.team.assemble.gui.DrawingNode;
import dream.team.assemble.gui.LinkPingGUI;
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
    public Topology(ArrayList<DrawingNode> GuiNodes, LinkPingGUI linkPingGUI)
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
        for(DrawingNode guiNode : GuiNodes)
        {
            NodeInformation realNode = nameToNodeInfo.get(guiNode.getLabel());
            ArrayList<DrawingNode> GuiLinkedNodes = guiNode.getLinkedNodes();
            for(DrawingNode link : GuiLinkedNodes)
            {
                realNode.addLink(nameToNodeInfo.get(link.getLabel()), linkPingGUI.getPing(guiNode, link));
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
