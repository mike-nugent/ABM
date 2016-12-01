package fx.screens;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.PopupStage;

public class PlayersPopupPage extends PopupStage
{

    public PlayersPopupPage()
    {
        super("Shugo Database");

        final TableView table = new TableView();

        final Scene scene = new Scene(new Group());
        final Label label = new Label("Player List");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        final TableColumn nameCol = new TableColumn("Name");
        final TableColumn serverCol = new TableColumn("Server");
        final TableColumn raceCol = new TableColumn("Race");
        final TableColumn classCol = new TableColumn("Class");

        table.getColumns().addAll(nameCol, serverCol, raceCol, classCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table);

        stage.getChildren().addAll(vbox);
    }
}
