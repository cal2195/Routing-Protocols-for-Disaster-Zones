package dream.team.assemble.gui;

import java.util.ArrayList;

/**
 *
 * @author Cal
 */
public class Screen
{

    int background;
    ArrayList<Button> buttonList;
    ArrayList<Node> nodeList;
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
        for (Node nodeList1 : nodeList)
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
            Node node = nodeList.get(i);
            if (!node.getLinkedNodes().isEmpty())
            {
                for (Node linkedNode : node.getLinkedNodes())
                gui.line(node.getX() + node.getWidth() / 2, node.getY() + node.getHeight() / 2, linkedNode.getX() + linkedNode.getWidth() / 2, linkedNode.getY() + linkedNode.getHeight()/ 2);
            }
        }

        for (int i = 0; i < nodeList.size(); i++)
        {
            Button aWidget = (Button) nodeList.get(i);
            aWidget.draw(gui);
        }
        
        for (int i = 0; i < buttonList.size(); i++)
        {
            Button aWidget = (Button) buttonList.get(i);
            aWidget.draw(gui);
        }

    }

    void addWidget(Button widget)
    {
        buttonList.add(widget);
    }

    void addNode(Node node)
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