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
        Topology top = new Topology(ex1);
        System.out.println(top);
        
        RoutingTable test = ShortestPathAlgorithm.getRoutingTable(top.nodes.get("E1"));
        System.out.println(test);
    }
}
