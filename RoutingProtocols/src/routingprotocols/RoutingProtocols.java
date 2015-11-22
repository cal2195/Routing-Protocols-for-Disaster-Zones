/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routingprotocols;

/**
 *
 * @author Dan
 */
public class RoutingProtocols {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
       //first example for assignment, see diagram
       
       String ex1 = "E1 = R1, E2 = R1, R1 = E1 E2 R3 R2, R2 = R1 R4, R3 = R1 E3, R4 = R2 E4, E3 = R3, E4 = R4";
       Topology test = new Topology(ex1);
       System.out.println(test);
       
    }
    
}
