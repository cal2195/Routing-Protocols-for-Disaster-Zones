
package dream.team.assemble.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Object that represents a router packet.
 * 
 * TODO:
 * Add management packets.
 * Check and throw error if address args are not well formed in constructor.
 * 
 * @author Dan
 */
public class RouterPacket {
    
    private final int HEADER_LENGTH = 9;
    private final int ADDR_LENGTH = 4;
    
    /* header data */
    private final String srcAddr;
    private final String dstAddr;
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
    public RouterPacket(int flags, String srcAddr, String dstAddr, byte[] payload)
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
        srcAddr = bytesToString(addrBuffer);
        /* read dstAddr from header */
        bin.read(addrBuffer, 0, ADDR_LENGTH);
        dstAddr = bytesToString(addrBuffer);
        /* read payload from header - assumes remaining data is payload */
        payload = new byte[bin.available()];
        bin.read(payload, 0, payload.length);
    }
    
    public byte[] toByteArray()
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bout.write(flags);
        bout.write(stringToBytesIP(srcAddr), 0, ADDR_LENGTH);
        bout.write(stringToBytesIP(dstAddr), 0, ADDR_LENGTH);
        bout.write(payload, 0, payload.length);
        return bout.toByteArray();
    }
    
    public String getSrcAddr()
    {
        return srcAddr;
    }

    public String getDstAddr()
    {
        return dstAddr;
    }

    public byte[] getPayload()
    {
        return payload;
    }
    

    private byte[] stringToBytesIP(String IP)
    {
        byte[] temp = new byte[4];
        String[] chunks = IP.split("\\.");
        for(int i = 0; i < temp.length; i++)
        {
            temp[i] = (byte) Integer.parseInt(chunks[i]);
        }
        
        return temp;
    }

    private String bytesToString(byte[] IP)
    {
        String temp = "";
        for(int i = 0; i < IP.length;i++)
        {
          if(i != 0) temp += ".";
          temp += (IP[i] & 0xFF);  
        }
        return temp;
    }
    
    public boolean isFlagSet(int i)
    {
        byte temp = 1;
        temp = (byte) (temp << i);
        return (temp & flags) > 0;
    }

    public String flagString()
    {
        return String.format("%8s", Integer.toBinaryString(flags & 0xFF)).replace(' ', '0');
    }
    
    @Override
    public String toString()
    {
       return "" + flagString() + " src: " + srcAddr + " dst: " + dstAddr; 
    }
    
    public static void main(String[] args)
    {
        RouterPacket testHeader = new RouterPacket(7, "255.12.1.255", "192.168.1.1", null);
        System.out.println(testHeader);
        for(int i = 0; i < 8; i++)
        {
            System.out.println(testHeader.isFlagSet(i));
        }
        
        testHeader = new RouterPacket(32, "1.254.6.24", "1.250.25.1", null);
        System.out.println(testHeader);
        for(int i = 0; i < 8; i++)
        {
            System.out.println(testHeader.isFlagSet(i));
        }
        
    }
    
}
