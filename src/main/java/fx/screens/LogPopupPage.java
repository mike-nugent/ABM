package fx.screens;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import logreader.AionLogResponder;

public class LogPopupPage extends PopupStage
{
    private final LogTextArea textArea;
    List<String>              recentLinesReferecnce = AionLogResponder.getRecentLines();
    String                    lastLine              = "";
    private final CheckBox    showTimestamps;
    private final CheckBox    pauseReader;
    private final Label       counter;

    @SuppressWarnings("unchecked")
    public LogPopupPage()
    {
        super("Active Chat.log File");

        textArea = new LogTextArea();
        textArea.setEditable(false);

        // Or you can set it by setStyle()
        textArea.setStyle("text-area-background: green;");

        final VBox vbox = new VBox();

        showTimestamps = new CheckBox("Show Timestamps");
        showTimestamps.setSelected(true);
        showTimestamps.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(final ObservableValue<? extends Boolean> ov, final Boolean old_val,
                    final Boolean new_val)
            {
                lastLine = "some randome string";
                updateScripts();
            }
        });

        pauseReader = new CheckBox("Pause Log Parsing");
        pauseReader.setSelected(false);

        final HBox hbox = new HBox();
        hbox.setPadding(new Insets(15));
        hbox.setSpacing(15);
        hbox.getChildren().addAll(showTimestamps, pauseReader);

        final VBox lineCountBox = new VBox();
        lineCountBox.setAlignment(Pos.CENTER_RIGHT);
        lineCountBox.setPadding(new Insets(10));
        counter = new Label("0/" + AionLogResponder.MAX_LINES_TO_SAVE);
        HBox.setHgrow(lineCountBox, Priority.ALWAYS); // Give stack any extra
                                                      // space

        lineCountBox.getChildren().addAll(new Label("lines shown"), counter);

        final HBox topBarWrapper = new HBox();
        topBarWrapper.getChildren().addAll(hbox, lineCountBox);

        VBox.setVgrow(textArea, Priority.ALWAYS);
        final Button saveBtn = new Button("Save To File...");

        saveBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(final ActionEvent e)
            {

                final FileChooser fileChooser = new FileChooser();
                final ExtensionFilter extFilter = new ExtensionFilter("Aion Chat Log file (*.log)", "*.log");
                fileChooser.getExtensionFilters().add(extFilter);
                fileChooser.setTitle("Save Log File");
                final File file = fileChooser.showSaveDialog(null);
                if (file != null)
                {
                    try
                    {
                        synchronized (recentLinesReferecnce)
                        {
                            final FileWriter writer = new FileWriter(file);
                            for (final String str : recentLinesReferecnce)
                            {
                                writer.write(str + "\n");
                            }
                            writer.close();
                        }
                    }
                    catch (final IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });

        saveBtn.setPadding(new Insets(10));
        vbox.getChildren().addAll(topBarWrapper, textArea, saveBtn);

        stage.getChildren().add(vbox);

        new AnimationTimer()
        {
            @Override
            public void handle(final long now)
            {
                updateScripts();
            }
        }.start();
    }

    protected synchronized void updateScripts()
    {
        if (pauseReader.isSelected())
        {
            return;
        }

        try
        {
            final int size = recentLinesReferecnce.size();
            if (size > 0)
            {
                // Since this list gets modified in another thread, need to
                // safely read it
                synchronized (recentLinesReferecnce)
                {
                    final String lastEntry = recentLinesReferecnce.get(size - 1);
                    if (lastEntry != null && !lastEntry.equals(lastLine))
                    {
                        counter.setText(size + "/" + AionLogResponder.MAX_LINES_TO_SAVE);
                        lastLine = lastEntry;
                        String out = "";
                        for (final String s : recentLinesReferecnce)
                        {
                            if (showTimestamps.isSelected())
                            {
                                out += (s + "\n");
                            }
                            else
                            {
                                try
                                {
                                    final String withoutTimestamp = s.substring(s.indexOf(" : ") + 3);
                                    out += (withoutTimestamp + "\n");
                                }
                                catch (final Exception e)
                                {
                                    System.err.println("could not remove timestamp from:" + s);
                                    out += (s + "\n");
                                }
                            }
                        }

                        textArea.setMessage(out);
                    }
                }
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

}
