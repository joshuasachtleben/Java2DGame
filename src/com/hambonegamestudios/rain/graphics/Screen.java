package com.hambonegamestudios.rain.graphics;

import java.util.Random;

import com.hambonegamestudios.rain.level.tile.Tile;

public class Screen {

	public int width, height;
	public int[] pixels;
	public final int MAP_SIZE = 64;
	public final int MAP_SIZE_MASK = MAP_SIZE - 1;
	public int[] tiles = new int[MAP_SIZE * MAP_SIZE]; // tilemap
	public int xOffset, yOffset;

	private Random random = new Random();

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];

		for (int i = 0; i < MAP_SIZE * MAP_SIZE; i++) {
			tiles[i] = random.nextInt(0xffffff);
		}
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public void renderTile(int xp, int yp, Tile tile) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < tile.sprite.SIZE; y++) {
			int ya = y + yp; // ya = absolute position
			for (int x = 0; x < tile.sprite.SIZE; x++) {
				int xa = x + xp; // xa = absolute position
				if (xa < -tile.sprite.SIZE || xa >= width || ya < 0 || ya >= height)
					break; // only render tiles we see...very important for performance
				if (xa < 0)
					xa = 0;
				pixels[xa + ya * width] = tile.sprite.pixels[(x + y * tile.sprite.SIZE)];
			}
		}
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

}
