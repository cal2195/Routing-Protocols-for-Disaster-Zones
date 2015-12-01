package dream.team.assemble.core.topology;

/**
 * This class is a wrapper class for the Link class. It adds a weight to each
 * link, as well as a direction given by the initial startNode.
 * 
 * @author Cal
 * @see Link
 * @see Node
 */
public class LinkWithWeight
{
    private final Link link;
    private final int weight;
    private final Node startNode;
    
    /**
     * This class is a wrapper class for the Link class. It adds a weight to each
     * link, as well as a direction given by the inital startNode.
     * 
     * @param link      the link to wrap
     * @param startNode specifies the direction of the link (from the startNode)
     * @param weight    specifies the weight (ping) of the link
     */
    public LinkWithWeight(Link link, Node startNode, int weight)
    {
        this.link = link;
        this.startNode = startNode;
        this.weight = weight;
    }
    
    /**
     * Returns the node at the end of the link.
     * 
     * @return the node the link points to
     */
    public Node getNode()
    {
        return link.getConnection(startNode);
    }
    
    /**
     * Returns the node at the start of the link.
     * 
     * @return startNode - the node the link points from
     */
    public Node getStartNode()
    {
        return startNode;
    }
    
    /**
     * Returns the weight (ping) of the link.
     * 
     * @return the weight of the link
     */
    public int getWeight()
    {
        return weight;
    }
    
    /**
     * Returns the link it wraps.
     * 
     * @return the wrapped link
     */
    public Link getLink()
    {
        return link;
    }
}
