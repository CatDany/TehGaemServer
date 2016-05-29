package catdany.android.tehgaem.entity;

import catdany.android.tehgaem.net.ClientHandler;

public class EntityPlayer extends EntityMoveable
{
	public ClientHandler client;
	
	/**
	 * {@link EntityMoveable#EntityMoveable(World, java.util.UUID, double, double) Create an entity with random position}
	 * @param client
	 * @param world
	 * @param radius
	 * @param speed
	 */
	public EntityPlayer(ClientHandler client, World world, double radius, double speed)
	{
		super(world, client.id, radius, speed);
		this.client = client;
	}
	
	@Override
	public boolean collide(Entity collision)
	{
		return collision instanceof EntityPlayer;
	}

	@Override
	public boolean collideWithBoundary()
	{
		return true;
	}
}
