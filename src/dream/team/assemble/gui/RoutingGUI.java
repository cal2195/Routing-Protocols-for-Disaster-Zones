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

        SELECT_MODE, ADD_NODE_MODE, ADD_LINK_MODE, ADD_LINK_SELECTING_SECOND, NODE_DRAG
    };

    static enum EVENT
    {

        NODE_START, NULL, CLICKED_SPACE, ADD_NODE
    };
    final int EVENT_NODE_START = 10;
    final int EVENT_NULL = 0;
    final int CLICKED_SPACE = 0;

    Screen[] screens = new Screen[1];
    MODE mode;
    Node draggingNode = null;
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

        screens[0] = new NetworkBuilderScreen(0, this);
    }

    @Override
    public void draw()
    {
        screens[0].draw(this);
    }

    @Override
    public void mousePressed()
    {
        screens[0].getEvent(this).event();
//        if (button == 1)
//        {
//            mode = MODE.ADD_NODE_MODE;
//        } else if (button == ADD_LINK_MODE)
//        {
//            println("Darkest black.");
//        } else if (button >= 10)
//        {
//            println("Node " + (button - 10) + " clicked");
//            currentlySelected = button - 10;
//        }
//
//        if (button == CLICKED_SPACE && mode == ADD_NODE_MODE)
//        {
//            Button tmpNode = new Button(mouseX, mouseY, 50, 50, "newNode", EVENT_NODE_START + nodesAdded++, this);
//            tmpNode.setWidgetColor(0x888);
//            screens[0].addNode(tmpNode);
//            mode = 0;
//        }
//
//        if (button >= 10 && mode == ADD_LINK_MODE)
//        {
//            Button tmpNode = (Button) screens[0].nodeList.get(button - 10);
//            tmpNode.addLink(button - 10);
//            mode = ADD_LINK_SELECTING_SECOND;
//        }
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
    }
}
