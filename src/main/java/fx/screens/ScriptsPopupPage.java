package fx.screens;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ScriptsPopupPage extends PopupStage
{
	static ScriptsPopupPage instance;
	TabPane pane = new TabPane();
	private VBox scripts;
    public ScriptsPopupPage()
    {
        super("Custom ASDM Scripts");
     
        instance = this;
        Tab scriptsPane = new Tab("Scripts");
        Tab soundsPane = new Tab("Sounds");
        Tab alertsPane = new Tab("Alert Options");
        
        scriptsPane.setClosable(false);
        soundsPane.setClosable(false);
        alertsPane.setClosable(false);

        scriptsPane.setStyle("-fx-font-size: 18px; ");
        soundsPane.setStyle("-fx-font-size: 18px; ");
        alertsPane.setStyle("-fx-font-size: 18px; ");

        
        pane.getTabs().addAll(scriptsPane, soundsPane, alertsPane);
        
        this.setWidth(1200);
        
        Button addNewBtn = new Button("Create New Script");
        addNewBtn.setStyle("-fx-font-size: 18px; ");

        
        ScrollPane p = new ScrollPane();
        VBox spView = new VBox();
        spView.setSpacing(10);
        spView.setPadding(new Insets(10, 10, 10, 10));
        spView.setAlignment(Pos.CENTER_LEFT);
        
        scripts = new VBox();
        scripts.setSpacing(10);
        scripts.setPadding(new Insets(10, 10, 10, 10));
        
        spView.getChildren().addAll(scripts, addNewBtn);
        p.setContent(spView);
        
        addNewBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				scripts.getChildren().add(new ScriptBar());
			}
		});
        
        scriptsPane.setContent(p);
        
        stage.getChildren().add(pane);
    }
    
	public static void deleteScript(ScriptBar bar) 
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Delete Script");
		alert.setHeaderText("Delete this script?");
	//	alert.setContentText("T");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			instance.scripts.getChildren().remove(bar);
		}
	}
}
