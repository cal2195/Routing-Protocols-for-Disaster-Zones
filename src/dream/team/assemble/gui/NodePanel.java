package dream.team.assemble.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Cal
 */
public class NodePanel extends Application
{

    float xOffset, yOffset, width, height;
    DrawingNode parent;
    
    public NodePanel()
    {
        
    }

    public NodePanel(float xOffset, float yOffset, float width, float height, DrawingNode parent)
    {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    public float getxOffset()
    {
        return xOffset;
    }

    public void setxOffset(float xOffset)
    {
        this.xOffset = xOffset;
    }

    public float getyOffset()
    {
        return yOffset;
    }

    public void setyOffset(float yOffset)
    {
        this.yOffset = yOffset;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }
    
    public void show()
    {
        launch(this.getClass());
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("NodePanel.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
}
