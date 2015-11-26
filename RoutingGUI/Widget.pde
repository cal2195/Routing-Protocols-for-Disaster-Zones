class  Widget {  
  int  x, y, width, height;  
  String  label;  
  int  event;  
  color  widgetColor, labelColor, selectedColor;  
  PFont  widgetFont; 
  ArrayList<Integer> links = new ArrayList<Integer>();
  boolean selected = false;
  Widget(int  x, int  y, int  width, int  height, String  label, 
  color  widgetColor, PFont  widgetFont, int  event) {  
    this.x=x;  
    this.y=y;  
    this.width  =  width;  
    this.height=  height;  
    this.label=label;  
    this.event=event;    
    this.widgetColor=widgetColor;  
    this.selectedColor = color(200);  
    this.widgetFont=widgetFont;  
    labelColor  =  color(0);
  }  
  void  draw() {
    if(this.selected)
      fill(selectedColor);
    else
      fill(widgetColor);   
    rect(x, y, width, height);  

      fill(labelColor);  
    text(label, x+10, y+height-10);
  }  

  int  getEvent(int  mX, int  mY) {  
    if (mX>x  &&  mX  <  x+width  &&  mY  >y  &&  mY  <y+height) {
      screens[0].unselectAll();
      this.selected = !this.selected; 
      return  event; 
    }
    else
      this.selected = false;  
    
    return  EVENT_NULL;
  }
  
  void addLink(Integer nodeID){
    links.add(nodeID);
  }
 
}

