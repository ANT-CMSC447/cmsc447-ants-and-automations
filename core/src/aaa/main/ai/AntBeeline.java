package aaa.main.ai;

import aaa.main.game.map.Ant;
import aaa.main.game.map.MapObject;
import aaa.main.game.map.MapObjectHandler;

public class AntBeeline implements Target {
    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isApplicable(MapObjectHandler moh, MapObject mobj) {
        Ant a = (Ant) mobj;
        if (a.getColony().getResources() < 10f && a.getAntResources() <= 10f && !a.wantsHome) {
            a.wantsResource = true;
        }
        return a.wantsResource;
    }

    @Override
    public void execute(float delta, MapObjectHandler moh, MapObject mobj) {

    }
}
