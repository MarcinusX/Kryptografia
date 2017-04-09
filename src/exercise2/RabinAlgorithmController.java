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
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.print("bbb");

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            System.out.print("aaaa");
            System.out.println(in.readLine());
        } catch (Exception e) {
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

