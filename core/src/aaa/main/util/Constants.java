package aaa.main.util;

import java.util.Map;

public final class Constants {
    //PPM = Pixels Per Meter, since Box2D uses meters, and we use pixels (for scaling)
    public static final float PPM = 32;
    public static final int BORDER_WIDTH = 200;
    public static final int BORDER_HEIGHT = 200;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;


    //camera zoom constants
    public static final float MAX_ZOOM = 2f;

    public static final float MIN_ZOOM = 0.2f;

    public static final float CAMERA_RETURN_SPEED = 0.1f;

    //Colony constants
    public static final float DEFAULT_HEALTH = 150;
    public static final float DEFAULT_RESOURCES = 25;
    public static final int DEFAULT_ANTS = 2;
    public static final int MAX_ANTS = 25;
    public static final int GLOBAL_MAX_COLONY = 5;
    public static final String COLONY_TEXTURE_FILE = "col_nest.png";

    //camera movement constants
    public static final float CAMERA_MOVE_SPEED = 8; //number is in pixels

    public static final float CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER = 2.5f; //best results between 2f-3f


    //player movement constants

    public static final float PLAYER_MOVE_SPEED = 5f;

    public static final float PLAYER_DIAGONAL_MOVE_SPEED_MODIFIER = 0.7f; //number should be between 0 and 1, gets multiplied by PLAYER_MOVE_SPEED

    //map generation

    public static final float WALL_THRESHOLD = 0.4f;
    public static final float COLONY_CANDIDATE_THRESHOLD = 0.2f;
    public static final float RESOURCE_THRESHOLD = 0.7f;

    public static final int ANT_WIDTH = 16;
    public static final int ANT_HEIGHT = 16;

    public static final int COLONY_WIDTH = 32;
    public static final int COLONY_HEIGHT = 32;


    // map constants

    public static final int MAP_TILE_WIDTH = 8;

    public static final int MAP_SCALE = 64;

    public static final int MAP_WIDTH = 200;
    public static final int MAP_HEIGHT = 200;

    public static class TileInfo {
        public TileInfo(int off_x, int off_y) {
            this.off_x = off_x * Constants.MAP_TILE_WIDTH;
            this.off_y = off_y * Constants.MAP_TILE_WIDTH;
            this.size = Constants.MAP_TILE_WIDTH;
        }
        public int off_x;
        public int off_y;
        public int size;
    }

    public static Map<String, TileInfo> TILE_LIST = Map.ofEntries(
            Map.entry("none", new TileInfo(0, 0)),
            Map.entry("debug", new TileInfo(1, 0)),
            Map.entry("dirt0", new TileInfo(2, 0)),
            Map.entry("dirt1", new TileInfo(3, 0)),
            Map.entry("dirt2", new TileInfo(4, 0)),
            Map.entry("dirt3", new TileInfo(5, 0)),
            Map.entry("stone", new TileInfo(6, 0))
    );
}
