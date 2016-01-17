package dream.team.assemble;

import dream.team.assemble.gui.RoutingGUI;
import dream.team.assemble.routing.core.simulation.Simulation;

/**
 *
 * @author Cal
 * @author Dan
 */
public class RoutingProtocolsForDisasterZones
{
    public static final boolean debugPrintouts = false;
    public static final boolean verboseDebugPrintouts = false;

    public RoutingProtocolsForDisasterZones()
    {
        RoutingGUI routingGUI = new RoutingGUI();
        routingGUI.main("dream.team.assemble.gui.RoutingGUI");
    }
    
    public static void main(String[] args)
    {
        new RoutingProtocolsForDisasterZones();
    }
}
