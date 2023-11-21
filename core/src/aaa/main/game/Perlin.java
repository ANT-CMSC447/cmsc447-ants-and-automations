package aaa.main.game;

import static aaa.main.util.Constants.WALL_THRESHOLD;
import static aaa.main.util.Constants.RESOURCE_THRESHOLD;

import aaa.main.util.OpenSimplexNoiseKS;

public class Perlin {

	// Returns a boolean array that has two tables. The first one
	// corresponds to the resource map where 0 corresponds to a
	// non-resource tile and 1 corresponds to a resource tile.
	// The second table corresponds to the wall map where 0 
        // corresponds to a tile without a wall and 1 corresponds
        // to a tile with a wall.

	public static boolean[][][] generateMap(int width, int height, int seed) {
		
		double[][] wall_array;
		double[][] resource_array;

		boolean[][][] map = new boolean[2][width][height];

		double xoff_set = 0;
		double yoff_set = 0;

		wall_array = new double[width][height];
		resource_array = new double[width][height];


                // setting the seed
                OpenSimplexNoiseKS osnoise;
                osnoise = new OpenSimplexNoiseKS(seed);

                // compute the perlin map for the wall_array using the given seed
		for (int i = 0; i < width; i++) {
			yoff_set = 0f;
			for (int j = 0; j < height; j++) {
				double value = osnoise.eval(xoff_set, yoff_set);

		                wall_array[i][j] = value;
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

				resource_array[i][j] = value;
				yoff_set += 0.1f;
			}
			xoff_set += 0.1f;
		}

		// convert the perlin maps to binary maps by comparing the value at index
		// with their respective thresholds
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

                                // if the value in the i,j index of the resource array
                                // is less than the theshold, make it as a resource
                                // tile
				if (RESOURCE_THRESHOLD < resource_array[i][j]) {
					map[0][i][j] = true;
				} else {
					map[0][i][j] = false;
				}

                                // if the value in the i,j index of the wall array
                                // is less than the theshold OR the title is marked
                                // as a resource tile, mark it so it doesn't have
                                // a wall
				if (WALL_THRESHOLD < wall_array[i][j] || map[0][i][j] == true) {
					map[1][i][j] = false;
				} else {
					map[1][i][j] = true;
				}
			}
		}

		return map;

	}
}
