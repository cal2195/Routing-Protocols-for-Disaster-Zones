PFont  stdFont;
final int  EVENT_BUTTON1  =  1;  
final int  EVENT_BUTTON2  =  2;  
final int  EVENT_BUTTON3  =  3;
final int  EVENT_BUTTON4  =  4; 
final int  EVENT_NULL  =  0;  
Widget  widget1, widget2, widget3, widget4;
Screen currentScreen, screen1, screen2;
Screen[] screens;
void setup() {  
  stdFont  =  loadFont("ComicSansMS-18.vlw");  
  textFont(stdFont);  

  widget1  =  new  Widget(100, 100, 100, 40, 
  "Red!", color(100), 
  stdFont, EVENT_BUTTON1);  

  widget2  =  new  Widget(100, 200, 100, 40, 
  "Green!", color(100), 
  stdFont, EVENT_BUTTON2); 

  widget3  =  new  Widget(100, 300, 100, 40, 
  "Blue!", color(100), 
  stdFont, EVENT_BUTTON3);
 
  widget4  =  new  Widget(200, 300, 100, 40, 
  "Blue!", color(100), 
  stdFont, EVENT_BUTTON4);  

  size(400, 400); 

  color background1 = color(120, 120, 0);
  color background2 = color(0, 0, 0);
   
  Screen currentScreen = new Screen(background2);
  Screen screen1 = new Screen(background1);
  Screen screen2 = new Screen(background2);
   
  screen1.setBackground(background1);
  screen2.setBackground(background2);
    
  screen1.addWidget(widget1);  
  screen1.addWidget(widget2);
  screen2.addWidget(widget3);
  screen2.addWidget(widget4);
 
  currentScreen = screen1;
  
  screens = new Screen[1];
  screens[0] = screen1;
}  
void draw() {  

screens[0].draw();

  
  
}


void mousePressed() {
}  
  


