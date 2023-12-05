package aaa.main.ai;

import aaa.main.game.map.Ant;
import aaa.main.game.map.MapObject;
import aaa.main.game.map.MapObjectHandler;
import aaa.main.util.Constants;

public class AntRandom implements Target {
    private float random_turn_cooldown = 0f;
    private float walk_cooldown = 0f;

    public int getPriority() {
        return 0;
    }

    public boolean isApplicable(MapObjectHandler moh, MapObject ant) {
        return true;
    }

    public void execute(float delta, MapObjectHandler moh, MapObject ant) {
        Ant a = (Ant) ant;
        if (walk_cooldown > 0) {
            walk_cooldown -= delta;
        }
        if (random_turn_cooldown > 0) {
            random_turn_cooldown -= delta;
        }
        if (walk_cooldown <= 0) {
            if (a.isMoving()) {
                walk_cooldown = (float) (Math.random() * Constants.ANT_WALK_COOLDOWN_MAX);
            }
            System.out.println("Walking");
            // how much does the ant want to walk forward
            double forward = Math.max(Constants.ANT_RANDOM_WALK_MIN, Math.random() * Constants.ANT_RANDOM_WALK_MAX) * (double) Constants.MAP_TILE_PIXELS;

            ant.moveForward((float) forward);

            walk_cooldown = (float) (Math.random() * Constants.ANT_WALK_COOLDOWN_MAX);
        }
        if (random_turn_cooldown <= 0) {
            System.out.println("Turning randomly");
            double random_turn = (
                    (Math.random() * Constants.ANT_RANDOM_TURN_RADIUS) - (Constants.ANT_RANDOM_TURN_RADIUS / 2)
            );
            random_turn = Math.signum(random_turn) * Math.max(Math.abs(random_turn), Constants.ANT_RANDOM_TURN_RADIUS_MIN);
            a.setRot(a.getRot() + random_turn);
            random_turn_cooldown = (float) (Math.random() * (Constants.ANT_RANDOM_TURN_TIME_MAX - Constants.ANT_RANDOM_TURN_TIME_MIN) + Constants.ANT_RANDOM_TURN_TIME_MIN);
        }
    }
}
