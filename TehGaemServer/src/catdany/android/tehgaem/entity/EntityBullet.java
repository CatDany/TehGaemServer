package catdany.android.tehgaem.entity;

import java.util.UUID;

public class EntityBullet extends EntityMoveable
{
	public EntityPlayer source;
	
	public EntityBullet(World world, EntityPlayer source, double posX, double posY, double radius, double speed, double velocityX, double velocityY)
	{
		super(world, UUID.randomUUID(), posX, posY, radius, speed);
		this.source = source;
		setVelocityX(velocityX*world.maxSize);
	}
	
	@Override
	public boolean collide(Entity collision)
	{
		if (collision instanceof EntityPlayer && collision != source)
		{
			((EntityPlayer)collision).client.isDisconnected = true;//Send a packet for Toast message (victory, defeat)
			remove();
			return true;
		}
		return false;
	}

	@Override
	public boolean collideWithBoundary()
	{
		if (getPositionX() < -getRadius() || getPositionX() > world.maxSize+getRadius() || getPositionY() < -getRadius() || getPositionY() > world.maxSize+getRadius())
		{
			isDead = true;
		}
		return false;
	}
}
