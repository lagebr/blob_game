package com.nameless.nameless_game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.nameless.nameless_game.render.ScreenGameRenderer;

/**
 * Entity describes a base entity which has a physics body and a texture for
 * rendering.
 * 
 * @author Isaac Arvestad, Henrik Lagebrand
 * @version 2016-05-04
 * 
 */
public abstract class Entity {

	protected Body body;
	protected Sprite sprite;

	private boolean flaggedForDeletion = false;

	// Filtering masks
	public final static short PLAYER_ENTITY = 0x1;
	public final static short NPC_ENTITY = 0x1 << 1;

	/**
	 * Creates an entity with a static physics body and a texture.
	 * 
	 * @param x
	 *            Center on x-axis in number of pixels.
	 * @param y
	 *            Center on y-axis in number of pixels.
	 * @param width
	 *            Width in number of pixels.
	 * @param height
	 *            Height in number of pixels.
	 * @param texture
	 *            Texture to use.
	 * @param world
	 *            Physics world to add body to.
	 */
	public Entity(float x, float y, float width, float height, Texture texture, World world) {
		body = PhysicsHelper.createStaticBody(ScreenGameRenderer.pixelToMeter(x), ScreenGameRenderer.pixelToMeter(y),
				ScreenGameRenderer.pixelToMeter(width), ScreenGameRenderer.pixelToMeter(height), world);

		sprite = new Sprite(texture, (int) width, (int) height);
	}

	/**
	 * Creates an entity with a texture.
	 * 
	 * @param texture
	 *            The texture.
	 */
	public Entity(Texture texture) {
		sprite = new Sprite(texture);
	}

	/**
	 * Empty constructor.
	 */
	public Entity() {
		// This constructor is intentionally left empty.
	}

	/**
	 * Updates the entities animation, and behavior.
	 * 
	 * @param deltaTime
	 *            Time past since last frame in seconds.
	 */
	public abstract void update(float deltaTime);

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public boolean isFlaggedForDeletion() {
		return flaggedForDeletion;
	}

	public void setFlaggedForDeletion(boolean flaggedForDeletion) {
		this.flaggedForDeletion = flaggedForDeletion;
	}

	public abstract void updateSprite();
}
