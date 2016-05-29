package catdany.android.tehgaem;

import java.text.SimpleDateFormat;

public class Utils
{
	public static final SimpleDateFormat dateFormatFile = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	public static final SimpleDateFormat dateFormatLog = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static <T>boolean arrayContains(T[] array, T element)
	{
		for (T i : array)
		{
			if (i.equals(element))
			{
				return true;
			}
		}
		return false;
	}
	public static <T>boolean arrayDeepContains(T[] array, T element)
	{
		for (T i : array)
		{
			if (i == element)
			{
				return true;
			}
		}
		return false;
	}
}
