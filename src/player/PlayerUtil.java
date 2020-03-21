package player;

import items.weapons.ProjectileUtil;
import javafx.util.Pair;
import resources.ResourcesUtil;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import structures.DrawCube;
import structures.DrawSphere;
import structures.StructureBuilder;
import utils.PhysicsUtil;
import utils.WindowUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PlayerUtil {
    public WindowUtil context;
    private Group player_group;

    private int player_width = 10;
    public int player_height = 60;

    public int fov_running = 70;
    public int fov_tired = 35;

    public double x = 0;
    public double y = 0;
    public double z = 0;

    private boolean tooHigh = false;

    public double runMultiplier = 1.5;
    public double speedForward = 3;
    public double speedBackward = 2;
    public double speedSide = 2;
    public double speedFly = 3;
    public double fallSpeed = 0; // Original speed before gravity is applied;

    double jump_start_height;
    private double jumpHeight = player_height * .75;
    private double autoJumpCutoffHeight = player_height / 2;
    public boolean canJump = true;
    public boolean isJumping = false;
    public boolean isRunning = false;

    public boolean isCrouching = false;
    public double crouch_multiplier = .4;

    public boolean isClipMode = false;
    public boolean isFlyMode = false;

    private boolean onGround = true;
    private boolean aboveGround = true;

    public PointLight uv_light;
    boolean uv_light_state = false;

    ArrayList<StructureBuilder>[] inventory = new ArrayList[9];
    int inventorySelected;

    public PlayerUtil(WindowUtil ctx) {
        context = ctx;
        player_group = new Group();

        uv_light = new PointLight();
        uv_light.setLightOn(false);
        uv_light.setColor(Color.DARKBLUE);
        player_group.getChildren().add(uv_light);


        inventorySelected = 1;
    }

    public void update_handler() {
//        System.out.println("Player X: " + getX() + " Y: " + getY() + " Z: " + getZ()  + " onGround: " +  isOnGround() + " aboveGround: " + isAboveGround());
//        System.out.println("isJumping: " + isJumping + " canJump: " + canJump);
        context.getCamera().update_handler();

        context.getEnvironment().generateChunks(getX(), getZ());
        context.getEnvironment().showChunksAroundPlayer(getX(), getZ());

        player_group.setTranslateX(getX());
        player_group.setTranslateY(-getY() - player_height);
        player_group.setTranslateZ(getZ());

        // Jumping Mechanism. As long as player is not in fly mode, execute mechanism
        if (!isFlyMode) {
            // If the player initiated a jump and hasn't reached the top, move the player up
//            System.out.println(jump_start_height);
            if (isJumping && y < jump_start_height + jumpHeight) {
                moveUp(speedFly);
            } else {
                // if the player reached the top, set isJumping to false, and let the player fall.
                isJumping = false;
                moveDown(fallSpeed);
                // gravity acceleration
                fallSpeed += PhysicsUtil.GRAVITY;
            }
        }

        // Running mechanism. Changes camera FOV incrementally from 45 to 60 when running and from 60 to 45 when not running
        double curr_fov = context.getCamera().getCamera().getFieldOfView();
        if (isRunning) {

            if (curr_fov < fov_running) {
                context.getCamera().getCamera().setFieldOfView(curr_fov + 1);
            }
        } else {
            if (isOnGround() && curr_fov > context.getCamera().fov_default) {
                context.getCamera().getCamera().setFieldOfView(curr_fov - 5);
            } else if (curr_fov < context.getCamera().fov_default - 2) {
                context.getCamera().getCamera().setFieldOfView(curr_fov + 2);
            }
        }


//        System.out.println(context.getCamera().getRotateX() + "    " + context.getCamera().getRotateY());

    }



    public void shoot(){
        DrawSphere model = new DrawSphere(2);
        model.setMaterial(ResourcesUtil.big_star);

        if(inventory[inventorySelected] != null && inventory[inventorySelected].get(0).getType() == StructureBuilder.TYPE_WEAPON){
            System.out.println("HERE");
            ProjectileUtil proj = new ProjectileUtil(context.getEnvironment(), inventory[inventorySelected].get(0));
            proj.setScale(100);
            proj.shoot(5);
        }
    }

    public void addToInventory(StructureBuilder b){
        ArrayList<StructureBuilder> list = new ArrayList<StructureBuilder>();
        list.add(b);

        for(int i = 0; i < inventory.length; i++){
            if(inventory[i] == null){
                System.out.println("Added single to inventory at "  + i );
                inventory[i] = list;
                break;
            }
        }

        System.out.println(inventory.toString());
    }
    public void addToInventory(StructureBuilder b, int ct){
        ArrayList<StructureBuilder> nums = new ArrayList<StructureBuilder>();
        for(int i = 0 ; i < ct; i++){
            nums.add(b);
        }

        for(int i = 0; i < inventory.length; i++){
            if(inventory[i] == null){
                System.out.println("Added collection to inventory at " + i);
                inventory[i] = nums;
                break;
            }
        }
        System.out.println(inventory.toString());

    }

    public void placeObject() {
        System.out.println(inventory);
        if(inventory[inventorySelected] != null && inventory[inventorySelected].get(0).getType() == StructureBuilder.TYPE_OBJECT) {
            System.out.println(inventory[inventorySelected]);
            context.getEnvironment().placeObject(getPoint2D(), inventory[inventorySelected].get(0), true);
        }
    }

    public void jump() {
        isJumping = true;
        canJump = false;
        jump_start_height = getY();
    }


    public Group getGroup() {
        return player_group;
    }

    public void moveForward(double val) {
        // If the player is running, move forward by the specified runMultiplier amount
        if (isRunning) val *= runMultiplier;
        if (isCrouching) val *= crouch_multiplier;

        double new_x = this.x + Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double new_z = this.z + Math.cos(context.getCamera().getRotateX() / 57.3) * val;

        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(new_x, new_z);
//        System.out.println(ground_level - y);
        if ((ground_level - y < autoJumpCutoffHeight) || isClipMode) {
            this.x = new_x;
            this.z = new_z;
        }

    }

    public void moveBackward(double val) {
        if (isCrouching) val *= crouch_multiplier;

        double new_x = this.x - Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double new_z = this.z - Math.cos(context.getCamera().getRotateX() / 57.3) * val;

        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(new_x, new_z);
        if ((ground_level - y < autoJumpCutoffHeight) || isClipMode) {
            this.x = new_x;
            this.z = new_z;
        }
    }

    public void moveLeft(double val) {
        if (isCrouching) val *= crouch_multiplier;

        double new_z = this.z + Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double new_x = this.x - Math.cos(context.getCamera().getRotateX() / 57.3) * val;
        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(new_x, new_z);
        if ((ground_level - y < autoJumpCutoffHeight) || isClipMode) {
            this.x = new_x;
            this.z = new_z;
        }
    }

    public void moveRight(double val) {
        if (isCrouching) val *= crouch_multiplier;

        double new_x = this.x + Math.cos(context.getCamera().getRotateX() / 57.3) * val;
        double new_z = this.z - Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(new_x, new_z);
        if ((ground_level - y < autoJumpCutoffHeight) || isClipMode) {
            this.x = new_x;
            this.z = new_z;
        }
    }

    public void moveUp(double val) {
        this.y += val;
        onGround = false;
    }


    public void moveDown(double val) {
        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(x, z);

        // if the player is above ground level, let the player fall
        if (getY() > ground_level || isClipMode) {
//            System.out.println("Above ground");
            y -= val;

            // if the player is more than a block above the ground , set onGround = false;
            if (y - ground_level > context.getEnvironment().getBlockDim()) {
                onGround = false;
            }
            if (!isOnGround() && !isRunning) {
                context.getCamera().getCamera().setFieldOfView(context.getCamera().fov_default + val * 5 * (1 - Math.cos((context.getCamera().getRotateY()) * Math.PI / 180)));
            }
        } else {
            // once the player fell enough to hit ground, set onGround to true
            onGround = true;
            // reposition the player back to above ground

            // CURRENTLY UNUSED IMPLEMENTATION WHERE THE PLAYER JUMPS IF HE HAS TO CLIMP MORE THAN A SET HEIGHT
//            System.out.println(y-ground_level);
            if (ground_level - y > context.getEnvironment().getBlockDim() * .75) {
                jump();
            } else {
                y = ground_level;
            }


            // reset the "current" fall speed back to 0 since the player is now on ground.
            // Next time player is above ground the gravity will keep on getting added to the fall speed, simulating the effects of gravity
            fallSpeed = 0;
        }

        if (y < -5000) {
            reset();
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

    /**
     * Returns the 3D placement of the character in the world (X, Y, Z coords)
     *
     * @return
     */
    public Point3D getPoint3D() {
        return new Point3D(getX(), -getY(), getZ());
    }

    /**
     * Returns the 2D placement of the character in the world (X and Z coords)
     *
     * @return
     */
    public Point2D getPoint2D() {
        return new Point2D(getX(), getZ());
    }

    public boolean isOnGround() {
        return onGround;
    }


    public void setPosition(double newx, double newy, double newz) {
        x = newx;
        y = newy;
        z = newz;
    }

    void reset() {
        setPosition(0, 0, 0);
        context.getCamera().setRotateX(0);
        context.getCamera().setRotateY(0);
        isClipMode = false;
        isRunning = false;
        isFlyMode = false;
        isJumping = false;


        context.getEnvironment().reset();
    }

    void setUV_light(boolean state) {
        uv_light.setLightOn(state);
        uv_light_state = state;
    }

    void toggleUVlight() {
        if (uv_light_state) {
            setUV_light(false);
        } else {
            setUV_light(true);
        }
    }

    void toggleCrouch() {
        if (context.getPlayer().isCrouching) {
            context.getPlayer().isCrouching = false;
        } else {
            context.getPlayer().isCrouching = true;
        }
    }

    void toggleNoClip() {
        if (context.getPlayer().isClipMode) {
            context.getPlayer().isClipMode = false;
        } else {
            context.getPlayer().isClipMode = true;
        }
    }

    void toggleFly() {
        if (context.getPlayer().isFlyMode) {
            context.getPlayer().isFlyMode = false;
        } else {
            context.getPlayer().isFlyMode = true;
        }
    }

}

