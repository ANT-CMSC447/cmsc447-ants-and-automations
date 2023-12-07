package aaa.main.game.map;

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

public class FoodSource extends MapObject {
    private boolean candy;
    private float foodRemaining;
    private Texture texture;
    private Sprite sprite;
    private OrthographicCamera camera;
    private Body source;
    private World world;

    public FoodSource(boolean type, OrthographicCamera camera, World world, Vector2 spawnPosition) {
        super(spawnPosition.x, spawnPosition.y);
        if (type) {
            setSize(CANDY_SIZE, CANDY_SIZE);
            foodRemaining = CANDY_START;
            source = RenderUtils.createBox(spawnPosition.x,spawnPosition.y,CANDY_SIZE * TILE_CONVERSION_FACTOR * MAP_TILE_PIXELS,CANDY_SIZE * TILE_CONVERSION_FACTOR * MAP_TILE_PIXELS,true, world);

        } else {
            setSize(FORAGE_SIZE, FORAGE_SIZE);
            foodRemaining = FORAGE_START;
            source = RenderUtils.createBox(spawnPosition.x,spawnPosition.y,FORAGE_SIZE * TILE_CONVERSION_FACTOR * MAP_TILE_PIXELS,FORAGE_SIZE * TILE_CONVERSION_FACTOR * MAP_TILE_PIXELS,true, world);
        }
        Vector2 transform = CoordinateUtils.getAbsoluteCoordinates(new Vector2(x, y));
        source.setTransform(transform, source.getAngle());
        this.candy = type;
        this.camera = camera;
        this.world = world;
        createTexture();
    }

   public float getFoodRemaining() {
        return foodRemaining;
   }

   public void harvest(Ant ant) {
        //Harvest an attacks worth of resources if there is enough remaining
        //otherwise harvest the remaining amount
        if (ant.getAttack() >= foodRemaining) {
            foodRemaining-= ant.getAttack();
            ant.incResources(ant.getAttack());
        } else {
            ant.incResources(foodRemaining);
            foodRemaining-=foodRemaining;
            destroy(); //the source is empty and is destroyed
        }
   }

   public boolean getType() { return candy; }

   public void destroy() {
        world.destroyBody(source);
        dispose();
   }

    public void render(SpriteBatch batch) {
        //first we position and rotate the sprite correctly
        // Project colony body position to screen coordinates
        Vector2 absPos = CoordinateUtils.getAbsoluteCoordinates(new Vector2(this.x, this.y));
        Vector2 actualPos = source.getPosition();
        if (!absPos.equals(actualPos)) {
            System.out.println("Positions different! expected: " + absPos.x + ", " + absPos.y + " actual: " + actualPos.x + ", " + actualPos.y);
            Vector2 conv = CoordinateUtils.getMapCoordinates(actualPos);
            this.setPosition(conv.x, conv.y);
        }
        source.setTransform(absPos, source.getAngle());

        Vector3 sourcePos = camera.project(new Vector3(actualPos.x, actualPos.y, 0));

        int SCALE_FACTOR = MAP_TILE_PIXELS  * TILE_CONVERSION_FACTOR;
        // Set sprite position and scale
        sprite.setPosition(sourcePos.x - sprite.getWidth() / 2, sourcePos.y - sprite.getHeight() / 2);
        if (candy){
            sprite.setScale(2*(SCALE_FACTOR*CANDY_SIZE/sprite.getWidth()) / camera.zoom);
        } else {
            sprite.setScale(2*(SCALE_FACTOR*FORAGE_SIZE/sprite.getWidth()) / camera.zoom);
        }

        // Set sprite rotation
        float rotation = (float) Math.toDegrees(source.getAngle());
        sprite.setRotation(rotation);

        // Draw the sprite
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    public void dispose() {
        texture.dispose();
    }

    /*private void createBody(World world, Vector3 position) {
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(position.x , position.y );
        def.fixedRotation = true;
        source = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        if (candy) {
            shape.setAsBox(CANDY_SIZE / 2f / PPM, CANDY_SIZE / 2f / PPM);
        } else {
            shape.setAsBox(FORAGE_SIZE / 2f / PPM, FORAGE_SIZE / 2f / PPM);
        }

        source.createFixture(shape, 1.0f);
        shape.dispose();

        source.setLinearDamping(5.0f);
    }*/

    private void createTexture() {
        if (candy) {
            texture = new Texture(Gdx.files.internal("candy source.png"));
            sprite = new Sprite(texture);
        } else {
            texture = new Texture(Gdx.files.internal("forage food.png"));
            sprite = new Sprite(texture);
        }
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    }
}
