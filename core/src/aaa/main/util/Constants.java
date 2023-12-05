package aaa.main.util;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    //Colony update interval in seconds
    public static final int COLONY_UPDATE_INTERVAL = 5;

    //PPM = Pixels Per Meter, since Box2D uses meters, and we use pixels (for scaling)
    public static final float PPM = 32;
    public static final int BORDER_WIDTH = 200;
    public static final int BORDER_HEIGHT = 200;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;


    //camera zoom constants
    public static final float MAX_ZOOM = 3f;

    public static final float MIN_ZOOM = 0.2f;

    public static final float CAMERA_RETURN_SPEED = 0.1f;

    //Colony constants
    public static final float DEFAULT_HEALTH = 150;
    public static final float DEFAULT_RESOURCES = 25;
    public static final int DEFAULT_ANTS = 2;
    public static final int MAX_ANTS = 25;
    public static final int GLOBAL_MAX_COLONY = 5;
    public static final float COST_PER_ANT = 25f;
    public static final String COLONY_TEXTURE_FILE = "col_nest.png";

    //happens every 5 seconds
    public static final float COLONY_RESOURCE_CONSUMPTION_LOSS = 1f;

    public static final float COLONY_STARVATION_HEALTH_LOSS = 1f;

    //camera movement constants
    public static final float CAMERA_MOVE_SPEED = 8; //number is in pixels

    public static final float CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER = 2.5f; //best results between 2f-3f

    // ai constants

    public static final float ANT_RANDOM_TURN_TIME_MIN = 5f;
    public static final float ANT_RANDOM_TURN_TIME_MAX = 10f;
    public static final float ANT_RANDOM_TURN_RADIUS = 30f; // it's in degrees
    public static final float ANT_RANDOM_TURN_RADIUS_MIN = 10f;
    public static final float ANT_TURN_AROUND_AMOUNT = 20f;
    public static final float ANT_RANDOM_WALK_MIN = 2f;
    public static final float ANT_RANDOM_WALK_MAX = 10.0f;
    public static final float ANT_WALK_SPEED = 10.0f;
    public static final float ANT_TURN_SPEED = 100.0f;
    public static final float ANT_WALK_COOLDOWN_MAX = 10f;
    public static final float ANT_BREAK_CHANCE = 0.1f;

    //player movement constants

    public static final float PLAYER_MOVE_SPEED = 5f;

    public static final float PLAYER_DIAGONAL_MOVE_SPEED_MODIFIER = 0.7f; //number should be between 0 and 1, gets multiplied by PLAYER_MOVE_SPEED

    //map generation

    public static final float WALL_THRESHOLD = 0.3f;
    public static final float COLONY_CANDIDATE_THRESHOLD = 0.2f;
    public static final float RESOURCE_THRESHOLD = 0.7f;
    public static final int ADJUSTMENT_FACTOR_X = -4;
    public static final int ADJUSTMENT_FACTOR_Y = -4;
    public static final int ALIGNMENT_FACTOR = 8;


    //Ant Constants
    public static final String WORKER_ANT = "Worker";
    public static final String SOLDIER_ANT = "Soldier";

    //Body sizes
    public static final int TILE_CONVERSION_FACTOR = 1;


    // now in tiles

    public static final int ANT_WIDTH = 1;
    public static final int ANT_HEIGHT = 1;

    public static final int COLONY_WIDTH = 4;
    public static final int COLONY_HEIGHT = 4;

    public static final float ANT_SPAWN_PADDING = 1f;

    // Food Sources

    public static final String CANDY_FOOD = "candy";
    public static final float CANDY_START = 120f;
    public static final int CANDY_SIZE = 6;
    public static final float CANDY_THRESHOLD = 0.7f;
    public static final float FORAGE_START = 30f;
    public static final int FORAGE_SIZE = 4;



    // map constants

    public static final int MAP_TILE_PIXELS = 8;

    public static final int MAP_SCALE = 32;

    // for some reason this factor changes in a way i can't actually predict whenever the map scale changes
    // so for now - just setting this manually :)
    public static final float MAP_DIFF = 2f;

    public static final int MAP_WIDTH = 400;
    public static final int MAP_HEIGHT = 400;


    public static class TileInfo {
        public TileInfo(int off_x, int off_y) {
            this.off_x = off_x * Constants.MAP_TILE_PIXELS;
            this.off_y = off_y * Constants.MAP_TILE_PIXELS;
            this.size = Constants.MAP_TILE_PIXELS;
        }
        public int off_x;
        public int off_y;
        public int size;
    }

    public static Map<String, TileInfo> TILE_LIST = new HashMap<String, TileInfo>() {{
        put("none", new TileInfo(0, 0));
        put("debug", new TileInfo(1, 0));
        put("dirt0", new TileInfo(2, 0));
        put("dirt1", new TileInfo(3, 0));
        put("dirt2", new TileInfo(4, 0));
        put("dirt3", new TileInfo(5, 0));
        put("stone", new TileInfo(6, 0));
    }};
}
