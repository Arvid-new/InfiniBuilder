package app.player;

import app.GUI.HUD.HUDElement;
import app.GUI.HUD.HUDUtil;
import app.GameBuilder;
import app.utils.InventoryUtil;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/*
 *  will display each item in the inventoryUtil
 *  needs to calculate size of individual boxes given number of slots and total width
 *
 * +-------------------------------+
 * | +---+ +---+ +---+ +---+ +---+ |
 * | |_0_| |_1_| |_2_| |_3_| |_4_| |
 * | +---+ +---+ +---+ +---+ +---+ |
 * +-------------------------------+
 *
 */


// have user set the sizes of the slots


public class Inventory extends HUDElement {

    private InventoryUtil inventoryUtil;
    private int inventorySize;
    private int slotWidth;
    private int slotHeight;
    private int borderWidth;
    private int totalWidth;
    private int totalHeight;
    private Paint panelColor;
    private Paint slotColor;
    private Paint selectedItemColor = Color.YELLOW;
    private Paint emptyItemColor = Color.DARKGRAY;
    private Paint borderColor = Color.BLACK;

    public Inventory(String elementTag,
                     Point2D pos,
                     InventoryUtil inventoryUtil,
                     int slotWidth,
                     int slotHeight,
                     int borderWidth,
                     Paint panelColor,
                     Paint slotColor) {
        super( elementTag, pos);
        this.inventoryUtil = inventoryUtil;
        this.inventorySize = inventoryUtil.getInventorySize();
        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
        this.borderWidth = borderWidth;
        this.panelColor = panelColor;
        this.slotColor = slotColor;
        totalWidth = ( inventorySize * slotWidth )+( ( 1 + inventorySize ) * borderWidth );
        totalHeight = slotHeight + ( 2 * borderWidth );
        update();
    }

    public InventoryUtil getInventoryUtil() { return inventoryUtil; }
    public int getInventorySize() { return inventorySize; }
    public double getBorderWidth() { return borderWidth; }
    public double getSlotHeight() { return slotHeight; }
    public double getSlotWidth() { return slotWidth; }
    public double getTotalHeight() { return totalHeight; }
    public double getTotalWidth() { return totalWidth; }
    public Paint getPanelColor() { return panelColor; }
    public Paint getSlotColor() { return slotColor; }
    public Paint getSelectedItemColor() { return selectedItemColor; }
    public Paint getEmptyItemColor() { return emptyItemColor; }
    public Paint getBorderColor() { return borderColor; }

    public void setPanelColor(Paint panelColor) { this.panelColor = panelColor; }
    public void setSlotColor(Paint slotColor) { this.slotColor = slotColor; }
    public void setSelectedItemColor(Paint selectedItemColor) { this.selectedItemColor = selectedItemColor; }
    public void setEmptyItemColor(Paint emptyItemColor) { this.emptyItemColor = emptyItemColor; }
    public void setBorderColor(Paint borderColor) { this.borderColor = borderColor; }

    public void fixToEdge(String edge){
        int x = 0;
        int y = 0;

        switch(edge){
            case HUDUtil.EDGE_BOTTOM:
                x = GameBuilder.getWindowWidth()/2 - totalWidth/2;
                y = GameBuilder.getWindowHeight() - totalHeight;
                break;
            case HUDUtil.EDGE_TOP:
                x = GameBuilder.getWindowWidth()/2 - totalWidth/2;
                //
                break;
            case HUDUtil.EDGE_LEFT:
                // TODO - set orientation to vertical
                y = GameBuilder.getWindowHeight()/2;
                break;
            case HUDUtil.EDGE_RIGHT:
                // TODO - set orientation to vertical
                x = GameBuilder.getWindowWidth() - totalWidth;
                y = GameBuilder.getWindowHeight()/2;
                break;
        }

        System.out.println(x + "  " + y);
        setPos(new Point2D(x,y));
    }

    public void update(){
        getGroup().getChildren().clear();

        // determines the slot width based off the given inventory width, inventory size, and border width

        double slotY = getPos().getY() + borderWidth;

        // draw inventory backdrop
        Rectangle inventoryBackdrop = new Rectangle(getPos().getX(),
                getPos().getY(),
                totalWidth,
                totalHeight);
        inventoryBackdrop.setStroke(borderColor);
        inventoryBackdrop.setFill(panelColor);
        getGroup().getChildren().add(inventoryBackdrop);

        // draw each individual panel
        for ( int i = 0 ; i < inventorySize ; i++ ) {
            Rectangle slotBackdrop = new Rectangle();

            // calculate start of the inventory box
            double currSlotX = (getPos().getX() + borderWidth) + (i * (slotWidth + borderWidth));

            // draw the slot border
            if (inventoryUtil.isCurrentIndex(i)) slotBackdrop.setFill(selectedItemColor);
            else slotBackdrop.setFill(slotColor);

            slotBackdrop.setX(currSlotX);
            slotBackdrop.setY(slotY);
            slotBackdrop.setWidth(slotWidth);
            slotBackdrop.setHeight(slotHeight);
            slotBackdrop.setStroke(borderColor);
            getGroup().getChildren().add(slotBackdrop);

            // draw each item
            Group item = inventoryUtil.getItem(i);
            item.setTranslateX(currSlotX+(slotWidth/2.0));
            item.setTranslateY(slotY+(slotHeight/2.0));
            item.setScaleX(slotWidth/3.0);
            item.setScaleY(slotWidth/3.0);
            item.setScaleZ(slotWidth/3.0);
            item.getTransforms().setAll(new Rotate(25,Rotate.X_AXIS),new Rotate(25,Rotate.Y_AXIS));
            item.toFront();
            getGroup().getChildren().add(item);

        }

    }
}
