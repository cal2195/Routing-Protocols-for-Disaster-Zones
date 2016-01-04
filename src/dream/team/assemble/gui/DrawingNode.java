package dream.team.assemble.gui;

import java.util.ArrayList;

/**
 *
 * @author Cal
 */
public class DrawingNode extends Button
{

    ArrayList<DrawingNode> linkedNodes;
    boolean dragged = false, start = false;
    float distanceFromParent = -1f;
    boolean shortest = false;
    NodeGUI nodeGUI;

    public DrawingNode(float x, float y, float width, float height, String label)
    {
        super(x, y, width, height, label);
        linkedNodes = new ArrayList<>();
        nodeGUI = new NodeGUI();
    }

    public ArrayList<DrawingNode> getLinkedNodes()
    {
        return linkedNodes;
    }

    public void addLinkedNode(DrawingNode node)
    {
        if(node == this)
            return;
        for (DrawingNode link : linkedNodes)
        {
            if (node == link)
            {
                return;
            }
        }
        linkedNodes.add(node);
    }

    public boolean isEndpoint()
    {
        return linkedNodes.size() <= 1;
    }

    public boolean isRouter()
    {
        return !isEndpoint();
    }

    @Override
    void draw(RoutingGUI gui)
    {
        if (this.selected)
        {
            gui.fill(selectedColor);
        } else
        {
            if (start)
            {
                gui.fill((Colour.colour(255, 0, 0)));
            } else if (isRouter())
            {
                gui.fill((Colour.colour(0, 0, 255)));
            } else
            {
                gui.fill((Colour.colour(0, 255, 0)));
            }
        }
        gui.rect(x, y, width, height);

        if (isRouter())
        {
            gui.fill((Colour.colour(255)));
        } else
        {
            gui.fill((Colour.colour(0)));
        }
        gui.text(label, x + 10, y + height - 10);
    }
}
