package sample;

import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentUtil {
    private SimplexNoise smp;
    public WindowUtil context;
    private SkyboxUtil skybox = null;
    public Group environment_group;


    private int chunk_depth = 20;
    private int chunk_width = 20;
    private int chunk_height = 20;

    private double terrain_height_multiplier = 1.5;
    private double terrain_spread_multiplier = 10;

    private Map<Point2D, Double> height_map = new HashMap<Point2D,Double>();
    private Map<Point2D, Box> box_map = new HashMap<Point2D,Box>();

    public double getSimplexHeight(double x, double z) {
        return smp.eval(x / terrain_spread_multiplier, z / terrain_spread_multiplier) * terrain_height_multiplier;
    }

    public double getTerrainX(double x){
        return Math.floor((x + chunk_width / 2) / chunk_width);
    }

    public double getTerrainZ(double z){
        return Math.floor((z + chunk_depth / 2) / chunk_depth);
    }

    public double getTerrainHeight(double x, double z) {
//          System.out.println(new Point2D(Double.valueOf(Math.floor((x + chunk_width / 2) / chunk_width)), Double.valueOf(Math.floor((z + chunk_depth / 2) / chunk_depth))));

        Point2D pt = new Point2D(Double.valueOf(getTerrainX(x)), Double.valueOf(getTerrainZ(z)));
        if(height_map.containsKey(pt)){
            return height_map.get(pt);
        } else{
            // Y down is positive.
            return Integer.MAX_VALUE;
        }
//       System.out.println(chunks.containsKey(new Point2D(Double.valueOf(Math.floor((x + chunk_width / 2) / chunk_width)), Double.valueOf(Math.floor((z + chunk_depth / 2) / chunk_depth)))));
    }


    EnvironmentUtil(WindowUtil ctx) {
        context = ctx;
        environment_group = new Group();
        smp = new SimplexNoise();
    }

    public void handle() {
        if (skybox != null) {
            skybox.handle();
        }
    }

    public Group getGroup() {
        return environment_group;
    }



    public void generateChunks(double playerx, double playerz) {
        playerx = getTerrainX(playerx);
        playerz = getTerrainZ(playerz);
        for (double i = -25+ playerx; i < 25+playerx; i++) {
            for (double j = -25+playerz; j < 25 + playerz; j++) {

                if(!box_map.containsKey(new Point2D(i,j))){
                    double x = i * chunk_depth;
                    double y = getSimplexHeight(i, j) * chunk_height + chunk_height / 2;
                    double z = j * chunk_width;
                    System.out.println("Chunk x: " + i + " y: " + y + " z: " + j);
                    Point2D key = new Point2D(i,j);
                    box_map.put(key,create_playform(x, y, z));
                    height_map.put(key, y);
                }
            }
        }
    }

    public void showChunksAroundPlayer(double playerx, double playerz){
        playerx = getTerrainX(playerx);
        playerz = getTerrainZ(playerz);
        getGroup().getChildren().removeAll();
        for (double i = -50+ playerx; i < 50+playerx; i++) {
            for (double j = -50+playerz; j < 50 + playerz; j++) {
                Point2D key = new Point2D(i,j);
                if(box_map.containsKey(key) && !getGroup().getChildren().contains(box_map.get(key))){
                    getGroup().getChildren().add(box_map.get(key));
                }
            }
        }
    }

    public Box create_playform(double x, double y, double z) {
        Box box = new Box();
        box.setMaterial(MaterialsUtil.grass);
        box.setCullFace(CullFace.NONE);

        box.setWidth(chunk_width);
        box.setHeight(chunk_height);
        box.setDepth(chunk_depth);

        box.setTranslateX(x);
        box.setTranslateY(y);
        box.setTranslateZ(z);

        return box;
    }


    public void setSkyBox(SkyboxUtil sky) {
        skybox = sky;
    }

    public SkyboxUtil getSkybox() {
        return skybox;
    }


    public void addMember(StructureBuilder member) {
        environment_group.getChildren().add(member.getGroup());
    }

    public void removeMember(StructureBuilder member) {
        environment_group.getChildren().remove(member.getGroup());
    }
}
