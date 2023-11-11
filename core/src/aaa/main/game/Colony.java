package aaa.main.game;

import aaa.main.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import static aaa.main.util.Constants.*;

public class Colony {
    private final String cName;

    private float resources;

    private float health;

    private int antsAlive;

    //private Ant[MAX_ANTS] antArray;

    private final Texture texture;

    private Sprite sprite;

    private Body colony;

    // Constructor
    public Colony(String name, float cResources, float cHealth, int ants, Body cBody) {
        cName = name;
        resources=cResources;
        health=cHealth;
        antsAlive=ants;
        colony=cBody;
        texture = new Texture(Gdx.files.internal(COLONY_TEXTURE_FILE));
        sprite = new Sprite(texture);

        //for rendering later
        sprite.setScale((float)SCREEN_WIDTH/BORDER_WIDTH,(float)SCREEN_HEIGHT/BORDER_HEIGHT);
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

    public int getAntsAlive() {return antsAlive;}

    public String getName() {return cName;}

    //Render method for drawing colony sprite
    public void render(SpriteBatch batch) {
        // First we position and rotate the sprite correctly
        float posX = colony.getPosition().x * (10.8f *1.0775f);
        float posY = colony.getPosition().y * (7.20f *1.075f);
        float rotation = (float) Math.toDegrees(colony.getAngle());
        sprite.setRotation(rotation);
        sprite.setPosition(posX, posY);

        // Then we simply draw it as a normal sprite.
        sprite.draw(batch);
    }

    public void dispose() {
        texture.dispose();
    }


    //public Ant createAnt(float x, float y, String type) {}

    //probably also an overloaded
    //public Ant createAnt(float x, float y) {this(x,y,ANT)}

}
