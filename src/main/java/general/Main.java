package general;

import interfaceElements.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.ElementBox;
import models.ManageButton;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Scanner;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This is the interpretation of the mobile game "2048",
 * where the standard squares are replaced by
 * stars of different luminosity (from M to O),
 * and the field is represented in the form of space.
 *
 * @author  Alexey Deusov
 * @version 1.4
 * @since   2018-02-24
 */
public class Main extends Application {

    /**
     * The start method, where created all scenes, settled primary
     * stage and added all of interface elements.
     * Also here has realised main logic and bot.
     * @param primaryStage This is the standard parameter for
     * application window display.
     */

    @Override
    public void start(Stage primaryStage) {

        root = new Pane();
        Image fon = new Image(getClass().getClassLoader().getResourceAsStream("fon.jpg"));
        ImageView fonView = new ImageView(fon);
        fonView.setFitHeight(680);
        fonView.setFitWidth(780);
        root.getChildren().add(fonView);

        fileManager = new File(filename);
        initialJSONS();
        initialInfo();

        matrix = new ElementBox[4][4];
        initialMatrix();

        panels = new Panels();
        setPanels();

        addElementsOnScreen();

        banner = new GameName();
        setBanner();

        score = new ScoreBox();
        initialScore();

        highScore = new HighScoreBox();
        initialHighScore();

        newGameButton = new ManageButton("New Game");
        setNewGameButton();

        botButton = new ManageButton("Bot Auto Step");
        setBotButton();

        undoButton = new ManageButton("Undo");
        setUndoButton();

        backToButton = new ManageButton("Back To...");
        setBackToButton();

        restoreArea = new TextField();
        setRestoreArea();

        exitButton = new ManageButton("Exit");
        setExitButton();

        Scene scene = new Scene(root,780,680);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) {
                moveLeft();
            }
            else if (event.getCode() == KeyCode.D) {
                moveRight();
            }
            else if (event.getCode() == KeyCode.W) {
                moveUp();
            }
            else if (event.getCode() == KeyCode.S) {
                moveDown();
            }
        });

        primaryStage.setTitle("2048 Stars Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This is constant data file name.
     */
    private static final String filename= "C:\\Main\\Java Projects\\Stars_2048_release\\data";

    /**
     * This is method to temporal JSON-elements initialisation.
     */
    private void initialJSONS(){
        jsonObject = new JSONObject();
        jsonArray = new JSONArray();
    }

    /**
     * This is method, who add create new JSON field.
     */
    private void addJSON(){
        jsonObject.put("maxScore", info.getMaxScore());
        jsonObject.put("actualScore", info.getScore());

        JSONArray numbers = new JSONArray();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                numbers.add(info.getNumbers(i, j));
            }
        }

        jsonObject.put("data", numbers);
    }

    /**
     * This is method, who write all available
     * JSON fields in file.
     */
    private void writeJSON(){
        addJSON();
        jsonArray.add(jsonObject);
        jsonObject = new JSONObject();

        try (FileWriter writer = new FileWriter(filename)){


            if(jsonArray.size() > 100){
                jsonArray.remove(0);
            }

            for(int i = 0; i < jsonArray.size(); i++){
                JSONObject temp = (JSONObject) jsonArray.get(i);
                writer.write(temp.toJSONString() + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        jsonArray.clear();
    }

    /**
     * This is method, who read all
     * JSON fields from file.
     */
    private void readJSON() {
        jsonArray.clear();
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(filename);
            Scanner scanner = new Scanner(reader);

            while(scanner.hasNextLine()) {
                jsonObject = (JSONObject) parser.parse(scanner.nextLine());
                jsonArray.add(jsonObject);
                jsonObject = new JSONObject();
            }

            fileManager.delete();
            fileManager.createNewFile();

        } catch (IOException | ParseException ex) {
            Logger.getLogger(Main.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This is support method, who read and write
     * JSON fields.
     */
    private void updateJSON(){
        readJSON();
        writeJSON();
    }

    /**
     * This is method for first stage of restore information
     * from previous session to info class.
     */
    private void initialInfo() {
        info = new TempInfo();

        readJSON();

        try {
            fileManager.delete();
            fileManager.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        JSONObject actual = (JSONObject) jsonArray.get(jsonArray.size()-1);

        Long tempMaxScore = (Long) actual.get("maxScore");
        int resultMaxScore = tempMaxScore.intValue();
        info.setMaxScore(resultMaxScore);

        Long tempScore = (Long) actual.get("actualScore");
        int resultScore = tempScore.intValue();
        info.setScore(resultScore);

        JSONArray data = (JSONArray) actual.get("data");
        Iterator<Long> iterator = data.iterator();

        int i = 0;
        while(iterator.hasNext()) {
            int number = iterator.next().intValue();
            info.setNumbers(number, i / 4, i % 4);
            i++;
        }
    }

    /**
     * This is method for info class initialisation
     * from real matrix.
     */
    private void setInfo(){
        info.setMaxScore(highScore.getScore());
        info.setScore(score.getScore());
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++) {
                if(matrix[i][j] != null) {
                    info.setNumbers(Integer.parseInt(matrix[i][j].getAmount()), i, j);
                }
                else{
                    info.setNumbers(0, i, j);
                }
            }
        }
    }

    /**
     * This is method for matrix begin initialisation.
     */
    private void initialMatrix(){
        for(int i = 0; i < 4; i++){
            matrix[i] = new ElementBox[4];
            for(int j = 0; j < 4; j++){
                matrix[i][j] = null;
            }
        }
    }

    /**
     * There is formed panels object.
     */
    private void setPanels(){
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(info.getNumbers(i, j) != 0){
                    panels.addHandle(info.getNumbers(i, j), i, j);
                }
            }
        }
        Panels.setScore(info.getScore());
        Panels.setMaxScore(info.getMaxScore());
    }

    /**
     * This is method for placing all
     * matrix elements on scene.
     */
    private void addElementsOnScreen(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                int amount = panels.getState(i, j);
                if(amount != 0){
                    matrix[i][j] = new ElementBox(amount, i, j);
                    int posX = matrix[i][j].getPosX();
                    matrix[i][j].setTranslateX(getPositionInScreenX(posX));
                    int posY = matrix[i][j].getPosY();
                    matrix[i][j].setTranslateY(getPositionInScreenY(posY));
                    root.getChildren().add(matrix[i][j]);
                }
            }
        }
    }

    /**
     * This is method for placing
     * game's banner.
     */
    private void setBanner(){
        banner.setTranslateX(65);
        banner.setTranslateY(35);
        root.getChildren().add(banner);
    }

    /**
     * This is method for placing
     * score field on scene.
     */
    private void initialScore(){
        score.setScore(Panels.getScore());
        score.setTranslateX(350);
        score.setTranslateY(35);
        root.getChildren().add(score);
    }

    /**
     * This is method for placing
     * max score field on scene.
     */
    private void initialHighScore(){
        highScore.setScore(Panels.getMaxScore());
        highScore.setTranslateX(480);
        highScore.setTranslateY(35);
        root.getChildren().add(highScore);
    }

    /**
     * This is method for
     * matrix cleaning.
     */
    private void cleanMatrix(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(matrix[i][j] != null){
                    root.getChildren().remove(matrix[i][j]);
                    matrix[i][j] = null;
                }
            }
        }
    }

    /**
     * This is method for placing
     * new game button on scene.
     */
    private void setNewGameButton(){
        newGameButton.setTranslateX(640);
        newGameButton.setTranslateY(35);
        root.getChildren().add(newGameButton);
        newGameButton.setOnMouseClicked(event -> {
            cleanMatrix();
            panels.reload();

            for(int i = 0; i < 2; i++){
                panels.add();
            }
            addElementsOnScreen();

            score.setScore(0);
            Panels.setScore(0);

            setInfo();

            updateJSON();
        });
    }

    /**
     * This is method for compare all possible
     * movement and definition the better.
     * @param left This is result of future left move.
     * @param right This is result of future right move.
     * @param up This is result of future up move.
     * @param down This is result of future down move.
     * @return TestReturn The better possible movement.
     */
    private TestReturn betterAutoMove(TestReturn left, TestReturn right, TestReturn up, TestReturn down){
        int leftResult = left.getFreeCellsAmount() + left.getScore();
        int rightResult = right.getFreeCellsAmount() + right.getScore();
        int upResult = up.getFreeCellsAmount() + up.getScore();
        int downResult = down.getFreeCellsAmount() + down.getScore();

        int maxResult = leftResult;
        if(rightResult > maxResult){
            maxResult = rightResult;
        }
        if(upResult > maxResult){
            maxResult = upResult;
        }
        if(downResult > maxResult){
            maxResult = downResult;
        }

        if(maxResult == leftResult){
            return left;
        }
        if(maxResult == rightResult){
            return right;
        }
        if(maxResult == upResult){
            return up;
        }
        return down;
    }

    /**
     * This is method for placing
     * bot auto step button on scene.
     */
    private void setBotButton(){
        botButton.setTranslateX(640);
        botButton.setTranslateY(115);
        root.getChildren().add(botButton);
        botButton.setOnMouseClicked(event ->{
            TestReturn left = panels.toLeftTest();
            TestReturn right = panels.toRightTest();
            TestReturn up = panels.toUpTest();
            TestReturn down = panels.toDownTest();

            TestReturn returnResult = betterAutoMove(left, right, up, down);

            if(returnResult.getScore() == left.getScore()
                    && returnResult.getFreeCellsAmount() == left.getFreeCellsAmount()){
                moveLeft();
            }
            else if(returnResult.getScore() == up.getScore()
                    && returnResult.getFreeCellsAmount() == up.getFreeCellsAmount()){
                moveUp();
            }
            else if(returnResult.getScore() == right.getScore()
                    && returnResult.getFreeCellsAmount() == right.getFreeCellsAmount()){
                moveRight();
            }
            else{
                moveDown();
            }
        });
    }

    /**
     * Back to previous step.
     */
    private void toPrevStep(TempInfo prevState){
        if(prevState != null) {
            cleanMatrix();
            panels.toPrevStep();

            addElementsOnScreen();
            score.setScore(panels.getScore());

            setInfo();

            updateJSON();
        }
    }

    /**
     * This is method for placing
     * undo button on scene.
     */
    private void setUndoButton(){
        undoButton.setTranslateX(640);
        undoButton.setTranslateY(195);
        root.getChildren().add(undoButton);
        undoButton.setOnMouseClicked(event -> {
            TempInfo prevState = panels.getPrevStep();
            toPrevStep(prevState);
        });
    }

    /**
     * This is support method for numeric checking.
     * @param str It's checking string.
     * @return boolean Logical result.
     */
    private static boolean isNumeric(String str){
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    /**
     * This is method for placing
     * restore area on scene.
     */
    private void setRestoreArea(){
        restoreArea.setPrefSize(100, 60);
        restoreArea.setStyle("-fx-background-color: grey");
        restoreArea.setOpacity(0.4);
        restoreArea.setTranslateX(640);
        restoreArea.setTranslateY(335);
        restoreArea.setPromptText("From 1 to 100");
        root.getChildren().add(restoreArea);
    }

    /**
     * This is method for placing
     * to back button on scene.
     */
    private void setBackToButton(){
        backToButton.setTranslateX(640);
        backToButton.setTranslateY(275);
        root.getChildren().add(backToButton);
        backToButton.setOnMouseClicked(event -> {
            if(restoreArea.getText().isEmpty()){
                restoreArea.setPromptText("No value");
            }
            else if(isNumeric(restoreArea.getText())) {
                int number = Integer.parseInt(restoreArea.getText());
                if (number < 1 || number > 100) {
                    restoreArea.setPromptText("Invalid value");
                } else {
                    jsonArray.clear();
                    JSONParser parser = new JSONParser();
                    try {
                        FileReader reader = new FileReader(filename);
                        Scanner scanner = new Scanner(reader);

                        while(scanner.hasNextLine()) {
                            jsonObject = (JSONObject) parser.parse(scanner.nextLine());
                            jsonArray.add(jsonObject);
                            jsonObject = new JSONObject();
                        }
                        int position = jsonArray.size() - 1 - number;
                        if(position < 0){
                            restoreArea.setPromptText("You enter invalid value");
                        }
                        else{
                            cleanMatrix();
                            panels.reload();

                            jsonObject = (JSONObject) jsonArray.get(position);

                            Long tempMaxScore = (Long) jsonObject.get("maxScore");
                            int resultMaxScore = tempMaxScore.intValue();
                            info.setMaxScore(resultMaxScore);

                            Long tempScore = (Long) jsonObject.get("actualScore");
                            int resultScore = tempScore.intValue();
                            info.setScore(resultScore);

                            JSONArray data = (JSONArray) jsonObject.get("data");
                            Iterator<Long> iterator = data.iterator();

                            int i = 0;
                            while(iterator.hasNext()) {
                                int numeric = iterator.next().intValue();
                                info.setNumbers(numeric, i / 4, i % 4);
                                i++;
                            }

                            setPanels();

                            addElementsOnScreen();

                            score.setScore(info.getScore());
                            Panels.setScore(info.getScore());

                            setInfo();

                            updateJSON();

                            restoreArea.setPromptText("From 1 to 100");
                        }
                    } catch (IOException | ParseException ex) {
                        Logger.getLogger(Main.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            }
            else{
                restoreArea.setPromptText("Invalid value");
            }
            restoreArea.clear();
        });
    }

    /**
     * This is method for placing
     * exit button on scene.
     */
    private void setExitButton(){
        exitButton.setTranslateX(640);
        exitButton.setTranslateY(415);
        root.getChildren().add(exitButton);
        exitButton.setOnMouseClicked(event-> System.exit(0));
    }

    /**
     * This is method, whose updating
     * max score, if it is more than actual.
     */
    private void updateMaxScore(){
        if(highScore.getScore() < score.getScore()){
            highScore.setScore(score.getScore());
        }
    }

    /**
     * This is method for element's left shifting.
     */
    private void moveLeft(){
        cleanMatrix();
        score.setScore(panels.toLeft());
        updateMaxScore();
        addElementsOnScreen();
        setInfo();

        updateJSON();
    }

    /**
     * This is method for element's right shifting.
     */
    private void moveRight(){
        cleanMatrix();
        score.setScore(panels.toRight());
        updateMaxScore();
        addElementsOnScreen();
        setInfo();

        updateJSON();
    }

    /**
     * This is method for element's up shifting.
     */
    private void moveUp(){
        cleanMatrix();
        score.setScore(panels.toUp());
        updateMaxScore();
        addElementsOnScreen();
        setInfo();

        updateJSON();
    }

    /**
     * This is method for element's down shifting.
     */
    private void moveDown(){
        cleanMatrix();
        score.setScore(panels.toDown());
        updateMaxScore();
        addElementsOnScreen();
        setInfo();

        updateJSON();
    }

    /**
     * This is temporary method for positioning "stars" on axis X.
     * @param num This is the number of row item in matrix
     * @return double The position on StackPane on axis X.
     */
    private double getPositionInScreenX(int num){
        if(num == 0)
            return 65;
        else
            return (65 + (130 * num));
    }

    /**
     * This is temporary method for positioning "stars" on axis Y.
     * @param num This is the number of column in matrix
     * @return double The position on StackPane on axis Y.
     */
    private double getPositionInScreenY(int num){
        if(num == 0)
            return 115;
        else
            return (115 + (130 * num));
    }

    /**
     * This is main method, where the primary stage launching.
     * @param args This is the standard parameter to command line.
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Pane root;
    private File fileManager;
    private TempInfo info;
    private ElementBox matrix[][];
    private Panels panels;
    private GameName banner;
    private ScoreBox score;
    private HighScoreBox highScore;
    private ManageButton newGameButton;
    private ManageButton botButton;
    private ManageButton undoButton;
    private ManageButton backToButton;
    private TextField restoreArea;
    private ManageButton exitButton;

    private JSONArray jsonArray;
    private JSONObject jsonObject;
}