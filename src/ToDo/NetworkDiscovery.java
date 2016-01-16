package ToDo;

import ToDo.WIPRouter;

/**
 * Utility class for responding to a network discovery packet.
 * 
 * @author aran
 */
public class NetworkDiscovery {
    
    public static void process(WIPRouter router, Packet packet) {
        
        // CURRENTLY BROKEN
//        switch(packet.getCode()) {
//
//            case Packet.DISCOVERY_CALL:
//                /* Add newly discovered device to connections table */
//                router.connections.put(packet.getSrcID(), packet.getSrcIPAddr());
//                /* Reply to 'call' */
//                byte[] data = {(byte) Packet.NET_DISCOVERY, (byte) Packet.DISCOVERY_REPLY};
//                router.toNextHop(packet.getSrcID(), data);
//                break;
//            
//            case Packet.DISCOVERY_REPLY:
//                /* Add newly discovered device to connections table */
//                router.connections.put(packet.getSrcID(), packet.getSrcIPAddr());
//                break;
//            
//            default:
//                System.err.println("Network Discovery code not recognised");
//                break;
//        }
    }
}
