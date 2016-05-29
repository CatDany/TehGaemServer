package catdany.android.tehgaem;

public class TGException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2660389720755375616L;
	
	/**
	 * Used for reference
	 * @param format
	 * @param args
	 */
	public TGException(String format, Object... args)
	{
		super(String.format(format, args));
	}
}
