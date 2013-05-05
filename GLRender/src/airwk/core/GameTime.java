package airwk.core;

public final class GameTime
{
	private Time elapsedGameTime;
	private Time totalGameTime;
	private long start;

	private long frameTime;

	private GameTime()
	{
		totalGameTime = new Time();
		elapsedGameTime = new Time();
	}

	public void start()
	{
		start = System.currentTimeMillis();
	}

	public void update()
	{
		if(start == 0)
		{

		}
		else
		{
			long current = System.currentTimeMillis();

			// Total Game Time
			long totalMillis = current - start;
			totalGameTime.update(totalMillis);

			long elapsedMillis = totalMillis - frameTime;
			frameTime = totalMillis;

			elapsedGameTime.update(elapsedMillis);
		}
	}

	private static GameTime instance = new GameTime();
	static GameTime getInstance()
	{
		return instance;
	}

	public static Time getTotalGameTime()
	{
		return instance.totalGameTime;
	}

	public static Time getElapsedGameTime()
	{
		return instance.elapsedGameTime;
	}
}