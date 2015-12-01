package dream.team.assemble.gui;

/**
 *
 * @author Cal
 */
public class HelpTextBar
{
    String currentHelpText, newHelpText = "";
    float defaultX, defaultY, x;
    float goalX;
    int colour, background;
    float easing = 0.1f;

    public HelpTextBar(String currentHelpText)
    {
        this.currentHelpText = currentHelpText;
        this.defaultX = 0;
        this.defaultY = 0;
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
        
        gui.rect(defaultX, defaultY, gui.width, 30);
        gui.textAlign(gui.CENTER);
        gui.fill(colour);
        gui.text(currentHelpText, x + gui.width / 2, defaultY + 30 * 0.75f);
        gui.text(newHelpText, x - gui.width + gui.width / 2, defaultY + 30 * 0.75f);
        
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
