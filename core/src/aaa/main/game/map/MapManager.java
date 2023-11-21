package aaa.main.game.map;

import aaa.main.game.Perlin;
import aaa.main.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import static aaa.main.util.Constants.COLONY_WIDTH;

public class MapManager {
    private Texture tileset;
    private TiledMap map;
    private TiledMapRenderer renderer;

    private int mapWidth, mapHeight;

    public void setup(SpriteBatch batch) {
        tileset = new Texture(Gdx.files.internal("tileset.png"));
        map = new TiledMap();
        map.getProperties().put("width", Constants.MAP_WIDTH);
        map.getProperties().put("height", Constants.MAP_HEIGHT);
//        renderer = new OrthogonalTiledMapRenderer(map, 1/2f);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM, batch);

        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);

        StaticTiledMapTile[] dirt = {
                new StaticTiledMapTile(new TextureRegion(tileset, 0, 0, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH)),
                new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_WIDTH, 0, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH)),
                new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_WIDTH * 2, 0, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH)),
                new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_WIDTH * 3, 0, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH))
        };
        TiledMapTileLayer floorLayer = getTiledMapTileLayer();
        map.getLayers().add(floorLayer);

        TiledMapTileLayer wallLayer = new TiledMapTileLayer(mapWidth, mapHeight, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH);
        boolean[][] perlinWallsMap = Perlin.generateMap(mapWidth, mapHeight, 0)[1];
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++){
                if (!perlinWallsMap[x][y]) {
                    continue;
                }
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                int random = (int) Math.floor(Math.random() * dirt.length);
                cell.setTile(dirt[random]);
                wallLayer.setCell(x, y, cell);
            }
        }
        map.getLayers().add(wallLayer);
    }

    private TiledMapTileLayer getTiledMapTileLayer() {
        StaticTiledMapTile stone = new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_WIDTH * 4, 0, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH));

        TiledMapTileLayer floorLayer = new TiledMapTileLayer(mapWidth, mapHeight, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH);

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++){
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(stone);
                floorLayer.setCell(x, y, cell);
            }
        }
        return floorLayer;
    }

    public void render(OrthographicCamera camera) {
        //camera.update();
        float width = 1.12f * camera.viewportWidth * camera.zoom;
        float height = camera.viewportHeight * camera.zoom;
        renderer.setView(camera.projection, -camera.position.x, -camera.position.y, width, height);
//        renderer.setView(camera.combined, -camera.position.x, -camera.position.y, , camera.zoom / camera.position.y);
        renderer.render();
    }

    public void dispose() {
        tileset.dispose();
        map.dispose();
    }
}
