package aaa.main.game;

import aaa.main.util.ColonyUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private boolean playerOwned = false;
    private Texture texture;
    private Sprite sprite;


    public Ant() {

    }
    public Ant(Colony colony, String type, World world) {
        this.colony = colony;
        this.antType = type;

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

        return antBody;
    }

    private Sprite createSprite() {
        if (antType.equals("Worker")) {
            texture = new Texture(Gdx.files.internal("workerAnt.png"));
            sprite = new Sprite(texture);

        } else if (antType.equals("Soldier")) {

        } else {

        }
        //set sprite to center on body
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);

        return sprite;
    }

}
