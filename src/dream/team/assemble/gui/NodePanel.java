package dream.team.assemble.gui;

import processing.core.PFont;

/**
 *
 * @author Cal
 */
public class NodePanel
{
    float x, y, width, height;
    String label;
    int widgetColor, labelColor = 0;
    PFont widgetFont;

    public NodePanel(float x, float y, float width, float height, String label)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
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

    public PFont getWidgetFont()
    {
        return widgetFont;
    }

    public void setWidgetFont(PFont widgetFont)
    {
        this.widgetFont = widgetFont;
    }
    
    
    
}
