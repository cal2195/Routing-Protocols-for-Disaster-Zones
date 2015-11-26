package dream.team.assemble.gui;

/**
 *
 * @author Cal
 */
public class NetworkBuilderScreen extends Screen
{

    Button addNode, addLink;

    public NetworkBuilderScreen(int screenID, RoutingGUI gui)
    {
        super(screenID, gui);
        setup();
    }

    public void setup()
    {
        addNode = new Button(10, 10, 100, 40, "Add Node");
        addNode.setLabelColor(Colour.colour(100));
        addNode.setEvent(new Event()
        {
            @Override
            void event()
            {
                gui.mode = RoutingGUI.MODE.ADD_NODE_MODE;
            }
        });

        addLink = new Button(10, 60, 100, 40, "Add Link");
        addLink.setLabelColor(Colour.colour(100));

        buttonList.add(addNode);
        buttonList.add(addLink);

        background = Colour.colour(255, 255, 255);

        event = new Event()
        {
            @Override
            void event()
            {
                if (gui.mode == RoutingGUI.MODE.ADD_NODE_MODE)
                {
                    Button tmpNode = new Button(gui.mouseX, gui.mouseY, 150, 50, "newNode");
                    tmpNode.setWidgetColor(0x888);
                    nodeList.add(tmpNode);
                }
            }
        };
    }
}
