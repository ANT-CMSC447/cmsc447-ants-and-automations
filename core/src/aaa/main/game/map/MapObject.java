package aaa.main.game.map;

import com.badlogic.gdx.math.Vector2;

public class MapObject {
    float x;
    float y;
    // TODO
    float width;
    float height;

    public Vector2 getPos() {
        return new Vector2(x, y);
    }

    public MapObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
