package aaa.main.util;

import aaa.main.game.map.Ant;
import aaa.main.game.map.Colony;
import aaa.main.game.map.MapObjectHandler;
import aaa.main.screens.MainScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static aaa.main.util.Constants.*;
import java.util.ArrayList;

//utility for accessing colonies from the master ArrayList in MainScreen
public class ColonyUtils {

    //Example creation would look like:
    //createColony("Colony 1", false, 100, 100, 10, colonyBody, camera, screen);
    public static Colony createColony(String name, boolean isPlayer, float cResources, float cHealth, int ants, float x, float y, MainScreen screen) {
        Colony colony = new Colony(name, isPlayer, cResources, cHealth, ants, x, y, screen.camera, screen.world);
        addColony(colony, screen);
        return colony;
    }

    public static Colony getColonyByName(String name, MainScreen screen) {
        for (Colony colony : screen.colonies) {
            if (colony.getName().equals(name)) {
                return colony;
            }
        }
        return null;
    }

    public static void removeColonyByName(String name, MainScreen screen, World world) {
        Colony colony = getColonyByName(name, screen);
        if (colony != null) {
            //delete the box
            world.destroyBody(colony.getColonyBody());
            screen.colonies.remove(colony);
        }
    }

    public static void removeColony(Colony colony, MainScreen screen) {
        screen.colonies.remove(colony);
    }

    public static void addColony(Colony colony, MainScreen screen) {
        screen.colonies.add(colony);
    }

    public static Body getColonyBodyByName(String name, MainScreen screen) {
        Colony colony = getColonyByName(name, screen);
        if (colony != null) {
            return colony.getColonyBody();
        }
        return null;
    }

    public Body getColonyBody(Colony colony, MainScreen screen) {
        return colony.getColonyBody();
    }

    public ArrayList<Ant> getAntsList(Colony colony, MainScreen screen) {
        return colony.getAntsList();
    }

    public static Ant addAnt(Colony colony, String type, OrthographicCamera camera, World world, MapObjectHandler moh) {
        Ant ant = new Ant(colony, type, camera, world, moh);
        colony.getAntsList().add(ant);
        return ant;
    }

    //Takes in a colony and checks for the closest open spot to spawn an Ant
    //Begins checking the right, down, left, and up. Returns the world coords
    //where the ant can spawn.
    public static Vector3 getAntSpawn(Colony colony, World world, MapObjectHandler moh) {
        float step = smallestStep(world);

        Vector2 pos = colony.getPos();
        System.out.println("Colony position: " + pos.x + ", " + pos.y);
        Vector2 up = new Vector2(pos.x, pos.y + 2);
        Vector2 down = new Vector2(pos.x, pos.y - 2);
        Vector2 left = new Vector2(pos.x - 2, pos.y);
        Vector2 right = new Vector2(pos.x + 2, pos.y);

        if (moh.collides(up.x, up.y)) {
            System.out.println("Collision detected: up");
        } else {
            return new Vector3(up.x, up.y, 0);
        }
        if (moh.collides(down.x, down.y)) {
            System.out.println("Collision detected: down");
        } else {
            return new Vector3(down.x, down.y, 0);
        }
        if (moh.collides(left.x, left.y)) {
            System.out.println("Collision detected: left");
        } else {
            return new Vector3(left.x, left.y, 0);
        }
        if (moh.collides(right.x, right.y)) {
            System.out.println("Collision detected: right");
        } else {
            return new Vector3(right.x, right.y, 0);
        }

        //check an area in each direction which is the size of an ant
//        if (checkArea(step, world, new Vector2(up.x -ANT_WIDTH/2f/PPM, up.y -ANT_HEIGHT/2f/PPM), new Vector2(up.x +ANT_WIDTH/2f/PPM,up.y +ANT_HEIGHT/2f/PPM))) {
//            return new Vector3(up.x, up.y, 0);
//        } else {
//            System.out.println("Collision detected: up");
//        }
//        if (checkArea(step, world, new Vector2(down.x -ANT_WIDTH/2f/PPM, down.y -ANT_HEIGHT/2f/PPM), new Vector2(down.x +ANT_WIDTH/2f/PPM,down.y +ANT_HEIGHT/2f/PPM))) {
//            return new Vector3(down.x, down.y, 0);
//        } else {
//            System.out.println("Collision detected: down");
//        }
//        if (checkArea(step, world, new Vector2(left.x -ANT_WIDTH/2f/PPM, left.y -ANT_HEIGHT/2f/PPM), new Vector2(left.x +ANT_WIDTH/2f/PPM,left.y +ANT_HEIGHT/2f/PPM))) {
//            return new Vector3(left.x, left.y, 0);
//        } else {
//            System.out.println("Collision detected: left");
//        }
//        if (checkArea(step, world, new Vector2(right.x -ANT_WIDTH/2f/PPM, right.y -ANT_HEIGHT/2f/PPM), new Vector2(right.x +ANT_WIDTH/2f/PPM,right.y +ANT_HEIGHT/2f/PPM))) {
//            return new Vector3(right.x, right.y, 0);
//        } else {
//            System.out.println("Collision detected: right");
//        }

        return null;
    }

