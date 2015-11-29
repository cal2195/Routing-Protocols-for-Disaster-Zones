package dream.team.assemble.networking;

/**
 * Interface 
 * 
 * @author Aran
 */
public interface Node
{
    public void physicalToNetwork(byte[] data, String deviceID);
    public void networkToPhysical(byte[] data, String deviceID);
    
    public void networkToTransport(Packet packet);
    public void transportToNetwork(Packet packet);
    
    public void transportToApplication(Packet packet);
    public void applicationToTransport(Packet packet);
}