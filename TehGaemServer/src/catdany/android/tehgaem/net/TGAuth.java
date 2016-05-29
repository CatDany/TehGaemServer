package catdany.android.tehgaem.net;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import catdany.android.tehgaem.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TGAuth
{
	public static File fileAuth = new File("auth.json");
	
	public static final int RESULT_LOGGED_IN = 0;
	public static final int RESULT_NEW_ACCOUNT = 1;
	public static final int RESULT_INCORRECT_PASSWORD = -1;
	public static final int RESULT_ALREADY_CONNECTED = -2;
	
	/**
	 * Try to authorize a client
	 * @param id
	 * @param password
	 * @return
	 * 		<code>{@value #RESULT_LOGGED_IN}</code> - Authentication complete, logged in successfully<br>
	 * 		<code>{@value #RESULT_NEW_ACCOUNT}</code> - Authentication complete, created a new account<br>
	 * 		<code>{@value #RESULT_INCORRECT_PASSWORD}</code> - Authentication failed, incorrect password<br>
	 * 		<code>{@value #RESULT_ALREADY_CONNECTED}</code> - Authentication failed, a client with given UUID is already connected
	 */
	public static int auth(UUID id, String password)
	{
		try
		{
			String idStr = id.toString();
			for (ClientHandler i : TGServer.instance.clients)
			{
				if (i.id != null && i.id.equals(id))
				{
					Log.i("Authentication for %s (password: %s) resulted with RESULT_ALREADY_CONNECTED", idStr, password);
					return RESULT_ALREADY_CONNECTED;
				}
			}
			JsonArray json;
			if (fileAuth.exists() && !fileAuth.isDirectory())
			{
				JsonParser parser = new JsonParser();
				FileReader reader = new FileReader(fileAuth);
				json = parser.parse(reader).getAsJsonArray();
				reader.close();
			}
			else
			{
				json = new JsonArray();
			}
			for (int i = 0; i < json.size(); i++)
			{
				JsonObject a = json.get(i).getAsJsonObject();
				if (a.get("UUID").getAsString().equals(idStr))
				{
					if (a.get("Password").getAsString().equals(password))
					{
						Log.i("Authentication for %s (password: %s) resulted with RESULT_LOGGED_IN", idStr, password);
						return RESULT_LOGGED_IN;
					}
					else
					{
						Log.i("Authentication for %s (password: %s) resulted with RESULT_INCORRECT_PASSWORD", idStr, password);
						return RESULT_INCORRECT_PASSWORD;
					}
				}
			}
			JsonObject newAccount = new JsonObject();
			newAccount.addProperty("UUID", idStr);
			newAccount.addProperty("Password", password);
			json.add(newAccount);
			FileWriter fileWriter = new FileWriter(fileAuth);
			fileWriter.write(json.toString());
			fileWriter.close();
			Log.i("Authentication for %s (password: %s) resulted with RESULT_NEW_ACCOUNT", idStr, password);
			return RESULT_NEW_ACCOUNT;
		}
		catch (IOException t)
		{
			Log.e("Unable to read auth file.");
			Log.t(t);
		}
		return -1;
	}
	
	public static boolean isResultSuccessful(int resultCode)
	{
		switch (resultCode)
		{
		case RESULT_LOGGED_IN:
		case RESULT_NEW_ACCOUNT:
			return true;
		case RESULT_INCORRECT_PASSWORD:
		case RESULT_ALREADY_CONNECTED:
			return false;
		}
		throw new IllegalArgumentException("Result code is invalid.");
	}
}
