package aaa.main.game.map;

import aaa.main.util.ColonyUtils;
import aaa.main.util.RenderUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import static aaa.main.util.Constants.*;


public class Colony extends MapObject {
    private String cName;

    private float resources;

    private float health;

    private int antsAlive;

    private Texture texture;

    private Sprite sprite;

    private Body colonyBody;

    private OrthographicCamera camera;
    private World world;

    private boolean playerOwned;

    private ArrayList<Ant> antList = new ArrayList<>();

    float x;
    float y;

    // Constructor
    public Colony(String name, boolean isPlayer, float cResources, float cHealth, int ants, float x, float y, OrthographicCamera cCamera, World world) {
        super(x, y);
        playerOwned = isPlayer;
        cName = name;
        resources=cResources;
        health=cHealth;
        antsAlive=ants;
        colonyBody = RenderUtils.createBox(150,150,COLONY_WIDTH,COLONY_HEIGHT,true, world);
        texture = new Texture(Gdx.files.internal(COLONY_TEXTURE_FILE));
        sprite = new Sprite(texture);
        camera = cCamera;
        this.world = world;
        this.x = x;
        this.y = y;

        //set sprite to center on body
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        //set position of sprite to position of body
        sprite.setPosition(colonyBody.getPosition().x, colonyBody.getPosition().y);

        /*for (int i = 0; i < ants; i++) {
            if (antsAlive < MAX_ANTS) {
                antArray[i] = createAnt(colony.getPosition().x, colony.getPosition().y);
                antsAlive++;
            }
        }
        * */
    }

    //Overloaded constructors
    public Colony(String name, boolean isPlayer, float x, float y, OrthographicCamera camera, World world) {
        this(name, false, DEFAULT_RESOURCES, DEFAULT_HEALTH, DEFAULT_ANTS, x, y, camera, world);
    }

    public float getResources() {return resources;}

    public void setResources(float newResources) {resources=newResources;}

    public float getHealth() {return health;}

    public void setHealth(float newHealth) {health=newHealth;}

    public int getAntsAlive() {return antsAlive;}

    public String getName() {return cName;}

    //Render method for drawing colony sprite
    public void render(SpriteBatch batch) {
         //first we position and rotate the sprite correctly
        // Project colony body position to screen coordinates
//        Vector3 colonyPos = camera.project(new Vector3(colony.getPosition().x, colony.getPosition().y, 0));
//        System.out.println("Colony position: " + x + ", " + y);
        Vector2 absPos = ColonyUtils.getAbsoluteCoordinates(new Vector2(this.x, this.y));
//        System.out.println("Translated position: " + absPos.x + ", " + absPos.y);
        colonyBody.setTransform(absPos, colonyBody.getAngle());
        Vector3 colonyPos = camera.project(new Vector3(absPos.x, absPos.y, 0));

        // Set sprite position and scale
        sprite.setPosition(colonyPos.x - sprite.getWidth() / 2, colonyPos.y - sprite.getHeight() / 2);
        sprite.setScale(2*(COLONY_WIDTH/sprite.getWidth()) / camera.zoom);

        // Set sprite rotation
        float rotation = (float) Math.toDegrees(colonyBody.getAngle());
        sprite.setRotation(rotation);

        // Draw the sprite
        batch.begin();
        sprite.draw(batch);
        batch.end();

        batch = new SpriteBatch();

        for (Ant ant : antList) {
            ant.render(batch);
        }
    }

    public Body getColonyBody() {return colonyBody;}

    public void dispose() {
        for (Ant ant : antList) {
            ant.dispose();
        }
        texture.dispose();
    }

    public boolean isPlayerOwned() {return playerOwned;}

    public ArrayList<Ant> getAntsList() {return antList;}


    //public Ant createAnt(float x, float y, String type) {}

    //probably also an overloaded
    //public Ant createAnt(float x, float y) {this(x,y,ANT)}

}