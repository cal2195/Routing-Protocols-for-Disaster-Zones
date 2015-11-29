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

![node-srtucture](http://i.imgur.com/QQAtgDJ.png)

This is the structure of the two types of `Node`. The rounded rectangles are instance variables and the arrows are methods that create a link between the layers. Take `Router` as an example:

```java
public class Router
{
    private Network network;
    private Physical physical;

    public boolean getNIC(String nicID)
    {
        /* check if the NIC is already connected to */
        if (physical.containsKey(nicID)) return false;

        /* create a pair of links */
        Link.Pair links = new Link.Pair();

        /* give each layer one of the links
           parameters are (key, value) */
        network.addLink(nicID, links.getUpLink());
        physical.addLink(nicID, links.getDownLink());

        /* return that the link was successfully created */
        return true;
    }
}
```

There can (and usually would be) more than one link between each layer. There would be a NIC for every node connected to a router.

#### Example
Let's say there are three nodes **A**, **B** and **C** connected our router. Let's think about how the network layer of our router would see these nodes:
* **A** is connected to wlan0
* **B** is connected to wlan1
* **C** is connected to wlan2

The **A** connection could have been established by `physical` calling `getController("wlan0")`. This would give both `physical` and `network` a `Link` object with the associated key `"wlan0"`.

If the network layer wanted to send **A** data, it would call `links.get("wlan0").send(data)`, where `links` is a symbol table containing all of it's links. The physical layer would then `receive()` this data, see that it is from wlan0, and thus would send it on to node **A**.



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

## Loose Thoughts

* Each `Link` needs to be threaded as it must constantly be received from. As there will be a bunch of links per `Node` a `fixedThreadPoolExecutor` should be used. As the threads will be sleeping most of the time (while waiting for data), the number of threads in the pool could be reasonably small.
