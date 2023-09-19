package cn.mcmod_mmf.mmlib.client.model.pojo;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.Direction;

public class FaceUVsItem {
    @SerializedName("down")
    private FaceItem down = FaceItem.empty();
    @SerializedName("east")
    private FaceItem east = FaceItem.empty();
    @SerializedName("north")
    private FaceItem north = FaceItem.empty();
    @SerializedName("south")
    private FaceItem south = FaceItem.empty();
    @SerializedName("up")
    private FaceItem up = FaceItem.empty();
    @SerializedName("west")
    private FaceItem west = FaceItem.empty();

    public static FaceUVsItem singleSouthFace() {
        FaceUVsItem faces = new FaceUVsItem();
        faces.north = FaceItem.empty();
        faces.east = FaceItem.empty();
        faces.west = FaceItem.empty();
        faces.south = FaceItem.single16X();
        faces.up = FaceItem.empty();
        faces.down = FaceItem.empty();
        return faces;
    }

    public FaceItem getFace(Direction direction) {
        switch (direction) {
            case EAST:
                return west;
            case WEST:
                return east;
            case NORTH:
                return north;
            case SOUTH:
                return south;
            case UP:
                return down;
            case DOWN:
            default:
                return up;
        }
    }
}
