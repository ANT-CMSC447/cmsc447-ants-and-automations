package aaa.main.game.map;

import aaa.main.screens.MainScreen;
import aaa.main.util.ColonyUtils;
import aaa.main.util.CoordinateUtils;
import aaa.main.util.FoodSourceUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapObjectHandler implements ContactListener {
    private TiledMap map;
    private MainScreen screen;

    private List<MapObject> objects;

    private Map<Body, MapObject> bodyMap = new HashMap<>();

    public MapObjectHandler(TiledMap map, MainScreen screen) {
        this.map = map;
        this.screen = screen;
        this.objects = new ArrayList<>();
    }

    public void handleAI(float delta) {
        for (MapObject obj : this.objects) {
            obj.executeAI(delta, this);
        }
    }

    public void update(float delta) {
        for (MapObject obj : this.objects) {
            obj.update(delta, this);
        }
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

    public void addObject(MapObject o, Body b) {
        objects.add(o);
        bodyMap.put(b, o);
    }

    private boolean intersects(float sx1, float sy1, float ex1, float ey1, float sx2, float sy2, float ex2, float ey2) {
        // check if any of the corners of the first rectangle are inside the second rectangle

        // sx1, sy1
        if (sx1 >= sx2 && sx1 <= ex2 && sy1 >= sy2 && sy1 <= ey2) {
            return true;
        }
        // ex1, sy1
        if (ex1 >= sx2 && ex1 <= ex2 && sy1 >= sy2 && sy1 <= ey2) {
            return true;
        }
        // sx1, ey1
        if (sx1 >= sx2 && sx1 <= ex2 && ey1 >= sy2 && ey1 <= ey2) {
            return true;
        }
        // ex1, ey1
        if (ex1 >= sx2 && ex1 <= ex2 && ey1 >= sy2 && ey1 <= ey2) {
            return true;
        }
        return false;
    }

    // this will only ever be used for ant spawning checks
    public boolean collides(float x, float y, float width, float height, float objRotation) {
        // check if the coordinates intersect with a wall
        TiledMapTileLayer layer = (TiledMapTileLayer) this.map.getLayers().get(1);

        float sx = x - (width / 2f);
        float sy = y - (height / 2f);
        float ex = x + (width / 2f);
        float ey = y + (height / 2f);

        // rotate points around center
        Vector2 objCenter = new Vector2(x, y);
        Vector2 os = new Vector2(sx, sy).rotateAroundRad(objCenter, objRotation);
        Vector2 oe = new Vector2(ex, ey).rotateAroundRad(objCenter, objRotation);

        List<Vector2> corners = new ArrayList<>();
        corners.add(CoordinateUtils.getTileMapOffsetCoordinates(new Vector2(os.x, os.y)));
        corners.add(CoordinateUtils.getTileMapOffsetCoordinates(new Vector2(oe.x, os.y)));
        corners.add(CoordinateUtils.getTileMapOffsetCoordinates(new Vector2(os.x, oe.y)));
        corners.add(CoordinateUtils.getTileMapOffsetCoordinates(new Vector2(oe.x, oe.y)));

        for (Vector2 corner : corners) {
//            System.out.println("Checking corner " + corner.x + ", " + corner.y);
            float cx = corner.x;
            float cy = corner.y;
            float dcx = cx - (int) cx;
            float dcy = cy - (int) cy;
            int ix = (int) (dcx > 0.5 ? Math.floor(cx) : Math.ceil(cx));
            int iy = (int) (dcy > 0.5 ? Math.floor(cy) : Math.ceil(cy));

            boolean mapColl = layer.getCell(ix, iy) != null;
            if (corner.x > layer.getWidth() || corner.y > layer.getHeight() || corner.x < 0 || corner.y < 0) {
                mapColl = true;
            }
            if (mapColl) {
                return true;
            }
        }

        // check if the coordinates intersect with an object
        for (MapObject o : objects) {
            float startx = o.getPos().x - (o.getSize().x / 2f);
            float starty = o.getPos().y - (o.getSize().y / 2f);
            float endx = o.getPos().x + (o.getSize().x / 2f);
            float endy = o.getPos().y + (o.getSize().y / 2f);
            // rotate points around center
            double rot = o.getRot();
            Vector2 center = o.getPos();
            Vector2 s = new Vector2(startx, starty).rotateAroundRad(center, (float) rot);
            Vector2 e = new Vector2(endx, endy).rotateAroundRad(center, (float) rot);

            if (intersects(sx, sy, ex, ey, s.x, s.y, e.x, e.y)) {
                return true;
            }
        }

        return false;
    }

    public boolean collides(float x, float y, float width, float height) {
        return collides(x, y, width, height, 0);
    }
    public boolean collides(MapObject self) {
        return false;
//        if (self instanceof Ant) {
//            Ant a = (Ant) self;
//            return collides(a);
//        }
//        return collides(self.getPos().x, self.getPos().y, self.getSize().x, self.getSize().y, (float) self.getRot());
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Collision");
        Body a = contact.getFixtureA().getBody();
        if (bodyMap.containsKey(a)) {
            bodyMap.get(a).hitSomething();
        }
        Body b = contact.getFixtureB().getBody();
        if (bodyMap.containsKey(b)) {
            bodyMap.get(b).hitSomething();
        }
    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("End collision");
        Body a = contact.getFixtureA().getBody();
        if (bodyMap.containsKey(a)) {
            bodyMap.get(a).previous_collision = false;
        }
        Body b = contact.getFixtureB().getBody();
        if (bodyMap.containsKey(b)) {
            bodyMap.get(b).previous_collision = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
