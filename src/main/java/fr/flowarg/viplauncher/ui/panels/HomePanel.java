package fr.flowarg.viplauncher.ui.panels;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.viplauncher.VIPLauncher;
import fr.flowarg.viplauncher.auth.mojang.Auth;
import fr.flowarg.viplauncher.auth.mojang.exceptions.AuthenticationUnavailableException;
import fr.flowarg.viplauncher.ui.PanelManager;
import fr.flowarg.viplauncher.ui.components.IDefaultJFXPlayButtonUser;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class HomePanel extends AbstractPanel implements IProgressCallback, IDefaultJFXPlayButtonUser
{
    private static HomePanel instance;

    private final Label downloadStatus = new Label("Download status");
    private String step = "Idle";

    public HomePanel()
    {
        instance = this;
    }

    @Override
    public void init(PanelManager panelManager)
    {
        super.init(panelManager);
        final VIPLauncher launcher = panelManager.getVIPLauncher();

        final boolean[] clicked = {false};
        final JFXButton play = this.getDefaultJFXPlayButton("Jouer");

        play.setOnMouseClicked(event -> {
            if(!clicked[0])
            {
                clicked[0] = true;
                play.setCursor(Cursor.DEFAULT);
                new Thread(() -> {
                    try
                    {
                        launcher.update();
                        launcher.launch();
                    } catch (Exception e)
                    {
                        launcher.getLogger().printStackTrace(e);
                    }
                }).start();
            }
        });

        final JFXSlider slider = new JFXSlider(0, 10d, Double.parseDouble(panelManager.getVIPLauncher().getConfig().get("ram")));
        final MaterialDesignIconView disconnect = new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_OFF);

        disconnect.setFill(Color.RED);
        disconnect.setSize("35px");
        disconnect.setOnMouseEntered(event -> disconnect.setCursor(Cursor.HAND));
        disconnect.setOnMouseExited(event -> disconnect.setCursor(Cursor.DEFAULT));
        disconnect.setOnMouseClicked(event -> {
            try
            {
                Auth.invalidate(launcher.getConfig().get("mojang_accessToken"), launcher.getConfig().get("mojang_clientToken"));
                panelManager.showPanel(new LoginPanel());
            } catch (AuthenticationUnavailableException e)
            {
                launcher.getLogger().printStackTrace(e);
            }
        });

        slider.setIndicatorPosition(JFXSlider.IndicatorPosition.LEFT);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setMaxHeight(600d);
        slider.setMinHeight(300d);
        slider.setOnMouseReleased(event -> launcher.getConfig().set("ram", String.valueOf(slider.getValue())));

        this.downloadStatus.setTextFill(Color.YELLOW);

        this.setCanTakeAllSize(play);
        this.setCanTakeAllSize(disconnect);
        this.setCanTakeAllSize(this.downloadStatus);
        this.setCanTakeAllSize(slider);
        this.setCenterH(play);
        this.setCenterV(play);
        this.setCenterH(this.downloadStatus);
        this.setCenterV(slider);

        this.setTop(disconnect);
        this.setRight(slider);
        this.setTop(this.downloadStatus);
        this.setRight(disconnect);

        disconnect.setTranslateX(-30d);
        disconnect.setTranslateY(30d);
        slider.setTranslateX(-70d);
        this.downloadStatus.setTranslateY(10d);

        this.layout.getChildren().addAll(play, disconnect, this.downloadStatus, slider);
    }

    public static HomePanel getHomePanel()
    {
        return instance;
    }

    @Override
    public String getName()
    {
        return "HomePanel";
    }

    @Override
    public void init(ILogger logger)
    {
        logger.info("Initialized VIPLauncher calllback.");
    }

    @Override
    public void step(Step step)
    {
        Platform.runLater(() -> {
            switch (step)
            {
                case READ:
                    this.step = "Fetching data... (Please Wait)";
                    break;
                case END:
                    this.step = "Finished!";
                    break;
                case MODS:
                    this.step = "Installing mods...";
                    break;
                case FORGE:
                    this.step = "Installing Forge... (Please Wait)";
                    break;
                case DL_LIBS:
                    this.step = "Downloading libraries...";
                    break;
                case DL_ASSETS:
                    this.step = "Downloading assets...";
                    break;
                case PREREQUISITES:
                    this.step = "Installing FlowUpdater plugins... (Please Wait)";
                    break;
                case EXTRACT_NATIVES:
                    this.step = "Extracting natives... (Please Wait)";
                    break;
            }
            this.downloadStatus.setText(this.step);
        });
    }

    @Override
    public void update(long downloaded, long max)
    {
        Platform.runLater(() -> this.downloadStatus.setText(this.step + " " + (downloaded * 100) / max + "%"));
    }
}
