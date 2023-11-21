package aaa.main.game;

import aaa.main.game.map.Colony;
import aaa.main.util.ColonyUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static aaa.main.util.Constants.*;


public class Ant {
    private int health;
    private int attack;
    private String antType;
    private Colony colony;
    private Body antBody;
    private boolean playerOwned;
    private Texture texture;
    private Sprite sprite;
    private OrthographicCamera camera;


    public Ant() {

    }
    public Ant(Colony colony, String type, OrthographicCamera camera, World world) {
        this.colony = colony;
        this.antType = type;
        this.camera = camera;

        //calls CreateBody, which will get the spawn location from ColonyUtils.getAntSpawn
        //then will create the body in the world (shouldn't need to render), and then apply a sprite on top of it.
        Vector3 spawnLocation;

        spawnLocation = ColonyUtils.getAntSpawn(colony, world);

        if (spawnLocation != null) {
            System.out.println("Spawn location found for ant");
        } else {
            System.out.println("Error: No spawn location found for ant ");
            spawnLocation = new Vector3(0,0,0);
        }

        createBody(world, spawnLocation);
        createType();

    }

    public void render(SpriteBatch batch) {
        //first we position and rotate the sprite correctly
        // Project colony body position to screen coordinates
        Vector3 antPos = camera.project(new Vector3(antBody.getPosition().x, antBody.getPosition().y, 0));

        // Set sprite position and scale
        sprite.setPosition(antPos.x - sprite.getWidth() / 2, antPos.y - sprite.getHeight() / 2);
        sprite.setScale(2*(ANT_WIDTH/sprite.getWidth()) / camera.zoom);

        // Set sprite rotation
        float rotation = (float) Math.toDegrees(antBody.getAngle());
        sprite.setRotation(rotation);

        // Draw the sprite
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    public void dispose() {
        texture.dispose();
    }

    private Body createBody(World world, Vector3 position) {
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(position.x , position.y );
        def.fixedRotation = true;
        antBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(ANT_WIDTH / 2f / PPM, ANT_HEIGHT / 2f / PPM);

        antBody.createFixture(shape, 1.0f);
        shape.dispose();

        antBody.setLinearDamping(5.0f);

        return antBody;
    }

    private void createType() {
        if (antType.equals("Worker")) {
            texture = new Texture(Gdx.files.internal("worker ant.png"));
            sprite = new Sprite(texture);

        } else if (antType.equals("Soldier")) {
            texture = new Texture(Gdx.files.internal("soldier ant.png"));
            sprite = new Sprite(texture);
        } else {
            texture = new Texture(Gdx.files.internal("basic ant.png"));
            sprite = new Sprite(texture);
        }
        //set sprite to center on body
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    }


}
