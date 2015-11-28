# Routing Protocols for Disaster Zones

## Design

The project is split into separate layers. They interact through interface objects. This means the implementation of a layer can be changed freely without breaking compatibility. The four main layers are:

* Application
* Transport
* Network
* Physical

This is based on the OSI and TCP/IP models.

![TPC/IP vs OSI](http://i.stack.imgur.com/FOfAU.jpg)

Each node in the network is built up from the layers. Nodes are of two types:

1. *Routers* consist of Physical and Network only.
2. *Endpoints* consist of **all** four layers.

### Interfacing

The `Link` class is used to allow layers to pass data between each other.

`Link` objects can only be created in pairs. Each `Link` object has two methods:

* `void send(byte[] data):` Send data to it's paired link.
* `byte[] receive():` Receive data from it's paired link.

A pair of links can be thought as of as either end of a connection. For two layers to communicate, a pair of links are created and each layer is given one of the links.

### Node Structure

![node-srtucture](http://i.imgur.com/mRA4tFu.png)

This is the structure of the two types of `Node`. The rounded rectangles are instance variables and the arrows are methods that create a link between the layers. Take `Router` as an example:

```java
public class Router
{
    private Network network;
    private Physical physical;

    public boolean getController(int nicNumber)
    {
        /* check if the NIC is already connected to */
        if (physical.isTaken(nicNumber)) return false;

        /* create a pair of links */
        Link.Pair links = new Link.Pair();

        /* give each layer one of the links
           parameters are (key, value) */
        network.addLink(nicNumber, links.getUpLink());
        physical.addLink(nicNumber, links.getDownLink());

        /* return that the link was successfully created */
        return true;
    }
}
```

### Headers

As data is passed *down* the layers, each one adds a header to the data. This contains information about the type of the content or other such metadata.

As data is pass *up* the layers, each one removes it's corresponding header. The header contains the information required by that layer to process the data.

### Application

This layer consists of *Clients* and *Content types*. A Client is used to create and display Content. For example:

Client|Content
------|-------
messaging app|strings
image sharing app|image files
file transfer app|files

When a data packet is received by the Application layer, it's header is used to determine the type of the Content the data should be converted into. This would play the role of the *presentation layer* in the OSI model.
