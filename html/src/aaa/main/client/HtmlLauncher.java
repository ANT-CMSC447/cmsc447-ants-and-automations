package aaa.main.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import aaa.main.AntGame;
import com.badlogic.gdx.backends.gwt.GwtGraphics;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig () {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(true);
        config.fullscreenOrientation = GwtGraphics.OrientationLockType.LANDSCAPE;
        return config;
    }

    @Override
    public ApplicationListener createApplicationListener () {
        return new AntGame();
    }
}