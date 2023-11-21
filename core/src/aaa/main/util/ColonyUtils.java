package aaa.main.util;

import aaa.main.AntGame;
import aaa.main.game.Ant;
import aaa.main.game.map.Colony;
import aaa.main.screens.MainScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    public static void createColony(String name, boolean isPlayer, float cResources, float cHealth, int ants, Body cBody, OrthographicCamera cCamera, MainScreen screen) {
        Colony colony = new Colony(name, isPlayer, cResources, cHealth, ants, cBody, cCamera);
        screen.colonies.add(colony);
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

    public static void addAnt(Colony colony, String type, OrthographicCamera camera, World world) {
        colony.getAntsList().add(new Ant(colony, type, camera, world));
    }

    //Takes in a colony and checks for the closest open spot to spawn an Ant
    //Begins checking the right, down, left, and up. Returns the world coords
    //where the ant can spawn.
    public static Vector3 getAntSpawn(Colony colony, World world) {
        float step = smallestStep(world);

        Vector2 up = new Vector2(colony.getColonyBody().getPosition().x, colony.getColonyBody().getPosition().y);
        up.y += COLONY_HEIGHT/PPM;
        Vector2 down = new Vector2(colony.getColonyBody().getPosition().x, colony.getColonyBody().getPosition().y);
        down.y -= COLONY_HEIGHT/PPM;
        Vector2 left = new Vector2(colony.getColonyBody().getPosition().x, colony.getColonyBody().getPosition().y);
        left.x -= COLONY_WIDTH/PPM;
        Vector2 right = new Vector2(colony.getColonyBody().getPosition().x, colony.getColonyBody().getPosition().y);
        right.x += COLONY_WIDTH/PPM;




        //check an area in each direction which is the size of an ant
        if (checkArea(step, world, new Vector2(up.x -ANT_WIDTH/2f/PPM, up.y -ANT_HEIGHT/2f/PPM), new Vector2(up.x +ANT_WIDTH/2f/PPM,up.y +ANT_HEIGHT/2f/PPM))) {
            return new Vector3(up.x, up.y, 0);
        } else {
            System.out.println("Collision detected: up");
        }
        if (checkArea(step, world, new Vector2(down.x -ANT_WIDTH/2f/PPM, down.y -ANT_HEIGHT/2f/PPM), new Vector2(down.x +ANT_WIDTH/2f/PPM,down.y +ANT_HEIGHT/2f/PPM))) {
            return new Vector3(down.x, down.y, 0);
        } else {
            System.out.println("Collision detected: down");
        }
        if (checkArea(step, world, new Vector2(left.x -ANT_WIDTH/2f/PPM, left.y -ANT_HEIGHT/2f/PPM), new Vector2(left.x +ANT_WIDTH/2f/PPM,left.y +ANT_HEIGHT/2f/PPM))) {
            return new Vector3(left.x, left.y, 0);
        } else {
            System.out.println("Collision detected: left");
        }
        if (checkArea(step, world, new Vector2(right.x -ANT_WIDTH/2f/PPM, right.y -ANT_HEIGHT/2f/PPM), new Vector2(right.x +ANT_WIDTH/2f/PPM,right.y +ANT_HEIGHT/2f/PPM))) {
            return new Vector3(right.x, right.y, 0);
        } else {
            System.out.println("Collision detected: right");
        }

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
