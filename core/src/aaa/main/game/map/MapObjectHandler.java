package aaa.main.game.map;

import aaa.main.screens.MainScreen;
import aaa.main.util.ColonyUtils;
import aaa.main.util.CoordinateUtils;
import aaa.main.util.FoodSourceUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class MapObjectHandler {
    private TiledMap map;
    private MainScreen screen;

    private List<MapObject> objects;

    public MapObjectHandler(TiledMap map, MainScreen screen) {
        this.map = map;
        this.screen = screen;
        this.objects = new ArrayList<>();
    }

    public void setup() {
        int colonyCount = 0;
        TiledMapTileLayer layer = (TiledMapTileLayer) this.map.getLayers().get(2);
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                if (layer.getCell(x, y) != null) {
                    Vector2 coords = CoordinateUtils.getMapCoordinatesFromTileMapOffset(new Vector2(x, y));
                    System.out.println(
                            "Cell at " + x + ", " + y + " is not null, offset: " + coords.x + ", " + coords.y);
                    MapObject o = ColonyUtils.createColony(
                            "test" + (colonyCount++),
                            false,
                            100,
                            100,
                            10,
                            coords.x,
                            coords.y,
                            screen
                    );
                    objects.add(o);
                }
            }
        }

        layer = (TiledMapTileLayer) this.map.getLayers().get(3);
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                if (layer.getCell(x, y) != null) {
                    Vector2 coords = CoordinateUtils.getMapCoordinatesFromTileMapOffset(new Vector2(x, y));
                    System.out.println(
                            "Cell at " + x + ", " + y + " is not null, offset: " + coords.x + ", " + coords.y);
                    MapObject o = FoodSourceUtils.createFoodSource(
                            false,
                            coords.x,
                            coords.y,
                            screen
                    );
                    objects.add(o);
                }
            }
        }

        layer = (TiledMapTileLayer) this.map.getLayers().get(4);
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                if (layer.getCell(x, y) != null) {
                    Vector2 coords = CoordinateUtils.getMapCoordinatesFromTileMapOffset(new Vector2(x, y));
                    System.out.println(
                            "Cell at " + x + ", " + y + " is not null, offset: " + coords.x + ", " + coords.y);
                    MapObject o = FoodSourceUtils.createFoodSource(
                            true,
                            coords.x,
                            coords.y,
                            screen
                    );
                    objects.add(o);
                }
            }
        }

    }

    public void addObject(MapObject o) {
        objects.add(o);
    }

    public boolean collides(float x, float y) {
        TiledMapTileLayer layer = (TiledMapTileLayer) this.map.getLayers().get(1);

        Vector2 tilePos = CoordinateUtils.getTileMapOffsetCoordinates(new Vector2(x, y));
        boolean mapColl = layer.getCell((int) tilePos.x, (int) tilePos.y) != null;

        if (mapColl) {
            System.out.println("Return on map collision");
            return true;
        }

        boolean objColl = false;

        // TODO handle object size
        for (MapObject o : objects) {
            float startx = o.getPos().x - (o.getSize().x / 2f);
            float starty = o.getPos().y - (o.getSize().y / 2f);
            float endx = o.getPos().x + (o.getSize().x / 2f);
            float endy = o.getPos().y + (o.getSize().y / 2f);
            if (x >= startx && x <= endx && y >= starty && y <= endy) {
                objColl = true;
                break;
            }
        }
        System.out.println("Return on object collision, val: " + objColl);
        return objColl;
    }
}
