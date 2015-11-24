package dream.team.assemble;

/**
 *
 * @author Cal
 */
public class LinkWithWeight
{
    Link link;
    int weight;
    Node startNode;
    
    public LinkWithWeight(Link link, Node startNode, int weight)
    {
        this.link = link;
        this.startNode = startNode;
        this.weight = weight;
    }
    
    public Node getNode()
    {
        return link.getConnection(startNode);
    }
    
    public void increment(int i)
    {
        weight += i;
    }
    
    public int getWeight()
    {
        return weight;
    }
    
    public Link getLink()
    {
        return link;
    }
}
