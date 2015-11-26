package dream.team.assemble.gui;

import processing.core.PApplet;
import processing.core.PFont;

/**
 *
 * @author Cal
 */
public class RoutingGUI extends PApplet
{

    PFont stdFont;
    final int EVENT_BUTTON1 = 1;
    final int EVENT_BUTTON2 = 2;
    final int EVENT_NODE_START = 10;
    final int EVENT_NULL = 0;
    final int CLICKED_SPACE = 0;
    final int SELECT_MODE = 0;
    final int ADD_NODE_MODE = 1;
    final int ADD_LINK_MODE = 2;
    final int ADD_LINK_SELECTING_SECOND = 3;
    
    Screen[] screens = new Screen[1];
    int mode = 0;
    int nodesAdded = 0;
    int currentlySelected = 0;
    int[] linking = new int[2];
    boolean addingLinks = false;

    @Override
    public void settings()
    {
        size(800, 600);
    }
    
    @Override
    public void setup()
    {
        stdFont = loadFont("ComicSansMS-18.vlw");
        textFont(stdFont);

        screens[0] = new NetworkBuilderScreen();
    }
    
    public static color(int r, int g, int b)
    {
        
    }

    @Override
    public void draw()
    {
        screens[0].draw(this);
    }

    @Override
    public void mousePressed()
    {
        int event = screens[0].getEvent(this);
        if (event == 1)
        {
            mode = ADD_NODE_MODE;
        } else if (event == ADD_LINK_MODE)
        {
            println("Darkest black.");
        } else if (event >= 10)
        {
            println("Node " + (event - 10) + " clicked");
            currentlySelected = event - 10;
        }

        if (event == CLICKED_SPACE && mode == ADD_NODE_MODE)
        {
            Widget tmpNode = new Widget(mouseX, mouseY, 50, 50, "newNode", EVENT_NODE_START + nodesAdded++, this);
            tmpNode.setWidgetColor(0x888);
            screens[0].addNode(tmpNode);
            mode = 0;
        }

        if (event >= 10 && mode == ADD_LINK_MODE)
        {
            Widget tmpNode = (Widget) screens[0].nodeList.get(event - 10);
            tmpNode.addLink(event - 10);
            mode = ADD_LINK_SELECTING_SECOND;
        }
    }

}
