
package dream.team.assemble.routing.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Object that represents a router packet.
 * 
 * TODO:
 * Add management packets.
 * 
 * @author Dan
 */
public class RouterPacket {
    
    private final int HEADER_LENGTH = 9;
    private final int ADDR_LENGTH = 4;
    
    /* header data */
    private final int srcAddr;
    private final int dstAddr;
    private final int flags;
    
    /* packet data content */
    private final byte[] payload;
    
    /**
     *
     * @param flags
     * @param srcAddr
     * @param dstAddr
     * @param payload
     */
    public RouterPacket(int flags, int srcAddr, int dstAddr, byte[] payload)
    {
        this.flags = flags;
        
        this.srcAddr = srcAddr;        

        this.dstAddr = dstAddr;
        
        this.payload = payload;
    }
    
    public RouterPacket(byte[] packet)
    {
        if (packet.length < HEADER_LENGTH)
        {
            throw new IllegalArgumentException("Packet is too small to contain a well formed header");
        }
        ByteArrayInputStream bin = new ByteArrayInputStream(packet);
        byte[] addrBuffer = new byte[ADDR_LENGTH];
        /* read flags from header */
        flags = (byte) bin.read();
        /* read srcAddr from header */
        bin.read(addrBuffer, 0, ADDR_LENGTH);
        srcAddr = bytesToInt(addrBuffer);
        /* read dstAddr from header */
        bin.read(addrBuffer, 0, ADDR_LENGTH);
        dstAddr = bytesToInt(addrBuffer);
        /* read payload from header - assumes remaining data is payload */
        payload = new byte[bin.available()];
        bin.read(payload, 0, payload.length);
    }
    
    public byte[] toByteArray()
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bout.write(flags);
        bout.write(intToBytes(srcAddr), 0, ADDR_LENGTH);
        bout.write(intToBytes(dstAddr), 0, ADDR_LENGTH);
        bout.write(payload, 0, payload.length);
        return bout.toByteArray();
    }
    
    /**
     * Gets source of packet.
     * N.B. this is the initial sender, not necessarily the router this has arrived from.
     * @return 
     */
    public int getSrcAddr()
    {
        return srcAddr;
    }

    public int getDstAddr()
    {
        return dstAddr;
    }

    public byte[] getPayload()
    {
        return payload;
    }
    

    private byte[] intToBytes(int myInt)
    {
        return ByteBuffer.allocate(4).putInt(myInt).array();
    }

    private int bytesToInt(byte[] bytes)
    {
        return ByteBuffer.wrap(bytes).getInt();
    }
    
    public static boolean isValidIP(String IP)
    {
        if(IP.length() < 7 || IP.length() > 15)
            return false;
        
        String[] chunks = IP.split("\\.");
        if(chunks.length != 4)
            return false;
        
        for (String chunk : chunks) {
            try
            {
                int temp = Integer.parseInt(chunk);
                if(temp > 255 || temp < 0)
                  return false;
            }
              catch(NumberFormatException nfe)  
            {  
              return false;  
            }  

        }
        
        return true;
    }
    
    public boolean isFlagSet(int i)
    {
        byte temp = 1;
        temp = (byte) (temp << i);
        return (temp & flags) > 0;
    }
    
    /**
     * Broadcast id is -1
     * @return 
     */
    public boolean isBroadcast()
    {
        return dstAddr == -1;
    }

    public String flagString()
    {
        return String.format("%8s", Integer.toBinaryString(flags & 0xFF)).replace(' ', '0');
    }
    
    public int getFlags()
    {
        return flags;
    }
            
    @Override
    public String toString()
    {
       return "" + flagString() + " src: " + srcAddr + " dst: " + dstAddr; 
    }

}
