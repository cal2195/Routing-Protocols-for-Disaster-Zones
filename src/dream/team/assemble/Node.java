/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dream.team.assemble;

import java.util.ArrayList;

/**
 *
 * @author Dan
 */
public class Node
{

    String name;
    ArrayList<Node> heardBy; //represents physical restrictions of network, not our routing
    final int myPort;

    public Node(String name, int myPort)
    {
        this.name = name;
        this.heardBy = new ArrayList<>();
        this.myPort = myPort;
    }

    public String description()
    {
        String tmp = "";
        tmp += "Name = " + name;
        tmp += ", myPort = " + myPort;
        tmp += ", heardBy = " + heardByToString() + "\n";
        return tmp;
    }

    @Override
    public String toString()
    {
        return "Name = " + name + ", heardBy = " + heardByToString();
    }

    private String heardByToString()
    {
        String tmp = "";
        for (int i = 0; i < heardBy.size(); i++)
        {
            tmp += " " + heardBy.get(i).name;
        }
        return tmp;
    }

    public void addListener(Node node)
    {
        heardBy.add(node);
    }
}
