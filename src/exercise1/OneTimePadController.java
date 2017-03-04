package exercise1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    @FXML
    public void handleEncryptClick() {
        byte[] text = textFieldInput.getText().getBytes();
        labelByteText.setText(new String(text));
    }
}
