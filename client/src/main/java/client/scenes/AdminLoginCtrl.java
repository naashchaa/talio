package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class AdminLoginCtrl {
    private MainCtrl mainCtrl;
    @FXML
    private PasswordField password;
    @FXML
    private Text wrongPW;

    @Inject
    public void AdminLoginCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * checks if the password is correct and sets the admin boolean in the main controller.
     */
    public void login() {
        if (this.password.getText().equals("worcestershireSauce")) {
            this.wrongPW.setVisible(false);
            this.mainCtrl.setAdmin(true);
            this.mainCtrl.hidePopUp();
        }
        else {
            this.wrongPW.setVisible(true);
        }
    }

    public void cancel() {
        this.wrongPW.setVisible(false);
        this.mainCtrl.hidePopUp();
    }
}
