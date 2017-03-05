package exercise1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * Created by szale_000 on 2017-03-04.
 */
public class OneTimePadController {
    public TextField textFieldInput;
    public Button buttonChooseFile;
    public Label labelFilePath;
    public Button buttonEncrypt;
    public Label labelByteText;
    public Label labelKey;
    public Label labelCypherText;
    public Button buttonDecrypt;
    public Label labelDecodedText;

    private byte[] key;
    private byte[] cyphered;

    //TODO: Wczytywanie z pliku

    @FXML
    public void handleEncryptClick() {
        try {
            byte[] plainText = textFieldInput.getText().getBytes("utf-8");
            key = generateKey(plainText.length);
            cyphered = encryptText(plainText, key);

            //TODO: Tutaj jest pokazany problem:
            //jak mamy bajtowy tekst zaszyfrowany to on ma określoną ilość bajtów
            //jeśli te bajty zapisujemy do stringa a potem znowu do tablicy bajtów to ta wartość się zmienia
            //Innymi słowy: póki co opercja byte[]->string->byte[] jest nieodwracalna
            //Dlatego zapisujemy tekst zaszyfrowany w polu prywatnym,, a nie w labelu widoku.
            String x = "" + cyphered;
            byte[] y = x.getBytes("UTF-8");

            //Poza tym te tablice bajtów wyświetlają się tak jak się wyświetlają, jak coś dasz radę z tym zrboić to super
            labelByteText.setText(new String(plainText, "UTF-8"));
            labelKey.setText(new String(key, "UTF-8"));
            labelCypherText.setText(new String(cyphered, "UTF-8"));
        } catch (Exception e) {

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
        try {
            byte[] decrypted = encryptText(cyphered, key);

            labelDecodedText.setText(new String(decrypted, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private byte[] generateKey(int length) {
        byte[] key = new SecureRandom().generateSeed(length);
        return key;
    }
}
