package aaa.main.game;

import aaa.main.util.OpenSimplexNoiseKS;

import static aaa.main.util.Constants.*;

public class Perlin {

	// Returns a boolean array that has two tables. The first one
	// corresponds to the resource map where 0 corresponds to a
	// non-resource tile and 1 corresponds to a resource tile.
	// The second table corresponds to the wall map where 0 
        // corresponds to a tile without a wall and 1 corresponds
        // to a tile with a wall.

	public static boolean[][][] generateMap(int width, int height, long seed) {

		boolean[][][] map = new boolean[3][width][height];

		double xoff_set = 0;
		double yoff_set = 0;


		// setting the seed
                OpenSimplexNoiseKS osnoise;
                osnoise = new OpenSimplexNoiseKS(seed);

                // compute the perlin map for the wall_array using the given seed
		for (int i = 0; i < width; i++) {
			yoff_set = 0f;
			for (int j = 0; j < height; j++) {
				double value = osnoise.eval(xoff_set, yoff_set);
						if (value < WALL_THRESHOLD) {
							map[1][i][j] = true;
						}

						// inversion-!
						if (value > (1f - COLONY_CANDIDATE_THRESHOLD)) {
							map[2][i][j] = true;
						}
				yoff_set += 0.1f;
			}
			xoff_set += 0.1f;
		}

                // shift the seed
		seed = seed + 5;
                osnoise = new OpenSimplexNoiseKS(seed);

		xoff_set = 0;
		yoff_set = 0;

		// compute the perlin map for the resource_array using the shifted seed
		for (int i = 0; i < width; i++) {
			yoff_set = 0;
			for (int j = 0; j < height; j++) {
				double value = osnoise.eval(xoff_set, yoff_set);
				if (value > RESOURCE_THRESHOLD) {
					map[0][i][j] = true;
					// remove any walls if there should be resources present
					if (map[1][i][j] == true) {
						map[1][i][j] = false;
					}
				}
				yoff_set += 0.1f;
			}
			xoff_set += 0.1f;
		}

		return map;

	}
}
