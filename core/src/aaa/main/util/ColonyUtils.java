package aaa.main.util;

import aaa.main.AntGame;
import aaa.main.game.Ant;
import aaa.main.game.Colony;
import aaa.main.screens.MainScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import jdk.tools.jmod.Main;

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

    public static void addAnt(Colony colony, String type, World world) {
        colony.getAntsList().add(new Ant(colony, type, world));
    }

    //Takes in a colony and checks for the closest open spot to spawn an Ant
    //Begins checking the right, down, left, and up. Returns the world coords
    //where the ant can spawn.
    public static Vector3 getAntSpawn(Colony colony, World world) {
        float step = smallestStep(world);

        Vector2 up = colony.getColonyBody().getPosition();
        up.y += COLONY_HEIGHT;
        Vector2 down = colony.getColonyBody().getPosition();
        down.y -= COLONY_HEIGHT;
        Vector2 left = colony.getColonyBody().getPosition();
        left.x -= COLONY_WIDTH;
        Vector2 right = colony.getColonyBody().getPosition();
        right.x += COLONY_WIDTH;





        if (checkArea(step, world, up.add(-COLONY_WIDTH/2, -COLONY_HEIGHT/2), up.add(COLONY_WIDTH/2,COLONY_HEIGHT/2))) {
            return new Vector3(up.x, up.y, 0);
        } else {
            System.out.println("Collision detected: up");
        }
        if (checkArea(step, world, down.add(-COLONY_WIDTH/2, -COLONY_HEIGHT/2), down.add(COLONY_WIDTH/2,COLONY_HEIGHT/2))) {
            return new Vector3(down.x, down.y, 0);
        } else {
            System.out.println("Collision detected: down");
        }
        if (checkArea(step, world, left.add(-COLONY_WIDTH/2, -COLONY_HEIGHT/2), left.add(COLONY_WIDTH/2,COLONY_HEIGHT/2))) {
            return new Vector3(left.x, left.y, 0);
        } else {
            System.out.println("Collision detected: left");
        }
        if (checkArea(step, world, right.add(-COLONY_WIDTH/2, -COLONY_HEIGHT/2), right.add(COLONY_WIDTH/2,COLONY_HEIGHT/2))) {
            return new Vector3(right.x, right.y, 0);
        } else {
            System.out.println("Collision detected: right");
        }

        return null;
    }

    //Returns false when the area is NOT clear.
    private static boolean checkArea(float step, World world, Vector2 begin, Vector2 end) {
        Array<Body> bodies = new Array<Body>();
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
            bodies = new Array<Body>();
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
        return minSize / 2;
    }

}
