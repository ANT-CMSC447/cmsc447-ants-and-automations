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

import java.util.ArrayList;
import java.util.List;

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
        renderer = new OrthogonalTiledMapRenderer(map, Constants.MAP_SCALE / Constants.PPM, batch);

        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);

        StaticTiledMapTile[] dirt = {
                new StaticTiledMapTile(new TextureRegion(tileset, 0, 0, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH)),
                new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_WIDTH, 0, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH)),
                new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_WIDTH * 2, 0, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH)),
                new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_WIDTH * 3, 0, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH))
        };
        TiledMapTileLayer floorLayer = getFloorLayer();
        map.getLayers().add(floorLayer);

        boolean[][][] perlinMap = Perlin.generateMap(mapWidth, mapHeight, 0);
        boolean[][] perlinWallsMap = perlinMap[1];

        TiledMapTileLayer wallLayer = getLayer(
                List.of(
                        Constants.TILE_LIST.get("dirt0"),
                        Constants.TILE_LIST.get("dirt1"),
                        Constants.TILE_LIST.get("dirt2"),
                        Constants.TILE_LIST.get("dirt3")
                ),
                perlinWallsMap,
                mapWidth,
                mapHeight
        );
        map.getLayers().add(wallLayer);

        boolean[][] resourceMap = perlinMap[0];
        // todo

        Constants.TileInfo cTile = Constants.TILE_LIST.get("debug");
        boolean[][] colonyCandidateMap = perlinMap[2];
        TiledMapTileLayer candidateLayer = getLayer(cTile, colonyCandidateMap, mapWidth, mapHeight);
        map.getLayers().add(candidateLayer);
    }

    public TiledMap getMap() {
        return map;
    }

    private TiledMapTileLayer getLayer(List<Constants.TileInfo> ctl, boolean[][] map, int mapWith, int mapHeight) {
        List<StaticTiledMapTile> stl = new ArrayList<>();
        for (Constants.TileInfo cTile : ctl) {
            stl.add(new StaticTiledMapTile(new TextureRegion(tileset, cTile.off_x, cTile.off_y, cTile.size, cTile.size)));
        }
        TiledMapTileLayer layer = new TiledMapTileLayer(mapWidth, mapHeight, Constants.MAP_TILE_WIDTH, Constants.MAP_TILE_WIDTH);
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++){
                if (!map[x][y]) {
                    continue;
                }
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                int random = (int) Math.floor(Math.random() * stl.size());
                StaticTiledMapTile tile = stl.get(random);
                cell.setTile(tile);
                layer.setCell(x, y, cell);
            }
        }
        return layer;
    }

    private TiledMapTileLayer getLayer(Constants.TileInfo cTile, boolean[][] map, int mapWidth, int mapHeight) {
        List<Constants.TileInfo> ctl = new ArrayList<>();
        ctl.add(cTile);
        return getLayer(ctl, map, mapWidth, mapHeight);
    }

    private TiledMapTileLayer getFloorLayer() {
        Constants.TileInfo cTile = Constants.TILE_LIST.get("stone");
        StaticTiledMapTile stone = new StaticTiledMapTile(new TextureRegion(tileset, cTile.off_x, cTile.off_y, cTile.size, cTile.size));

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
        float width = 1.12f * camera.viewportWidth * camera.zoom;
        float height = 1.12f * camera.viewportHeight * camera.zoom;
        float ppm_adj_width = mapWidth * ((Constants.MAP_SCALE * 4) / Constants.PPM);
        float ppm_adj_height = mapHeight * ((Constants.MAP_SCALE * 4) / Constants.PPM);

        float vbX = -camera.position.x - ppm_adj_width;
        float vbY = -camera.position.y - ppm_adj_height;
        float vbWidth = width + ppm_adj_width + camera.position.x;
        float vbHeight = height + ppm_adj_height + camera.position.y;
        renderer.setView(camera.projection, vbX, vbY, vbWidth, vbHeight);
        renderer.render();
    }

    public void dispose() {
        tileset.dispose();
        map.dispose();
    }
}
