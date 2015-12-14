package dream.team.assemble.gui;

import processing.core.PFont;

/**
 *
 * @author Cal
 */
public class NodePanel
{

    float xOffset, yOffset, width, height;
    String label;
    int widgetColor = Colour.colour(0, 255, 255), labelColor = 0;
    PFont widgetFont;
    boolean show = false;
    DrawingNode parent;

    public NodePanel(float xOffset, float yOffset, float width, float height, String label, DrawingNode parent)
    {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
        this.label = label;
        this.parent = parent;
    }

    public void draw(RoutingGUI gui)
    {
        gui.fill(widgetColor);
        gui.rect(parent.getX() + xOffset - width, parent.getY() + yOffset - height, width, height);
        gui.line(parent.getX(), parent.getY(), parent.getX() + xOffset, parent.getY() + yOffset);
    }

    public float getxOffset()
    {
        return xOffset;
    }

    public void setxOffset(float xOffset)
    {
        this.xOffset = xOffset;
    }

    public float getyOffset()
    {
        return yOffset;
    }

    public void setyOffset(float yOffset)
    {
        this.yOffset = yOffset;
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
