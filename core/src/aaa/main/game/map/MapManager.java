package aaa.main.game.map;

import aaa.main.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class MapManager {
    private Texture tileset;
    private TiledMap map;
    private TiledMapRenderer renderer;

    private int mapWidth, mapHeight;

    public void setup() {
        tileset = new Texture(Gdx.files.internal("tileset.png"));
        map = new TiledMap();
        map.getProperties().put("width", Constants.MAP_WIDTH);
        map.getProperties().put("height", Constants.MAP_HEIGHT);
        renderer = new OrthogonalTiledMapRenderer(map);

        mapWidth = map.getProperties().get("width", Integer.class) * Constants.MAP_RESOLUTION;
        mapHeight = map.getProperties().get("height", Integer.class) * Constants.MAP_RESOLUTION;

        StaticTiledMapTile[] dirt = {
                new StaticTiledMapTile(new TextureRegion(tileset, 0, 0, 16, 16)),
                new StaticTiledMapTile(new TextureRegion(tileset, 16, 0, 16, 16)),
                new StaticTiledMapTile(new TextureRegion(tileset, 32, 0, 16, 16)),
                new StaticTiledMapTile(new TextureRegion(tileset, 48, 0, 16, 16))
        };
        StaticTiledMapTile stone = new StaticTiledMapTile(new TextureRegion(tileset, 64, 0, 16, 16));

        TiledMapTileLayer floorLayer = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16);

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++){
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(stone);
                floorLayer.setCell(x, y, cell);
            }
        }
        map.getLayers().add(floorLayer);

        TiledMapTileLayer wallLayer = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16);

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++){
                if (x > mapWidth / 3 && x < mapWidth / 3 * 2) continue;
                if (y > mapHeight / 3 && y < mapHeight / 3 * 2) continue;
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                int random = (int) Math.floor(Math.random() * dirt.length);
                cell.setTile(dirt[random]);
                wallLayer.setCell(x, y, cell);
            }
        }
        map.getLayers().add(wallLayer);
    }

    public void render(OrthographicCamera camera) {
        camera.update();
        renderer.setView(camera);
        renderer.render();
    }

    public void dispose() {
        tileset.dispose();
        map.dispose();
    }
}
