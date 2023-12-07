package aaa.main.ai;

import aaa.main.game.map.MapObject;
import aaa.main.game.map.MapObjectHandler;

public class AntBeeline implements Target {
    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isApplicable(MapObjectHandler moh, MapObject mobj) {

        return ;
    }

    @Override
    public void execute(float delta, MapObjectHandler moh, MapObject mobj) {

    }
}
