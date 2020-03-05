package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class MainExecution extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("MainExecution");
        MaterialsUtil materials = new MaterialsUtil();
        WindowUtil window = new WindowUtil(primaryStage, 800, 600);
        CameraUtil camera = new CameraUtil(window);
        ControlsUtil controls = new ControlsUtil(window);
        PlayerUtil player = new PlayerUtil(window);
        EnvironmentUtil envir = new EnvironmentUtil(window);
        DrawCube cube = new DrawCube(envir);

        window.buildMenu();
        window.setCamera(camera);
        window.setControls(controls);
        window.setEnvironment(envir);
        window.setPlayer(player);

        // close window on menu if ESC is pressed

        controls.getControllerForScene(window.SCENE_MENU).setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                window.closeWindow();
            }
        });


        player.showModel(true);
        envir.setLighting(new AmbientLight());
        envir.generateChunks(0, 0);
        window.showScene(window.SCENE_MENU);

        cube.setX(100);
        cube.setY(-100);
        cube.setZ(100);
        cube.setWidth(100);
        cube.setHeight(100);
        cube.setDepth(100);
        cube.setMaterial(MaterialsUtil.blue);
        cube.show(true);

        // MAIN GAME LOOP
        AnimationTimer timer = new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {

                // FPS HANDLING
                if ((now - last) > (1 / 60)) {
//                    System.out.println(now-last);

                    // IF THE PLAYER IS PLAYING THE GAME
                    if (window.getCurrentScene() == window.SCENE_GAME) {
//                        System.out.println("Player X: " + PlayerUtil.x + " Y: " + PlayerUtil.y + " Z: " + PlayerUtil.z + " isFlying: " + PlayerUtil.isFlying + " onGround: " + PlayerUtil.onGround);
//                        window.lockCursor(true);
                        controls.handleKeyboard(envir.getGroup());
                        if (!PlayerUtil.isFlying) {
                            player.moveDown(Physics.GRAVITY);
                        }
                        CameraUtil.resetCenter();
                    }

                    last = now;
                }
            }
        };
        timer.start();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
