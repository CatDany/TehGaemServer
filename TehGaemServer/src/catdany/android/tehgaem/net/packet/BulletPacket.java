package catdany.android.tehgaem.net.packet;

import catdany.android.tehgaem.Log;
import catdany.android.tehgaem.entity.EntityBullet;
import catdany.android.tehgaem.entity.World;
import catdany.android.tehgaem.net.ClientHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class BulletPacket extends TGPacket
{
	public final ClientHandler client;
	public final double targetX;
	public final double targetY;
	
	public BulletPacket(ClientHandler client, double targetX, double targetY)
	{
		this.client = client;
		this.targetX = targetX;
		this.targetY = targetY;
		Log.d("bullet tX %s tY %s", targetX, targetY);
	}
	
	@Override
	public ClientHandler getClient()
	{
		return client;
	}
	
	@Override
	public JsonObject handleJsonSerialization()
	{
		return null;
	}
	
	@Override
	public void handleReception()
	{
		EntityBullet bullet = new EntityBullet(World.instance, client.entity,
				client.entity.getPositionX(), client.entity.getPositionY(), 1, 4,
				targetX - client.entity.getPositionX(), targetY - client.entity.getPositionY());
	}
	
	public static BulletPacket handleJsonDeserialization(ClientHandler client, JsonObject json) throws JsonParseException
	{
		return new BulletPacket(
				client,
				json.get("TargetX").getAsDouble(),
				json.get("TargetY").getAsDouble()
		);
	}
}