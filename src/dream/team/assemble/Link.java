package dream.team.assemble;

/**
 *
 * @author Cal
 * 
 * Represents Links Between Nodes
 */
public class Link
{
    Node[] nodes = new Node[2];
    int weight;
    
    public Link(Node connectFrom, Node connectTo, int weight)
    {
        nodes[0] = connectFrom;
        nodes[1] = connectTo;
        this.weight = weight;
    }
    
    // Returns the other node in the connection
    public Node getConnection(Node connectFrom)
    {
        return ((nodes[0] == connectFrom) ? nodes[1] : nodes[0]);
    }
    
    public boolean contains(Node node)
    {
        return (nodes[0] == node || nodes[1] == node);
    }
    
    public String toString()
    {
        return nodes[0].getAddress() + " <-> " + nodes[1].getAddress();
    }
}
