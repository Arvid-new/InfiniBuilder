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

        window.buildMenu();
        window.setCamera(camera);
        window.setControls(controls);
        window.setEnvironment(envir);
        window.setPlayer(player);

        // close window on menu if ESC is pressed

        controls.getControllerForScene(window.SCENE_MENU).setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                window.closeWindow();
            }
        });


        player.showModel(true);
        envir.setLighting(new AmbientLight());
        envir.generateChunks(0, 0);
        window.showScene(window.SCENE_MENU);

        DrawCube cube = new DrawCube(100, 100, 100);
        cube.setPos(100, -100, 100);
        cube.setMaterial(MaterialsUtil.blue);
        envir.addMember(cube);

        DrawSphere sphere = new DrawSphere(50);
        sphere.setPos(300, -100, 100);
        sphere.setMaterial(MaterialsUtil.red);
        envir.addMember(sphere);

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
//                        System.out.println("Player X: " + player.getX() + " Y: " +player.getY() + " Z: " + player.getZ()  + " onGround: " + player.isOnGround() + " aboveGround: " + player.isAboveGround());


                        controls.handleKeyboard(envir.getGroup());
                        camera.handle();
                        player.moveDown(Physics.GRAVITY);

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
