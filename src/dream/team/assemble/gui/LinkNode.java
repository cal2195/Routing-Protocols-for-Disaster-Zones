package dream.team.assemble.gui;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/**
 *
 * @author Cal
 */
public class LinkNode extends Button
{
    DrawingNode[] nodes = new DrawingNode[2];

    public LinkNode(float width, float height, String label, DrawingNode to, DrawingNode from, LinkPingGUI linkPingGUI)
    {
        super(0, 0, width, height, label);
        nodes[0] = to;
        nodes[1] = from;
        widgetColor = Colour.colour(255);
        selectedColor = Colour.colour(240);
        setEvent(new Event()
        {

            @Override
            void event()
            {
                try
                {
                    int ping = Integer.parseInt(JOptionPane.showInputDialog(null));
                    linkPingGUI.setPing(nodes[0], nodes[1], ping);
                    setLabel(ping + "");
                } catch (HeadlessException headlessException)
                {
                } catch (NumberFormatException numberFormatException)
                {
                }
            }
        });
    }

    @Override
    public float getY()
    {
        return (nodes[0].getY()+ nodes[1].getY()) / 2;
    }

    @Override
    public float getX()
    {
        return (nodes[0].getX()+ nodes[1].getX()) / 2;
    }

    @Override
    void draw(RoutingGUI gui)
    {
        gui.pushStyle();
        gui.textSize(10);
        if (this.selected)
        {
            gui.fill(selectedColor);
        } else
        {
            gui.fill(widgetColor);
        }
        gui.rect(getX(), getY(), width, height);

        gui.fill(labelColor);
        gui.text(label, getX() + 10, getY() + height - 2);
        gui.popStyle();
    }

}
