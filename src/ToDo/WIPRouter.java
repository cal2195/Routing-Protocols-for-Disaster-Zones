package ToDo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aran
 */
public class WIPRouter {
    
    public static final int PACKET_MTU = 65535;
    public static final int DEFAULT_PORT = 54321;
    public static final String BROADCAST_ADDR = "255.255.255.255";
    
    /**
     * Maps between ad-hoc network ID and actual IP address.
     * 
     * Used for interfacing between UDP and our protocol.
     * 
     * Connections are to those devices to which direct communication is possible.
     * The NetworkDiscovery class is used to discover these connections.
     */
    protected final HashMap<Integer, InetAddress> connections;
    
    private DatagramSocket socket;
    private final ExecutorService executor;
    
    public WIPRouter() {
        try {
            socket = new DatagramSocket(DEFAULT_PORT);
        } catch (SocketException ex) {
            Logger.getLogger(WIPRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        connections = new HashMap<>();
        
        /* Listener for incoming packets */
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> listen());
    }
    
    /**
     * Listen for incoming packets.
     * 
     */
    private void listen() {
        byte[] buffer = new byte[PACKET_MTU];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
            onReceipt(packet);
        } catch (IOException ex) {
            Logger.getLogger(WIPRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Action to take on receipt of a packet.
     * 
     * @param packet 
     */
    private void onReceipt(DatagramPacket datagram) {
        //Process received packet here.
        
        Packet packet = new Packet(datagram);
        
        switch (packet.getType()) {
            case Packet.NET_DISCOVERY:
                NetworkDiscovery.process(this, packet);
                break;
            default:
                System.err.println("Packet type not recognised");
                break;
        }
        
    }
    
    /**
     * Sends a message to a directly connected device.
     * 
     * @param id
     * @param payload 
     * @return false if 'id' does not belong to a connected device.
     */
    public boolean toNextHop(int id, byte[] payload) {
        
        if (!connections.containsKey(id)) return false;
        
        InetAddress addr = connections.get(id);
        DatagramPacket packet = new DatagramPacket(payload, payload.length, addr, DEFAULT_PORT);
        try {
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(WIPRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    /**
     * Broadcast a messages to routers listening on the default port.
     * 
     * @param payload Data to be sent.
     */
    public void broadcast(byte[] payload) {
        try {
            InetAddress addr = Inet4Address.getByName(BROADCAST_ADDR);
            DatagramPacket packet = new DatagramPacket(payload, payload.length, addr, DEFAULT_PORT);
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(WIPRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
