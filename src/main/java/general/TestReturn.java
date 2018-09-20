package general;

/**
 * This is supported method for calculating
 * the better bot's movement.
 */
public class TestReturn {

    /**
     * The class constructor.
     * @param tempScore Score setting.
     * @param tempFreeCellsAmount Free cells amount setting.
     */
    TestReturn(int tempScore, int tempFreeCellsAmount){
        setScore(tempScore);
        setFreeCellsAmount(tempFreeCellsAmount);
    }

    /**
     * Score setting.
     * @param freeCellsAmount New score.
     */
    private void setFreeCellsAmount(int freeCellsAmount) {
        this.freeCellsAmount = freeCellsAmount;
    }

    /**
     * Free cells amount setting.
     * @param score New free cells amount.
     */
    private void setScore(int score) {
        this.score = score;
    }

    /**
     * This method is returning free cells amount.
     * @return int Free cells amount.
     */
    public int getFreeCellsAmount() {
        return freeCellsAmount;
    }

    /**
     * This method is return the score.
     * @return int The score.
     */
    public int getScore() {
        return score;
    }

    private int score;
    private int freeCellsAmount;
}
