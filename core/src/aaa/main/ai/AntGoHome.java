package aaa.main.ai;

import aaa.main.game.map.Ant;
import aaa.main.game.map.MapObject;
import aaa.main.game.map.MapObjectHandler;
import aaa.main.util.Constants;

public class AntGoHome implements Target{
    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isApplicable(MapObjectHandler moh, MapObject mobj) {
        Ant a = (Ant) mobj;
        if (a.getAntResources() >= Constants.ANT_RES_MAX) {
            a.wantsHome = true;
            a.wantsResource = false;
        }
        return a.wantsHome;
    }

    @Override
    public void execute(float delta, MapObjectHandler moh, MapObject mobj) {

    }
}
