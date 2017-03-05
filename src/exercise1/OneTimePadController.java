package exercise1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class OneTimePadController {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    public TextArea textFieldInput;
    public Button buttonChooseFile;
    public Button buttonEncrypt;
    public TextArea labelByteText;
    public TextArea labelByteTextHex;
    public TextArea labelKey;
    public TextArea labelCypherText;
    public Button buttonDecrypt;
    public TextArea labelDecodedText;

    private String textToEncrypt;

    private byte[] bytesFromOrgText;
    private byte[] key;
    private byte[] cyphered;

    public String byteToHex(byte b) {
        int i = b & 0xFF;
        String tmpHex = Integer.toHexString(i);
        String returnHex = "";
        if (tmpHex.length() < 2) {
            returnHex = "0" + tmpHex;
        } else {
            returnHex = tmpHex;
        }

        return returnHex;
    }

    public String printBytes(byte[] array) {
        String hexNumber = "";
        for (int k = 0; k < array.length; k++) {
            hexNumber += "0x" + byteToHex(array[k]) + " ";
        }
        return hexNumber;
    }

    private void printLabels() {
        labelByteTextHex.setText(printBytes(bytesFromOrgText));
        labelKey.setText(printBytes(key));
        labelCypherText.setText(printBytes(cyphered));
    }

    @FXML
    public void handleEncryptClick() {
        textToEncrypt = textFieldInput.getText();
        
        if (textToEncrypt.trim().length() < 1){
            showErrorAlert("Nie podałeś tekstu do tłumaczenia");
        } else {
            bytesFromOrgText = textToEncrypt.getBytes(CHARSET);
            key = generateKey(bytesFromOrgText.length);
            cyphered = encryptText(bytesFromOrgText, key);
            labelDecodedText.setText("");

            printLabels();
        }

        
    }

    private byte[] encryptText(byte[] plainText, byte[] key) {
        byte[] encryptedText = new byte[plainText.length];
        for (int i = 0; i < plainText.length; i++) {
            encryptedText[i] = (byte) (plainText[i] ^ key[i]);
        }
        return encryptedText;
    }

    @FXML
    public void handleDecryptClick() {
        if (cyphered != null && key != null && cyphered.length > 0 && key.length > 0){
            byte[] decrypted = encryptText(cyphered, key);
            labelDecodedText.setText(new String(decrypted, CHARSET));
        } else {
            showErrorAlert("Nie możesz deszyfrować");
        }
    }

    @FXML
    public void handleFileChoose() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj plik tekstowy do zaszyfrowania");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Pliki tekstowe", "*.txt"));
        
        File selectedFile = fileChooser.showOpenDialog(labelDecodedText.getScene().getWindow());
        if (selectedFile != null) {
            readFile(selectedFile);
        }
    }

    private byte[] generateKey(int length) {
        byte[] key = new SecureRandom().generateSeed(length);
        return key;
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
            textFieldInput.setText(fileContent);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger("readFile FileNotFoundException");
        } catch (IOException ex) {
            Logger.getLogger("readFile IOException");
        }
    }

    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Ostrzeżenie");
        alert.setHeaderText("Sprawdź poprawność danych");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
