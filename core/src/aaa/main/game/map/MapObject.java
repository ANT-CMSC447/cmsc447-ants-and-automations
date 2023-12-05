package aaa.main.game.map;

import aaa.main.ai.Target;
import aaa.main.util.Constants;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public abstract class MapObject {

    private List<Target> targets = new ArrayList<>();

    float x;
    float y;

    Optional<Float> expected_walk;

    float width;
    float height;

    float vel_x;
    float vel_y;

    // this is in radians (thanks, libgdx)
    double rot;

    Optional<Double> expected_rot;

    public boolean previous_collision = false;

    public MapObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.expected_walk = Optional.empty();
        this.expected_rot = Optional.empty();
        this.width = width;
        this.height = height;
    }

    public MapObject(float x, float y, float width, float height, Target[] targets) {
        this(x, y, width, height);
        this.targets.addAll(Arrays.asList(targets));
    }

    public MapObject(float x, float y, float width, float height, List<Target> targets) {
        this(x, y, width, height);
        this.targets.addAll(targets);
    }

    public MapObject(float x, float y) {
        this(x, y, 1, 1);
    }

    public MapObject() {
        this(0, 0);
    }

    public Vector2 getPos() {
        return new Vector2(x, y);
    }

    public Vector2 getSize() { return new Vector2(width, height); }

    public double getRot() { return this.rot; }

    public Vector2 getVel() { return new Vector2(vel_x, vel_y); }

    public void setRot(double rot) {
        this.expected_rot = Optional.of(rot);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void hitSomething() {
        System.out.println("Hit something");
        this.expected_walk = Optional.empty();
        this.vel_x = 0;
        this.vel_y = 0;
        previous_collision = true;
    }

    public void moveForward(float n) {
        this.expected_walk = Optional.of(n);
    }

    public void backUp(float n) {
        this.expected_walk = Optional.of(-n);
    }

    public boolean isMoving() {
        return this.expected_walk.isPresent();
    }

    public boolean isRotating() {
        return this.expected_rot.isPresent();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void executeAI(float delta, MapObjectHandler moh) {
        if (this.isMoving() || this.isRotating()) return;
        this.targets.sort(Comparator.comparingInt(Target::getPriority).reversed());
        for (Target t : this.targets) {
            if (t.isApplicable(moh, this)) {
                t.execute(delta, moh, this);
                break;
            }
        }
    }

    public void update(float delta, MapObjectHandler moh) {
//        this.x = this.expected_x;
//        this.y = this.expected_y;
        double next = this.rot;
        if (this.expected_rot.isPresent()) {
            double dif = this.expected_rot.get() - this.rot;
//            System.out.println("Rot: " + this.rot + " , " + this.expected_rot + " , " + dif);
            if (dif <= 0.01) {
                this.expected_rot = Optional.empty();
            }
            if (dif != 0.0) {
//                System.out.println("WE WANT TO TURN!!!!!!!!!!!!!!!!!!!!!!!!!!" + dif + " , " + Math.min(Math.abs(dif), Math.PI / 8) + " , " + delta);
                next += Math.signum(dif) * Math.min(Math.abs(dif), Math.PI * Constants.ANT_TURN_SPEED) * delta;
//                System.out.println(this.rot + " / " + next);
                this.rot = next;
                this.vel_x = 0;
                this.vel_y = 0;
                return; // don't move if we're turning
            }
        }
        double dif_n = 0.0;
        double speed = Constants.ANT_WALK_SPEED * delta;
        if (this.expected_walk.isPresent()) {
            dif_n = this.expected_walk.get();
        }

        if (dif_n == 0 || Math.abs(dif_n) < speed) {
            this.expected_walk = Optional.empty();
            return;
        }

        System.out.println(dif_n + ", " + speed);
        if (dif_n < 0.0) {
            // back up

            double vel_x = Math.cos(next) * speed * -1;
            double vel_y = Math.sin(next) * speed * -1;

            this.expected_walk = Optional.of((float) (dif_n + speed));

            this.vel_x = (float)vel_x;
            this.vel_y = (float)vel_y;

//            double next_x = this.x + vel_x + (this.width / 2);
//            double next_y = this.y + vel_y + (this.height / 2);
//            System.out.println(this.getClass().getName() + ", " + (this instanceof Ant));
//            if (!moh.collides(this)) {
//                previous_collision = false;
//                this.vel_x = (float)vel_x;
//                this.vel_y = (float)vel_y;
//            } else {
//                previous_collision = true;
//                this.vel_x = 0;
//                this.vel_y = 0;
//                this.expected_walk = Optional.empty(); // stop walking you dumb ant
////                System.out.println("MapObject collision "
////                        + this.x + ", " + this.y + " -> "
////                        + (float) next_x + ", " + (float) next_y);
//            }
        }
        if (dif_n > 0.0) {

            double vel_x = Math.cos(next) * speed;
            double vel_y = Math.sin(next) * speed;

            this.expected_walk = Optional.of((float) (dif_n - speed));

//            double next_x = this.x + vel_x + (this.width / 2);
//            double next_y = this.y + vel_y + (this.height / 2);
            this.vel_x = (float)vel_x;
            this.vel_y = (float)vel_y;
//            System.out.println(this.getClass().getName() + ", " + (this instanceof Ant));
//            if (!moh.collides(this)) {
//                previous_collision = false;
//            } else {
//                previous_collision = true;
//                this.vel_x = 0;
//                this.vel_y = 0;
//                this.expected_walk = Optional.empty(); // stop walking you dumb ant
////                System.out.println("MapObject collision "
////                        + this.x + ", " + this.y + " -> "
////                        + (float) next_x + ", " + (float) next_y);
//            }
        }
    }
}
