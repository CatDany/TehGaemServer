package catdany.android.tehgaem.net.packet;

import java.util.UUID;

import catdany.android.tehgaem.entity.EntityPlayer;
import catdany.android.tehgaem.entity.World;
import catdany.android.tehgaem.net.ClientHandler;
import catdany.android.tehgaem.net.TGAuth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Created by CatDany on 28.05.2016.
 */
public final class LoginPacket extends TGPacket
{
	private final ClientHandler client;
	public final UUID uuid;
	public final String password;

	public LoginPacket(ClientHandler client, UUID uuid, String password)
	{
		this.client = client;
		this.uuid = uuid;
		this.password = password;
	}

	public static LoginPacket handleJsonDeserialization(ClientHandler client, JsonObject json) throws JsonParseException
	{
		return new LoginPacket(
				client,
				UUID.fromString(json.get("UUID").getAsString()),
				json.get("Password").getAsString()
		);
	}

	@Override
	public JsonObject handleJsonSerialization()
	{
		return null;
	}

	@Override
	public void handleReception()
	{
		ClientHandler client = getClient();
		int authResult = TGAuth.auth(uuid, password);
		client.sendPacket(new LoginReplyPacket(client, authResult));
		if (TGAuth.isResultSuccessful(authResult))
		{
			client.id = uuid;
			client.entity = new EntityPlayer(client, World.instance, 5, 1);
		}
		else
		{
			client.isDisconnected = true;
		}
	}

	@Override
	public ClientHandler getClient()
	{
		return client;
	}
}
