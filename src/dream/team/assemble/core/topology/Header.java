
package dream.team.assemble.core.topology;

/**
 *
 * @author Dan
 */
public class Header {
    
    private final byte flags;
    private final byte[] sourceIP;
    private final byte[] destinationIP;
    
    /**
     *
     * @param flags
     * @param sourceIP
     * @param destinationIP
     */
    
    
    public Header(byte flags, String sourceIP, String destinationIP)
    {
        this.flags = flags;
        this.sourceIP = stringToBytesIP(sourceIP);
        this.destinationIP = stringToBytesIP(destinationIP);
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
          if(i != 0)
              temp += ".";
          temp +=  (IP[i] & 0xFF);  
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
        String temp = "";
        byte checker = 1;
        byte flagByte = flags;
        for(int i = 0; i < 8; i++)
        {
            if(i == 4)
                temp = " " + temp;
            
            if((checker & flagByte) > 0)
                temp = "1" + temp;
            else 
                temp = "0" + temp;
            flagByte = (byte) (flagByte >> 1);
        }
        return temp;
    }
    
    @Override
    public String toString()
    {
       return "" + flagString() + " src: " + bytesToString(sourceIP) + " dst: " + bytesToString(destinationIP); 
    }
    
    public static void main(String[] args)
    {
        Header testHeader = new Header((byte)7, "255.12.1.255", "192.168.1.1");
        System.out.println(testHeader);
        for(int i = 0; i < 8; i++)
        {
            System.out.println(testHeader.isFlagSet(i));
        }
        
        testHeader = new Header((byte)32, "1.254.6.24", "1.250.25.1");
        System.out.println(testHeader);
        for(int i = 0; i < 8; i++)
        {
            System.out.println(testHeader.isFlagSet(i));
        }
        
    }
    
}
