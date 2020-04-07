package app.GUI.HUD;

import app.GUI.HUD.HUDElements.HUDElement;
import app.GameBuilder;
import app.utils.Log;
import javafx.scene.Group;
import javafx.scene.SubScene;

import java.util.HashMap;
import java.util.Map;

public class HUDUtil {
    private static final String TAG = "HudUtil";

    public GameBuilder context;
    private final SubScene subScene;
    private final Group HUDGroup = new Group();

    public static final String HEALTH = "HEALTH";
    public static final String STAMINA = "STAMINA";
    public static final String HUNGER = "HUNGER";
    public static final String THIRST = "THIRST";
    public static final String TEMPERATURE = "TEMPERATURE";
    public static final String INVENTORY = "INVENTORY";
    public static final String PAUSE = "PAUSE";
    public static final String DEATH = "DEATH";
    public static final String CROSSHAIR = "CROSSHAIR";
    public static final String PLAYER_INFO = "PLAYER_INFO";


    public static final String EDGE_BOTTOM = "edge_bottom";
    public static final String EDGE_TOP = "edge_top";
    public static final String EDGE_LEFT = "edge_left";
    public static final String EDGE_RIGHT = "edge_right";

    private final Map<String, HUDElement> elements = new HashMap<>();

    public HUDUtil(GameBuilder ctx){
        Log.p(TAG,"CONSTRUCTOR");
        context = ctx;
        subScene = new SubScene(HUDGroup, ctx.getWindow().getWindowWidth(), ctx.getWindow().getWindowHeight());
    }

    public Group getHUDGroup(){ return HUDGroup; }
    public HUDElement getElement(String tag){ return elements.get(tag); }
    public SubScene getSubScene() { return subScene; }

    public void addElement(HUDElement element){
        elements.put(element.getElementTag(), element);
        HUDGroup.getChildren().add(element.getGroup());
    }

    public void removeElement(String tag){
        if(elements.containsKey(tag)) {
            HUDGroup.getChildren().remove(elements.get(tag));
            elements.remove(tag);
        }
    }

    public void hideElement(String tag){
        if(elements.containsKey(tag)) {
            HUDGroup.getChildren().remove(elements.get(tag));
            elements.remove(tag);
        }
    }

    public void showElement(String tag){
        if(elements.containsKey(tag)) {
            HUDGroup.getChildren().add(elements.get(tag).getGroup());
        }
    }

}

