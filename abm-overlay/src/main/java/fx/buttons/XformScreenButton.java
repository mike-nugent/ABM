package fx.buttons;

import config.ConfigFile;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.DisplayManager;

public class XformScreenButton extends ScreenButton
{
    private final Label _asmoTxt;
    private final Label _elyTxt;

    private final StackPane _asmoPane;
    private final StackPane _elyPane;

    public XformScreenButton(final int size)
    {
        super("transform_icon2.png", size, true);
        final Pane p = new Pane();

        _asmoTxt = new Label();
        _elyTxt = new Label();

        _elyPane = makeCountBox("#B2FE76", _elyTxt);
        _asmoPane = makeCountBox("#88F7FA", _asmoTxt);

        _elyPane.setLayoutX(size / 3);
        _elyPane.setLayoutY(0);

        _asmoPane.setLayoutX(size / 3);
        _asmoPane.setLayoutY(size - 10);

        _elyPane.setVisible(false);
        _asmoPane.setVisible(false);

        _elyTxt.setTooltip(new Tooltip("The number of Elyos with Active Transform"));
        _asmoTxt.setTooltip(new Tooltip("The number of Asmodians with Active Transform"));

        p.getChildren().addAll(_elyPane, _asmoPane);

        final String isShowing = ConfigFile.getProperty(ConfigFile.IS_XFORM_SHOWING);

        if (isShowing != null && isShowing.equals("true"))
        {
            DisplayManager.showTransformPopup();
            this.setIsOpen(true);
        }
        else
        {
            DisplayManager.hideTransformPopup();
            this.setIsOpen(false);
        }

        this.getChildren().add(p);
    }

    @Override
    protected void closeTriggered()
    {
        DisplayManager.hideTransformPopup();
        ConfigFile.setProperty(ConfigFile.IS_XFORM_SHOWING, "false");
    }

    @Override
    protected void openTriggered()
    {
        DisplayManager.showTransformPopup();
        ConfigFile.setProperty(ConfigFile.IS_XFORM_SHOWING, "true");
    }

    public StackPane makeCountBox(final String color, final Label txt)
    {
        final StackPane group = new StackPane();
        final Rectangle R = new Rectangle(0, 0, 30, 20);
        R.setFill(Color.BLACK);
        R.setEffect(new DropShadow(10, Color.BLACK));

        txt.setStyle("-fx-font-size: 18px;" + "-fx-font-weight: bold;" + "-fx-text-fill: " + color + ";" + "-fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );");

        group.getChildren().addAll(R, txt);
        return group;
    }

    public void updateXformNumbers(final int numActiveElyXforms, final int numActiveAsmoXforms)
    {
        // Update the ely numbers
        if (numActiveElyXforms > 0)
        {
            _elyPane.setVisible(true);
            _elyTxt.setText(numActiveElyXforms + "");
        }
        else
        {
            _elyPane.setVisible(false);
        }

        // update the asmo numbers
        if (numActiveAsmoXforms > 0)
        {
            _asmoPane.setVisible(true);
            _asmoTxt.setText(numActiveAsmoXforms + "");
        }
        else
        {
            _asmoPane.setVisible(false);
        }

    }
}
