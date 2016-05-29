package catdany.android.tehgaem.entity;

import java.util.UUID;

import com.google.gson.JsonObject;


public abstract class Entity
{
	public final World world;
	
	protected boolean isDead = false;
	
	public Entity(World world)
	{
		this.world = world;
		world.addEntity(this);
	}
	
	/**
	 * Remove this entity from the world
	 * @return Return value from {@link World#removeEntity(Entity)} 
	 */
	public synchronized boolean remove()
	{
		return world.removeEntity(this);
	}
	
	/**
	 * Get position on axis X
	 * @return
	 */
	public abstract double getPositionX();
	
	/**
	 * Get position on axis Y
	 * @return
	 */
	public abstract double getPositionY();
	
	/**
	 * Get momentum value for axis X
	 * @return
	 */
	public abstract double getVelocityX();
	
	/**
	 * Get momentum value for axis Y
	 * @return
	 */
	public abstract double getVelocityY();
	
	/**
	 * Get radius for collision boxes
	 * @return
	 */
	public abstract double getRadius();
	
	/**
	 * Called on a world tick
	 * @return <code>true</code> if any changes were done this tick
	 */
	public abstract boolean tick();
	
	/**
	 * Get unique Entity ID
	 * @return
	 */
	public abstract UUID getId();
	
	/**
	 * Add momentum for axis X
	 * @param x
	 */
	public abstract void addVelocityX(double x);
	
	/**
	 * Add momentum for axis Y
	 * @param y
	 */
	public abstract void addVelocityY(double y);
	
	/**
	 * Called when this entity collides with another entity
	 * @param collision Colliding entity
	 * @return <code>true</code> to prevent overlap
	 */
	public abstract boolean collide(Entity collision);
	
	/**
	 * Called when this entity collides with boundaries
	 * @return <code>true</code> to prevent entity from moving away
	 */
	public abstract boolean collideWithBoundary();
	
	@Override
	public String toString()
	{
		return "Entity-" + getId();
	}
	
	/**
	 * Serialize this world to {@link JsonObject}<br>
	 * Only information needed for clients will be serialized.
	 * @return
	 */
	public JsonObject toPacketInfo()
	{
		JsonObject json = new JsonObject();
		json.addProperty("UUID", getId().toString());
		json.addProperty("PosX", getPositionX());
		json.addProperty("PosY", getPositionY());
		json.addProperty("Radius", getRadius());
		return json;
	}
}
