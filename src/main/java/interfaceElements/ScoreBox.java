package interfaceElements;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This is a class of score field, where will
 * be display an actual score of the game.
 */
public class ScoreBox extends StackPane{
    public ScoreBox(){
        VBox layout = new VBox();

        score = 0;
        Text hint = new Text("Actual Score");
        hint.setFill(Color.AQUA);
        hint.setFont(Font.font("Cooper Black", FontWeight.BOLD,18));
        hint.setTextAlignment(TextAlignment.CENTER);

        scoreValue = new Text(String.valueOf(0));
        scoreValue.setFill(Color.WHITE);
        scoreValue.setFont(Font.font("Cooper Black", FontWeight.BOLD,25));
        scoreValue.setTextAlignment(TextAlignment.CENTER);

        layout.getChildren().addAll(hint, scoreValue);
        layout.setAlignment(Pos.CENTER);

        getChildren().add(layout);
        setAlignment(Pos.CENTER);
    }

    /**
     * The method set new score.
     * @param right New score amount.
     */
    public void setScore(int right){
        this.score = right;
        this.scoreValue.setText(String.valueOf(score));
    }

    /**
     * The method return score.
     * @return int The actual score.
     */
    public int getScore(){
        return score;
    }

    private Text scoreValue;
    private int score;
}
