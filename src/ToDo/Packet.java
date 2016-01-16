package ToDo;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 *
 * @author aran
 */
public class Packet {
    
    /* Packet Types */
    private final int type;
    public static final int GENERAL = 0;
    public static final int NET_DISCOVERY = 1;
    
    
    /* Datagram layer */
    private final InetAddress srcIPAddr;
    
    /* Ad-hoc network layer */
    private final int srcID;
    private final int dstID;
    public static final int BROADCAST_ID = -1;
    
    /* Raw packet data */
    private byte[] payload;   
    
    /**
     * Create packet from datagram.
     * 
     * @param datagram 
     */
    public Packet(DatagramPacket datagram) {
        srcIPAddr = datagram.getAddress();
        int dataLength = datagram.getLength();
        byte[] data = Arrays.copyOf(datagram.getData(), dataLength);
        
        // Change to use ByteArrayInputStream
        type = data[0];
        srcID = data[1];
        dstID = data[2];
        
    }

    public InetAddress getSrcIPAddr() {
        return srcIPAddr;
    }

    public int getSrcID() {
        return srcID;
    }

    public int getDstID() {
        return srcID;
    }
    
    public int getType() {
        return type;
    }
    
    public boolean isBroadcast() {
        return dstID == BROADCAST_ID;
    }
}
