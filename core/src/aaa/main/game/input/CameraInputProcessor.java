package aaa.main.game.input;

import aaa.main.screens.MainScreen;
import aaa.main.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import aaa.main.screens.MainScreen;
import aaa.main.game.map.Ant;
import aaa.main.game.map.Colony;
import com.badlogic.gdx.physics.box2d.Body;

import static aaa.main.util.Constants.CAMERA_MOVE_SPEED;


public class CameraInputProcessor implements InputProcessor {
    private OrthographicCamera camera;
    private float lastX, lastY;

    public CameraInputProcessor(OrthographicCamera camera) {
        this.camera = camera;
    }

    public Body selectedObject = null;

    //handler for horizontal/vertical movement
    @Override
    public boolean keyDown(int keycode) {
        //Camera zoom controls
        if (keycode == Input.Keys.MINUS) {
            //zoom out
            if (camera.zoom + 0.02f <= Constants.MAX_ZOOM) {
                camera.zoom += 0.02f;
            } else {
                camera.zoom = Constants.MAX_ZOOM;
            }
        } else if (keycode == Input.Keys.EQUALS) {
            //zoom in
            if (camera.zoom - 0.02f >= Constants.MIN_ZOOM) {
                camera.zoom -= 0.02f;
            } else {
                camera.zoom = Constants.MIN_ZOOM;
            }
        }

        //Camera movement controls
        if (keycode == Input.Keys.LEFT) {
            //move left
            camera.translate(-CAMERA_MOVE_SPEED, 0);
        }
        if (keycode == Input.Keys.RIGHT) {
            //move right
            camera.translate(CAMERA_MOVE_SPEED, 0);
        }
        if (keycode == Input.Keys.UP) {
            //move up
            camera.translate(0, CAMERA_MOVE_SPEED);
        }
        if (keycode == Input.Keys.DOWN) {
            //move down
            camera.translate(0, -CAMERA_MOVE_SPEED);
        }

        if (keycode == Input.Keys.C)
        {

        }
        return false;
    }

    //handler for diagonal movement
    public boolean keyDown2(int keycode, float modifier) {
        if (keycode == Input.Keys.LEFT) {
            //move left
            camera.translate(-CAMERA_MOVE_SPEED/modifier, 0);
        }
        if (keycode == Input.Keys.RIGHT) {
            //move right
            camera.translate(CAMERA_MOVE_SPEED/modifier, 0);
        }
        if (keycode == Input.Keys.UP) {
            //move up
            camera.translate(0, CAMERA_MOVE_SPEED/modifier);
        }
        if (keycode == Input.Keys.DOWN) {
            //move down
            camera.translate(0, -CAMERA_MOVE_SPEED/modifier);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //logic for switching player context should be to check if position clicked is on a object
        //checks would have to be in world coordinates, not screen coordinates
        //also would need to check for clicks on UI buttons, should be in screen coordinates then
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button, MainScreen screen) {
        //logic for switching player context should be to check if position clicked is on a object
        //checks would have to be in world coordinates, not screen coordinates
        //also would need to check for clicks on UI buttons, should be in screen coordinates then

        //print the click position in world coordinates
        Vector3 clickPos = camera.unproject(new Vector3(screenX, screenY, 0));

        //print the click position in world coordinates
        System.out.println("Click position: " + clickPos.x + ", " + clickPos.y);

        //print all colony locations
        //for (Colony colony : screen.colonies) {
        //    System.out.println("Colony position: " + colony.getColonyBody().getPosition().x*Constants.PPM + ", " + colony.getColonyBody().getPosition().y*Constants.PPM);
        //}

        //print all colony coordinates
        String result = isBodyClicked(clickPos, screen);
        if (result != null) {
            System.out.println(result);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public void setSelectedObject(Body body) {
        selectedObject = body;
    }

    //uncomment comments below for debugging, works as expected so far
    //switches control context to clicked body, focuses camera on clicked body (does not follow body, need to implement if wanted)
    private String isBodyClicked(Vector3 clickPos, MainScreen screen) {
        for (Colony colony : screen.colonies) {
            float colonyXMin = (colony.getColonyBody().getPosition().x*Constants.PPM) - ((Constants.COLONY_WIDTH * Constants.MAP_TILE_PIXELS)/2);
            float colonyXMax = (colony.getColonyBody().getPosition().x*Constants.PPM) + ((Constants.COLONY_WIDTH * Constants.MAP_TILE_PIXELS)/2);
            float colonyYMin = (colony.getColonyBody().getPosition().y*Constants.PPM) - ((Constants.COLONY_HEIGHT * Constants.MAP_TILE_PIXELS)/2);
            float colonyYMax = (colony.getColonyBody().getPosition().y*Constants.PPM) + ((Constants.COLONY_HEIGHT * Constants.MAP_TILE_PIXELS)/2);
            if (clickPos.x >= colonyXMin && clickPos.x <= colonyXMax && clickPos.y >= colonyYMin && clickPos.y <= colonyYMax) {
                //System.out.println("Colony found: " + colony.getName());
                screen.focusCameraOnColony(colony.getColonyBody());
                setSelectedObject(colony.getColonyBody());
                screen.setCameraLock(true);
            } else {
                //System.out.println("Colony not found");
            }
            //check if click was on colony ant
            for (Ant ant : colony.getAntsList()) {
                float antXMin = (ant.getAntBody().getPosition().x*Constants.PPM) - ((Constants.ANT_WIDTH * Constants.MAP_TILE_PIXELS)/2);
                float antXMax = (ant.getAntBody().getPosition().x*Constants.PPM) + ((Constants.ANT_WIDTH * Constants.MAP_TILE_PIXELS)/2);
                float antYMin = (ant.getAntBody().getPosition().y*Constants.PPM) - ((Constants.ANT_HEIGHT * Constants.MAP_TILE_PIXELS)/2);
                float antYMax = (ant.getAntBody().getPosition().y*Constants.PPM) + ((Constants.ANT_HEIGHT * Constants.MAP_TILE_PIXELS)/2);
                if (clickPos.x >= antXMin && clickPos.x <= antXMax && clickPos.y >= antYMin && clickPos.y <= antYMax) {
                    //System.out.println("Ant found: " + ant.getName());
                    screen.focusCameraOnAnt(ant.getAntBody());
                    screen.setPlayerInputProcessor(ant.getAntBody());
                    setSelectedObject(ant.getAntBody());
                    screen.setCameraLock(true);
                } else {
                    //System.out.println("Ant not found");
                }
            }
        }
        return null;
    }
}
