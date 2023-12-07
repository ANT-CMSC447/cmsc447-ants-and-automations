package aaa.main.game.map;

import aaa.main.ai.AntRandom;
import aaa.main.ai.AntTurnAround;
import aaa.main.ai.Target;
import aaa.main.game.map.Colony;
import aaa.main.util.ColonyUtils;
import aaa.main.util.CoordinateUtils;
import aaa.main.util.RenderUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

import static aaa.main.util.Constants.*;


public class Ant extends MapObject {
    private int health;
    private int attack;
    private String antType;
    private Colony colony;
    private Body antBody;
    private float antResources;
    private boolean playerOwned;
    private Texture texture;
    private Sprite sprite;
    private OrthographicCamera camera;
    private World world;

    public boolean wantsResource = false;
    public boolean wantsHome = false;

    private static final float rotOffset = -45;

    float x;
    float y;
    public Ant(Colony colony, String type, OrthographicCamera camera, World world, MapObjectHandler moh) {
        super(0,0, ANT_WIDTH, ANT_HEIGHT, new ArrayList<Target>() {{
            add(new AntRandom());
            add(new AntTurnAround());
        }});
//        super(0,0, ANT_WIDTH, ANT_HEIGHT);
        this.colony = colony;
        this.antType = type;
        this.camera = camera;
        this.world = world;
        antResources=0f;

        //calls CreateBody, which will get the spawn location from ColonyUtils.getAntSpawn
        //then will create the body in the world (shouldn't need to render), and then apply a sprite on top of it.
        Vector3 spawnLocation;

        spawnLocation = ColonyUtils.getAntSpawn(colony, world, moh);

        if (spawnLocation != null) {
            System.out.println("Spawn location found for ant");
        } else {
            System.out.println("Error: No spawn location found for ant ");
            spawnLocation = new Vector3(0,0,0);
        }

        this.setPosition(spawnLocation.x, spawnLocation.y);

        createBody(world, spawnLocation);
        createType();
        moh.addObject(this, this.antBody);
    }
    public void render(SpriteBatch batch) {
        //first we position and rotate the sprite correctly
        // Project colony body position to screen coordinates
        Vector2 pos = this.getPos();
        Vector2 absPos = CoordinateUtils.getAbsoluteCoordinates(pos);
        Vector2 actualPos = antBody.getPosition();
        Vector2 velocity = this.getVel();
        Vector2 bodyVel = antBody.getLinearVelocity();
//        System.out.println("Ant velocity: " + velocity.x + ", " + velocity.y + " vs " + bodyVel.x + ", " + bodyVel.y);
//        if (velocity.x != 0 || bodyVel.x != 0) {
//            double a = Math.signum(velocity.x);
//            double b = Math.signum(bodyVel.x);
//            if (a != b || velocity.x == 0.0 || bodyVel.x == 0.0) {
//                System.out.println("x mismatch");
//                this.hitSomething();
//            }
//        }
//        if (velocity.y != 0 || bodyVel.y != 0) {
//            double a = Math.signum(velocity.y);
//            double b = Math.signum(bodyVel.y);
//            if (a != b || velocity.y == 0.0 || bodyVel.y == 0.0) {
//                System.out.println("y mismatch");
//                this.hitSomething();
//            }
//        }
        if (!velocity.isZero()) {
            antBody.setLinearVelocity(velocity); // will this work?!?!?!?!?
        }
        if (!absPos.equals(actualPos)) {
//            System.out.println("Ant position mismatch: " + absPos.x + ", " + absPos.y + " vs " + actualPos.x + ", " + actualPos.y);
            Vector2 conv = CoordinateUtils.getMapCoordinates(actualPos);
            this.setPosition(conv.x, conv.y);
        }
        Vector3 antPos = camera.project(new Vector3(actualPos.x, actualPos.y, 0));
        double rot = this.getRot();

        this.antBody.setTransform(actualPos.x, actualPos.y, (float) rot);

        // Set sprite position and scale
        sprite.setPosition(antPos.x - sprite.getWidth() / 2, antPos.y - sprite.getHeight() / 2);

        // was 16
        int SCALE_FACTOR = MAP_TILE_PIXELS * ANT_WIDTH * TILE_CONVERSION_FACTOR;

        sprite.setScale(2*(SCALE_FACTOR/sprite.getWidth()) / camera.zoom);

        // Set sprite rotation
        float rotation = (float) Math.toDegrees(antBody.getAngle()) + rotOffset;
        sprite.setRotation(rotation);

        // Draw the sprite
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    public void setAntResources(float resources) {
        this.antResources = resources;
    }

    public float getAntResources() {
        return this.antResources;
    }

    public void dispose() {
        texture.dispose();
    }

    private Body createBody(World world, Vector3 position) {
        Vector2 converted = CoordinateUtils.getAbsoluteCoordinates(new Vector2(position.x, position.y));

        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(converted.x , converted.y );
        def.fixedRotation = true;
        antBody = world.createBody(def);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox((ANT_WIDTH * TILE_CONVERSION_FACTOR * MAP_TILE_PIXELS) / 2f / PPM, (ANT_HEIGHT * TILE_CONVERSION_FACTOR * MAP_TILE_PIXELS) / 2f / PPM);
//        CircleShape shape = new CircleShape();
//        shape.setPosition(new Vector2(position.x, position.y));
//        shape.setRadius(ANT_WIDTH * TILE_CONVERSION_FACTOR * MAP_TILE_PIXELS);
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

    public Body getAntBody() {
        return antBody;
    }

    public String getName() {
        return antType;
    }

    public void setName(String newName) {
        antType = newName;
    }

    public void incResources(float amount) {
        antResources+=amount;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int newHealth) {
        health = newHealth;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int newAttack) {
        attack = newAttack;
    }

    public Colony getColony() {
        return colony;
    }

    public void setColony(Colony newColony) {
        colony = newColony;
    }

}
