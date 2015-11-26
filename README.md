# Routing Protocols for Disaster Zones

## Plan

We are familiar with the OSI stack:

![See wikipedia](https://upload.wikimedia.org/wikipedia/commons/2/2b/Osi-model.png)

Our assignment is to create Network Layer protocols. This needs to sit on top of the layer below, so we need to be able to interact with the data link layer.

Transfer protocols (TCP, UDP) live on the transport layer. This is as low as java will realistically let us go. We have two options:

1. Try deal with the JNI to go lower, and lose everything that makes java the nice safe language that it is.
2. Simulate the data link and physical layers.

I am inclined to go for option 2.

### Outline of my idea

Each layer is to be implemented in seperate modules. The layers should not depend on eachothers implementations but only on their interfaces. 

The TCP/IP model is probably more suited to this module for each layer idea:
![TPC/IP vs OSI](http://i.stack.imgur.com/FOfAU.jpg)

So we would have **four** modules:

1. **Application** - This is the application a client uses to send messages, images, etc.
2. **Transport** - Flow control (, checksum, etc.) done here.
3. **Network** - Router protocols.
4. **Network Access** - A simulated enviroment. More ideas on this to come!

### Network Access Simulation

These are my ideas on how to implement the simulation.

#### Nodes

* Clients and Routers are both nodes.
* Each node has a (actual) IP and a port.
* Nodes are added to the **Enviroment** (open to a better name).
* Nodes don't have to be on the same host (i.e. localhost) as the Enviroment.

#### Enviroment

* Controls the simulation.
* Has a list of all the Nodes in the enviroment.
* Establish a network topology:
  * Based on (simulated) position and signal strength of Node
  * Explicitly input topology
* Could have a GUI to view (change?) topology.
