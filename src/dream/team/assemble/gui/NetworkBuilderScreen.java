package dream.team.assemble.gui;

/**
 *
 * @author Cal
 */
public class NetworkBuilderScreen extends Screen
{

    Button addNode, addLink;
    Node firstLinkNode;

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
        addLink.setEvent(new Event()
        {
            @Override
            void event()
            {
                gui.mode = RoutingGUI.MODE.ADD_LINK_MODE;
            }
        });

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
                    addNewNode(gui.mouseX, gui.mouseY);
                }
            }
        };
    }

    public void addNewNode(int x, int y)
    {
        Node tmpNode = new Node(x, y, 150, 50, "newNode");
        tmpNode.setWidgetColor(0x888);
        tmpNode.setSelectedColor(Colour.colour(255, 0, 0));
        tmpNode.setEvent(new Event()
        {
            @Override
            void event()
            {
                if (gui.mode == RoutingGUI.MODE.ADD_LINK_MODE)
                {
                    firstLinkNode = tmpNode;
                    gui.mode = RoutingGUI.MODE.ADD_LINK_SELECTING_SECOND;
                } else if (gui.mode == RoutingGUI.MODE.ADD_LINK_SELECTING_SECOND)
                {
                    tmpNode.linkedNode = firstLinkNode;
                    firstLinkNode.linkedNode = tmpNode;
                }
                //gui.mode = RoutingGUI.MODE.SELECT_MODE;
            }
        });
        nodeList.add(tmpNode);
    }
}
