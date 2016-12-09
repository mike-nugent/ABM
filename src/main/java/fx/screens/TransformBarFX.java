package fx.screens;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import gameinfo.IconLoader;
import gameinfo.PlayerData;
import gameinfo.Race;
import gameinfo.Rank;
import gameinfo.Server;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import utils.Times;

public class TransformBarFX extends HBox
{
    private static final long TEN_MINUTES = Times.TEN_MIUTES;
    public static final long  TWO_HOURS   = Times.ONE_HOUR*2;
    private String            _name;
    private Label      		  _timeTxt;
    private Date              _transformStartTime;
    private Rank              _rank;
    private ImageView   	   _icon;
	private Server			  _server;
	private Race _race;
	
	private Label nameLabel;
	private Label serverLabel;
	private Label rankLabel;
    public static int TIME_SIZE = 17;

    public TransformBarFX()
    {
        this.setSpacing(10);
        this.setPadding(new Insets(0, 7, 0, 0));
        this.setAlignment(Pos.CENTER_LEFT);
    }

    public void setInfo(final PlayerData newXform)
    {
    	_icon = new ImageView(IconLoader.loadFxImage(newXform.clazz.getIconName(), 26));
        _name = newXform.name;
        _transformStartTime = newXform.dateTime;
        _timeTxt = getTimeLabel();
        _rank = newXform.rank;
        _server = newXform.server;
        _race = newXform.race;
        
        nameLabel = new Label(_name+"-"+_server.getServerString());
        serverLabel = new Label(_server.getServerString());
        rankLabel = new Label(_rank.getRankAcronym());
        
        if(Race.Asmodian.equals(_race))
        {
            updateLabel(nameLabel, Color.DODGERBLUE, 15, "bold");
        }
        else
        {
            updateLabel(nameLabel, Color.LIMEGREEN, 15, "bold");
        }
        updateLabel(serverLabel, Color.WHITE, 15, "bold");
        
        updateRankColor(_rank);

        BorderPane box = new BorderPane();
        HBox left = new HBox();
        HBox center = new HBox();

        left.setPrefWidth(140);
        //center.setPrefWidth(50);

        left.setAlignment(Pos.CENTER_LEFT);
        center.setAlignment(Pos.CENTER_LEFT);

        
        this.getChildren().addAll(_timeTxt, _icon);
        left.getChildren().add(nameLabel);
        center.getChildren().add(rankLabel);

        box.setLeft(left);
        box.setCenter(center);
        this.getChildren().add(box);
    }
    
    private void updateRankColor(Rank rank) 
    {
    	if(Rank.I.equals(rank))
    	{
            updateLabel(rankLabel, Color.web("bbbbbb"), 15, "bold");
    	}
    	else if(Rank.II.equals(rank))
    	{
            updateLabel(rankLabel, Color.web("cccccc"), 15, "bold");
    	} 
    	else if(Rank.III.equals(rank))
    	{
            updateLabel(rankLabel,Color.web("dddddd"), 15, "bold");
    	}    	
    	else if(Rank.IV.equals(rank))
    	{
            updateLabel(rankLabel,Color.GOLDENROD, 15, "bold");
    	}    	
    	else if(Rank.V.equals(rank))
    	{
            updateLabel(rankLabel, Color.GOLD, 15, "bold");
    	}
    	else
    	{
            updateLabel(rankLabel, Color.GRAY, 15, "bold");
    	}		
	}

	private void updateLabel(Label label, Color c, int size, String font_weight) 
    {
    	label.setTextFill(c);
    	label.setFont(new Font(size));
    	label.setStyle("-fx-font-weight: "+font_weight+";");
	}


    private Label getTimeLabel()
    {
        final Label l = new Label("10:00");
        return l;
    }

    public String getMinSec(final long millisec)
    {
        final String ms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisec)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisec)),
                TimeUnit.MILLISECONDS.toSeconds(millisec)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
        return ms;
    }

    public String getHrMinSec(final long millisec)
    {
        final String ms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisec),
                TimeUnit.MILLISECONDS.toMinutes(millisec)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisec)),
                TimeUnit.MILLISECONDS.toSeconds(millisec)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec)));
        return ms;
    }

    /**
     * returns time left in ms;
     *
     * @param currentTime
     *
     * @return
     */
    public long updateTimerForActive(final Date currentTime)
    {
        final long difference = currentTime.getTime() - _transformStartTime.getTime();
        long countDown = TEN_MINUTES - difference;
        if (countDown < 0)
        {
            countDown = 0;
        }

        final double scale = ((double) countDown / (double) TEN_MINUTES);
        final double midLine = 150;
        final int edge = (int) (255 - midLine);
        final int gScale = (int) (midLine * scale);
        final int rScale = (int) (midLine - (midLine * scale));

        final String diffString = getMinSec(countDown);
        _timeTxt.setText(diffString);

        int R = edge / 2 + rScale;
        int G = edge / 2 + gScale;
        int B = 0;

        if (R < 0 || R > 255)
        {
            R = 0;
        }
        if (G < 0 || G > 255)
        {
            G = 0;
        }
        if (B < 0 || B > 255)
        {
            B = 0;
        }

        _timeTxt.setStyle("-fx-font-weight: bold;" + "-fx-text-fill: rgba(" + R + "," + G + "," + B + ",1);"
                + "-fx-font-size: "+TIME_SIZE+"px;");

        return countDown;
    }

    public long updateTimerForCooldown(final Date currentTime)
    {
        final long difference = currentTime.getTime() - _transformStartTime.getTime();
        final long cooldownLeft = TWO_HOURS - difference;
        final String cooldown = getHrMinSec(cooldownLeft);
        _timeTxt.setText(cooldown);

        return cooldownLeft;
    }

	public void goInactiveColors()
	{
		ColorAdjust grayscale = new ColorAdjust();
		grayscale.setSaturation(-1);
		this.setEffect(grayscale);		
	}
}
