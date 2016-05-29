package catdany.android.tehgaem.net.packet;

import catdany.android.tehgaem.Log;
import catdany.android.tehgaem.net.ClientHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Created by CatDany on 29.05.2016.
 */
public final class MovementPacket extends TGPacket
{
	public final ClientHandler client;
	public final double posX;
	public final double posY;

	public MovementPacket(ClientHandler client, double posX, double posY)
	{
		this.client = client;
		this.posX = posX;
		this.posY = posY;
	}

	@Override
	public JsonObject handleJsonSerialization()
	{
		return null;
	}

	@Override
	public void handleReception()
	{
		double entityPosX = client.entity.getPositionX();
		double entityPosY = client.entity.getPositionY();
		client.entity.setVelocityX(posX - entityPosX);
		client.entity.setVelocityY(posY - entityPosY);
		Log.d("MovementPacket >> %s;%s >> Velocity: %s;%s", posX, posY, posX - entityPosX, posY - entityPosY);
	}

	public static MovementPacket handleJsonDeserialization(ClientHandler client, JsonObject json) throws JsonParseException
	{
		return new MovementPacket(
				client,
				json.get("PosX").getAsDouble(),
				json.get("PosY").getAsDouble()
		);
	}
	
	@Override
	public ClientHandler getClient()
	{
		return client;
	}
}
