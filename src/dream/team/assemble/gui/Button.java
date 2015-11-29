package dream.team.assemble.gui;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PFont;

/**
 *
 * @author Cal
 */
public class Button
{

    float x, y, width, height;
    String label;
    int widgetColor, labelColor = 0, selectedColor = 0xDDD;
    PFont widgetFont;
    boolean selected = false;
    Event event = new Event()
    {
        @Override
        void event()
        {
            System.err.println("ERROR: Button has no event!");
        }
    };

    public Button(float x, float y, float width, float height, String label)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
    }

    public Event getEvent()
    {
        return event;
    }

    public void setEvent(Event event)
    {
        this.event = event;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
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
    
    public void event()
    {
        event.event();
    }

    void draw(RoutingGUI gui)
    {
        if (this.selected)
        {
            gui.fill(selectedColor);
        } else
        {
            gui.fill(widgetColor);
        }
        gui.rect(x, y, width, height);

        gui.fill(labelColor);
        gui.text(label, x + 10, y + height - 10);
    }

    public Button getEvent(int mX, int mY, RoutingGUI gui)
    {
        if (mX > x && mX < x + width && mY > y && mY < y + height)
        {
            gui.screens[0].unselectAll();
            this.selected = !this.selected;
            return this;
        } else
        {
            this.selected = false;
        }

        return null;
    }
}
