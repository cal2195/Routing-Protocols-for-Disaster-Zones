package dream.team.assemble.gui;

import processing.event.MouseEvent;

/**
 *
 * @author Cal
 */
public class NetworkBuilderScreen extends Screen
{

    Button addNode, addLink, selectMode, randomNetwork;
    Node firstLinkNode;

    public NetworkBuilderScreen(int screenID, RoutingGUI gui)
    {
        super(screenID, gui);
        setup();
    }

    public void setup()
    {
        addNode = new Button(10, 40, 120, 40, "Add Node");
        addNode.setLabelColor(Colour.colour(100));
        addNode.setEvent(new Event()
        {
            @Override
            void event()
            {
                gui.mode = RoutingGUI.MODE.ADD_NODE_MODE;
                gui.helpTextBar.setNewHelpText("Click anywhere on the canvas to add nodes!", gui);
            }
        });

        addLink = new Button(10, 90, 120, 40, "Add Link");
        addLink.setLabelColor(Colour.colour(100));
        addLink.setEvent(new Event()
        {
            @Override
            void event()
            {
                gui.mode = RoutingGUI.MODE.ADD_LINK_MODE;
                gui.helpTextBar.setNewHelpText("Click between nodes to add links!", gui);
            }
        });

        selectMode = new Button(10, 140, 120, 40, "Select Mode");
        selectMode.setLabelColor(Colour.colour(100));
        selectMode.setEvent(new Event()
        {
            @Override
            void event()
            {
                gui.mode = RoutingGUI.MODE.SELECT_MODE;
                gui.helpTextBar.setNewHelpText("Left Click and Drag on any node to move the whole system, Right Click and Drag to just move a single node!", gui);
            }
        });

        randomNetwork = new Button(10, 190, 160, 40, "Random Network");
        randomNetwork.setLabelColor(Colour.colour(100));
        randomNetwork.setEvent(new Event()
        {
            @Override
            void event()
            {
                gui.mode = RoutingGUI.MODE.SELECT_MODE;
                nodeList.clear();
                while (nodeList.isEmpty())
                {
                    int amount = (int) gui.random(10);
                    float radius = 300;
                    float angle = RoutingGUI.TWO_PI / (float) amount;
                    for (int i = 0; i < amount; i++)
                    {
                        addNewNode((int) (radius * RoutingGUI.sin(angle * i)) + gui.width / 2, (int) (radius * RoutingGUI.cos(angle * i)) + gui.height / 2);
                    }
                    for (Node node : nodeList)
                    {
                        for (Node node2 : nodeList)
                        {
                            if (node != node2 && (int) gui.random(5) == 0)
                            {
                                node.addLinkedNode(node2);
                                node2.addLinkedNode(node);
                            }
                        }
                    }
                    for (int i = 0; i < nodeList.size(); i++)
                    {
                        Node node = nodeList.get(i);
                        if (node.linkedNodes.isEmpty())
                        {
                            nodeList.remove(i--);
                        }
                    }
                }
            }
        });

        buttonList.add(addNode);
        buttonList.add(addLink);
        buttonList.add(selectMode);
        buttonList.add(randomNetwork);

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
            if (node.distanceFromParent == -1f)
            {
                node.distanceFromParent = (RoutingGUI.sqrt((node.getX() - parent.getX()) * (node.getX() - parent.getX()) + (node.getY() - parent.getY()) * (node.getY() - parent.getY())));
            }
            float dx = parent.getX() - node.getX();
            float dy = parent.getY() - node.getY();
            float angle = RoutingGUI.atan2(dy, dx);
            node.setX((parent.getX() - RoutingGUI.cos(angle) * node.distanceFromParent));
            node.setY((parent.getY() - RoutingGUI.sin(angle) * node.distanceFromParent));
            for (Node nodechild : node.getLinkedNodes())
            {
                dragNode(nodechild, node);
            }
        }
    }

    public void addNewNode(int x, int y)
    {
        Node tmpNode = new Node(x - 75, y - 25, 150, 50, "newNode");
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
                            firstLinkNode = tmpNode;
                            //gui.mode = RoutingGUI.MODE.ADD_LINK_MODE;
                            break;
                        case NODE_DRAG:
                            gui.draggingNode = tmpNode;
                            for (Node node : nodeList)
                            {
                                node.dragged = false;
                            }
                            tmpNode.setX(gui.mouseX - tmpNode.getWidth() / 2);
                            tmpNode.setY(gui.mouseY - tmpNode.getHeight() / 2);
                            tmpNode.dragged = true;
                            if (gui.mouseButton == gui.LEFT)
                            {
                                for (Node node : tmpNode.getLinkedNodes())
                                {
                                    dragNode(node, tmpNode);
                                }
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
