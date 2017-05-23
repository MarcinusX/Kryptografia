package exercise3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Monia on 2017-05-21.
 */
public class ElGamalAlgorithmController {
    private PythonInterpreter interpreter = new PythonInterpreter();

    @FXML
    private TextArea plainTextTextarea;
    @FXML
    private TextArea hashedMessageLabel;
    @FXML
    private TextArea publicKeyValueB;
    @FXML
    private TextArea keyValueP;
    @FXML
    private TextArea keyValueG;
    @FXML
    private TextArea privateKeyValueA;
    @FXML
    private TextArea digitalSignatureValueR;
    @FXML
    private TextArea digitalSignatureValueS;
    @FXML
    private Label isVerifiedSignatureText;

    @FXML
    public void generateSignatureHandler() {
        interpreter.execfile("elgamal.py");
        findPrivateKeys();
        findPublicKeys();
        signMessage();
    }

    private void findPrivateKeys() {
        PyObject findPrivateKeys = interpreter.get("findPrivateKeys");
        PyObject privateKeyResult = findPrivateKeys.__call__();
        List<String> privateKey = (List) privateKeyResult.__tojava__(List.class);

        privateKeyValueA.setText(privateKey.get(0));
        keyValueP.setText(privateKey.get(1));
        keyValueG.setText(privateKey.get(2));
    }

    private void findPublicKeys() {
        PyObject findPublicKeys = interpreter.get("findPublicKeys");
        PyObject publicKeyResult = findPublicKeys.__call__();
        List<String> publicKey = (List) publicKeyResult.__tojava__(List.class);
        publicKeyValueB.setText(publicKey.get(0));
    }

    private void signMessage() {
        PyObject getHashedMessageAndSignature = interpreter.get("signMessage");
        PyObject hashedMessageAndSignatureResult = getHashedMessageAndSignature.__call__(
                new PyString(convertTextToInteger(plainTextTextarea.getText())));
        List<String> hashedMessageAndSignature = (List) hashedMessageAndSignatureResult.__tojava__(List.class);
        hashedMessageLabel.setText(hashedMessageAndSignature.get(0));
        digitalSignatureValueR.setText(hashedMessageAndSignature.get(1));
        digitalSignatureValueS.setText(hashedMessageAndSignature.get(2));

    }

    @FXML
    public void verifySignatureHandler() {
        PyObject verifySignature = interpreter.get("verifySignature");
        PyObject[] args = new PyObject[5];
        args[0] = new PyString(publicKeyValueB.getText());
        args[1] = new PyString(keyValueG.getText());
        args[2] = new PyString(keyValueP.getText());
        args[3] = new PyString(digitalSignatureValueR.getText());
        args[4] = new PyString(digitalSignatureValueS.getText());
        PyObject verifySignatureResult = verifySignature.__call__(args);
        if (verifySignatureResult.__tojava__(Boolean.class).equals(true)) {
            isVerifiedSignatureText.setText("TAK");
        } else {
            isVerifiedSignatureText.setText("NIE");
        }
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

    private String convertTextToInteger(String text) {
        char[] chars = text.toCharArray();
        String result = "";
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
}
