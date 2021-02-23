package fr.flowarg.viplauncher.ui.components;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Cursor;
import javafx.scene.text.Font;

public interface IDefaultJFXPlayButtonUser
{
    default JFXButton getDefaultJFXPlayButton(String text)
    {
        final JFXButton button = new JFXButton(text);
        button.setFont(Font.font("Consolas", 34));
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.setStyle("-fx-text-fill: white; -fx-background-color: rgb(59, 58, 58);");
        button.setFocusTraversable(false);
        button.setMaxWidth(400d);
        button.setMinWidth(400d);
        button.setOnMouseEntered(event -> button.setCursor(Cursor.HAND));
        button.setOnMouseExited(event -> button.setCursor(Cursor.DEFAULT));
        return button;
    }
}
