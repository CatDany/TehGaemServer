package catdany.android.tehgaem.net.packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import catdany.android.tehgaem.net.ClientHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Created by CatDany on 28.05.2016.
 */
public abstract class TGPacket
{

	/**
	 * Serialize this packet to JSON format
	 * 
	 * @return
	 */
	public abstract JsonObject handleJsonSerialization();

	/**
	 * Called when a packet is received
	 */
	public abstract void handleReception();
	
	/**
	 * Get client who has sent or will receive this packet
	 * @return
	 */
	public abstract ClientHandler getClient();

	public final JsonObject toJson()
	{
		JsonObject json = new JsonObject();
		json.addProperty("PacketType", getClass().getName());
		json.add("PacketInfo", handleJsonSerialization());
		return json;
	}
	
	/**
	 * Create this static method on all subclasses
	 * @param client
	 * @param json
	 * @return Deserialized object, or <code>null</code> if this packet will only be sent to clients
	 * @throws JsonParseException
	 */
	public static TGPacket handleJsonDeserialization(ClientHandler client, JsonObject json) throws JsonParseException
	{
		return null;
	}

	/**
	 * Get packet object from JSON
	 * @param client
	 *            Client who has sent or will receive this packet
	 * @param json
	 *            Serialized packet
	 * @return Deserialized packet
	 * @throws JSONException
	 *             JSON parse error occurred
	 * @throws ClassNotFoundException
	 *             PacketType in JSONObject is not a valid class
	 * @throws IllegalAccessException
	 *             Unable to access
	 *             {@link #handleJsonDeserialization(JSONObject)} method for the
	 *             packet class
	 * @throws NoSuchMethodException
	 *             Method {@link #handleJsonDeserialization(JSONObject)} was not
	 *             declared in the packet class
	 * @throws InvocationTargetException
	 *             {@link JSONException} occurred during
	 *             {@link #handleJsonDeserialization(JSONObject)}
	 * @throws ClassCastException
	 */
	public static TGPacket fromJson(ClientHandler client, JsonObject json) throws JsonParseException, ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassCastException
	{
		Class<? extends TGPacket> clazz = Class.forName(json.get("PacketType").getAsString()).asSubclass(TGPacket.class);
		Method deserializeMethod = clazz.getDeclaredMethod("handleJsonDeserialization", ClientHandler.class, JsonObject.class);
		return (TGPacket)deserializeMethod.invoke(null, client, json.get("PacketInfo").getAsJsonObject());
	}
}
