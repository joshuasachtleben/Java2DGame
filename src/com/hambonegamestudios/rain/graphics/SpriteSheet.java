package com.hambonegamestudios.rain.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private String path;
	public final int SIZE;
	public int[] pixels;
	
	public static SpriteSheet tiles = new SpriteSheet("/textures/spritesheet.png", 256);

	public SpriteSheet(String path, int size) {
		this.path = path;
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		load();
	}

	private void load() {
		try {
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path)); //creates a buffered image object and sets image to the one from the ImageIO.read path
			
			//get pixel information to work with image as pixels
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixels, 0, w); //translates image into the pixels array
				//0 = start scanning on x
				//0 = start scanning image on y
				//w = how many pixels to scan on the x axis
				//h = how many pixels to scan on the y axis
				//pixels = where to store the array of pixel information read from the getRGB method
				//0 = offset of where to begin scanning
				//w = scansize of how big the scanning area is
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
