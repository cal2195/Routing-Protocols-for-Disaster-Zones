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
}
