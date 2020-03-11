package main.java.com.pdag;

import java.util.Arrays;

/**
 * Score class - stores and calculates all data connected with game scores
 */
public class Score {

    /**
     * Score
     */
    private static double totalScore = 0;
    private static double completedLevelsScore = 0;
    private static double thisLevelBonusScore = 0;

    /**
     * This level Score
     */
    private static double thisLevelTimeScore = 0;
    private static double thisLevelFuelScore = 0;
    private static double currentTotalScore = 0;
    private static final double[] thisLevelTotalScore;

    /**
     * Time and Timer
     */
    private static double levelTime = 0;
    private static double timeLeft = Config.timeForLevel;
    private static double timer;

    /**
     * completedLevels - for every completed level player gets 100 points
     */
    private static final boolean[] completedLevels;
    private static boolean allLevelsCompleted = false;

    static {
        thisLevelTotalScore = new double[Config.numberOfLevels];
        completedLevels = new boolean[Config.numberOfLevels];
        for (int i = 0; i < Config.numberOfLevels; i++) {
            completedLevels[i] = false;
            thisLevelTotalScore[i] = 0;
        }
    }

    /**
     * claimBonus method - adds bonus to bonusScore attribute
     *
     * @param value -specifies  how many bonus points should method add
     */
    public static void claimBonus(double value) {
        thisLevelBonusScore += value;
    }

    /**
     * levelCompleted method - if level has been completed it checks which one (needed when calculating total score)
     *
     * @param levelNumber - specifies which level has been completed
     */
    public static void levelCompleted(int levelNumber) {
        completedLevels[levelNumber - 1] = true;

        int counter = 0;
        for (int i = 0; i < Config.numberOfLevels; i++) {
            if (completedLevels[i]) {
                counter++;
            }
            if (counter == Config.numberOfLevels) {
                allLevelsCompleted = true;
            }
        }
    }

    /**
     * update method - updates Score. Method called from Game class
     *
     * @see Game
     */
    public static void update() {

        switch (Game.gameState) {
            case RUNNING:

                //Time Update
                levelTime = (System.currentTimeMillis() - timer) / 1000;
                if (levelTime < 0) {
                    levelTime = 0;
                }

                //Score Update

                //to see score in-game
                timeLeft = Config.timeForLevel - levelTime;
                if (Game.gameDifficulty != Game.GameDifficulty.HARD) {
                    if (timeLeft < 0) {
                        timeLeft = 0;
                    }
                }
                thisLevelTimeScore = timeLeft;

                thisLevelFuelScore = Spacecraft.getFuelLeft() * Config.fuelScoreMultiplier;
                currentTotalScore = totalScore + thisLevelTimeScore + thisLevelFuelScore;
                if (currentTotalScore < 0) {
                    currentTotalScore = 0;
                }
                break;

            case PROCESSING_WIN:

                //calculating total score

                completedLevelsScore = 0;
                for (int i = 0; i < Config.numberOfLevels; i++) {
                    if (completedLevels[i]) {
                        completedLevelsScore += Config.completedLevelScore;
                    }
                }

                thisLevelTotalScore[Config.levelNumber - 1] = thisLevelTimeScore + thisLevelFuelScore + thisLevelBonusScore;
                if (thisLevelTotalScore[Config.levelNumber - 1] < 0)
                    thisLevelTotalScore[Config.levelNumber - 1] = 0;

                totalScore = 0;
                totalScore += completedLevelsScore;
                for (int i = 0; i < Config.numberOfLevels; i++) {
                    totalScore += thisLevelTotalScore[i];
                }
                resetLevelScore();
                break;

            case FAILURE:
                currentTotalScore = 0;
                break;

            default:
                break;
        }
    }

    /**
     * getTotalScore method - returns total score
     *
     * @return total score
     */
    public static double getTotalScore() {
        return totalScore;
    }

    /**
     * getCurrentTotalScore method - returns current total score
     *
     * @return current total score (used in ScoreBox)
     */
    public static double getCurrentTotalScore() {
        return currentTotalScore;
    }

    /**
     * getTimeLeft method - returns time left for current level
     *
     * @return time left for level
     */
    public static double getTimeLeft() {
        return timeLeft;
    }

    /**
     * getCompletedLevels method - returns completed levels array
     *
     * @return array of completed levels (true/false)
     */
    public static boolean[] getCompletedLevels() {
        return completedLevels;
    }

    /**
     * getThisLevelTotalScore method - returns particular level score
     *
     * @param index - specifies which level score should it return
     * @return total score for particular level
     */
    public static double getThisLevelTotalScore(int index) {
        return thisLevelTotalScore[index];
    }

    /**
     * isAllLevelsCompleted method - returns true if all level have been completed
     *
     * @return true if all levels are completed
     */
    public static boolean isAllLevelsCompleted() {
        return allLevelsCompleted;
    }

    /**
     * resetLevelTimer method - resets timer and starts it
     */
    public static void resetLevelTimer() {
        levelTime = 0;
        timer = System.currentTimeMillis();
    }

    /**
     * resetLevelScore method - resets score for current level
     */
    public static void resetLevelScore() {
        thisLevelTimeScore = 0;
        thisLevelFuelScore = 0;
        currentTotalScore = 0;
        thisLevelBonusScore = 0;
    }

    /**
     * reset method - resets the whole score
     */
    public static void reset() {
        //Score
        totalScore = 0;
        completedLevelsScore = 0;
        resetLevelScore();
        for (int i = 0; i < Config.numberOfLevels; i++) {
            thisLevelTotalScore[i] = 0;
        }

        //Time
        levelTime = 0;
        Arrays.fill(completedLevels, false);
    }
}