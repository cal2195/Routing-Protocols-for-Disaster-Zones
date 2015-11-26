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
    ArrayList<Button> nodeList;
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

    void setBackground(int setColor)
    {
        background = setColor;
    }

    void draw(RoutingGUI gui)
    {
        gui.background(background);
        
        for (int i = 0; i < nodeList.size(); i++)
        {
            Button aWidget = (Button) nodeList.get(i);
            aWidget.draw(gui);
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

    void addNode(Button widget)
    {
        nodeList.add(widget);
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