    //Returns false when the area is NOT clear.
    private static boolean checkArea(float step, World world, Vector2 begin, Vector2 end) {
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        //Iterate over every body
        for (Body body : bodies) {
            //Iterate over each fixture for each body
            for (Fixture fixture : body.getFixtureList()) {
                while (begin.x < end.x) {
                    while (begin.y < end.y) {
                        if (fixture.testPoint(begin)) {
                            System.out.println("Collision detected");
                            return false;
                        }
                        begin.y += step;
                    }
                    begin.x += step;
                }
            }
        }
        System.out.println("No collision detected");
        return true;
    }

    public static Vector2 getAbsoluteCoordinates(Vector2 mapCoordinates) {
        float difference = 256f / (MAP_TILE_WIDTH * MAP_SCALE);
//        System.out.println("Difference: " + difference);
//        System.out.println("Map coordinates: " + mapCoordinates.x + ", " + mapCoordinates.y);
        return new Vector2(mapCoordinates.x * difference, mapCoordinates.y * difference);
    }

    public static Vector2 getMapCoordinates(Vector2 absoluteCoordinates) {
        float difference = (MAP_TILE_WIDTH * MAP_SCALE) / 256f;
        return new Vector2(absoluteCoordinates.x * difference, absoluteCoordinates.y * difference);
    }

    public static boolean hasMapCollision(Body b1, Vector2 size, MapObjectHandler moh) {
        float b1x = b1.getWorldCenter().x;
        float b1y = b1.getWorldCenter().y;
        float b1w = size.x;
        float b1h = size.y;
        float startx = b1x - b1w/2;
        float starty = b1y - b1h/2;
        float endx = b1x + b1w/2;
        float endy = b1y + b1h/2;
        for (float x = startx; x < endx; x += Constants.MAP_TILE_WIDTH) {
            for (float y = starty; y < endy; y += Constants.MAP_TILE_WIDTH) {
                if (moh.collides(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    //return the half the smallest body size
    //used to iterate over an area checking for bodies
    //without skipping any bodies
    private static float smallestStep(World world) {
        //Start min size at max float value
        float minSize = Float.MAX_VALUE;
        //Gets bodies in world
        Array<Body> bodies;
            bodies = new Array<>();
            world.getBodies(bodies);

        //Iterate over every body
        for (Body body : bodies) {
            //Iterate over each fixture for each body
            for (Fixture fixture : body.getFixtureList()) {
                //Get fixture shape
                Shape shape = fixture.getShape();

                //Check if shape's "radius" is smaller than current min value
                if (shape.getRadius() < minSize) {
                    //Updates min size
                    minSize = shape.getRadius();
                }
            }
        }
        return minSize / 3;
    }

}
