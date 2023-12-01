package aaa.main.util;

import com.badlogic.gdx.math.Vector2;

import static aaa.main.util.Constants.*;
import static aaa.main.util.Constants.MAP_HEIGHT;

public class CoordinateUtils {
    public static Vector2 getAbsoluteCoordinates(Vector2 mapCoordinates) {
        float difference = MAP_DIFF / (MAP_TILE_PIXELS);
//        System.out.println("Difference: " + difference);
//        System.out.println("Map coordinates: " + mapCoordinates.x + ", " + mapCoordinates.y);
        return new Vector2(mapCoordinates.x * difference, mapCoordinates.y * difference);
    }

    public static Vector2 getMapCoordinates(Vector2 absoluteCoordinates) {
        float difference = (MAP_TILE_PIXELS) / MAP_DIFF;
        return new Vector2(absoluteCoordinates.x * difference, absoluteCoordinates.y * difference);
    }

    public static Vector2 getTileMapOffsetCoordinates(Vector2 mapCoordinates) {
        return new Vector2(mapCoordinates.x + (MAP_WIDTH / 2), mapCoordinates.y + (MAP_HEIGHT / 2));
    }

    public static Vector2 getMapCoordinatesFromTileMapOffset(Vector2 tileMapOffsetCoordinates) {
        return new Vector2(tileMapOffsetCoordinates.x - (MAP_WIDTH / 2), tileMapOffsetCoordinates.y - (MAP_HEIGHT / 2));
    }
}
