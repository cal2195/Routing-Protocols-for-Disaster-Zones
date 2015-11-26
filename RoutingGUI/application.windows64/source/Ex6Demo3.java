import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Ex6Demo3 extends PApplet {

PFont  stdFont;
final int  EVENT_BUTTON1  =  1;  
final int  EVENT_BUTTON2  =  2;  
final int  EVENT_BUTTON3  =  3;
final int  EVENT_BUTTON4  =  4; 
final int  EVENT_NULL  =  0;  
Widget  widget1, widget2, widget3, widget4;
Screen currentScreen, screen1, screen2;
Screen[] screens;
public void setup() {  
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

  int background1 = color(120, 120, 0);
  int background2 = color(0, 0, 0);
   
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
public void draw() {  

screens[0].draw();

  
  
}


public void mousePressed() {
}  
  


class Screen {
  int background;
  ArrayList widgetList;
  int screenID;
  Screen(int background) {
  this.background = background;
    widgetList  =  new  ArrayList();
  }
  
  public int getBackground() {
    return background;
  }
  
  public int getEvent(){
    int event;
  for (int  i  =  0; i<widgetList.size (); i++) {  
    Widget  aWidget  =  (Widget)  widgetList.get(i);  
    event  =  aWidget.getEvent(mouseX, mouseY);
    if(event != 0)
       return event; 
    }
    return 0;
  }

  public void setBackground(int setColor) {
    background = setColor;
  } 

  public void draw() { 
    //rect(20, 20, 20, 20);
    background(background);
   
    for (int  i  =  0; i<widgetList.size (); i++) {  
      Widget  aWidget  =  (Widget)  widgetList.get(i);  
      aWidget.draw();
    }
    
    
  }
  public void addWidget(Widget widget){
  widgetList.add(widget);   
  }
  
  
  }

class  Widget {  
  int  x, y, width, height;  
  String  label;  
  int  event;  
  int  widgetColor, labelColor;  
  PFont  widgetFont; 
  Widget(int  x, int  y, int  width, int  height, String  label, 
  int  widgetColor, PFont  widgetFont, int  event) {  
    this.x=x;  
    this.y=y;  
    this.width  =  width;  
    this.height=  height;  
    this.label=label;  
    this.event=event;    
    this.widgetColor=widgetColor;  
    this.widgetFont=widgetFont;  
    labelColor  =  color(0);
  }  
  public void  draw() {

    fill(widgetColor);   
    rect(x, y, width, height);  
    fill(labelColor);  
    text(label, x+10, y+height-10);
  }  

  public int  getEvent(int  mX, int  mY) {  
    if (mX>x  &&  mX  <  x+width  &&  mY  >y  &&  mY  <y+height) {  
      return  event;
    }  
    return  EVENT_NULL;
  }
  
 
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Ex6Demo3" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
