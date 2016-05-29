package catdany.android.tehgaem.net.packet;

import catdany.android.tehgaem.entity.World;
import catdany.android.tehgaem.net.ClientHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public final class WorldSyncPacket extends TGPacket
{
	public final ClientHandler client;
	public final World world;
	
	public WorldSyncPacket(ClientHandler client, World world)
	{
		this.client = client;
		this.world = world;
	}
	
	@Override
	public JsonObject handleJsonSerialization()
	{
		return world.toPacketInfo();
	}

	@Override
	public void handleReception() {}

	@Override
	public ClientHandler getClient()
	{
		return client;
	}
	
	public static WorldSyncPacket handleJsonDeserialization(ClientHandler client, JsonObject json) throws JsonParseException
	{
		return null;
	}
}
