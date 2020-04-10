package app.structures.objects;

import app.environment.EnvironmentUtil;
import app.structures.SpawnableStructure;
import app.structures.StructureBuilder;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Material;

import java.util.Map;

public class SpawnableStructureItem extends Base_Cube{
    private final SpawnableStructure spawnable;

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, double width, double height, double depth) {
        super(ITEM_TAG, width, height, depth);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, double all_side_length) {
        super(ITEM_TAG, all_side_length);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG) {
        super(ITEM_TAG);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, Material mat, double width, double height, double depth) {
        super(ITEM_TAG, mat, width, height, depth);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, Material mat, double all_side_length) {
        super(ITEM_TAG, mat, all_side_length);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, Material mat) {
        super(ITEM_TAG, mat);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }


    @Override
    public void use() {
        super.use();
    }
}
