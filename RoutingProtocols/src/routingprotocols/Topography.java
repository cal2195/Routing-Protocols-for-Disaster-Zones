/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routingprotocols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Dan
 */
public class Topography {
    final int START_PORT = 50000;
    HashMap<String, Node> nodes = new HashMap<>();
    
    public Topography(String topo){
        
        //first example for assignment formatted like so - 
        //E1 = R1, E2 = R1, R1 = E1 E2 R3 R2, R2 = R1 R2, R3 = R1 E3, R4 = R2 E4, E3 = R3, E4 = R4
        
        HashMap<String, Integer> namePorts = new HashMap<>();
        String[] split = topo.split(",");
        for(int i = 0; i < split.length; i++)
        {
            Scanner tmpScanner = new Scanner(split[i]);
            String nodeName = tmpScanner.next();
            namePorts.put(nodeName, START_PORT + i);
            Node temp = new Node(nodeName, START_PORT + i);
            nodes.put(nodeName, temp);
        }
        
        System.out.println("List of all nodes in system = " + namePorts.toString());
        
        for(int i = 0; i < split.length; i++)
        {
            Scanner tmpScanner = new Scanner(split[i]);
            Node tempNode = nodes.get(tmpScanner.next());
            while(tmpScanner.hasNext())
            {
                Integer readPort = namePorts.get(tmpScanner.next());
                if(readPort != null)
                {
                    tempNode.addListener(readPort);
                }
            }
        }
        

        System.out.println(nodes.toString());
            
       
            
        }
    

                
    
    
}
