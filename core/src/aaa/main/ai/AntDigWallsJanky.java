package aaa.main.ai;

import aaa.main.game.map.MapObject;
import aaa.main.game.map.MapObjectHandler;

public class AntDigWallsJanky implements Target{
    @Override
    public int getPriority() {
        return 9;
    }

    @Override
    public boolean isApplicable(MapObjectHandler moh, MapObject mobj) {
        return false;
    }

    @Override
    public void execute(float delta, MapObjectHandler moh, MapObject mobj) {

    }
}
