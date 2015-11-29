package dream.team.assemble.networking;

/**
 *
 * @author Cal
 */
public class Router implements Node
{
    private final Network network;
    private final Physical physical;
    
    public Router() throws Exception
    {
        network = new Network(this);
        physical = new Physical(this);
    }
    
    @Override
    public void physicalToNetwork(byte[] data, String deviceID)
    {
        network.onReceiptFromPhysical(data, deviceID);
    }

    @Override
    public void networkToPhysical(byte[] data, String deviceID)
    {
        physical.onReceiptFromNetwork(data, deviceID);
    }

    @Override
    public void networkToTransport(Packet packet)
    {
        throw new UnsupportedOperationException("Transport layer does not exist for Router.");
    }

    @Override
    public void transportToNetwork(Packet packet)
    {
        throw new UnsupportedOperationException("Transport layer does not exist for Router.");
    }

    @Override
    public void transportToApplication(Packet packet)
    {
        throw new UnsupportedOperationException("Application and Transport layers do not exist for Router.");
    }

    @Override
    public void applicationToTransport(Packet packet)
    {
        throw new UnsupportedOperationException("Application and Transport layers do not exist for Router.");
    }
}
