package aaa.main.util;

import aaa.main.AntGame;
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

    public static Body getColonyBody(Colony colony, MainScreen screen) {
        return colony.getColonyBody();
    }

}
