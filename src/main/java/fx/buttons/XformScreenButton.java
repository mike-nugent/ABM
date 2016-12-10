package fx.buttons;

import config.ConfigFile;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
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

    public XformScreenButton()
    {
        super("transform_icon.jpg", 60);
        final Pane p = new Pane();

        _asmoTxt = new Label();
        _elyTxt = new Label();

        _elyPane = makeCountBox("#B2FE76", _elyTxt);
        _asmoPane = makeCountBox("#88F7FA", _asmoTxt);

        _elyPane.setLayoutX(30);
        _elyPane.setLayoutY(0);

        _asmoPane.setLayoutX(30);
        _asmoPane.setLayoutY(50);

        _elyPane.setVisible(false);
        _asmoPane.setVisible(false);

        _elyTxt.setTooltip(new Tooltip("The number of Elyos with Active Transform"));
        _asmoTxt.setTooltip(new Tooltip("The number of Asmodians with Active Transform"));

        p.getChildren().addAll(_elyPane, _asmoPane);

        setupRegion(region, 80, 80);
        final String isShowing = ConfigFile.getProperty(ConfigFile.IS_XFORM_SHOWING);

        if (isShowing != null && isShowing.equals("true"))
        {
            DisplayManager.showTransformPopup();
            region.setVisible(true);
        }
        else
        {
            DisplayManager.hideTransformPopup();
            region.setVisible(false);
        }

        this.getChildren().add(p);
    }

    public StackPane makeCountBox(final String color, final Label txt)
    {
        final StackPane group = new StackPane();
        final Rectangle R = new Rectangle(0, 0, 30, 20);
        R.setFill(Color.BLACK);
        R.setEffect(new DropShadow(10, Color.BLACK));

        txt.setStyle("-fx-font-size: 18px;" + "-fx-font-weight: bold;" + "-fx-text-fill: " + color + ";"
                + "-fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );");

        group.getChildren().addAll(R, txt);
        return group;
    }

    @Override
    protected void mousePressed(final MouseEvent event)
    {
        if (DisplayManager.isTransformShowing())
        {
            DisplayManager.hideTransformPopup();
            region.setVisible(false);
            ConfigFile.setProperty(ConfigFile.IS_XFORM_SHOWING, "false");
        }
        else
        {
            DisplayManager.showTransformPopup();
            region.setVisible(true);
            ConfigFile.setProperty(ConfigFile.IS_XFORM_SHOWING, "true");
        }
        // uncomment to test adding a transform
        // PlayerData random = PlayerData.generateRandom();
        // TransformManager.transformDetected(random);
    }

    @Override
    protected void mouseEntered(final MouseEvent event)
    {
        /// TransformManager.toggleTransformPopup();

    }

    @Override
    protected void mouseExited(final MouseEvent event)
    {
        /// TransformManager.toggleTransformPopup();

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
