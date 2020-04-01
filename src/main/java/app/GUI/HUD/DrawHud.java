package app.GUI.HUD;

import app.player.Inventory;
import app.utils.InventoryUtil;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class DrawHud {
    public static void DrawHud(HUDUtil hudUtil,InventoryUtil inventoryUtil, double PRIMARY_WIDTH, double PRIMARY_HEIGHT){
        //health bar
        StatusBar health = new StatusBar(   HUDUtil.HEALTH,
                new Point2D(25,10),
                500,
                15,
                200,
                Color.RED,
                Color.valueOf("400000"));
        health.setCurrStatus(425);
        health.setVertical(true);
        health.setBorder(true);
        health.setDefaultDirection(false);
        health.setBorderColor(Color.WHITE);
        health.setArcHeight(20);
        health.setArcWidth(20);
        health.update();
        hudUtil.addElement(health);

        //stamina bar
        StatusBar stamina = new StatusBar(   HUDUtil.STAMINA,
                new Point2D(55,10),
                200,
                15,
                200,
                Color.BLUE,
                Color.valueOf("010048"));
        stamina.setCurrStatus(160);
        stamina.setVertical(true);
        stamina.setBorder(true);
        stamina.setDefaultDirection(false);
        stamina.setBorderColor(Color.WHITE);
        stamina.setArcHeight(20);
        stamina.setArcWidth(20);
        stamina.update();
        hudUtil.addElement(stamina);



        Inventory inv = new Inventory(  HUDUtil.INVENTORY,
                new Point2D(200,100),
                inventoryUtil,
                50,50,5,Color.WHITE,Color.GREY );
        inv.fixToEdge(HUDUtil.EDGE_BOTTOM);
        inv.setDisplayNumbers(true);
        inv.update();
        hudUtil.addElement(inv);


        PauseMenu pauseMenu = new PauseMenu(HUDUtil.PAUSE,
                new Point2D(100,200),100,200);
        pauseMenu.setPaused(true);
        pauseMenu.update();
//        hudUtil.addElement(pauseMenu);

        Crosshair crosshair = new Crosshair(HUDUtil.CROSSHAIR,PRIMARY_WIDTH,PRIMARY_HEIGHT,3,25,5,Color.WHITE);
        crosshair.setCrosshairBorderWidth(1);
        crosshair.setCrosshairBorderColor(Color.BLACK);
        crosshair.update();
        hudUtil.addElement(crosshair);
    }
}
