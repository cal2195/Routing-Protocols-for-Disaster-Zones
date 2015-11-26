package dream.team.assemble.gui;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PFont;

/**
 *
 * @author Cal
 */
public class Widget
{

    int x, y, width, height;
    String label;
    int event;
    int widgetColor, labelColor = 0, selectedColor = 0xDDD;
    PFont widgetFont;
    ArrayList<Integer> links = new ArrayList<Integer>();
    boolean selected = false;

    public Widget(int x, int y, int width, int height, String label, int event, PApplet applet)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.event = event;
       // this.widgetColor = widgetColor;
//        this.selectedColor = applet.color(200);
//        labelColor = applet.color(0);
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public int getWidgetColor()
    {
        return widgetColor;
    }

    public void setWidgetColor(int widgetColor)
    {
        this.widgetColor = widgetColor;
    }

    public int getLabelColor()
    {
        return labelColor;
    }

    public void setLabelColor(int labelColor)
    {
        this.labelColor = labelColor;
    }

    public int getSelectedColor()
    {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor)
    {
        this.selectedColor = selectedColor;
    }

    public PFont getWidgetFont()
    {
        return widgetFont;
    }

    public void setWidgetFont(PFont widgetFont)
    {
        this.widgetFont = widgetFont;
    }
    
    

    void draw(PApplet applet)
    {
        if (this.selected)
        {
            applet.fill(selectedColor);
        } else
        {
            applet.fill(widgetColor);
        }
        applet.rect(x, y, width, height);

        applet.fill(labelColor);
        applet.text(label, x + 10, y + height - 10);
    }

    int getEvent(int mX, int mY, RoutingGUI gui)
    {
        if (mX > x && mX < x + width && mY > y && mY < y + height)
        {
            gui.screens[0].unselectAll();
            this.selected = !this.selected;
            return event;
        } else
        {
            this.selected = false;
        }

        return 0;//EVENT_NULL;
    }

    void addLink(Integer nodeID)
    {
        links.add(nodeID);
    }

}
