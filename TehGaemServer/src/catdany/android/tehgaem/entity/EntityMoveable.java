package catdany.android.tehgaem.entity;

import java.util.UUID;

import catdany.android.tehgaem.Log;

public abstract class EntityMoveable extends Entity
{
	public final UUID id;
	public double radius;
	private double posX;
	private double posY;
	protected double prevPosX;
	protected double prevPosY;
	private double velocityX;
	private double velocityY;
	
	/**
	 * Movement per tick
	 */
	private double speed;
	
	public EntityMoveable(World world, UUID id, double posX, double posY, double radius, double speed)
	{
		super(world);
		this.id = id;
		this.radius = radius;
		this.posX = posX;
		this.posY = posY;
		this.speed = speed;
	}
	
	/**
	 * Create an entity and pick a random position that's not already taken
	 * @param world
	 * @param id
	 * @param radius
	 * @param speed
	 */
	public EntityMoveable(World world, UUID id, double radius, double speed)
	{
		this(world, id, 0, 0, radius, speed);
		double randomX = 0;
		double randomY = 0;
		//TODO: Fix positioning algorithm or adjust collision
		for (int i = 0; i < 500; i++)
		{
			randomX = radius + Math.random() * (world.maxSize - 2*radius);
			randomY = radius + Math.random() * (world.maxSize - 2*radius);
			if (world.checkEntityCollision(this, randomX, randomY))
			{
				break;
			}
		}
		setPositionX(randomX);
		setPositionY(randomY);
	}
	
	public synchronized void setPositionX(double posX)
	{
		this.posX = posX;
	}
	
	public synchronized void setPositionY(double posY)
	{
		this.posY = posY;
	}
	
	@Override
	public double getPositionX()
	{
		return posX;
	}

	@Override
	public double getPositionY()
	{
		return posY;
	}

	@Override
	public double getVelocityX()
	{
		return velocityX;
	}

	@Override
	public double getVelocityY()
	{
		return velocityY;
	}

	@Override
	public boolean tick()
	{
		boolean changesDone = false;
		if (velocityX != 0 && velocityY != 0)
		{
			double angle = Math.atan2(velocityY, velocityX);
			double sin = Math.sin(angle);
			double cos = Math.cos(angle);
			double addPosX = speed * cos;
			double addPosY = speed * sin;
			changesDone = move(addPosX, addPosY);
		}
		else if (velocityX != 0 && velocityY == 0)
		{
			double addPosX = velocityX > 0 ? speed : -speed;
			changesDone = move(addPosX, 0);
		}
		else if (velocityX == 0 && velocityY != 0)
		{
			double addPosY = velocityY > 0 ? speed : -speed;
			changesDone = move(0, addPosY);
		}
		prevPosX = posX;
		prevPosY = posY;
		return changesDone;
	}
	
	protected boolean move(double addPosX, double addPosY)
	{
		boolean f0 = world.checkBoundaries(this, posX + addPosX, posY + addPosY);
		boolean f1 = world.checkEntityCollision(this, posX + addPosX, posY + addPosY);
		Log.d("f0 %s f1 %s", f0, f1);
		if (f0 && f1)
		{
			posX += addPosX;
			posY += addPosY;
			recalculateVelocity(addPosX, addPosY);
			return true;
		}
		//TODO: Slow down on collision
		return false;
	}
	
	private void recalculateVelocity(double addPosX, double addPosY)
	{
		if (velocityX < 0)
		{
			velocityX = Math.min(velocityX - addPosX, 0);
		}
		else
		{
			velocityX = Math.max(velocityX - addPosX, 0);
		}
		if (velocityY < 0)
		{
			velocityY = Math.min(velocityY - addPosY, 0);
		}
		else
		{
			velocityY = Math.max(velocityY - addPosY, 0);
		}
	}
	
	@Override
	public abstract boolean collide(Entity collision);
	
	public abstract boolean collideWithBoundary();

	@Override
	public double getRadius()
	{
		return radius;
	}

	@Override
	public UUID getId()
	{
		return id;
	}
	
	public synchronized void setVelocityX(double x)
	{
		this.velocityX = x;
	}
	
	public synchronized void setVelocityY(double y)
	{
		this.velocityY = y;
	}

	@Override
	public synchronized void addVelocityX(double x)
	{
		velocityX += x;
	}

	@Override
	public synchronized void addVelocityY(double y)
	{
		velocityY += y;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public synchronized void setSpeed(double speed)
	{
		this.speed = speed;
	}
}
