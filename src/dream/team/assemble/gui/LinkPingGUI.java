package dream.team.assemble.gui;

import java.util.HashMap;

/**
 *
 * @author Cal
 */
public class LinkPingGUI
{
    HashMap<Integer, Integer> pings = new HashMap<>();

    public void setPing(DrawingNode node, DrawingNode node2, int ping)
    {
        pings.put(node.hashCode() + node2.hashCode(), ping);
    }

    public int getPing(DrawingNode node, DrawingNode node2)
    {
        return pings.get(node.hashCode() + node2.hashCode());
    }
}
