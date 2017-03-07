/*

    KRYPTOGRAFIA - ZESTAW III
        ZADANIE 1 - Szyfrowanie/deszyfrowanie danych wykorzystując algorytm
            One-time pad

        AUTORZY:
            Krystian Szabat 195727
            Marcin Szałek 195729
            Monika Stobieniecka 195718

*/

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
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class OneTimePadController {

    // Typ kodowania znaków używany przy dekodowaniu i kodowaniu tekstu
    private static final Charset CHARSET = Charset.forName("UTF-8");

    // Zmienne związane z GUI
    public TextArea textFieldInput;
    public Button buttonChooseFile;
    public Button buttonEncrypt;
    public TextArea labelByteText;
    public TextArea labelByteTextHex;
    public TextArea labelKey;
    public TextArea labelCypherText;
    public Button buttonDecrypt;
    public TextArea labelDecodedText;

    // Zmienne przechowujace bajtowa reprezentacje tekstu do zaszyfrowania, klucza i zaszyfrowanego
    private byte[] bytesFromOrgText;
    private byte[] key;
    private byte[] cyphered;


    /**
     * Funkcja dokonujaca konwersji pomiedzy liczbą typu Byte do Stringa w formie HEX
     * @param b Liczba do konwersji
     * @return Wartosc liczby wyspisana w formie Hex
     */
    public String byteToHex(byte b) {
        int i = b & 0xFF;
        String tmpHex = Integer.toHexString(i);
        String returnHex = "";
        
        // Skonwertowana liczba zawsze w dwoch znakach
        if (tmpHex.length() < 2) {
            returnHex = "0" + tmpHex;
        } else {
            returnHex = tmpHex;
        }

        return returnHex;
    }

    /**
     * Zamiana strina z ciagiem liczb w hex na tablice bajtow
     * @param key String z liczbami hex oddzielonymi spacja
     * @return Tablica bajtow
     */
    private byte[] stringToBytes(String key){
        String[] arr = key.split(" ");
        byte[] bytes = new byte[arr.length];
        for(int i=0; i<arr.length; i++) {
            bytes[i] = (byte) Integer.parseInt(arr[i],16);
        }
        return bytes;
    }

    /**
     * Funkcja wypisujaca tablice bajtow w postaci ciagu wartości hex
     * @param array tablica byte z liczbami do konwersji
     * @return Ciag znakow w postaci Hex
     */
    public String printBytes(byte[] array) {
        String hexNumber = "";
        for (int k = 0; k < array.length; k++) {
            hexNumber += byteToHex(array[k]) + " ";
        }
        return hexNumber;
    }

    /**
     * Wyswietlenie klucza, zaszyfrowanego tekstu i tekstu w Hex w GUI
     */
    private void printLabels() {
        labelByteTextHex.setText(printBytes(bytesFromOrgText));
        labelKey.setText(printBytes(key));
        labelCypherText.setText(printBytes(cyphered));
    }

    /**
     * Handler do klikniecia na przycisk od szyfrowania
     * Sprawdza poprawnosc danych, zarzadza wygenereowaniem klucza
     * Zaszyfrowaniem tekstu
     */
    @FXML
    public void handleEncryptClick() {
        String textToEncrypt = textFieldInput.getText();
        //stringToBytes("sada");

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

    /**
     * Szyfruje tekst, dokonujac operacji XOR na każdym bitów tekstu wejściowego i klucza
     * @param plainText Tekst bazowy do szyfrowania/deszyfrowaniu
     * @param key Klucz używany w szyfrowaniu/deszyfrowaniu
     * @return Tablica byte z zaszyfrowanymi/deszyfrowanymi danymi
     */
    private byte[] encryptText(byte[] plainText, byte[] key) {
        byte[] encryptedText = new byte[plainText.length];
        for (int i = 0; i < plainText.length; i++) {
            encryptedText[i] = (byte) (plainText[i] ^ key[i]);
        }
        return encryptedText;
    }

    /**
     * Handler do klikniecia na przycisk od deszyfrowania
     * Sprawdza poprawnosc danych, zarzadza deszyfrowaniem danych
     * Do deszyfrowania używa jako tekstu bazowego tekstu zaszyfrowanego
     * i klucza wygenerowanego przy szyfrowaniu
     */
    @FXML
    public void handleDecryptClick() {
        cyphered = stringToBytes(labelCypherText.getText());
        key = stringToBytes(labelKey.getText());
        if (cyphered != null && key != null && cyphered.length > 0 && key.length > 0 && cyphered.length == key.length){
            byte[] decrypted = encryptText(cyphered, key);
            labelDecodedText.setText(new String(decrypted, CHARSET));
        } else {
            showErrorAlert("Nie możesz deszyfrować");
        }
    }

    /**
     * Handler do klikniecia na przycisk od wyboru pliku
     * Otwiera okna do wyboru pliku tekstowego, po wybraniu pliku
     * przekazuje kontrole do funkcji odczytujacej plik
     */
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

    /**
     * Funkcja generujaca losowy klucz o podanej dlugosci
     * @param length Pozadana dlugosc klucza do wygenerowania
     * @return Tablica byte zawierajaca klucz
     */
    private byte[] generateKey(int length) {
        byte[] key = new SecureRandom().generateSeed(length);
        return key;
    }

    
    /**
     * Odczyt zawartosci pliku tekstowego wraz z wyswietleniem jej w GUI
     * @param selectedFile wybrany plik w oknie wyboru
     */
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

    /**
     * Wyswietlenie okna z komunikatem bledu podanym przez uzytkownika
     * @param errorMessage Komunikat bledu do wyswietlenia
     */
    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Ostrzeżenie");
        alert.setHeaderText("Sprawdź poprawność danych");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
