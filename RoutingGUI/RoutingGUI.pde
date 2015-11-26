PFont  stdFont;
final int  EVENT_BUTTON1  =  1;  
final int  EVENT_BUTTON2  =  2;  
final int  EVENT_NODE_START  =  10;  
final int  EVENT_NULL  =  0;  
final int  CLICKED_SPACE  =  0;  
final int  SELECT_MODE  =  0;  
final int  ADD_NODE_MODE  =  1; 
final int  ADD_LINK_MODE  =  2; 
final int  ADD_LINK_SELECTING_SECOND  =  3;  
Widget  addNode, addLink, widget3, widget4;
Screen screen1;
Screen[] screens = new Screen[1];
int mode = 0;
int nodesAdded = 0;
int currentlySelected = 0;
int[] linking = new int[2];
boolean addingLinks = false;


void setup() {  
  stdFont  =  loadFont("ComicSansMS-18.vlw");  
  textFont(stdFont);  

  addNode  =  new  Widget(10, 10, 100, 40, 
  "Add Node", color(100), 
  stdFont, EVENT_BUTTON1);  

  addLink  =  new  Widget(10, 60, 100, 40, 
  "Add Link", color(100), 
  stdFont, EVENT_BUTTON2); 

  size(800, 600); 

  color background1 = color(255, 255, 255);

  Screen screen1 = new Screen(background1);
  screen1.setBackground(background1);

  screen1.addWidget(addNode);  
  screen1.addWidget(addLink);

  screens[0] = screen1;
}  
void draw() {  
  screens[0].draw();
}


void mousePressed() {
  int event = screens[0].getEvent();
  if (event == 1)
  {
    mode = ADD_NODE_MODE;
  }
  else if (event == ADD_LINK_MODE)
  {
    println("Darkest black.");
  }
  else if (event >= 10)
  {
    println("Node " + (event - 10) + " clicked");
    currentlySelected = event - 10;
  }
  
  if (event == CLICKED_SPACE && mode == ADD_NODE_MODE)
  {
    Widget tmpNode  =  new  Widget(mouseX, mouseY, 50, 50, 
    "newNode", color(250), 
    stdFont, EVENT_NODE_START + nodesAdded++);  
    screens[0].addNode(tmpNode);
    mode = 0;
  }
  
    
  if (event >= 10 && mode == ADD_LINK_MODE)
  {
    Widget tmpNode  =  (Widget)screens[0].nodeList.get(event - 10);
    tmpNode.addLink(event - 10);
    mode = ADD_LINK_SELECTING_SECOND;
  }
}  


