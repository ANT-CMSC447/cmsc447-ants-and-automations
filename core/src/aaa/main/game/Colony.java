package aaa.main.game;

import aaa.main.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;

import static aaa.main.util.Constants.*;


public class Colony {
    private String cName;

    private float resources;

    private float health;

    private int antsAlive;

    //private Ant[MAX_ANTS] antArray;

    private Texture texture;

    private Sprite sprite;

    private Body colony;

    private OrthographicCamera camera;

    private boolean playerOwned = false;

    // Constructor
    public Colony(String name, boolean isPlayer, float cResources, float cHealth, int ants, Body cBody, OrthographicCamera cCamera) {
        playerOwned = isPlayer;
        cName = name;
        resources=cResources;
        health=cHealth;
        antsAlive=ants;
        colony=cBody;
        texture = new Texture(Gdx.files.internal(COLONY_TEXTURE_FILE));
        sprite = new Sprite(texture);
        camera = cCamera;

        sprite.setOriginCenter();
        sprite.setPosition(colony.getPosition().x - sprite.getWidth() / 2, colony.getPosition().y - sprite.getHeight() / 2);

        /*for (int i = 0; i < ants; i++) {
            if (antsAlive < MAX_ANTS) {
                antArray[i] = createAnt(colony.getPosition().x, colony.getPosition().y);
                antsAlive++;
            }
        }
        * */
    }

    //Overloaded constructors
    public Colony(String name, boolean isPlayer, Body cBody, OrthographicCamera camera) {
        this(name, false, DEFAULT_RESOURCES, DEFAULT_HEALTH, DEFAULT_ANTS, cBody, camera);
    }

    public float getResources() {return resources;}

    public void setResources(float newResources) {resources=newResources;}

    public float getHealth() {return health;}

    public void setHealth(float newHealth) {health=newHealth;}

    public int getAntsAlive() {return antsAlive;}

    public String getName() {return cName;}

    //Render method for drawing colony sprite
    public void render(SpriteBatch batch) {
        // First we position and rotate the sprite correctly

        //set sprite batch position to world coordinates

        Vector3 colonyPos = camera.project(new Vector3(colony.getPosition().x, colony.getPosition().y, 0));

        sprite.setScale(2/camera.zoom);

        float posX = colonyPos.x - sprite.getWidth()/2;
        float posY = colonyPos.y - sprite.getHeight()/2;
        float rotation = (float) Math.toDegrees(colony.getAngle());
        sprite.setRotation(rotation);
        sprite.setPosition(posX, posY);

        // Then we simply draw it as a normal sprite.
        sprite.draw(batch);
    }

    public Body getColonyBody() {return colony;}

    public void dispose() {
        texture.dispose();
    }

    public boolean isPlayerOwned() {return playerOwned;}


    //public Ant createAnt(float x, float y, String type) {}

    //probably also an overloaded
    //public Ant createAnt(float x, float y) {this(x,y,ANT)}

}