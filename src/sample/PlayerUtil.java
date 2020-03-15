package sample;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

public class PlayerUtil {
    public WindowUtil context;
    private Group player_group;

    private Sphere playerHead;
    private Cylinder playerNeck;
    private Cylinder playerBody;
    private Cylinder playerLeftArm;
    private Cylinder playerRightArm;
    private Cylinder playerLeftLeg;
    private Cylinder playerRightLeg;


    public double x = 0;
    public double y = 0;
    public double z = 0;

    public double speedForward = 5;
    public double speedBackward = 5;
    public double speedSide = 2;
    public double speedFly = 2;
    private double fallSpeed = 0; // Original speed before gravity is applied;

    double jump_start_height;
    private int jumpHeight = 30;
    public boolean canJump = true;
    public boolean isJumping = false;

    public boolean isClipMode = false;
    public boolean isFlyMode = false;

    private boolean onGround = true;
    private boolean aboveGround = true;


   private Box hitbox = new Box();
    private int player_width = 10;
    public int player_height = 30;


    PlayerUtil(WindowUtil ctx) {
        context = ctx;
        player_group = new Group();



    }

    public void handle() {
//        System.out.println("Player X: " + getX() + " Y: " + getY() + " Z: " + getZ()  + " onGround: " +  isOnGround() + " aboveGround: " + isAboveGround());
//        System.out.println("isJumping: " + isJumping + " canJump: " + canJump);
        context.getCamera().handle();

        player_group.setTranslateX(getX());
        player_group.setTranslateY(-getY() - player_height * 2);
        player_group.setTranslateZ(getZ());



        context.getEnvironment().generateChunks(getX(),getZ());

        context.getEnvironment().showChunksAroundPlayer(getX(),getZ());

        if(!isFlyMode){
            if (isJumping && y < jump_start_height + jumpHeight) {
                moveUp(speedFly);
            } else {
                isJumping = false;
                moveDown(fallSpeed);
                fallSpeed+= PhysicsUtil.GRAVITY;
            }
        }
    }

    public void jump() {
        jump_start_height = -context.getEnvironment().getTerrainHeight(x,z) + player_height;
        System.out.println(jump_start_height);
        isJumping = true;
        canJump = false;
    }


    public Group getGroup() {
        return player_group;
    }

    public void moveForward(double val) {
//        System.out.println("x: " + Math.cos(context.getCamera().rotx/57.3) + " y: " + Math.sin(context.getCamera().rotx/57.3) );
        this.z += Math.cos(context.getCamera().rotx / 57.3) * val;
        this.x += Math.sin(context.getCamera().rotx / 57.3) * val;
    }

    public void moveBackward(double val) {
//        System.out.println("Move Backward");
        this.z -= Math.cos(context.getCamera().rotx / 57.3) * val;
        this.x -= Math.sin(context.getCamera().rotx / 57.3) * val;
    }

    public void moveLeft(double val) {
//        System.out.println("Move Left");
        this.x -= Math.cos(context.getCamera().rotx / 57.3) * val;
        this.z += Math.sin(context.getCamera().rotx / 57.3) * val;
    }

    public void moveRight(double val) {
//        System.out.println("Move Right");
        this.x += Math.cos(context.getCamera().rotx / 57.3) * val;
        this.z -= Math.sin(context.getCamera().rotx / 57.3) * val;
    }

    public void moveUp(double val) {
//        System.out.println("Move Up");
        this.y += val;
        onGround = false;
    }


    public void moveDown(double val) {
        double ground_level = -context.getEnvironment().getTerrainHeight(x,z) + player_height;

        if (getY() > ground_level || isClipMode) {
            System.out.println("Above ground");
            y -= val;
        } else {
            onGround = true;
            y = ground_level;
            fallSpeed = 0;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isAboveGround() {
//        System.out.println(EnvironmentUtil.chunks.toString());
//        System.out.println("Player X: " + Player.x + " Y: " + Player.y + " Z: " + Player.z + " isFlying: " + Player.isFlying + " onGround: " + Player.onGround);


        boolean result = false;

        /*
        if (y > 0) {
            double curr_chunk_x = Math.floor((this.x + context.getEnvironment().chunk_width / 2) / context.getEnvironment().chunk_width);
            double curr_chunk_z = Math.floor((this.z + context.getEnvironment().chunk_depth / 2) / context.getEnvironment().chunk_depth);
            if (context.getEnvironment().getChunks().contains(new Point2D(curr_chunk_x, curr_chunk_z))) {
                aboveGround = true;
            } else {
                aboveGround = false;
            }
        }

         */
        return result;
    }

    public void setPosition(double newx, double newy, double newz) {
        x = newx;
        y = newy;
        z = newz;
    }

    public void showModel(boolean state) {
        if (state) {
            addModelComponents();
        } else {
            player_group.getChildren().removeAll();
        }
    }

    private void addModelComponents() {
/*
        hitbox.setHeight(player_height);
        hitbox.setWidth(player_width);
        hitbox.setDepth(player_width);
        hitbox.setMaterial(MaterialsUtil.blue);

 */

        /*
        // draw player head
        playerHead = new Sphere(6.5);
        playerHead.setMaterial(MaterialsUtil.blue);
        playerHead.setTranslateY(-38);

        // draw player neck
        playerNeck = new Cylinder(2.0, 2);
        playerNeck.setMaterial(MaterialsUtil.purple);
        playerNeck.setTranslateY(-31);

        // draw player body
        playerBody = new Cylinder(7.5, 20);
        playerBody.setMaterial(MaterialsUtil.red);
        playerBody.setTranslateY(-20);


        // draw player left arm
        playerLeftArm = new Cylinder(2.25, 8);
        playerLeftArm.setMaterial(MaterialsUtil.purple);
        playerLeftArm.setRotationAxis(Rotate.Z_AXIS);
        playerLeftArm.setRotate(90);
        playerLeftArm.setTranslateY(-25);
        playerLeftArm.setTranslateX(-11);

        // draw player right arm
        playerRightArm = new Cylinder(2.25, 8);
        playerRightArm.setMaterial(MaterialsUtil.purple);
        playerRightArm.setRotationAxis(Rotate.Z_AXIS);
        playerRightArm.setRotate(90);
        playerRightArm.setTranslateY(-25);
        playerRightArm.setTranslateX(11);


        // draw player left leg
        playerLeftLeg = new Cylinder(2.5, 10);
        playerLeftLeg.setMaterial(MaterialsUtil.purple);
        playerLeftLeg.setTranslateY(-5);
        playerLeftLeg.setTranslateX(-5);

        // draw player right leg
        playerRightLeg = new Cylinder(2.5, 10);
        playerRightLeg.setMaterial(MaterialsUtil.purple);
        playerRightLeg.setTranslateY(-5);
        playerRightLeg.setTranslateX(5);

        player_group.getChildren().setAll(playerRightLeg, playerLeftLeg, playerRightArm, playerLeftArm, playerNeck, playerBody, playerHead);
*/
        player_group.getChildren().setAll(hitbox);
    }


}

