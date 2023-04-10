package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AdminLoginCtrl {
    private MainCtrl mainCtrl;
    private ApplicationOverviewCtrl appCtrl;
    @FXML
    private PasswordField password;
    @FXML
    private Text wrongPW;

    @Inject
    public AdminLoginCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * checks if the password is correct and sets the admin boolean in the main controller.
     */
    public void login() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("config/adminPassword"));
        String password = scanner.nextLine();
        if (this.password.getText().equals(password)) {
            this.wrongPW.setVisible(false);
            this.mainCtrl.setAdmin(true);
            this.appCtrl.loadExistingBoards();
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

    public void setAppCtrl(ApplicationOverviewCtrl appOverviewCtrl) {
        this.appCtrl = appOverviewCtrl;
    }
}
