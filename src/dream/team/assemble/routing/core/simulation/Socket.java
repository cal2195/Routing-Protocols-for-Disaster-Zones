/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dream.team.assemble.routing.core.simulation;

import dream.team.assemble.routing.core.RouterPacket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *  A class to emulate a socket.
 * 
 * @author aran
 */
public class Socket {
    
    private static final HashMap<Integer, Socket> systemSockets = new HashMap<>();
    private final LinkedBlockingQueue<RouterPacket> buffer;
    
    public Socket(int port)
    {
        buffer = new LinkedBlockingQueue<>();
        systemSockets.put(port, this);
    }
    
    public void send(RouterPacket packet, int port) {
        systemSockets.get(port).buffer.add(packet);
    }
    
    public RouterPacket receive() throws InterruptedException {
        return buffer.take();
    }
}
