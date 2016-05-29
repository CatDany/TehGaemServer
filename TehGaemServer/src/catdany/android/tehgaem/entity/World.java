package catdany.android.tehgaem.entity;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import catdany.android.tehgaem.Log;
import catdany.android.tehgaem.net.TGServer;
import catdany.android.tehgaem.net.packet.WorldSyncPacket;

public class World implements Runnable
{
	public static World instance;
	
	/**
	 * World tick thread
	 */
	public Thread threadWorld;
	/**
	 * List of all added entities
	 */
	public ArrayList<Entity> entityList = new ArrayList<Entity>();
	
	/**
	 * Is this world queued for killing
	 */
	private boolean isKilled;
	
	/**
	 * Tick frequency for this world (in Hertz)
	 */
	public final int tickFrequency;
	/**
	 * World boundary
	 */
	public final double maxSize;
	
	private long lastTickTime = 0;
	
	/**
	 * Create a new world
	 * @param tickFrequency Tick frequency for this world (in Hertz)
	 */
	public World(int tickFrequency, double maxSize)
	{
		this.tickFrequency = tickFrequency;
		this.maxSize = maxSize;
		this.threadWorld = new Thread(this, "World");
	}
	
	/**
	 * Start world tick thread
	 * @see #threadWorld
	 */
	public void start()
	{
		threadWorld.start();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		Log.i("Started world with tick frequency %s Hz.", tickFrequency);
		while (!isKilled)
		{
			long now = System.currentTimeMillis();
			if (now > lastTickTime + 1000D/tickFrequency)
			{
				for (Entity i : entityList)
				{
					//Log.d("ticking %s", i);
					i.tick();
				}
				for (Entity i : (ArrayList<Entity>)entityList.clone())
				{
					if (i.isDead)
					{
						i.remove();
					}
				}
				TGServer.instance.sendPacketToAll(new WorldSyncPacket(null, this));
				lastTickTime = now;
			}
		}
		Log.i("World died");
	}
	
	/**
	 * Queue world kill
	 * @return <code>false</code> if world kill is already queued, otherwise <code>true</code>
	 */
	public boolean kill()
	{
		boolean result = !isKilled;
		this.isKilled = true;
		return result;
	}
	
	/**
	 * Add entity to this world
	 * @param entity
	 */
	public synchronized void addEntity(Entity entity)
	{
		entityList.add(entity);
		Log.d("%s was added to world", entity);
	}
	
	/**
	 * Check if this world is dead
	 * @return <code>true</code> if {@link #threadWorld} is not alive
	 */
	public boolean isDead()
	{
		return !threadWorld.isAlive();
	}

	/**
	 * Remove given entity from this world
	 * @param entity
	 * @return <code>true</code> if this world contained given entity and it has been removed, <code>false</code> otherwise
	 */
	public synchronized boolean removeEntity(Entity entity)
	{
		Log.d("Removing %s", entity);
		return entityList.remove(entity);
	}
	
	/**
	 * Serialize this world to {@link JsonObject}<br>
	 * Only information needed for clients will be serialized.
	 * @return
	 */
	public JsonObject toPacketInfo()
	{
		JsonObject json = new JsonObject();
		JsonArray entityArray = new JsonArray();
		for (Entity i : entityList)
		{
			entityArray.add(i.toPacketInfo());
		}
		json.add("Entities", entityArray);
		return json;
	}
	
	/**
	 * Check for collision with other colliding entities.
	 * @param newPosX
	 * @param newPosY
	 * @see Entity#collide(Entity)
	 * @return <code>true</code> if new position is not taken by another entity, or that entity allows the overlap
	 */
	public boolean checkEntityCollision(Entity entity, double newPosX, double newPosY)
	{
		for (Entity i : entityList)
		{
			if (i != entity && Math.sqrt(Math.pow(newPosX - i.getPositionX(), 2) + Math.pow(newPosY - i.getPositionY(), 2)) < i.getRadius() + entity.getRadius())
			{
				if (i.collide(entity) || entity.collide(i))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if new coordinates are within world boundaries [0;0][N;N] (N={@link World#maxSize})
	 * @param newPosX
	 * @param newPosY
	 * @return
	 */
	public boolean checkBoundaries(Entity entity, double newPosX, double newPosY)
	{
		if (newPosX - entity.getRadius() < 0 || newPosY - entity.getRadius() < 0 || newPosX + entity.getRadius() > maxSize || newPosY + entity.getRadius() > maxSize)
		{
			if (entity.collideWithBoundary())
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		return true;
	}
}