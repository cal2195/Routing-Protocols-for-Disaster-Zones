/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dream.team.assemble;

/**
 *
 * @author Cal
 */
public class RoutingEntry
{
    String address;
    Node node;
    int weight;
    
    public RoutingEntry(String address, Node node, int weight)
    {
        this.address = address;
        this.node = node;
        this.weight = weight;
    }
}
