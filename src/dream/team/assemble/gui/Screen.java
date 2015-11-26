package dream.team.assemble.gui;

import java.util.ArrayList;

/**
 *
 * @author Cal
 */
public class Screen
{

    int background;
    ArrayList<Widget> widgetList;
    ArrayList<Widget> nodeList;
    int screenID;

    public Screen(int screenID)
    {
        this.screenID = screenID;
        widgetList = new ArrayList<>();
        nodeList = new ArrayList<>();
    }

    int getBackground()
    {
        return background;
    }

    int getEvent(RoutingGUI gui)
    {

        int event;
        for (int i = 0; i < widgetList.size(); i++)
        {
            Widget aWidget = (Widget) widgetList.get(i);
            event = aWidget.getEvent(gui.mouseX, gui.mouseY, gui);
            if (event != 0)
            {
                return event;
            }
        }
        for (int i = 0; i < nodeList.size(); i++)
        {
            Widget aWidget = (Widget) nodeList.get(i);
            event = aWidget.getEvent(gui.mouseX, gui.mouseY, gui);
            if (event != 0)
            {
                return event;
            }
        }
        return 0;
    }

    void setBackground(int setColor)
    {
        background = setColor;
    }

    void draw(RoutingGUI gui)
    {
        gui.background(background);

        for (int i = 0; i < widgetList.size(); i++)
        {
            Widget aWidget = (Widget) widgetList.get(i);
            aWidget.draw(gui);
        }

        for (int i = 0; i < nodeList.size(); i++)
        {
            Widget aWidget = (Widget) nodeList.get(i);
            aWidget.draw(gui);
        }

    }

    void addWidget(Widget widget)
    {
        widgetList.add(widget);
    }

    void addNode(Widget widget)
    {
        nodeList.add(widget);
    }

    void unselectAll()
    {

        for (int i = 0; i < widgetList.size(); i++)
        {
            Widget aWidget = (Widget) widgetList.get(i);
            aWidget.selected = false;
        }

        for (int i = 0; i < nodeList.size(); i++)
        {
            Widget aWidget = (Widget) nodeList.get(i);
            aWidget.selected = false;
        }

    }

}
