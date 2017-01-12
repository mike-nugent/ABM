package fx.screens;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import triage.ExceptionHandler;

public class UpdateAvailableAlert extends Stage
{
    private final UpdateAvailableAlert me;
    private final StackPane   stage;
    final Button later = new Button("Later");
    final Button download = new Button("Download Now");
    
    public void disableDownloadLink()
    {
    	download.setVisible(false);
    	later.setText("OK");
    }

    public UpdateAvailableAlert(final String title, final String description, String currentVersion, String newVersion)
    {
        super();
        me = this;

        this.initStyle(StageStyle.TRANSPARENT);
        this.setTitle(title);

        stage = new StackPane();
        stage.setStyle("-fx-background-color: rgba(20, 120, 40, 0.9); -fx-background-radius: 10;");

        final Label customTitle = new Label(title);
        customTitle.setFont(Font.font(null, FontWeight.BOLD, 20));
        customTitle.setTextFill(Color.ALICEBLUE);
        final Label txt = new Label();
        txt.setMaxWidth(500);
        txt.setFont(Font.font(null, FontWeight.BOLD, 14));
        txt.setTextFill(Color.FLORALWHITE);

        txt.setWrapText(true);
        txt.setText(description);


        final HBox btnBox = new HBox(70);
        btnBox.setAlignment(Pos.BOTTOM_RIGHT);
        btnBox.getChildren().addAll(download, later);
        later.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setHgrow(later, Priority.ALWAYS);
        
        Label cver   = new Label("Current Version:   " + currentVersion);
        Label aver = new Label("Available Version: " + newVersion);
        
        cver.setFont(Font.font(null, FontWeight.BOLD, 16));
        aver.setFont(Font.font(null, FontWeight.BOLD, 16));
        cver.setTextFill(Color.GAINSBORO);
        aver.setTextFill(Color.GAINSBORO);

        final VBox box = new VBox();
        box.setSpacing(10);
        box.setPadding(new Insets(10));
        box.getChildren().addAll(customTitle, txt, new VBox(cver,aver), btnBox);
        stage.getChildren().add(box);

        later.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent event)
            {
                me.close();
            }
        });
        
        download.setOnAction(new EventHandler<ActionEvent>() 
        {
			
			@Override
			public void handle(ActionEvent event) 
			{
	            try 
	            {
					Desktop.getDesktop().browse(new URI("http://blindparadox.wixsite.com/asdm/download"));
				} 
	            catch (Exception e) 
	            {
	            	System.out.println("Could not open web browser");
				}

		              
				me.close();
			}
		});

        final Scene scene = new Scene(stage);
        scene.setFill(Color.TRANSPARENT);
        this.setAlwaysOnTop(true);
        this.setScene(scene);
    }
}
