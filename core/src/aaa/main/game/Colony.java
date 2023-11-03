package aaa.main.game;

import aaa.main.game.PlayerInputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;

import static aaa.main.util.Constants.*;

public class Colony {
    private String cName;

    private float resources;

    private float health;

    private int antsAlive;

    //private Ant[MAX_ANTS] antArray;

    private Body colony;

    // Constructor
    public Colony(String name, float cResources, float cHealth, int ants, Body cBody) {
        cName = name;
        resources=cResources;
        health=cHealth;
        antsAlive=ants;
        colony=cBody;
        /*for (int i = 0; i < ants; i++) {
            if
        }
        * */
    }

    //Overloaded constructors
    public Colony(String name, Body cBody) {this(name, DEFAULT_RESOURCES, DEFAULT_HEALTH, DEFAULT_ANTS, cBody);}

    public float getResources() {return resources;}

    public void setResources(float newResources) {resources=newResources;}

    public float getHealth() {return health;}

    public void setHealth(float newHealth) {health=newHealth;}

}
