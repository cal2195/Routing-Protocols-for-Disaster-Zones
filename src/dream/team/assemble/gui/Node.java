package dream.team.assemble.gui;

/**
 *
 * @author Cal
 */
public class Node extends Button
{
    Node linkedNode;
    
    public Node(int x, int y, int width, int height, String label)
    {
        super(x, y, width, height, label);
    }

    public Node getLinkedNode()
    {
        return linkedNode;
    }

    public void setLinkedNode(Node linkedNode)
    {
        this.linkedNode = linkedNode;
    }
}
