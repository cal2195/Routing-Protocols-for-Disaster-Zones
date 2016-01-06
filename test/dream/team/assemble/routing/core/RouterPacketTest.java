/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dream.team.assemble.routing.core;

import static dream.team.assemble.routing.core.RouterPacket.isValidIP;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dan
 */
public class RouterPacketTest {
 
    /**
     * Test of testIPParsing method, of class RouterPacket.
     */
    @Test
    public void testIPParsing() {
        //random tests
        Random myRNG = new Random();
        for(int i = 0; i < 1000;i++)
        {
            int first = myRNG.nextInt(256);
            int second = myRNG.nextInt(256);
            int third = myRNG.nextInt(256);
            int fourth = myRNG.nextInt(256);
            String IP = "" + first + "." + second + "." + third + "." + fourth;
            System.out.println(IP + " " + isValidIP(IP));
            assertTrue(isValidIP(IP));
        }
        //edge cases
        String IP = "0.0.0.0";
        System.out.println(IP + " " + isValidIP(IP));
        assertTrue(isValidIP(IP));
        IP = "255.255.255.255";
        System.out.println(IP + " " + isValidIP(IP));
        assertTrue(isValidIP(IP));
        IP = "-255.255.255.255";
        System.out.println(IP + " " + isValidIP(IP));
        assertFalse(isValidIP(IP));
        IP = "FF.A5.255.255";
        System.out.println(IP + " " + isValidIP(IP));
        assertFalse(isValidIP(IP));

    }

    
}
