package dream.team.assemble.gui;

import dream.team.assemble.routing.core.topology.RoutingEntry;
import dream.team.assemble.routing.core.topology.RoutingTable;
import dream.team.assemble.routing.core.topology.ShortestPathAlgorithm;
import dream.team.assemble.routing.core.topology.Topology;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Cal
 */
public class NetworkBuilderScreen extends Screen
{

    Button addNode, addLink, selectMode, randomNetwork, shortestPath, inspect;
    DrawingNode firstLinkNode;
    Timer shortestRandom = new Timer(3000, new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if (nodeList.size() > 2)
            {
                randomShorestRoute();
            }
        }
    });

    public NetworkBuilderScreen(int screenID, RoutingGUI gui)
    {
        super(screenID, gui);
        setup();
        shortestRandom.start();
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
                    int amount = (int) gui.random(12);
                    float radius = (gui.width - 200) / 4;
                    float angle = RoutingGUI.TWO_PI / (float) (amount + 5);
                    for (int i = 0; i < amount; i++)
                    {
                        addNewNode((radius * RoutingGUI.sin(angle * i)) + gui.width / 2, (radius * RoutingGUI.cos(angle * i)) + gui.height / 2, "" + (char) ('A' + nodeList.size()));
                    }
                    nodeList.stream().forEach((node)
                            -> 
                            {
                                nodeList.stream().filter((node2) -> (node != node2 && (int) gui.random(3) == 0)).map((node2)
                                        -> 
                                        {
                                            node.addLinkedNode(node2);
                                            return node2;
                                }).forEach((node2)
                                        -> 
                                        {
                                            node2.addLinkedNode(node);
                                });
                    });
                    //Throw in some extra endpoints
                    for (int i = 0; i < 5; i++)
                    {
                        addNewNode((radius * RoutingGUI.sin(angle * (amount + i))) + gui.width / 2, (radius * RoutingGUI.cos(angle * (amount + i))) + gui.height / 2, "" + (char) ('A' + nodeList.size()));
                        DrawingNode endpoint = nodeList.get(nodeList.size() - 1);
                        DrawingNode randomNode = nodeList.get((int) gui.random(nodeList.size() - 2));
                        endpoint.addLinkedNode(randomNode);
                        randomNode.addLinkedNode(endpoint);
                    }

                    for (int i = 0; i < nodeList.size(); i++)
                    {
                        DrawingNode node = nodeList.get(i);
                        if (node.linkedNodes.isEmpty())
                        {
                            nodeList.remove(i--);
                        }
                    }
                    System.out.println(toTopology());
                }
            }
        });

        shortestPath = new Button(10, 240, 140, 40, "Shortest Path");
        shortestPath.setLabelColor(Colour.colour(100));
        shortestPath.setEvent(new Event()
        {
            @Override
            void event()
            {
                gui.mode = RoutingGUI.MODE.SHORTEST_PATH_MODE;
                gui.helpTextBar.setNewHelpText("Click on any node to see it's shortest path tree!", gui);
                //randomShorestRoute();
            }
        });
        
        inspect = new Button(10, 290, 140, 40, "Inspect");
        inspect.setLabelColor(Colour.colour(100));
        inspect.setEvent(new Event()
        {
            @Override
            void event()
            {
                gui.mode = RoutingGUI.MODE.INSPECTOR_MODE;
                gui.helpTextBar.setNewHelpText("Click on any node to inspect it!", gui);
            }
        });

        buttonList.add(addNode);
        buttonList.add(addLink);
        buttonList.add(selectMode);
        buttonList.add(randomNetwork);
        buttonList.add(shortestPath);
        buttonList.add(inspect);

        background = Colour.colour(255, 255, 255);

        event = new Event()
        {
            @Override
            void event()
            {
                if (gui.mode == RoutingGUI.MODE.ADD_NODE_MODE)
                {
                    addNewNode(gui.mouseX, gui.mouseY, "" + (char) ('A' + nodeList.size()));
                }
            }
        };
    }

    @Override
    void draw(RoutingGUI gui)
    {
        super.draw(gui);
        if (gui.mode == RoutingGUI.MODE.ADD_LINK_SELECTING_SECOND)
        {
            gui.line(firstLinkNode.getX() + firstLinkNode.getWidth() / 2, firstLinkNode.getY() + firstLinkNode.getHeight() / 2, gui.mouseX, gui.mouseY);
        }
        if (gui.mode != RoutingGUI.MODE.NODE_DRAG)
        {
            updateNodePositions();
        }
    }

    public String toTopology()
    {
        String topology = "";
        for (DrawingNode node : nodeList)
        {
            topology += (topology.equals("") ? "" : ", ") + node.getLabel() + " =";
            for (DrawingNode link : node.getLinkedNodes())
            {
                topology += " " + link.getLabel();
            }
        }
        return topology;
    }

    public void dragNode(DrawingNode node, DrawingNode parent)
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
            for (DrawingNode nodechild : node.getLinkedNodes())
            {
                dragNode(nodechild, node);
            }
        }
    }

    public void randomShorestRoute()
    {
        for (DrawingNode node : nodeList)
        {
            node.shortest = false;
        }

        DrawingNode randomStart = nodeList.get((int) gui.random(nodeList.size() - 1));
        Topology topology = new Topology(toTopology());
        RoutingTable table = ShortestPathAlgorithm.getRoutingTable(topology.getNodes().get(randomStart.getLabel()));

        DrawingNode randomEnd = nodeList.get((int) gui.random(nodeList.size() - 1));
        
        while(randomEnd == randomStart)
            randomEnd = nodeList.get((int) gui.random(nodeList.size() - 1));
        
        randomStart.shortest = true;
        randomEnd.shortest = true;

        gui.helpTextBar.setNewHelpText("Shortest path between " + randomStart.getLabel() + " and " + randomEnd.getLabel(), gui);

        while (randomStart != randomEnd)
        {
            boolean found = false;
            for (RoutingEntry entry : table.getTable())
            {
                if (entry.getDest().getName().equals(randomEnd.getLabel()))
                {
                    found = true;
                    setShorest(entry.getNode().getName());
                    for (DrawingNode node : nodeList)
                    {
                        if (node.getLabel().equals(entry.getNode().getName()))
                        {
                            randomEnd = node;
                        }
                    }
                }
            }
            if (!found)
            {
                return;
            }
        }
    }

    public void setShorest(String name)
    {
        System.out.println("Setting " + name + " as shortest");
        for (DrawingNode node : nodeList)
        {
            if (node.getLabel().equals(name))
            {
                node.shortest = true;
            }
        }
    }

    public void addNewNode(float x, float y, String name)
    {
        DrawingNode tmpNode = new DrawingNode(x - 30, y - 15, 60, 30, name);
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
                        case ADD_NODE_MODE:
                            if (gui.mouseButton == gui.RIGHT)
                            {
                                nodeList.remove(tmpNode);
                                for (DrawingNode node : tmpNode.linkedNodes)
                                {
                                    node.linkedNodes.remove(tmpNode);
                                }
                                break;
                            }
                            break;
                        case ADD_LINK_MODE:
                            if (gui.mouseButton == gui.RIGHT)
                            {
                                nodeList.remove(tmpNode);
                                for (DrawingNode node : tmpNode.linkedNodes)
                                {
                                    node.linkedNodes.remove(tmpNode);
                                }
                                break;
                            }
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
                            for (DrawingNode node : nodeList)
                            {
                                node.dragged = false;
                            }
                            tmpNode.setX(gui.mouseX - tmpNode.getWidth() / 2);
                            tmpNode.setY(gui.mouseY - tmpNode.getHeight() / 2);
                            tmpNode.dragged = true;
                            if (gui.mouseButton == gui.LEFT)
                            {
                                for (DrawingNode node : tmpNode.getLinkedNodes())
                                {
                                    dragNode(node, tmpNode);
                                }
                            }
                            break;
                        case SHORTEST_PATH_MODE:
                            Topology topology = new Topology(toTopology());
                            RoutingTable table = ShortestPathAlgorithm.getRoutingTable(topology.getNodes().get(tmpNode.getLabel()));
                            System.out.println(table);
                            nodeList.clear();
                            addNewNode(gui.random(gui.width), gui.random(gui.height), tmpNode.getLabel());
                            nodeList.get(nodeList.size() - 1).start = true;
                            for (RoutingEntry entry : table.getTable())
                            {
                                boolean foundDest = false, foundStart = false;
                                for (DrawingNode node : nodeList)
                                {
                                    if (node.getLabel().equals(entry.getDest().getName()))
                                    {
                                        foundDest = true;
                                    }
                                    if (node.getLabel().equals(entry.getNode().getName()))
                                    {
                                        foundStart = true;
                                    }
                                }
                                if (!foundDest)
                                {
                                    addNewNode(gui.random(gui.width), gui.random(gui.height), entry.getDest().getName());
                                }
                                if (!foundStart)
                                {
                                    addNewNode(gui.random(gui.width), gui.random(gui.height), entry.getNode().getName());
                                }
                                for (DrawingNode node : nodeList)
                                {
                                    if (node.getLabel().equals(entry.getNode().getName()))
                                    {
                                        for (DrawingNode node2 : nodeList)
                                        {
                                            if (node2.getLabel().equals(entry.getDest().getName()))
                                            {
                                                node.addLinkedNode(node2);
                                                node2.addLinkedNode(node);
                                            }
                                        }
                                    }
                                }
                            }
                        default:
                            break;
                    }
                }
            }
        });
        nodeList.add(tmpNode);
    }

    ;

    float distanceSqr(float dx, float dy)
    {
        return dx * dx + dy * dy;
    }

    Force force(DrawingNode nodeA, DrawingNode nodeB, int force)
    {
        float dx = nodeA.x - nodeB.x;
        float dy = nodeA.y - nodeB.y;
        float angle = (float) Math.atan2(dy, dx);
        float ds = 0;
        switch (force)
        {
            case 0:
                ds = repelForce(distanceSqr(dx, dy));
                break;
            case 1:
                ds = attractForce(distanceSqr(dx, dy));
                break;
            case 2:
                ds = gravityForce(distanceSqr(dx, dy));
                break;
        }
        return new Force((float) Math.cos(angle) * ds, (float) Math.sin(angle) * ds);
    }

    float repelForce(float distanceSqr)
    {
        return 70000.0f / distanceSqr;
    }

    float attractForce(float distanceSqr)
    {
        return -distanceSqr / 6000.0f;
    }

    float gravityForce(float distanceSqr)
    {
        return (float) (-Math.sqrt(distanceSqr) / ((RoutingGUI.sin(gui.frameCount / 100f) * 10f) + 40f));
    }

    Force[] calculateForces()
    {
        Force[] forces = new Force[nodeList.size()];
        for (int i = 0; i < nodeList.size(); i++)
        {
            forces[i] = new Force(0, 0);

            // repelling between nodes:
            for (int j = 0; j < nodeList.size(); j++)
            {
                if (i == j)
                {
                    continue;
                }
                Force f = force(nodeList.get(i), nodeList.get(j), 0);
                forces[i].x += f.x;
                forces[i].y += f.y;
            }

            // attraction between connected nodes:
            for (int j = 0; j < nodeList.get(i).linkedNodes.size(); j++)
            {
                Force f = force(nodeList.get(i), nodeList.get(i).linkedNodes.get(j), 1);
                forces[i].x += f.x;
                forces[i].y += f.y;
            }

            // gravity:
            //var center = { x: 400, y: 300 };
            DrawingNode center = new DrawingNode(gui.width / 2, gui.height / 2, 0, 0, "Center");
            Force f = force(nodeList.get(i), center, 2);
            forces[i].x += f.x;
            forces[i].y += f.y;
        }
        return forces;
    }

    void updateNodePositions()
    {
        Force[] forces = calculateForces();
        for (int i = 0; i < forces.length; i++)
        {
            nodeList.get(i).x += forces[i].x;
            nodeList.get(i).y += forces[i].y;
        }
    }
}
