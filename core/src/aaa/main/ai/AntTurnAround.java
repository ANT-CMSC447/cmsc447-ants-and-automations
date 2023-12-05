package aaa.main.ai;

import aaa.main.game.map.MapObject;
import aaa.main.game.map.MapObjectHandler;
import aaa.main.util.Constants;

public class AntTurnAround implements Target{
    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isApplicable(MapObjectHandler moh, MapObject mobj) {
        return mobj.previous_collision;
    }

    @Override
    public void execute(float delta, MapObjectHandler moh, MapObject mobj) {
        if (!mobj.isRotating()) {
            boolean leftTurn = Math.random() > 0.5;
            double r = mobj.getRot() + Math.toRadians((leftTurn ? -1 : 1) * Constants.ANT_TURN_AROUND_AMOUNT);
//            System.out.println(mobj.getRot() + " -> " + r);
            mobj.backUp(0.5f);
            mobj.setRot(r);
        }
    }
}
