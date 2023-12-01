package aaa.main.game.map;

import aaa.main.screens.MainScreen;
import aaa.main.util.ColonyUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

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
                    System.out.println("Cell at " + x + ", " + y + " is not null");
                    MapObject o = ColonyUtils.createColony("test" + (colonyCount++), false, 100, 100, 10,  x - 100, y - 100, screen);
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
        boolean mapColl = layer.getCell((int) x, (int) y) != null;

        boolean objColl = false;

        // TODO handle object size
        for (MapObject o : objects) {
            if ((int) o.getPos().x == (int) x && (int) o.getPos().y == (int) y) {
                objColl = true;
                break;
            }
        }
        System.out.println("Pos: " + x + ", " + y);
        System.out.println("Map collision: " + mapColl);
        System.out.println("Object collision: " + objColl);
        return mapColl || objColl;
    }
}
