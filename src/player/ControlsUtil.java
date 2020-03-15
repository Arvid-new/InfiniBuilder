package player;

import com.interactivemesh.jfx.importer.Importer;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import environment.MaterialsUtil;
import environment.SkyboxUtil;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Box;
import environment.StructureBuilder;
import utils.WindowUtil;

import java.net.URL;
import java.util.ArrayList;

public class ControlsUtil {
    private WindowUtil context;
    double last_mouse_x;
    double last_mouse_y;


    public static ArrayList<KeyCode> pressed;

    public static boolean rotating = false;

    public ControlsUtil(WindowUtil ctx) {
        context = ctx;
        pressed = new ArrayList<>();
    }

    public void apply(Scene game_scene) {


        game_scene.setOnMouseMoved(event -> {
            double differencex = event.getSceneX() - last_mouse_x;
            double differencey = event.getSceneY() - last_mouse_y;

//            System.out.println("diffX: " + differencex + " diffY: " + differencey);

//            context.getCamera().setRotate(context.getCamera().getRotateX() + differencey, context.getCamera().getRotateY() + differencex);
            context.getCamera().rotateX(differencex);
            context.getCamera().rotateY(-differencey);

            last_mouse_x = event.getSceneX();
            last_mouse_y = event.getSceneY();
        });

        game_scene.setOnMouseDragged(event -> {

        });

        game_scene.setOnMousePressed(event -> {
            switch (event.getButton()) {
                case PRIMARY:

                    StructureBuilder str = new StructureBuilder(0, 0, 0);
//                    javafx.scene.shape.Box b = new Box(20, 20, 20);
//                    b.setMaterial(MaterialsUtil.stone);
//                    str.addMember(b);


                    URL modelUrl = getClass().getResource("../res/Oak_Tree.3ds");
                    TdsModelImporter abc = new TdsModelImporter();
                    abc.read(modelUrl);
                    Node[] nodes = abc.getImport();

                    context.getEnvironment().getEnvironmentGroup().getChildren().addAll(nodes);
//                    str.addMember();

                    System.out.println(abc.getNamedNodes().keySet());

                    context.getPlayer().placeObject(str, true);
                    break;
            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            System.out.println("Pressed " + event.getCharacter().toUpperCase());
            if (!pressed.contains(event.getCode())) {
                pressed.add(event.getCode());
            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (pressed.contains(event.getCode())) {
                pressed.remove(event.getCode());
//                System.out.println("Released " + event.getText());
                switch (event.getCode()) {
                    case SPACE:
                        context.getPlayer().canJump = true;
                        break;
                    case ESCAPE:
                        context.showScene(context.SCENE_MENU);
                        break;
                    case F:
                        if (context.getPlayer().isFlyMode) {
                            context.getPlayer().isFlyMode = false;
                        } else {
                            context.getPlayer().isFlyMode = true;
                        }
                        break;
                    case C:
                        if (context.getPlayer().isClipMode) {
                            context.getPlayer().isClipMode = false;
                        } else {
                            context.getPlayer().isClipMode = true;
                        }
                        break;
                    case P:
                        switch (context.getEnvironment().getSkybox().getMode()) {
                            case SkyboxUtil.MODE_CYCLE:
                                context.getEnvironment().getSkybox().setMode(SkyboxUtil.MODE_DAY);
                                break;
                            case SkyboxUtil.MODE_DAY:
                                context.getEnvironment().getSkybox().setMode(SkyboxUtil.MODE_NIGHT);
                                break;
                            case SkyboxUtil.MODE_NIGHT:
                                context.getEnvironment().getSkybox().setMode(SkyboxUtil.MODE_CYCLE);
                                break;
                        }
                    case R:
                        context.getPlayer().reset();
                        break;
                    case SHIFT:
                        context.getPlayer().isRunning = false;
                }
            }

        });
    }

    public Scene getControllerForScene(Scene scn) {
        return scn;
    }

    public void handleKeyboard(Group environment) {

        for (KeyCode e : pressed) {
            switch (e) {
                case Q:
                    break;
                case E:
                    break;
                case W:
                    context.getPlayer().moveForward(context.getPlayer().speedForward);
                    break;
                case A:
                    context.getPlayer().moveLeft(context.getPlayer().speedSide);
                    break;
                case S:
                    context.getPlayer().moveBackward(context.getPlayer().speedBackward);
                    break;
                case D:
                    context.getPlayer().moveRight(context.getPlayer().speedSide);
                    break;

                case SPACE:
//                    System.out.println(context.getPlayer().isFlyMode);
                    if (context.getPlayer().isFlyMode) {
                        context.getPlayer().moveUp(context.getPlayer().speedFly);
                    } else {
                        if (context.getPlayer().isOnGround() && context.getPlayer().canJump) {
                            context.getPlayer().jump();
                        }
                    }
                    break;
                case SHIFT:
                    if (context.getPlayer().isFlyMode) {
                        context.getPlayer().moveDown(context.getPlayer().speedFly);
                    } else {
                        context.getPlayer().isRunning = true;
                    }

                    break;

            }
        }

    }

}
