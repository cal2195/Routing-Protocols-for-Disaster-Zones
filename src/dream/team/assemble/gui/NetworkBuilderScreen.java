package dream.team.assemble.gui;

/**
 *
 * @author Cal
 */
public class NetworkBuilderScreen
{
    Widget addNode, addLink;
    
    public void setup()
    {
        addNode = new Widget(10, 10, 100, 40, "Add Node", EVENT_BUTTON1, this);
        addNode.setLabelColor(color(100));

        addLink = new Widget(10, 60, 100, 40, "Add Link", EVENT_BUTTON2, this);
        addLink.setLabelColor(color(100));

        int background1 = color(255, 255, 255);
    }
}
