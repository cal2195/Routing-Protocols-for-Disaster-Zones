package dream.team.assemble.networking;

/**
 *
 * @author aran
 */
public class Network
{
    private final Node parent;
    
    public Network(Node parent)
    {
        this.parent = parent;
    }
    
    public void onReceiptFromPhysical(byte[] data, String nicID)
    {
    /**
     * Check which NIC data is from
     * 
     */
    }
}
