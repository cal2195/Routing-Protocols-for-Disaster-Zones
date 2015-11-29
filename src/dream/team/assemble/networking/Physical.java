package dream.team.assemble.networking;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The physical layer of a Node.
 * 
 * TODO: improve method names
 * 
 * @author aran
 */
public class Physical
{
    private final Node parent;
    private final DatagramSocket socket;
    private final BiMap<String, SocketAddress> nodeMap;
    private final Executor listener;
    
    public Physical(Node parent) throws SocketException
    {
        this.parent = parent;
        nodeMap = HashBiMap.create();
        /* create a new socket on an available port */
        socket = new DatagramSocket();
        /* listen for incoming packets */
        listener = Executors.newSingleThreadExecutor();
        listener.execute(() -> listen());
    }
    
    /**
     * 
     * 
     * @param data
     * @param deviceID 
     * @return  
     */
    public boolean onReceiptFromNetwork(byte[] data, String deviceID)
    {
        SocketAddress dst = nodeMap.get(deviceID);
        /* check if deviceID is invalid */
        if (dst == null) 
        {
            return false;
        }
        /* create and send packet to target node */
        DatagramPacket packet = new DatagramPacket(data, data.length, dst);
        try
        {
            socket.send(packet);
            return true;
        } catch (IOException ex)
        {
            Logger.getLogger(Physical.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void addDevice()
    {
        
    }
    
    /**
     * Gets the socket address on the local network.
     * 
     * @return socket address on the local network
     */
    public SocketAddress getSocketAddress()
    {
        return new InetSocketAddress(socket.getLocalAddress(), socket.getLocalPort());
    }
    
    /**
     * Action to take upon receiving a packet;
     * 
     * @param packet 
     */
    private void onReceiptFromDevice(DatagramPacket packet)
    {
        /* check which device the packet arrived on */
        String deviceID = nodeMap.inverse().get(packet.getSocketAddress());
        if (deviceID == null)
        {
            onNewNodeDetection(packet); // Remove if not implemented
            return;
        }
        /* get data from packet and send to the Network layer */
        int offset = packet.getOffset();
        int length = packet.getLength();
        byte[] buffer = packet.getData();
        byte[] data = Arrays.copyOfRange(buffer, offset, offset + length);
        parent.physicalToNetwork(data, deviceID);
    }
    
    /**
     * Action to take when a packet is received from a node that is not 
     * in the nodeMap.
     */
    private void onNewNodeDetection(DatagramPacket packet)
    {
        // TODO: Implement if needed.
    }
    
    /**
     * Listen for incoming packets.
     * 
     * This is run from an Executor.
     */
    private void listen()
    {
        try
        {
            /* continuously try to receive packets */
            while (true)
            {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                socket.receive(packet);
                onReceiptFromDevice(packet);
            }
        } catch (IOException ex)
        {
            Logger.getLogger(Physical.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
