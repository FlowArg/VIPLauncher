package fr.flowarg.viplauncher.ui;

import fr.flowarg.flowcompat.Platform;
import fr.flowarg.viplauncher.Main;
import fr.flowarg.viplauncher.VIPLauncher;
import fr.flowarg.viplauncher.ui.panels.IPanel;
import fr.flowarg.viplauncher.ui.panels.includes.TopPanel;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// Adapted version of the panel manager made by Arinonia for my JavaFX projects.
public class PanelManager
{
    private final VIPLauncher vipLauncher;
    private final Stage stage;
    private final IPanel topPanel;
    private final GridPane centerPanel;

    public PanelManager(VIPLauncher vipLauncher, Stage stage)
    {
        this.vipLauncher = vipLauncher;
        this.stage = stage;
        this.topPanel = new TopPanel();
        this.centerPanel = new GridPane();
    }

    public void init()
    {
        final boolean linux = Platform.isOnLinux();
        this.stage.getIcons().add(new Image(Main.class.getResourceAsStream("/assets/logo.png")));
        this.stage.setTitle("VIPLauncher");
        this.stage.setMinWidth(1280);
        this.stage.setMinHeight(720);
        this.stage.setWidth(1280);
        this.stage.setHeight(720);
        this.stage.initStyle(linux ? StageStyle.DECORATED : StageStyle.UNDECORATED);
        this.stage.show();
        this.stage.centerOnScreen();

        final GridPane layout = new GridPane();
        this.setBackground(layout);
        this.stage.setScene(new Scene(layout));

        final RowConstraints topPanelRules = new RowConstraints();
        topPanelRules.setValignment(VPos.TOP);
        topPanelRules.setMaxHeight(27);
        topPanelRules.setMinHeight(27);

        layout.getRowConstraints().addAll(topPanelRules, new RowConstraints());
        if(!linux)
        {
            layout.add(this.topPanel.getLayout(), 0, 0);
            this.topPanel.init(this);
        }

        layout.add(this.centerPanel, 0, linux ? 0 : 1);
        GridPane.setHgrow(this.centerPanel, Priority.ALWAYS);
        GridPane.setVgrow(this.centerPanel, Priority.ALWAYS);
    }

    public void showPanel(IPanel panel)
    {
        this.vipLauncher.getLogger().debug("Opening : " + panel.getName());
        this.centerPanel.getChildren().clear();
        this.centerPanel.getChildren().add(panel.getLayout());
        panel.init(this);
        panel.onShow();
    }

    public void setBackground(GridPane layout)
    {
        layout.setStyle("-fx-background-color: rgb(28,27,27);" + "-fx-backgound-repeat: skretch;" + "-fx-backgound-position: center;" + "-fx-background-size: cover;");
    }

    public Stage getStage()
    {
        return this.stage;
    }

    public VIPLauncher getVIPLauncher()
    {
        return this.vipLauncher;
    }
}
