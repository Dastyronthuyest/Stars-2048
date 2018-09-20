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
 * This is a class of high score field, where will be display the high score.
 */
public class HighScoreBox extends StackPane {
    public HighScoreBox(){
        VBox layout = new VBox();

        Text hint = new Text("High Score");
        hint.setFill(Color.AQUA);
        hint.setFont(Font.font("Cooper Black", FontWeight.BOLD,18));
        hint.setTextAlignment(TextAlignment.CENTER);

        score = 0;

        scoreValue = new Text(String.valueOf(this.score));
        scoreValue.setFill(Color.WHITE);
        scoreValue.setFont(Font.font("Cooper Black", FontWeight.BOLD,25));
        scoreValue.setTextAlignment(TextAlignment.CENTER);

        layout.getChildren().addAll(hint, scoreValue);
        layout.setAlignment(Pos.CENTER);

        getChildren().add(layout);
        setAlignment(Pos.CENTER);
    }

    /**
     * The method set the high score.
     * @param right The high score for remaining.
     */
    public void setScore(int right){
        this.score = right;
        this.scoreValue.setText(String.valueOf(score));
    }

    /**
     * The method return max score.
     * @return int Max score.
     */
    public int getScore(){
        return score;
    }

    private Text scoreValue;
    private int score;
}
