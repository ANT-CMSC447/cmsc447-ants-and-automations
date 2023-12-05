package aaa.main.ai;

import aaa.main.game.map.MapObject;
import aaa.main.game.map.MapObjectHandler;

public class Random implements Target {

    public int getPriority() {
        return 0;
    }

    public boolean isApplicable(MapObjectHandler moh, MapObject ant) {
        return true;
    }

    public void execute(MapObjectHandler moh, MapObject ant) {

    }
}
