package dream.team.assemble.gui;

/**
 *
 * @author Cal
 */
public class HelpTextBar
{
    String currentHelpText, newHelpText = "";
    float defaultX, defaultY, x, width, height;
    float goalX;
    int colour, background;
    float easing = 0.1f;

    public HelpTextBar(String currentHelpText, float defaultX, float defaultY, float width, float height)
    {
        this.currentHelpText = currentHelpText;
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.width = width;
        this.height = height;
    }

    public void setNewHelpText(String newHelpText, RoutingGUI gui)
    {
        this.newHelpText = newHelpText;
        goalX = defaultX + gui.width;
    }

    public int getColour()
    {
        return colour;
    }

    public void setColour(int colour)
    {
        this.colour = colour;
    }

    public int getBackground()
    {
        return background;
    }

    public void setBackground(int background)
    {
        this.background = background;
    }

    public float getEasing()
    {
        return easing;
    }

    public void setEasing(float easing)
    {
        this.easing = easing;
    }
    
    public void draw(RoutingGUI gui)
    {
        gui.pushMatrix();
        gui.pushStyle();
        gui.fill(background);
        gui.noStroke();
        
        gui.rect(defaultX, defaultY, width, height);
        gui.textAlign(gui.CENTER);
        gui.fill(colour);
        gui.text(currentHelpText, x + width / 2, defaultY + height * 0.75f);
        gui.text(newHelpText, x - gui.width + width / 2, defaultY + height * 0.75f);
        
        if (!newHelpText.equals(""))
        {
            x += (goalX - x) * easing;
            if (goalX - x < 1)
            {
                currentHelpText = newHelpText;
                newHelpText = "";
                x = defaultX;
            }
        }
        
        gui.popStyle();
        gui.popMatrix();
    }
    
}
