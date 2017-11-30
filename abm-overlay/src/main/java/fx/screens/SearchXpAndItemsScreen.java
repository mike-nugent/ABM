package fx.screens;

import gameinfo.IconLoader;
import gameinfo.PlayerData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SearchXpAndItemsScreen extends VBox
{
    public SearchXpAndItemsScreen()
    {
        final TableView<PlayerData> table = new TableView<PlayerData>();

        final Label label = new Label("XP And Item Database");
        label.setFont(new Font("Arial", 20));
        final TextField searchFiled = new TextField();
        searchFiled.setFont(new Font("Arial", 20));
        // searchFiled.setPrefWidth(320);

        final Button searchBtn = new Button("", new ImageView(IconLoader.loadFxImage("search_icon.png", 30)));
        final HBox barNBtn = new HBox();
        barNBtn.setSpacing(15);
        barNBtn.getChildren().addAll(searchFiled, searchBtn);
        HBox.setHgrow(barNBtn, Priority.ALWAYS); // Give stack any extra
        barNBtn.setAlignment(Pos.CENTER_RIGHT);

        final HBox topBox = new HBox();
        topBox.setSpacing(15);
        topBox.getChildren().addAll(label, barNBtn);
        topBox.setAlignment(Pos.CENTER_LEFT);

        final VBox searchVBox = new VBox();
        searchVBox.setSpacing(15);
        searchVBox.getChildren().addAll(topBox);
        final TableColumn<PlayerData, String> nameCol = new TableColumn<PlayerData, String>("XP");

        table.getColumns().addAll(nameCol);

        this.setSpacing(5);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.getChildren().addAll(searchVBox, table);
    }
}
