package general;

import models.Coordinates;

import java.util.ArrayList;
import java.util.Random;

/**
 * This is support class, there are stored actual
 * score, and calculated results of movements.
 */
public class Panels {
    private final Random random = new Random();

    /**
     * This is support class for string shift result returning.
     */
    private static class ShiftRowResult{
        boolean didAnythingMove;
        int[] shiftedRow;
    }

    /**
     * This is class constructor.
     */
    Panels() {
        matrix = new int[4][];
        for(int i = 0; i < 4; i++){
            matrix[i] = new int[4];
            for(int j = 0; j < 4; j++){
                matrix[i][j] = 0;
            }
        }

        freeCells = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                freeCells.add(new Coordinates(i, j));
            }
        }

        score = 0;
        maxScore = 0;

        prevStep = null;
    }

    /**
     * The method adds one cells in inner matrix.
     */
    public void add(){
        if(freeCells.size() == 0){
            return;
        }
        int position = random.nextInt(freeCells.size());
        Coordinates tempCoordinates = freeCells.remove(position);
        int number = (int)Math.pow(2, random.nextInt(2) + 1);
        int tempX = tempCoordinates.getX();
        int tempY = tempCoordinates.getY();
        matrix[tempX][tempY] = number;
    }

    /**
     * The method adds one cells in inner matrix by hands.
     * @param number The count of new cell.
     * @param column The column position on matrix.
     * @param row The row position on matrix.
     */
    public void addHandle(int number, int column, int row){
        matrix[column][row] = number;
        Coordinates temp = new Coordinates(column, row);
        freeCells.remove(temp);
    }

    /**
     * The method update selected column.
     * @param i The number of column.
     * @param newColumn New value of column.
     */
    private void setColumn(int i, int[] newColumn) {
        matrix[i] = newColumn;
    }

    /**
     * The method getting selected column.
     * @param i The number of column.
     * @return int[] The value of column.
     */
    private int[] getColumn(int i) {
        return matrix[i];
    }

    /**
     * The method update selected line.
     * @param i The number of line.
     * @param newLine New value of line.
     */
    private void setLine(int i, int[] newLine) {
        for(int j = 0; j < 4; j++){
            matrix[j][i] = newLine[j];
        }
    }

    /**
     * The method getting selected line.
     * @param i The number of line.
     * @return int[] The value of line.
     */
    private int[] getLine(int i) {
        int[] ret = new int[4];
        for(int j = 0; j < 4; j++){
            ret[j] = matrix[j][i];
        }
        return ret;
    }

    /**
     * The method returning selected cell amount.
     * @param x The number of line.
     * @param y The number of column.
     * @return int The cell amount.
     */
    public int getState(int x, int y){
        return matrix[x][y];
    }

    /**
     * The method calculate result of shift.
     * @param oldRow The shiftable line
     * @return ShiftRowResult Updated line and flag
     * if something changed.
     */
    private static ShiftRowResult shiftRow (int[] oldRow) {
        ShiftRowResult ret = new ShiftRowResult();
        int[] oldRowWithoutZeroes = new int[oldRow.length];
        {
            int q = 0;
            for (int i = 0; i < oldRow.length; i++) {
                if(oldRow[i] != 0){
                    if(q != i){
                        ret.didAnythingMove = true;
                    }
                    oldRowWithoutZeroes[q] = oldRow[i];
                    q++;
                }
            }

            for(int i = q; i < oldRowWithoutZeroes.length; i++) {
                oldRowWithoutZeroes[i] = 0;
            }
        }
        ret.shiftedRow = new int[oldRowWithoutZeroes.length];
        {
            int q = 0;
            {
                int i = 0;
                while (i < oldRowWithoutZeroes.length) {
                    if((i+1 < oldRowWithoutZeroes.length) && (oldRowWithoutZeroes[i] == oldRowWithoutZeroes[i + 1])
                            && oldRowWithoutZeroes[i]!=0) {
                        ret.didAnythingMove = true;
                        ret.shiftedRow[q] = oldRowWithoutZeroes[i] * 2;
                        score += oldRowWithoutZeroes[i] * 2;
                        i++;
                    } else {
                        ret.shiftedRow[q] = oldRowWithoutZeroes[i];
                    }
                    q++;
                    i++;
                }

            }
            for(int j = q; j < ret.shiftedRow.length; j++) {
                ret.shiftedRow[j] = 0;
            }
        }
        return ret;
    }

    /**
     * Left shift.
     * @return New score.
     */
    public int toLeft(){
        saveLastStep();
        freeCells.clear();
        for(int i = 0; i < 4; i++){
            int[] arg = getColumn(i);
            ShiftRowResult result = shiftRow (arg);
            setColumn(i, result.shiftedRow);
        }
        for(int i = 0; i <4; i++){
            for(int j = 0; j < 4; j++){
                if(matrix[i][j] == 0){
                    freeCells.add(new Coordinates(i, j));
                }
            }
        }
        add();
        return getScore();
    }

     /**
     * Right shift.
     * @return New score.
     */
    public int toRight(){
        saveLastStep();
        freeCells.clear();
        for(int i = 0; i < 4; i++){
            int[] arg = getColumn(i);

            {
                int[] tmp = new int[arg.length];
                for (int e = 0; e < tmp.length; e++) {
                    tmp[e] = arg[tmp.length - e - 1];
                }
                arg = tmp;
            }

            ShiftRowResult result = shiftRow (arg);

            {
                int[] tmp = new int[result.shiftedRow.length];
                for(int e = 0; e < tmp.length; e++){
                    tmp[e] = result.shiftedRow[tmp.length-e-1];
                }
                result.shiftedRow = tmp;
            }

            setColumn(i, result.shiftedRow);
        }
        for(int i = 0; i <4; i++){
            for(int j = 0; j < 4; j++){
                if(matrix[i][j] == 0){
                    freeCells.add(new Coordinates(i, j));
                }
            }
        }
        add();
        return getScore();
    }

    /**
     * Up shift.
     * @return New score.
     */
    public int toUp(){
        saveLastStep();
        freeCells.clear();
        for(int i = 0; i < 4; i++){
            int[] arg = getLine(i);
            ShiftRowResult result = shiftRow (arg);
            setLine(i, result.shiftedRow);
        }
        for(int i = 0; i <4; i++){
            for(int j = 0; j < 4; j++){
                if(matrix[i][j] == 0){
                    freeCells.add(new Coordinates(i, j));
                }
            }
        }
        add();
        return getScore();
    }

    /**
     * Down shift.
     * @return New score.
     */
    public int toDown(){
        saveLastStep();
        freeCells.clear();
        for(int i = 0; i < 4; i++){
            int[] arg = getLine(i);
            {
                int[] tmp = new int[arg.length];
                for(int e = 0; e < tmp.length; e++){
                    tmp[e] = arg[tmp.length-e-1];
                }
                arg = tmp;
            }

            ShiftRowResult result = shiftRow (arg);

            {
                int[] tmp = new int[result.shiftedRow.length];
                for(int e = 0; e < tmp.length; e++){
                    tmp[e] = result.shiftedRow[tmp.length-e-1];
                    }
                    result.shiftedRow = tmp;
                }

                setLine(i, result.shiftedRow);
            }
            for(int i = 0; i <4; i++){
                for(int j = 0; j < 4; j++){
                    if(matrix[i][j] == 0){
                        freeCells.add(new Coordinates(i, j));
                    }
                }
            }
        add();
        return getScore();
    }

    /**
     * Game's process reset.
     */
    public void reload(){
        matrix = new int[4][];
        for(int i = 0; i < 4; i++){
            matrix[i] = new int[4];
            for(int j = 0; j < 4; j++){
                matrix[i][j] = 0;
            }
        }

        freeCells = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                freeCells.add(new Coordinates(i, j));
            }
        }
    }

    /**
     * Calculate of possible left shift.
     * @return TestReturn Possible score and
     * count of free cells.
     */
    public TestReturn toLeftTest(){
        ArrayList<Coordinates> prevFreeCells = freeCells;
        int prevScore = score;
        int prevMatrix[][] = new int[4][4];
        for (int i = 0; i < 4; i++){
            prevMatrix[i] = new int[4];
            for (int j = 0; j < 4; j++){
                prevMatrix[i][j] = matrix[i][j];
            }
        }

        toLeft();

        TestReturn result = new TestReturn(score, freeCells.size());

        freeCells = prevFreeCells;
        score = prevScore;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                matrix[i][j] = prevMatrix[i][j];
            }
        }

        return result;
    }

    /**
     * Calculate of possible right shift.
     * @return TestReturn Possible score and
     * count of free cells.
     */
    public TestReturn toRightTest(){
        ArrayList<Coordinates> prevFreeCells = freeCells;
        int prevScore = score;
        int prevMatrix[][] = new int[4][4];
        for (int i = 0; i < 4; i++){
            prevMatrix[i] = new int[4];
            for (int j = 0; j < 4; j++){
                prevMatrix[i][j] = matrix[i][j];
            }
        }

        toRight();

        TestReturn result = new TestReturn(score, freeCells.size());

        freeCells = prevFreeCells;
        score = prevScore;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                matrix[i][j] = prevMatrix[i][j];
            }
        }

        return result;
    }

    /**
     * Calculate of possible up shift.
     * @return TestReturn Possible score and
     * count of free cells.
     */
    public TestReturn toUpTest(){
        ArrayList<Coordinates> prevFreeCells = freeCells;
        int prevScore = score;
        int prevMatrix[][] = new int[4][4];
        for (int i = 0; i < 4; i++){
            prevMatrix[i] = new int[4];
            for (int j = 0; j < 4; j++){
                prevMatrix[i][j] = matrix[i][j];
            }
        }

        toUp();

        TestReturn result = new TestReturn(score, freeCells.size());

        freeCells = prevFreeCells;
        score = prevScore;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                matrix[i][j] = prevMatrix[i][j];
            }
        }

        return result;
    }

    /**
     * Calculate of possible down shift.
     * @return TestReturn Possible score and
     * count of free cells.
     */
    public TestReturn toDownTest(){
        ArrayList<Coordinates> prevFreeCells = freeCells;
        int prevScore = score;
        int prevMatrix[][] = new int[4][4];
        for (int i = 0; i < 4; i++){
            prevMatrix[i] = new int[4];
            for (int j = 0; j < 4; j++){
                prevMatrix[i][j] = matrix[i][j];
            }
        }

        toDown();

        TestReturn result = new TestReturn(score, freeCells.size());

        freeCells = prevFreeCells;
        score = prevScore;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                matrix[i][j] = prevMatrix[i][j];
            }
        }

        return result;
    }

    /**
     * Actual score updating.
     * @param right New score.
     */
    public static void setScore(int right){
        score = right;
    }

    /**
     * Max score updating.
     * @param right New max score.
     */
    public static void setMaxScore(int right){
        maxScore = right;
    }

    /**
     * Returns the score.
     * @return int Actual score.
     */
    public static int getScore(){
        return score;
    }

    /**
     * Returns the max score.
     * @return int Max score.
     */
    public static int getMaxScore(){
        return maxScore;
    }

    /**
     * Back all parameters to previous step.
     */
    public void toPrevStep(){
        score = prevStep.getScore();
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                matrix[i][j] = prevStep.getNumbers(i, j);
            }
        }

        prevStep = null;
    }

    /**
     * Returns previous step to main class for
     * elements updating.
     * @return TempInfo Max score.
     */
    public TempInfo getPrevStep(){
        if(prevStep != null){
            TempInfo result = new TempInfo();
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    result.setNumbers(prevStep.getNumbers(i, j), i, j);
                }
            }
            result.setScore(prevStep.getScore());
            return result;
        }
        return null;
    }

    /**
     * Saving actual state to previous step.
     */
    private void saveLastStep(){
        if(prevStep == null){
            prevStep = new TempInfo();
        }

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                prevStep.setNumbers(matrix[i][j], i, j);
            }
        }

        prevStep.setScore(score);
    }

    private static int maxScore;
    private static int score;
    private int matrix[][];
    private ArrayList<Coordinates> freeCells;
    private TempInfo prevStep;
}