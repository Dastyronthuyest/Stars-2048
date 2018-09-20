package general;

/**
 * This is class for game information storage.
 */
public class TempInfo {
    /**
     * The class constructor.
     */
    TempInfo(){
        numbers = new int[4][4];
        for(int i = 0; i < 4; i++){
            numbers[i] = new int[4];
            for(int j = 0; j < 4; j++){
                numbers[i][j] = 0;
            }
        }
        setMaxScore(0);
        setScore(0);
    }

    /**
     * This method return selected matrix cell amount.
     * @param x The number of line.
     * @param y The number of column.
     * @return int The selected matrix cell amount.
     */
    public int getNumbers(int x, int y) {
        return numbers[x][y];
    }

    /**
     * This method setting selected matrix cell amount.
     * @param number The cell future amount.
     * @param x The number of line.
     * @param y The number of column.
     */
    public void setNumbers(int number, int x, int y) {
        this.numbers[x][y] = number;
    }

    /**
     * This method returning max score.
     * @return int Max score amount.
     */
    public int getMaxScore() {
        return maxScore;
    }

    /**
     * This method setting new max score amount.
     * @param maxScore New max score amount.
     */
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * This method returning actual score.
     * @return int Actual score amount.
     */
    public int getScore() {
        return score;
    }

    /**
     * This method setting new actual score amount.
     * @param score Actual max score amount.
     */
    public void setScore(int score) {
        this.score = score;
    }

    private int numbers[][];
    private int maxScore;
    private int score;
}
