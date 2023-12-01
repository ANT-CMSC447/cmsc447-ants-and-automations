package aaa.main.game.map;

import com.badlogic.gdx.math.Vector2;

public class MapObject {
    float x;
    float y;
    // TODO
    float width;
    float height;

    public MapObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height=height;
    }

    public MapObject(float x, float y) {
        this(x, y, 1, 1);
    }

    public MapObject() {
        this(0, 0);
    }

    public Vector2 getPos() {
        return new Vector2(x, y);
    }

    public Vector2 getSize() { return new Vector2(width, height); }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
