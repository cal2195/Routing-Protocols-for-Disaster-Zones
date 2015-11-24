package dream.team.assemble;

/**
 *
 * @author Cal, Aran, Daniel
 */
public class RoutingProtocolsForDisasterZones
{

    public static void main(String[] args)
    {

        //first example for assignment, see diagram
        String ex1 = "E1 = R1, E2 = R1, R1 = E1 E2 R3 R2, R2 = R1 R4, R3 = R1 E3, R4 = R2 E4, E3 = R3, E4 = R4";
        String ex2 = "E1 = R1, R1 = E1 E2 E5, E2 = R1 E3, E3 = E2 E4, E4 = E3, E5 = R1";
        String ex3 = "A = B G, B = A E, E = B F G, F = E C H, C = D F, H = F D, G = A E, D = C H";
        Topology top = new Topology(ex3);
        System.out.println(top);
        
        RoutingTable test = ShortestPathAlgorithm.getRoutingTable(top.nodes.get("A"));
        System.out.println("Routing Table for A");
        System.out.println(test);
    }
}
