package aaa.main.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import static aaa.main.util.Constants.*;

public class Colony {
    private String cName;

    private float resources;

    private float health;

    private int antsAlive;

    //private Ant[MAX_ANTS] antArray;

    private final Sprite sprite = new Sprite(new Texture(""));

    private Body colony;

    // Constructor
    public Colony(String name, float cResources, float cHealth, int ants, Body cBody) {
        cName = name;
        resources=cResources;
        health=cHealth;
        antsAlive=ants;
        colony=cBody;
        /*for (int i = 0; i < ants; i++) {
            if (antsAlive < MAX_ANTS) {
                antArray[i] = createAnt(colony.getPosition().x, colony.getPosition().y);
                antsAlive++;
            }
        }
        * */
    }

    //Overloaded constructors
    public Colony(String name, Body cBody) {this(name, DEFAULT_RESOURCES, DEFAULT_HEALTH, DEFAULT_ANTS, cBody);}

    public float getResources() {return resources;}

    public void setResources(float newResources) {resources=newResources;}

    public float getHealth() {return health;}

    public void setHealth(float newHealth) {health=newHealth;}

    //Render method for drawing colony sprite
    public void render(SpriteBatch batch) {
        // First we position and rotate the sprite correctly
        int posX = (int) colony.getPosition().x;
        int posY = (int) colony.getPosition().y;
        float rotation = (float) Math.toDegrees(colony.getAngle());
        sprite.setPosition(posX, posY);
        sprite.setRotation(rotation);

        // Then we simply draw it as a normal sprite.
        sprite.draw(batch);
    }


    //public Ant createAnt(float x, float y, String type) {}

    //probably also an overloaded
    //public Ant createAnt(float x, float y) {this(x,y,ANT)}

}
