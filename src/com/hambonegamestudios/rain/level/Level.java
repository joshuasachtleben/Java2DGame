package com.hambonegamestudios.rain.level;

import com.hambonegamestudios.rain.graphics.Screen;
import com.hambonegamestudios.rain.level.tile.Tile;

public class Level {

	protected int width, height;
	protected int[] tiles;

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new int[width * height];
		generateLevel();
	}

	public Level(String path) {
		loadLevel(path);
	}

	protected void generateLevel() {

	}

	private void loadLevel(String path) {

	}

	public void update() {
		// updates mobs, entities, and other objects
	}

	private void time() {
		// used to track time of day
	}

	public void render(int xScroll, int yScroll, Screen screen) {
		// this will be overridden with a superclass
		screen.setOffset(xScroll, yScroll);

		// set corner pins
		int x0 = xScroll >> 4; // precision at tile class versus pixels when dividing by length of tile.
		int x1 = (xScroll + screen.width + 16) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.height + 16) >> 4;

		// render only the pixels showing on the window
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return Tile.voidTile;  //fixes issue with rendering out of bounds of the level
		if (tiles[x + y * width] == 0)
			return Tile.grass;
		return Tile.voidTile;
	}

}
