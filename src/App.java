import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\sbraa\\Documents\\Varsity Tutors\\Yara\\Exercises\\Scorpion\\Scorpion\\ScorpionRequiredInput");

        String input = new String();
        BufferedReader br = new BufferedReader(new FileReader(file));
        input = br.readLine();
        br.close();

        Game g = new Game();
        Player p1 = new Player();
        
        p1.startGame(g, input);
        p1.playGame();
    }
}
