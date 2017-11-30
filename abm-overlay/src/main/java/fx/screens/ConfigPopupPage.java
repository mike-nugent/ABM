package fx.screens;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ConfigPopupPage extends PopupStage
{
    private final TabPane _tabPane = new TabPane();
    EnableChatLogScreen   _chatScreen;

    public ConfigPopupPage()
    {
        super("Configure Aion Battle Meter");

        final Tab characterTab = new Tab("Character Settings");
        characterTab.setStyle("-fx-font-size: 18px;");
        characterTab.setClosable(false);
        characterTab.setContent(new CharacterConfigScreen(this));

        final Tab loggingTab = new Tab("Logging Setting");
        loggingTab.setStyle("-fx-font-size: 18px; ");
        loggingTab.setClosable(false);
        _chatScreen = new EnableChatLogScreen(this);
        loggingTab.setContent(_chatScreen);

        _tabPane.getTabs().addAll(characterTab, loggingTab);
        _tabPane.setTabMaxHeight(40);

        this.setAlwaysOnTop(false);

        stage.getChildren().add(_tabPane);
    }

    public void saveCharacterCompleted()
    {
        _tabPane.getSelectionModel().select(1);
        _chatScreen.checkFiles();
    }

    public void showLoggingSettings()
    {
        _tabPane.getSelectionModel().select(1);
    }

}
