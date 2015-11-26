class Screen {
  color background;
  ArrayList widgetList;
  int screenID;
  Screen(color background) {
  this.background = background;
    widgetList  =  new  ArrayList();
  }
  
  color getBackground() {
    return background;
  }
  
  int getEvent(){
    int event;
  for (int  i  =  0; i<widgetList.size (); i++) {  
    Widget  aWidget  =  (Widget)  widgetList.get(i);  
    event  =  aWidget.getEvent(mouseX, mouseY);
    if(event != 0)
       return event; 
    }
    return 0;
  }

  void setBackground(color setColor) {
    background = setColor;
  } 

  void draw() { 
    //rect(20, 20, 20, 20);
    background(background);
   
    for (int  i  =  0; i<widgetList.size (); i++) {  
      Widget  aWidget  =  (Widget)  widgetList.get(i);  
      aWidget.draw();
    }
    
    
  }
  void addWidget(Widget widget){
  widgetList.add(widget);   
  }
  
  
  }

