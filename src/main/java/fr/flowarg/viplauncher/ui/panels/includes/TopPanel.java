package fr.flowarg.viplauncher.ui.panels.includes;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.flowarg.viplauncher.Main;
import fr.flowarg.viplauncher.ui.PanelManager;
import fr.flowarg.viplauncher.ui.panels.AbstractPanel;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TopPanel extends AbstractPanel
{
    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void init(PanelManager panelManager)
    {
        super.init(panelManager);
        final Stage stage = panelManager.getStage();

        this.layout.setStyle("-fx-background-color: rgb(31,35,37);");

        final GridPane topBarButton = new GridPane();
        final Label title = new Label();

        this.layout.getChildren().add(topBarButton);
        this.layout.getChildren().add(title);
        title.setFont(Font.font("Consolas", FontWeight.THIN, FontPosture.REGULAR, 19.0f));
        title.setStyle("-fx-text-fill: white;");
        title.setText("VIPLauncher");

        this.setCenterH(title);
        topBarButton.setMinWidth(100);
        topBarButton.setMaxWidth(100);

        this.setCanTakeAllSize(topBarButton);
        this.setRight(topBarButton);

        final MaterialDesignIconView close = new MaterialDesignIconView(MaterialDesignIcon.CLOSE);
        final MaterialDesignIconView resize = new MaterialDesignIconView(MaterialDesignIcon.WINDOW_MAXIMIZE);
        final MaterialDesignIconView minimize = new MaterialDesignIconView(MaterialDesignIcon.WINDOW_MINIMIZE);
        final Image iconImage = new Image(Main.class.getResourceAsStream("/assets/logo.png"));
        final ImageView icon = new ImageView(iconImage);

        GridPane.setVgrow(close, Priority.ALWAYS);
        GridPane.setVgrow(resize, Priority.ALWAYS);
        GridPane.setVgrow(minimize, Priority.ALWAYS);
        GridPane.setVgrow(icon, Priority.ALWAYS);

        icon.setFitHeight(27);
        icon.setFitWidth(27);

        this.setLeft(icon);
        icon.setTranslateX(7);
        icon.setTranslateY(1);

        this.layout.getChildren().add(icon);

        close.setFill(Color.WHITE);
        close.setOpacity(0.70d);
        close.setSize("30px");
        close.setOnMouseEntered(event -> {
            close.setFill(Color.RED);
            close.setOpacity(1.0d);
        });
        close.setOnMouseExited(event -> {
            close.setFill(Color.WHITE);
            close.setOpacity(0.70d);
        });
        close.setOnMouseClicked(event -> System.exit(0));
        close.setTranslateX(65);

        resize.setFill(Color.WHITE);
        resize.setOpacity(0.70d);
        resize.setSize("28px");
        resize.setOnMouseEntered(event -> resize.setOpacity(1.0d));
        resize.setOnMouseExited(event -> resize.setOpacity(0.70d));
        resize.setOnMouseClicked(event -> stage.setMaximized(!stage.isMaximized()));
        resize.setTranslateX(38);

        minimize.setFill(Color.WHITE);
        minimize.setOpacity(0.70d);
        minimize.setSize("30px");
        minimize.setOnMouseEntered(event -> minimize.setOpacity(1.0d));
        minimize.setOnMouseExited(event -> minimize.setOpacity(0.70d));
        minimize.setOnMouseClicked(event -> stage.setIconified(true));
        minimize.setTranslateX(7);

        this.layout.setOnMousePressed(event -> {
            if(!stage.isMaximized())
            {
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            }
        });

        this.layout.setOnMouseClicked(event -> {
            if(!stage.isMaximized())
            {
                stage.setOpacity(0.75d);
                if(event.getClickCount() == 2)
                    stage.setMaximized(true);
            }
            else
            {
                if(event.getClickCount() == 2)
                    stage.setMaximized(false);
            }
        });

        this.layout.setOnMouseReleased(event -> stage.setOpacity(1d));

        this.layout.setOnMouseDragged(event -> {
            if(!stage.isMaximized())
            {
                stage.setOpacity(0.75d);
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });

        topBarButton.getChildren().addAll(close, resize, minimize);
    }

    // Unused
    @Override
    public String getName()
    {
        return "TopPanel";
    }
}
