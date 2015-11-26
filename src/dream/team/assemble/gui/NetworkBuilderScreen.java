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

    public void dragNode(Node node, Node parent)
    {
        if (node != parent && !node.dragged)
        {
            node.dragged = true;
            float dx = parent.getX() - node.getX();
            float dy = parent.getY() - node.getY();
            float angle = RoutingGUI.atan2(dy, dx);
            node.setX((int) (parent.getX() - RoutingGUI.cos(angle) * 200));
            node.setY((int) (parent.getY() - RoutingGUI.sin(angle) * 200));
            for (Node nodechild : node.getLinkedNodes())
            {
                dragNode(nodechild, node);
            }
        }
    }

    public void addNewNode(int x, int y)
    {
        Node tmpNode = new Node(x, y, 150, 50, "newNode");
        tmpNode.setWidgetColor(Colour.colour(255));
        tmpNode.setSelectedColor(Colour.colour(255, 0, 0));
        tmpNode.setEvent(new Event()
        {
            @Override
            void event()
            {
                if (null != gui.mode)
                {
                    switch (gui.mode)
                    {
                        case ADD_LINK_MODE:
                            firstLinkNode = tmpNode;
                            gui.mode = RoutingGUI.MODE.ADD_LINK_SELECTING_SECOND;
                            break;
                        case ADD_LINK_SELECTING_SECOND:
                            tmpNode.addLinkedNode(firstLinkNode);
                            firstLinkNode.addLinkedNode(tmpNode);
                            break;
                        case NODE_DRAG:
                            for (Node node : nodeList)
                            {
                                node.dragged = false;
                            }
                            tmpNode.setX(gui.mouseX - tmpNode.getWidth() / 2);
                            tmpNode.setY(gui.mouseY - tmpNode.getHeight() / 2);
                            tmpNode.dragged = true;
                            for (Node node : tmpNode.getLinkedNodes())
                            {
                                dragNode(node, tmpNode);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        nodeList.add(tmpNode);
    }
}
