package catdany.android.tehgaem.net.packet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import catdany.android.tehgaem.net.ClientHandler;

public final class LoginReplyPacket extends TGPacket
{
	public final ClientHandler client;
	public final int resultCode;
	
	public LoginReplyPacket(ClientHandler client, int result)
	{
		this.client = client;
		this.resultCode = result;
	}
	
	@Override
	public ClientHandler getClient()
	{
		return client;
	}
	
	@Override
	public JsonObject handleJsonSerialization()
	{
		JsonObject json = new JsonObject();
		json.addProperty("ResultCode", resultCode);
		return json;
	}
	
	public static LoginReplyPacket handleJsonDeserialization(ClientHandler client, JsonObject json) throws JsonParseException
	{
		return null;
	}
	
	@Override
	public void handleReception() {}
}
