package fr.flowarg.viplauncher.ui.panels;

import fr.flowarg.viplauncher.ui.PanelManager;
import fr.flowarg.viplauncher.ui.components.ITakePlace;
import javafx.scene.layout.GridPane;

public interface IPanel extends ITakePlace
{
    void init(PanelManager panelManager);
    GridPane getLayout();
    void onShow();
    String getName();
}
