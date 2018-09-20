package interfaceElements;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * This is a class of banner of game.
 */
public class GameName extends StackPane{
    public GameName() {
        Text name = new Text("2048 Stars");
        name.setFill(Color.YELLOW);
        name.setFont(Font.font("Cooper Black", FontWeight.BOLD, 45));
        getChildren().add(name);
    }
}
