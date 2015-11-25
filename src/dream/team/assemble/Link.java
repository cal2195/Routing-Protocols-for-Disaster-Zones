package dream.team.assemble;

/**
 * A class to represent a directionless Link between two Nodes.
 * 
 * @author Cal
 * @see Node
 */
public class Link
{
    private final Node[] nodes = new Node[2];
    private final int weight;
    
    /**
     * A class to represent a directionless Link between two Nodes.
     * 
     * @param connectFrom   the node to connect from
     * @param connectTo     the node to connect to
     * @param weight        the weight (ping) of the link
     */
    public Link(Node connectFrom, Node connectTo, int weight)
    {
        nodes[0] = connectFrom;
        nodes[1] = connectTo;
        this.weight = weight;
    }
    
    /**
     * Returns the node that this link connects connectFrom to.
     * 
     * @param connectFrom   one node in the link
     * @return the other node in the link
     */
    public Node getConnection(Node connectFrom)
    {
        return ((nodes[0] == connectFrom) ? nodes[1] : nodes[0]);
    }
    
    /**
     * Returns whether node is part of this link.
     * 
     * @param node  the node to check
     * @return true if the node is part of this link, false otherwise
     */
    public boolean contains(Node node)
    {
        return (nodes[0] == node || nodes[1] == node);
    }
    
    @Override
    public String toString()
    {
        return nodes[0].getAddress() + " <-> " + nodes[1].getAddress();
    }
}
