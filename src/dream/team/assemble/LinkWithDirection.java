package dream.team.assemble;

/**
 *
 * @author Cal
 */
public class LinkWithDirection
{
    Link link;
    Node startNode;
    
    public LinkWithDirection(Link link, Node startNode)
    {
        this.link = link;
        this.startNode = startNode;
    }
    
    public void getNode()
    {
        link.getConnection(startNode);
    }
}
