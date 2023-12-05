package aaa.main.ai;

import aaa.main.game.map.Ant;
import aaa.main.game.map.MapObject;
import aaa.main.game.map.MapObjectHandler;

public class AntGoHome implements Target{
    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isApplicable(MapObjectHandler moh, MapObject mobj) {
        Ant a = (Ant) mobj;

    }

    @Override
    public void execute(float delta, MapObjectHandler moh, MapObject mobj) {

    }
}
