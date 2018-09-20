package models;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.util.Duration;

/**
 * This is a class of game element - "star".
 * Star growing, if user pointer at her on mouse.
 */
public class ElementBox extends StackPane {
     public ElementBox(int number, int col, int row) {
        setPathe();
        setAmount(number);

        coordinates = new Coordinates(row, col);

        Circle circle = new Circle();
        circle.setRadius(60.0);

        Text text = new Text(getAmount());
        text.setFill(this.numberColor);
        text.setFont(Font.font("Cooper Black",FontWeight.BOLD,35));
        setAlignment(Pos.CENTER);
        getChildren().addAll(circle, text);

        circle.setFill(new ImagePattern(new Image(getPathe(getIPN()))));

        setOnMouseEntered(event -> {
            ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), circle);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });

        setOnMouseExited(event -> {
            ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), circle);
            st.setFromX(1.1);
            st.setFromY(1.1);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
    }

    /**
     * Setting path to image for each type of star.
     */
    private void setPathe(){
        pathways = new String[]{
                getClass().getClassLoader().getResource("M0.jpg").toExternalForm(),
                getClass().getClassLoader().getResource("M1.png").toExternalForm(),
                getClass().getClassLoader().getResource("M2.jpg").toExternalForm(),
                getClass().getClassLoader().getResource("M3.jpg").toExternalForm(),
                getClass().getClassLoader().getResource("K0.jpg").toExternalForm(),
                getClass().getClassLoader().getResource("K1.jpg").toExternalForm(),
                getClass().getClassLoader().getResource("K2.png").toExternalForm(),
                getClass().getClassLoader().getResource("G0.jpg").toExternalForm(),
                getClass().getClassLoader().getResource("G1.jpg").toExternalForm(),
                getClass().getClassLoader().getResource("G2.jpg").toExternalForm(),
                getClass().getClassLoader().getResource("F0.png").toExternalForm(),
                getClass().getClassLoader().getResource("B0.png").toExternalForm(),
                getClass().getClassLoader().getResource("O0.png").toExternalForm(),
                getClass().getClassLoader().getResource("S0.jpg").toExternalForm()
        };
    }

    /**
     * @param num The number of type of star.
     * @return String line - the path to image.
     */
    private String getPathe(int num){
        return pathways[num];
    }

    /**
     * Setting number inside star.
     * @param right Initialise the number.
     */
    private void setAmount(int right){
        amount = right;
        setParametrs();
    }

    /**
     * Return number inside star.
     * @return String The number in star.
     */
    public String getAmount() {
        return String.valueOf(amount);
    }

    /**
     * Setting image for each star number, color and number
     * of path to the image.
     */
    private void setParametrs() {
        switch (amount) {
            case 2 :
                imagePatternNumber = 0;
                numberColor = Color.WHITE;
                setIPN(0);
                break;
            case 4 :
                imagePatternNumber = 1;
                numberColor = Color.WHITE;
                setIPN(1);
                break;
            case 8 :
                imagePatternNumber = 2;
                numberColor = Color.WHITE;
                setIPN(2);
                break;
            case 16 :
                imagePatternNumber = 3;
                numberColor = Color.WHITE;
                setIPN(3);
                break;
            case 32 :
                imagePatternNumber = 4;
                numberColor = Color.WHITE;
                setIPN(4);
                break;
            case 64 :
                imagePatternNumber = 5;
                numberColor = Color.WHITE;
                setIPN(5);
                break;
            case 128 :
                imagePatternNumber = 6;
                numberColor = Color.ORANGE;
                setIPN(6);
                break;
            case 256 :
                imagePatternNumber = 7;
                numberColor = Color.ORANGE;
                setIPN(7);
                break;
            case 512 :
                imagePatternNumber = 8;
                numberColor = Color.WHITE;
                setIPN(8);
                break;
            case 1024 :
                imagePatternNumber = 9;
                numberColor = Color.WHITE;
                setIPN(9);
                break;
            case 2048 :
                imagePatternNumber = 10;
                numberColor = Color.YELLOW;
                setIPN(10);
                break;
            case 4096 :
                imagePatternNumber = 11;
                numberColor = Color.AQUA;
                setIPN(11);
                break;
            case 8192 :
                imagePatternNumber = 12;
                numberColor = Color.BLUE;
                setIPN(12);
                break;
            case 16384 :
                imagePatternNumber = 13;
                numberColor = Color.WHITE;
                setIPN(13);
                break;
            default:
                imagePatternNumber = 0;
                numberColor = Color.WHITE;
                setIPN(0);
                break;
        }
    }

    /**
     * This method returning the number of row item in matrix,
     * who'll be implement later.
     * @return int Returning the number of row item in matrix.
     */
    public int getPosX() {
        return coordinates.getX();
    }

    /**
     * This method returning the number of column in matrix,
     * who'll be implement later.
     * @return int Returning the number of column in matrix.
     */
    public int getPosY() {
        return coordinates.getY();
    }

    /**
     * This is an auxiliary method for setting path to the image.
     * @param right This is number of path in array of pathways.
     */
    private void setIPN(int right){
        imagePatternNumber = right;
    }

    /**
     * This is an auxiliary method for returning path to the image.
     * @return int right This is number of actual path to image.
     */
    private int getIPN(){
        return imagePatternNumber;
    }

    private Coordinates coordinates;
    private int amount;
    private int imagePatternNumber;
    private String[] pathways;
    private Color numberColor;
}
