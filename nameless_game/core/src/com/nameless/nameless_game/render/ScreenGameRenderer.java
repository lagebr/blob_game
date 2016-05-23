package com.nameless.nameless_game.render;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.nameless.nameless_game.model.Entity;
import com.nameless.nameless_game.model.Hostile;

/**
 * ScreenRenderer provides functionality to draw to a LibGDX screen.
 * 
 * @author Isaac Arvestad, Henrik Lagebrand
 * @version 2016-05-09
 */
public class ScreenGameRenderer extends GameRenderer {
	public static final int METER_TO_PIXEL = 100;

	private Camera camera;
	private SpriteBatch batch;
	private SpriteBatch guiBatch;

	private Box2DDebugRenderer debugRenderer;
	private FPSLogger logger = new FPSLogger();

	private ShaderProgram shader;
	private String vertexShader;
	private String fragmentShader;
	
	private int countDown = 1;
	private BitmapFont font;

	/**
	 * Draws all entities on screen using an perspective camera.
	 * 
	 * @param width
	 *            The width of the screen.
	 * @param height
	 *            The height of the screen.
	 */
	public ScreenGameRenderer(float width, float height) {
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		camera = new PerspectiveCamera(67, width, height);

		batch = new SpriteBatch();
		guiBatch = new SpriteBatch();

		debugRenderer = new Box2DDebugRenderer();

		camera.position.set(width / 2, height / 2 - 100, 700);
		camera.lookAt(width / 2, height / 2, 0);
		camera.far = 100000;

		vertexShader = Gdx.files.internal("vertex.glsl").readString();
		fragmentShader = Gdx.files.internal("fragment.glsl").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
	}

	/**
	 * Prepare clears the screen and updates the camera to prepare for a new
	 * render frame.
	 * 
	 * @param color
	 *            The background color.
	 */
	public void prepare(Color color) {
		Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	/**
	 * Draws entities. All entities are drawn in the same batch.
	 * 
	 * @param entities
	 *            The entities to be drawn.
	 */
	@Override
	public void renderHostiles(ArrayList<Hostile> hostiles) {
		batch.begin();
		batch.setShader(null);
		for (Hostile hostile : hostiles) {
			hostile.getSprite().draw(batch);
		}

		batch.end();
	}

	@Override
	public void renderKeySeq(ArrayList<Texture> keySeqTextureList) {
		float iconSize = 45;
		float offset = 50;
		float wHalf = Gdx.graphics.getWidth() / 2;
		float hFull = Gdx.graphics.getHeight();

		// TODO Refine it
		float start = wHalf - (offset * ((keySeqTextureList.size() - 1 / 2)));
		// Centering
		if (keySeqTextureList.size() % 2 == 1) {
			float x = start; // start point, origo bottom left corner of window
			guiBatch.begin();
			for (Texture texture : keySeqTextureList) {
				guiBatch.draw(texture, x, hFull - 65, iconSize, iconSize); // add
																			// width
																			// and
																			// height
				x = x + offset + texture.getWidth();
			}
			guiBatch.end();

		} else {
			start = wHalf - offset * (keySeqTextureList.size() / 2);
			float x = start;
			guiBatch.begin();
			for (Texture texture : keySeqTextureList) {
				guiBatch.draw(texture, x, hFull - 65, iconSize, iconSize); // add
																			// width
																			// and
																			// height
				x = x - offset;
			}
			guiBatch.end();
		}

	}
	
	public void renderCountDown(int time) {
		batch.begin();
		font.setColor(Color.WHITE);
		font.draw(batch, String.valueOf(time), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		batch.end();
		if (time % 1 > 0) {
			time -= 1;
		}
	}

	/**
	 * Draws a single entity in one batch.
	 * 
	 * @param entity
	 *            The entity to be drawn.
	 */
	public void render(Entity entity) {
		batch.begin();
		entity.getSprite().draw(batch);
		batch.end();
	}

	/**
	 * Draws all physics bodies.
	 * 
	 * @param world
	 *            The world that the physics bodies belongs to.
	 */
	public void renderDebug(World world) {
		debugRenderer.render(world, camera.combined.scale(meterToPixel(1),
				meterToPixel(1), meterToPixel(1)));
		// logger.log();
	}

	/**
	 * Converts screen pixels to physics meters.
	 * 
	 * @param pixels
	 * @return meters
	 */
	public static float pixelToMeter(float pixels) {
		return pixels / (float) METER_TO_PIXEL;
	}

	/**
	 * Converts physics meters to screen pixels
	 * 
	 * @param meters
	 * @return pixels
	 */
	public static float meterToPixel(float meters) {
		return meters * (float) METER_TO_PIXEL;
	}

	/**
	 * Returns the camera of the renderer.
	 * 
	 * @return camera
	 */
	public Camera getCamera() {
		return camera;
	}
}
