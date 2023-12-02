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
import java.util.Random;

public class MapManager {
    private Texture tileset;
    private TiledMap map;
    private TiledMapRenderer renderer;

    private int mapWidth, mapHeight;

    public void setup(SpriteBatch batch, long seed, int num_colonies) {
        tileset = new Texture(Gdx.files.internal("tileset.png"));
        map = new TiledMap();
        map.getProperties().put("width", Constants.MAP_WIDTH);
        map.getProperties().put("height", Constants.MAP_HEIGHT);
//        renderer = new OrthogonalTiledMapRenderer(map, 1/2f);
        renderer = new OrthogonalTiledMapRenderer(map, Constants.MAP_SCALE / Constants.PPM, batch);

        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);

        StaticTiledMapTile[] dirt = {
                new StaticTiledMapTile(new TextureRegion(tileset, 0, 0, Constants.MAP_TILE_PIXELS, Constants.MAP_TILE_PIXELS)),
                new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_PIXELS, 0, Constants.MAP_TILE_PIXELS, Constants.MAP_TILE_PIXELS)),
                new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_PIXELS * 2, 0, Constants.MAP_TILE_PIXELS, Constants.MAP_TILE_PIXELS)),
                new StaticTiledMapTile(new TextureRegion(tileset, Constants.MAP_TILE_PIXELS * 3, 0, Constants.MAP_TILE_PIXELS, Constants.MAP_TILE_PIXELS))
        };
        TiledMapTileLayer floorLayer = getFloorLayer();
        map.getLayers().add(floorLayer);

        boolean[][][] perlinMap = Perlin.generateMap(mapWidth, mapHeight, seed);
        boolean[][] perlinWallsMap = perlinMap[1];

        List<Constants.TileInfo> ctl = new ArrayList<>();
        ctl.add(Constants.TILE_LIST.get("dirt0"));
        ctl.add(Constants.TILE_LIST.get("dirt1"));
        ctl.add(Constants.TILE_LIST.get("dirt2"));
        ctl.add(Constants.TILE_LIST.get("dirt3"));

        TiledMapTileLayer wallLayer = getLayer(
                ctl,
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
        class Coordinate {
            int x, y;
            Coordinate(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }
        List<Coordinate> colonyCandidates = new ArrayList<>();
        for (int x = 0; x < colonyCandidateMap.length; x++) {
            for (int y = 0; y < colonyCandidateMap[0].length; y++) {
                if (colonyCandidateMap[x][y]) {
                    colonyCandidates.add(new Coordinate(x, y));
                }
            }
        }
        Random rand = new Random(seed);
        for (int i = 0; i < num_colonies; i++) {
            int random = rand.nextInt(colonyCandidates.size());
            Coordinate c = colonyCandidates.get(random);
            colonyCandidates.remove(random);
        }
        for (Coordinate c : colonyCandidates) {
            candidateLayer.setCell(c.x, c.y, null);
        }
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
        TiledMapTileLayer layer = new TiledMapTileLayer(mapWidth, mapHeight, Constants.MAP_TILE_PIXELS, Constants.MAP_TILE_PIXELS);
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

        TiledMapTileLayer floorLayer = new TiledMapTileLayer(mapWidth, mapHeight, Constants.MAP_TILE_PIXELS, Constants.MAP_TILE_PIXELS);

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
