package dream.team.assemble.gui;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Cal
 */
public class Screen
{

    int background;
    ArrayList<Button> buttonList;
    ArrayList<DrawingNode> nodeList;
    int screenID;
    RoutingGUI gui;
    Event event = new Event()
    {
        @Override
        void event()
        {
            System.err.println("ERROR: Screen has no event!");
        }
    };

    public Screen(int screenID, RoutingGUI gui)
    {
        this.screenID = screenID;
        this.gui = gui;
        buttonList = new ArrayList<>();
        nodeList = new ArrayList<>();
    }

    int getBackground()
    {
        return background;
    }
    
    public DrawingNode getNode(String label)
    {
        for (DrawingNode node : nodeList)
        {
            if (label.equals(node.getLabel()))
            {
                return node;
            }
        }
        return null;
    }

    public Event getEvent(RoutingGUI gui)
    {
        for (int i = 0; i < buttonList.size(); i++)
        {
            Button button = buttonList.get(i).getEvent(gui.mouseX, gui.mouseY, gui);
            if (button != null)
            {
                return button.getEvent();
            }
        }
        for (int i = 0; i < nodeList.size(); i++)
        {
            Button nodeButton = (Button) nodeList.get(i).getEvent(gui.mouseX, gui.mouseY, gui);
            if (nodeButton != null)
            {
                return nodeButton.getEvent();
            }
        }
        return event;
    }

    public boolean mouseOnButton(RoutingGUI gui)
    {
        for (Button buttonList1 : buttonList)
        {
            Button button = buttonList1.getEvent(gui.mouseX, gui.mouseY, gui);
            if (button != null)
            {
                return true;
            }
        }
        for (DrawingNode nodeList1 : nodeList)
        {
            Button nodeButton = (Button) nodeList1.getEvent(gui.mouseX, gui.mouseY, gui);
            if (nodeButton != null)
            {
                return true;
            }
        }
        return false;
    }

    void setBackground(int setColor)
    {
        background = setColor;
    }

    void draw(RoutingGUI gui)
    {
        gui.background(background);

        for (int i = 0; i < nodeList.size(); i++)
        {
            DrawingNode node = nodeList.get(i);
            if (!node.getLinkedNodes().isEmpty())
            {
                for (DrawingNode linkedNode : node.getLinkedNodes())
                {
                    gui.pushStyle();
                    if (node.shortest && linkedNode.shortest)
                    {
                        gui.stroke(255,0,0);
                        gui.strokeWeight(4);
                    }
                    gui.line(node.getX() + node.getWidth() / 2, node.getY() + node.getHeight() / 2, linkedNode.getX() + linkedNode.getWidth() / 2, linkedNode.getY() + linkedNode.getHeight() / 2);
                    gui.popStyle();
                }
            }
        }

        for (int i = 0; i < nodeList.size(); i++)
        {
            Button node = (Button) nodeList.get(i);
            node.draw(gui);
        }

        for (int i = 0; i < buttonList.size(); i++)
        {
            Button button = (Button) buttonList.get(i);
            button.draw(gui);
        }

    }

    void addWidget(Button widget)
    {
        buttonList.add(widget);
    }

    void addNode(DrawingNode node)
    {
        nodeList.add(node);
    }

    void unselectAll()
    {

        for (int i = 0; i < buttonList.size(); i++)
        {
            Button aWidget = (Button) buttonList.get(i);
            aWidget.selected = false;
        }

        for (int i = 0; i < nodeList.size(); i++)
        {
            Button aWidget = (Button) nodeList.get(i);
            aWidget.selected = false;
        }

    }

}
