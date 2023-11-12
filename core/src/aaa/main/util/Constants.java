package aaa.main.util;

public final class Constants {
    //PPM = Pixels Per Meter, since Box2D uses meters, and we use pixels (for scaling)
    public static final float PPM = 32;
    public static final int BORDER_WIDTH = 200;
    public static final int BORDER_HEIGHT = 200;

    public static final int SCREEN_WIDTH = 1080;
    public static final int SCREEN_HEIGHT = 720;


    //camera zoom constants
    public static final float MAX_ZOOM = 2.0f;

    public static final float MIN_ZOOM = 0.3f;

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

    public static final float WALL_THRESHOLD = 0.5;
    public static final float RESOURCE_THRESHOLD = 0.7;

}
