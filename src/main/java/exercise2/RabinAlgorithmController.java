package exercise2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by szale_000 on 2017-03-04.
 */
public class RabinAlgorithmController {
    private static final Integer PRIVATE_KEY_P = 19;
    private static final Integer PRIVATE_KEY_Q = 23;
    private static final Integer PUBLIC_KEY_N = PRIVATE_KEY_P * PRIVATE_KEY_Q;
    @FXML
    TextArea plainTextTextarea;
    public Button buttonEncrypt;
    @FXML
    private TextArea privateKeyPTextarea;
    @FXML
    private TextArea privateKeyQTextarea;
    @FXML
    private TextArea publicKeyTextarea;
    @FXML
    private TextArea plainTextIntegerTextarea;
    @FXML
    private TextArea encryptedTextTextarea;
    @FXML
    private TextArea decryptedTextTextarea;
    @FXML
    private TextField encryptedTextParametr1;
    @FXML
    private TextField encryptedTextParametr2;

    private PythonInterpreter interpreter = new PythonInterpreter();

    public RabinAlgorithmController() {

    }

    @FXML
    public void convertTextToNumber() {
        interpreter.execfile("rabin.py");
        calculateTextToInteger();
    }

    @FXML
    public void handleEncryptClick() {
        interpreter.execfile("rabin.py");
//        calculateTextToInteger();
//        calculateIntegerToText(plainTextIntegerTextarea.getText());
        findPrivateKey();
        findPublicKey();
        encrypt();
    }

    @FXML
    public void handleDecryptClick() {
        interpreter.execfile("rabin.py");
        PyObject decryptMessage = interpreter.get("decrypt");
        PyObject decryptedMessageResult = decryptMessage.__call__(
                new PyString(privateKeyPTextarea.getText()),
                new PyString(privateKeyQTextarea.getText()),
                new PyString(encryptedTextTextarea.getText()),
                new PyString(encryptedTextParametr1.getText() + encryptedTextParametr2.getText())
        );

        String result = (String) decryptedMessageResult.__tojava__(String.class);
        result = convertIntegerToText(result);
        decryptedTextTextarea.setText(result);
    }

    @FXML
    public void handleFileChoose() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj plik tekstowy do zaszyfrowania");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(plainTextTextarea.getScene().getWindow());
        if (selectedFile != null) {
            readFile(selectedFile);
        }
    }

    private void readFile(File selectedFile) {

        FileReader inputText = null;
        try {
            inputText = new FileReader(selectedFile.toString());
            BufferedReader br = new BufferedReader(inputText);

            String fileContent = "";
            String fileRead;
            while ((fileRead = br.readLine()) != null) {
                fileContent += fileRead;
                fileContent += System.getProperty("line.separator");
            }
            plainTextTextarea.setText(fileContent);

        } catch (FileNotFoundException ex) {
            Logger.getLogger("readFile FileNotFoundException");
        } catch (IOException ex) {
            Logger.getLogger("readFile IOException");
        }
    }

    private void calculateTextToInteger() {
        String intMsg = convertTextToInteger(plainTextTextarea.getText());
        plainTextIntegerTextarea.setText(intMsg);
    }

    private void findPrivateKey() {
        PyObject findPrivateKey = interpreter.get("findPrivateKey");
        PyObject privateKeyResult = findPrivateKey.__call__(
                new PyString(plainTextIntegerTextarea.getText())
        );
        List<String> privateKey = (List) privateKeyResult.__tojava__(List.class);

        privateKeyPTextarea.setText(privateKey.get(0));
        privateKeyQTextarea.setText(privateKey.get(1));
    }

    private void findPublicKey() {
        PyObject findPublicKey = interpreter.get("findPublicKey");
        PyObject publicKeyResult = findPublicKey.__call__(
                new PyString(privateKeyPTextarea.getText()),
                new PyString(privateKeyQTextarea.getText())
        );
        publicKeyTextarea.setText((String) publicKeyResult.__tojava__(String.class));
    }

    private void calculateIntegerToText(String message) {
        PyObject textFromInteger = interpreter.get("getTextFromInteger");
        PyObject textFromIntegerResult = textFromInteger.__call__(
                new PyString(message)
        );
    }

    private String convertTextToInteger(String text) {
        char[] chars = text.toCharArray();
        String result = "1";
        for (char c :
                chars) {
            String valueAsString = Integer.toString((int) c);
            while (valueAsString.length() < 3) {
                valueAsString = "0" + valueAsString;
            }
            result += valueAsString;
        }
        return result;
    }

    private String convertIntegerToText(String integer) {
        integer = integer.substring(1);
        String result = "";
        for (int i = 0; i < integer.length() - 2; i += 3) {
            String stringValue = integer.substring(i, i + 3);
            int intValue = Integer.valueOf(stringValue);
            char c = (char) intValue;
            result += c;
        }
        return result;
    }


    private void encrypt() {
        PyObject encryptMessage = interpreter.get("encrypt");
        PyObject encryptedMessageResult = encryptMessage.__call__(
                new PyString(plainTextIntegerTextarea.getText()),
                new PyString(publicKeyTextarea.getText())
        );
        List<String> encryptedList = (List) encryptedMessageResult.__tojava__(List.class);
        encryptedTextTextarea.setText(encryptedList.get(0));
        encryptedTextParametr1.setText(encryptedList.get(1));
        encryptedTextParametr2.setText(encryptedList.get(2));
    }
}

