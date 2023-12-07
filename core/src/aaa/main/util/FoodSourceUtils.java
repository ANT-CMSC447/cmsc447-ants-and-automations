package aaa.main.util;

import aaa.main.game.map.FoodSource;
import aaa.main.screens.MainScreen;
import com.badlogic.gdx.math.Vector2;

public class FoodSourceUtils {
    public static FoodSource createFoodSource(boolean type, float x, float y, MainScreen screen) {
        FoodSource source = new FoodSource(type, screen.camera, screen.world, new Vector2(x,y));
        addFoodSource(source,screen);
        return source;
    }

    public static void addFoodSource(FoodSource foodSource, MainScreen screen) {
        if (foodSource.getType()) {
            screen.candy.add(foodSource);
        } else {
            screen.forage.add(foodSource);
        }
    }
}
