package com.hambonegamestudios.rain;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.hambonegamestudios.rain.graphics.Screen;
import com.hambonegamestudios.rain.input.Keyboard;
import com.hambonegamestudios.rain.level.Level;
import com.hambonegamestudios.rain.level.RandomLevel;

public class Game extends Canvas implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;

	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private Level level;
	public static String title = "Rain";
	private boolean running = false;

	private int x = 0, y = 0;

	private Screen screen;

	// this is used to display a rendered image
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	// get the information from the raster and convert them to a DataBufferInt type so we can directly manipulate pixels
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		level = new RandomLevel(64, 64);
		
		frame.addKeyListener(key);
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0; // 1/60 of a second. 60.0 can be changed to meet a different number of updates allowed per second, if required.
		double delta = 0;
		int frames = 0;
		int updates = 0;
		//requestFocus();  //this was added to focus the window without having to click, but it wasn't needed for mine, as it was already focused.
		while (running) {
			long now = System.nanoTime();

			// calculates percentage of time needed to allow a new update to happen. this will continue to increase until it hits >= 1, which then will be reset when the while loops runs and resets it.
			delta += (now - lastTime) / ns;

			// replace lastTime with current time for next iteration
			lastTime = now;

			while (delta >= 1) {
				// update() will be limited to 60 updates a second avoid issues with game logic.
				// this allows for consistency for multiple computers.
				update();
				updates++;
				delta--;
			}
			// render() will not be limited to allow as fast as possible graphics rendering
			render();
			frames++;
			if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000; // add another second to be evaluated again one second later
				System.out.println(updates + " Updates, " + frames + " Frames");
				frame.setTitle(title + " | " + frames + " FPS");
				updates = 0;
				frames = 0;
			}
		}
		stop(); // stops the program in case while(running) is exited unexpectedly
	}

	public void update() {
		key.update();
		
		if(key.up) y--;
		if(key.down) y++;
		if(key.left) x--;
		if(key.right) x++;
	}

	public void render() {
		// this will get the BufferStrategy that is implemented from the Canvas class.
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		// call the clear method to reset pixels to void before rendering
		screen.clear();

		// call the render method to create the image
		level.render(x, y, screen);

		// set the pixels from screen pixels to our rendered image's pixels
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		// creates a link between the BufferStrategy and the screen to allow for graphics to be rendered
		Graphics g = bs.getDrawGraphics();

		// ***** graphics logic
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		// ***** end graphics logic

		g.dispose();

		// critical to show buffer
		bs.show();
	}

	public static void main(String[] args) {
		Game game = new Game();
		// set this to avoid issues with rendering graphics.
		// should be the first thing set when creating the frame
		game.frame.setResizable(false);

		game.frame.setTitle(title);
		game.frame.add(game);

		// sets size of frame to component (game)
		game.frame.pack();

		// closes program when JFrame window closes...avoids having it run in background with no frame
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// centers frame
		game.frame.setLocationRelativeTo(null);

		// frame won't show without this
		game.frame.setVisible(true);

		game.start();
	}

}
