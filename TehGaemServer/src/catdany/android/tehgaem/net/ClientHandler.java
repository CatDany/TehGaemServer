package catdany.android.tehgaem.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.UUID;

import catdany.android.tehgaem.Log;
import catdany.android.tehgaem.entity.EntityPlayer;
import catdany.android.tehgaem.net.packet.TGPacket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class ClientHandler implements Runnable
{
	public final Socket socket;
	public final BufferedReader reader;
	public final PrintWriter writer;
	public final Thread threadHandler;
	
	public UUID id;
	public boolean isDisconnected;
	
	public EntityPlayer entity;
	
	public ClientHandler(Socket socket) throws IOException
	{
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), TGServer.SOCKET_CHARSET));
		this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), TGServer.SOCKET_CHARSET), true);
		this.threadHandler = new Thread(this, "ClientHandler-" + socket.getRemoteSocketAddress().toString());
	}
	
	@Override
	public void run()
	{
		String read;
		try
		{
			while (!isDisconnected && (read = reader.readLine()) != null)
			{
				Log.d("Received message: %s", read);
				TGPacket packet = parsePacket(read);
				packet.handleReception();
			}
			throw isDisconnected ? new IOException("Forcefully disconnected.") : new IOException("Connection lost.");
		}
		catch (IOException t)
		{
			Log.e("Unable to keep connection with %s", this);
			Log.t(t);
		}
		try
		{
			socket.close();
		}
		catch (IOException t)
		{
			Log.e("Unable to close client socket for %s", this);
			Log.t(t);
		}
		entity.remove();
		TGServer.instance.kick(this);
	}
	
	public void sendPacket(TGPacket packet)
	{
		String s = packet.toJson().toString();
		writer.println(s);
		//Log.d("Sent packet: %s", s);
	}
	
	public TGPacket parsePacket(String s)
	{
		try
		{
			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(s).getAsJsonObject();
			return TGPacket.fromJson(this, json);
		}
		catch (JsonParseException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassCastException t)
		{
			Log.e("Unable to parse packet: %s", s);
			Log.t(t);
			return null;
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("Client %s %s", socket.getRemoteSocketAddress().toString(), id != null ? id : "UNIDENTIFIED");
	}
}
