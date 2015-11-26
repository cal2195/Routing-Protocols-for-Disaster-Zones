class Screen {
  color background;
  ArrayList widgetList;
  ArrayList nodeList;
  int screenID;
  Screen(color background) {
  this.background = background;
    widgetList  =  new  ArrayList();
    nodeList  =  new  ArrayList();
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
    for (int  i  =  0; i<nodeList.size (); i++) {  
    Widget  aWidget  =  (Widget)  nodeList.get(i);  
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
    background(background);
   
    for (int  i  =  0; i<widgetList.size (); i++) {  
      Widget  aWidget  =  (Widget)  widgetList.get(i);  
      aWidget.draw();
    }
    
      for (int  i  =  0; i<nodeList.size (); i++) {  
      Widget  aWidget  =  (Widget)  nodeList.get(i);  
      aWidget.draw();
    }
    
  }
  
  
  void addWidget(Widget widget){
  widgetList.add(widget);   
  }  
  
  void addNode(Widget widget){
  nodeList.add(widget);   
  }
  
  void unselectAll(){

      for (int  i  =  0; i<widgetList.size (); i++) {  
      Widget  aWidget  =  (Widget)  widgetList.get(i);  
      aWidget.selected = false;
      }
      
      for (int  i  =  0; i<nodeList.size (); i++) {  
      Widget aWidget  =  (Widget)  nodeList.get(i);  
      aWidget.selected = false;
      }
      
  }
  
}

