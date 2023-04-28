import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ScoreManager {
    private static final String FILENAME = "scores.txt";

    public static void saveScore(int score, long timePlayed) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME, true))) {
            String scoreString = String.format("Score: %d, Time played: %d seconds", score, timePlayed/1000);
            bw.write(scoreString);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}