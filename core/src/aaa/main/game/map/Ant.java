package aaa.main.game.map;

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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static aaa.main.util.Constants.*;


public class Ant extends MapObject {
    private int health;
    private int attack;
    private String antType;
    private Colony colony;
    private Body antBody;
    private boolean playerOwned;
    private Texture texture;
    private Sprite sprite;
    private OrthographicCamera camera;

    float x;
    float y;
    public Ant(Colony colony, String type, OrthographicCamera camera, World world, MapObjectHandler moh) {
        super(0,0, ANT_WIDTH, ANT_HEIGHT);
        this.colony = colony;
        this.antType = type;
        this.camera = camera;

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
        moh.addObject(this);
    }

    public void render(SpriteBatch batch) {
        //first we position and rotate the sprite correctly
        // Project colony body position to screen coordinates
        Vector2 pos = this.getPos();
        Vector2 absPos = CoordinateUtils.getAbsoluteCoordinates(pos);
        Vector2 actualPos = antBody.getPosition();
        if (!absPos.equals(actualPos)) {
            System.out.println("Ant position mismatch: " + absPos.x + ", " + absPos.y + " vs " + actualPos.x + ", " + actualPos.y);
            Vector2 conv = CoordinateUtils.getMapCoordinates(actualPos);
            this.setPosition(conv.x, conv.y);
        }
        Vector3 antPos = camera.project(new Vector3(actualPos.x, actualPos.y, 0));
        this.antBody.setTransform(actualPos.x, actualPos.y, this.antBody.getAngle());

        // Set sprite position and scale
        sprite.setPosition(antPos.x - sprite.getWidth() / 2, antPos.y - sprite.getHeight() / 2);

        // was 16
        int SCALE_FACTOR = MAP_TILE_PIXELS * ANT_WIDTH * TILE_CONVERSION_FACTOR;

        sprite.setScale(2*(SCALE_FACTOR/sprite.getWidth()) / camera.zoom);

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
        Vector2 converted = CoordinateUtils.getAbsoluteCoordinates(new Vector2(position.x, position.y));

        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(converted.x , converted.y );
        def.fixedRotation = true;
        antBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((ANT_WIDTH * TILE_CONVERSION_FACTOR * MAP_TILE_PIXELS) / 2f / PPM, (ANT_HEIGHT * TILE_CONVERSION_FACTOR * MAP_TILE_PIXELS) / 2f / PPM);

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
