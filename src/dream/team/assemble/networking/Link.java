package dream.team.assemble.networking;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * One end of a pair of links.
 * 
 * A link pair is used to interface between two layers.
 * 
 * @author aran
 */
public class Link
{
    private final Link dest;
    private final LinkedBlockingQueue<byte[]> buffer = new LinkedBlockingQueue();

    /**
     * Two connected links.
     * 
     * This is used instead of constructing a new Link directly as links
     * can only exist as a pair.
     * 
     * The links are distinguished as upLink and downLink. There is no 
     * difference between the two other than name.
     */
    public static class Links
    {
        private final Link upLink = new Link();
        private final Link downLink = new Link();

        public Link getUpLink()
        {
            return upLink;
        }
        
        public Link getDownLink()
        {
            return downLink;
        }
    }
    
    /**
     * Use Links instead when a new Link is needed.
     * 
     * The Link constructor is private so that a loose link cannot exits.
     */
    private Link()
    {
        dest = new Link();
    }
    
    /**
     * Sends data to the link that this one connects to.
     * 
     * The data is stored in a buffer until it is received by the other end.
     * 
     * @param data to be sent
     */
    public void send(byte[] data) {
        try
        {
            this.buffer.put(data);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Link.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Receive data from this link.
     * 
     * This method blocks until data is sent by the other end of this link.
     * 
     * @return the data received.
     */
    public byte[] receive() {
        byte[] data = null;
        try
        {
            return dest.buffer.take();
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Link.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
    
    /**
     * Example program.
     * 
     * This results in the two targeted Layers being able to send each other
     * data using their Link.
     */
    public static void example() 
    {
        Links links = new Links();
        
        Link upLink = links.getUpLink();
        
        /* Code that gives upLink to a Layer */
        
        Link downLink = links.getDownLink();
        
        /* Code that gives downLink to the Layer below*/
    }
}
