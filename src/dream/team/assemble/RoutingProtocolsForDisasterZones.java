package dream.team.assemble;

import dream.team.assemble.gui.RoutingGUI;

/**
 *
 * @author Cal
 * @author Dan
 */
public class RoutingProtocolsForDisasterZones
{

    public RoutingProtocolsForDisasterZones()
    {
        RoutingGUI routingGUI = new RoutingGUI();
        routingGUI.main("dream.team.assemble.gui.RoutingGUI");
    }
    
    public static void main(String[] args)
    {
        //first example for assignment, see diagram
//        String ex1 = "E1 = R1, E2 = R1, R1 = E1 E2 R3 R2, R2 = R1 R4, R3 = R1 E3, R4 = R2 E4, E3 = R3, E4 = R4";
//        String ex2 = "E1 = R1, R1 = E1 E2 E5, E2 = R1 E3, E3 = E2 E4, E4 = E3, E5 = R1";
//        //example from routing slides
//        String ex3 = "A = B G, B = A E C, E = B F G, F = E C H, C = B D F, H = F D G, G = A E H, D = C H";
//        Topology top = new Topology(ex3);
//        System.out.println(top);
//        
//        RoutingTable test = ShortestPathAlgorithm.getRoutingTable(top.getNodes().get("A"));
//        System.out.println("Routing Table for A");
//        System.out.println(test);
        new RoutingProtocolsForDisasterZones();
    }
}
