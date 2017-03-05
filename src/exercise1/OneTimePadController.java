package exercise1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import javafx.scene.control.TextArea;

public class OneTimePadController {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    public TextArea textFieldInput;
    public Button buttonChooseFile;
    public Label labelFilePath;
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
        if (tmpHex.length() < 2){
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

    private void printLabels(){
        labelByteTextHex.setText(printBytes(bytesFromOrgText));
        labelKey.setText(printBytes(key));
        labelCypherText.setText(printBytes(cyphered));
    }
    
    @FXML
    public void handleEncryptClick() {
        textToEncrypt = textFieldInput.getText();
        
        bytesFromOrgText = textToEncrypt.getBytes(CHARSET);
        key = generateKey(bytesFromOrgText.length);
        cyphered = encryptText(bytesFromOrgText, key);
        labelDecodedText.setText("");
        
        printLabels();
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
        byte[] decrypted = encryptText(cyphered, key);
        labelDecodedText.setText(new String(decrypted, CHARSET));
    }

    private byte[] generateKey(int length) {
        byte[] key = new SecureRandom().generateSeed(length);
        return key;
    }
}
