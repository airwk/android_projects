package airwk.core;

public final class Time
{
	private long days;
	private long hours;
	private long minutes;
	private long seconds;
	private long milliseconds;

	private double totalDays;
	private double totalHours;
	private double totalMinutes;
	private double totalSeconds;
	private double totalMilliseconds;

	public Time()
	{

	}

	public void update(long totalMillis)
	{
		totalDays = totalMillis / 86400000D;
		totalHours = totalMillis / 3600000D;
		totalMinutes = totalMillis / 60000D;
		totalSeconds = totalMillis / 1000D;
		totalMilliseconds = totalMillis;

		// Clock Time
		milliseconds = totalMillis % 1000L;
		days = (long) (totalDays % 30L);
		hours = (long) (totalHours % 24L);
		minutes = (long) (totalMinutes % 60L);
		seconds = (long) (totalSeconds % 60L);
	}

	/*
	public void setDays(long days)
	{
		this.days = days;
	}
	
	public void setHours(long hours)
	{
		this.hours = hours;
	}
	
	public void setMinutes(long mins)
	{
		this.minutes = mins;
	}
	
	public void setSeconds(long sec)
	{
		this.seconds = sec;
	}
	
	public void setMilliseconds(long millis)
	{
		this.milliseconds = millis;
	}
	*/

	public long getDays()
	{
		return days;
	}

	public long getHours()
	{
		return hours;
	}

	public long getMinutes()
	{
		return minutes;
	}

	public long getSeconds()
	{
		return seconds;
	}

	public long getMilliseconds()
	{
		return milliseconds;
	}

	public double getTotalDays()
	{
		return totalDays;
	}

	public double getTotalHours()
	{
		return totalHours;
	}

	public double getTotalMinutes()
	{
		return totalMinutes;
	}

	public double getTotalSeconds()
	{
		return totalSeconds;
	}

	public double getTotalMilliseconds()
	{
		return totalMilliseconds;
	}
}
