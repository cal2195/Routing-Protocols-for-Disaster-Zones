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

    static enum MOUSE
    {

        BUTTON1, BUTTON2
    };

    static enum MODE
    {

        SELECT_MODE, ADD_NODE_MODE, ADD_LINK_MODE, ADD_LINK_SELECTING_SECOND, NODE_DRAG, SHORTEST_PATH_MODE, INSPECTOR_MODE
    };

    static enum EVENT
    {

        NODE_START, NULL, CLICKED_SPACE, ADD_NODE
    };
    final int EVENT_NODE_START = 10;
    final int EVENT_NULL = 0;
    final int CLICKED_SPACE = 0;

    Screen[] screens = new Screen[1];
    HelpTextBar helpTextBar;
    MODE mode;
    DrawingNode draggingNode = null;
    int nodesAdded = 0;
    int currentlySelected = 0;
    int[] linking = new int[2];
    boolean addingLinks = false;

    @Override
    public void settings()
    {
        size(1200, 600);
        //size(1700, 900);
    }

    @Override
    public void setup()
    {
        surface.setResizable(true);
        stdFont = loadFont("ComicSansMS-18.vlw");
        textFont(stdFont);

        screens[0] = new NetworkBuilderScreen(0, this);
        helpTextBar = new HelpTextBar("Click on Add Nodes to being node placement!");
        helpTextBar.background = Colour.colour(0);
        helpTextBar.colour = Colour.colour(0, 255, 0);
    }

    @Override
    public void draw()
    {
        screens[0].draw(this);
        helpTextBar.draw(this);
    }

    @Override
    public void mousePressed()
    {
        screens[0].getEvent(this).event();
    }

    @Override
    public void mouseDragged()
    {
        if (mode == MODE.SELECT_MODE || mode == MODE.NODE_DRAG)
        {
            if (draggingNode == null)
            {
                mode = MODE.NODE_DRAG;
                screens[0].getEvent(this).event();
            } else
            {
                draggingNode.event();
            }
        }

    }

    @Override
    public void mouseReleased()
    {
        draggingNode = null;
        for (DrawingNode node : screens[0].nodeList)
        {
            node.distanceFromParent = -1f;
        }
        if (mode == MODE.NODE_DRAG)
        {
            mode = MODE.SELECT_MODE;
        }
    }
}
