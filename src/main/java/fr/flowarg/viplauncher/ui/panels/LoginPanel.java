package fr.flowarg.viplauncher.ui.panels;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import fr.flowarg.viplauncher.VIPLauncher;
import fr.flowarg.viplauncher.auth.microsoft.MicrosoftAuthentication;
import fr.flowarg.viplauncher.ui.PanelManager;
import fr.flowarg.viplauncher.ui.components.IDefaultJFXPlayButtonUser;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

public class LoginPanel extends AbstractPanel implements IDefaultJFXPlayButtonUser
{
    @Override
    public void init(PanelManager panelManager)
    {
        super.init(panelManager);
        final VIPLauncher launcher = panelManager.getVIPLauncher();

        final JFXTextField emailField = new JFXTextField(launcher.getConfig().get("mojang_email", ""));
        emailField.setFocusTraversable(false);
        emailField.setMinSize(400, 40);
        emailField.setMaxSize(400, 40);
        emailField.setFocusColor(Color.WHITE);
        emailField.setPromptText("E-Mail");
        emailField.setUnFocusColor(Color.BLUEVIOLET);
        emailField.setStyle("-fx-text-fill: white;");
        emailField.setFont(Font.font("Roboto", 18));
        emailField.setOnMouseClicked(event -> emailField.setStyle("-fx-text-fill: white;"));

        final JFXPasswordField passwordField = new JFXPasswordField();
        passwordField.setFocusTraversable(false);
        passwordField.setMinSize(400, 40);
        passwordField.setMaxSize(400, 40);
        passwordField.setFocusColor(Color.WHITE);
        passwordField.setUnFocusColor(Color.BLUEVIOLET);
        passwordField.setPromptText("Mot de passe");
        passwordField.setStyle("-fx-text-fill: white;");
        passwordField.setFont(Font.font("Roboto", 18));
        passwordField.setOnMouseClicked(event -> passwordField.setStyle("-fx-text-fill: white;"));

        final JFXButton connect = this.getDefaultJFXPlayButton("Se connecter");
        connect.setOnMouseClicked(event -> {
            connect.setCursor(Cursor.DEFAULT);
            if(launcher.auth(emailField.getText(), passwordField.getText())) panelManager.showPanel(new HomePanel());
            else
            {
                emailField.setFocusTraversable(true);
                emailField.setStyle("-fx-text-fill: #f62727;");
                passwordField.setStyle("-fx-text-fill: #f62727;");
            }
        });

        final JFXButton microsoft = new JFXButton("Microsoft");
        microsoft.setFont(Font.font("Consolas", 34));
        microsoft.setButtonType(JFXButton.ButtonType.RAISED);
        microsoft.setStyle("-fx-text-fill: white; -fx-background-color: rgb(16, 137, 85);");
        microsoft.setFocusTraversable(false);
        microsoft.setMaxWidth(400d);
        microsoft.setMinWidth(400d);
        microsoft.setOnMouseEntered(event -> microsoft.setCursor(Cursor.HAND));
        microsoft.setOnMouseExited(event -> microsoft.setCursor(Cursor.DEFAULT));
        microsoft.setOnMouseClicked(event -> {
            microsoft.setCursor(Cursor.DEFAULT);
            final WebView webView = new WebView();
            final WebEngine webEngine = webView.getEngine();
            webEngine.load("https://login.live.com/oauth20_authorize.srf" +
                    "?client_id=00000000402b5328" +
                    "&response_type=code" +
                    "&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL" +
                    "&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf");
            webEngine.setJavaScriptEnabled(true);
            webView.setPrefHeight(406);
            webView.setPrefWidth(406);

            webEngine.getHistory().getEntries().addListener((ListChangeListener<WebHistory.Entry>) c -> {
                if (c.next() && c.wasAdded()) {
                    for (final WebHistory.Entry entry : c.getAddedSubList()) {
                        if (entry.getUrl().startsWith("https://login.live.com/oauth20_desktop.srf?code=")) {
                            final String authCode = entry.getUrl().substring(entry.getUrl().indexOf("=") + 1, entry.getUrl().indexOf("&"));
                            this.layout.getChildren().removeAll(webView);
                            new MicrosoftAuthentication().acquireAccessToken(authCode);
                            panelManager.showPanel(new HomePanel());
                        }
                    }
                }
            });
            this.layout.getChildren().addAll(webView);
        });

        this.setCanTakeAllSize(emailField);
        this.setCanTakeAllSize(passwordField);
        this.setCanTakeAllSize(connect);
        this.setCanTakeAllSize(microsoft);

        this.setCenterH(emailField);
        this.setCenterH(passwordField);
        this.setCenterH(connect);
        this.setCenterH(microsoft);
        this.setTop(emailField);
        this.setTop(passwordField);
        this.setTop(connect);
        this.setTop(microsoft);

        emailField.setTranslateY(170d);
        passwordField.setTranslateY(270d);
        connect.setTranslateY(420d);
        microsoft.setTranslateY(580d);

        this.layout.getChildren().addAll(emailField, passwordField, connect, microsoft);
    }

    @Override
    public String getName()
    {
        return "LoginPanel";
    }
}
