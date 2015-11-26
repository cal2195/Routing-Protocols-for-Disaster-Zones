package dream.team.assemble.gui;

import java.util.ArrayList;

/**
 *
 * @author Cal
 */
public class Node extends Button
{
    ArrayList<Node> linkedNodes;
    boolean dragged = false;
    
    public Node(int x, int y, int width, int height, String label)
    {
        super(x, y, width, height, label);
        linkedNodes = new ArrayList<>();
    }

    public ArrayList<Node> getLinkedNodes()
    {
        return linkedNodes;
    }

    public void addLinkedNode(Node node)
    {
        linkedNodes.add(node);
    }
}
