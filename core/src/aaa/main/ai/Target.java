package aaa.main.ai;

import aaa.main.game.map.MapObject;
import aaa.main.game.map.MapObjectHandler;

public interface Target {
    int getPriority();
    boolean isApplicable(MapObjectHandler moh, MapObject mobj);
    void execute(float delta, MapObjectHandler moh, MapObject mobj);
}
