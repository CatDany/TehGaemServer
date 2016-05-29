package catdany.android.tehgaem.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;

import catdany.android.tehgaem.Log;
import catdany.android.tehgaem.Log.Level;
import catdany.android.tehgaem.Utils;
import catdany.android.tehgaem.entity.World;
import catdany.android.tehgaem.net.packet.TGPacket;

public class TGServer implements Runnable
{
	public static Charset SOCKET_CHARSET = Charset.forName("UTF-8");
	public static final int SERVER_PORT = 10200;
	
	public static TGServer instance;
	
	public final int maxClients;
	public final ServerSocket socket;
	public final Thread threadSocketAccepter;
	public final ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	
	public TGServer(int port, int maxClients) throws IOException
	{
		this.maxClients = maxClients;
		this.socket = new ServerSocket(port);
		this.threadSocketAccepter = new Thread(this, "Socket-Accepter");
	}
	
	public static void main(String[] args) throws Exception
	{
		Log.init(Utils.arrayContains(args, "--enableDebugLogging") ? Level.DEBUG : Level.INFO);
		instance = new TGServer(SERVER_PORT, Integer.parseInt(args[0]));
		instance.threadSocketAccepter.start();
		World.instance = new World(20, 50D);
		World.instance.start();
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Socket socketClient = socket.accept();
				ClientHandler client = new ClientHandler(socketClient);
				client.threadHandler.start();
				clients.add(client);
				Log.i("Client connecting from %s", socketClient.getRemoteSocketAddress());
			}
			catch (IOException t)
			{
				Log.e("Couldn't accept client socket.");
				Log.t(t);
			}
		}
	}
	
	/**
	 * Send specified packet to all connected clients
	 * @param packet
	 */
	public void sendPacketToAll(TGPacket packet)
	{
		for (ClientHandler i : clients)
		{
			i.sendPacket(packet);
		}
	}
	
	public void kick(ClientHandler client)
	{
		if (clients.contains(client))
		{
			clients.remove(client);
			Log.i("Kicked client %s", client);
		}
		else
		{
			Log.w("Attempted to remove client %s from the list of connected clients, but it is not on the list.", client);
		}
	}
}