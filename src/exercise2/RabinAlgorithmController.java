package exercise2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by szale_000 on 2017-03-04.
 */
public class RabinAlgorithmController {
    private static final Integer PRIVATE_KEY_P = 19;
    private static final Integer PRIVATE_KEY_Q = 23;
    private static final Integer PUBLIC_KEY_N = PRIVATE_KEY_P * PRIVATE_KEY_Q;
    public TextArea textFieldInput;
    public Button buttonEncrypt;

    public RabinAlgorithmController() {

    }

    public void runPythonScript(String textToEncrypt) throws IOException {
        String[] cmd = {
                "C:\\Users\\Monia\\AppData\\Local\\Programs\\Python\\Python36-32\\python",
                "C:\\Users\\Monia\\Documents\\_STUDIA\\studia - 6 semestr\\KRYPTOGRAFIA\\Kryptografia\\src\\exercise2\\rabinAlgorithm\\rabinWrapper.py",
                textToEncrypt,
        };
        String s = null;

        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec(cmd);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.exit(0);
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }


    @FXML
    public void handleEncryptClick() {
        String textToEncrypt = textFieldInput.getText();
        try {
            runPythonScript(textToEncrypt);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

