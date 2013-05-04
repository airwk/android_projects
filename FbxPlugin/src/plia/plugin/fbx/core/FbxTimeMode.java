package plia.plugin.fbx.core;

public class FbxTimeMode
{
	private int framerateValue;
	private long oneFramevalue;

	public FbxTimeMode()
	{
		setMode(0);
	}
	
	public void setMode(int mode)
	{
		int _fps = 0;

		switch(mode)
		{
			case 0 : // <-- Default
			case 6 : // <-- NTSC
			case 7 : 
			case 8 : 
			case 9 : _fps = 30; break;

			case 1 : _fps = 120; break;

			case 2 : _fps = 100; break;
			case 3 :
			case 17 : _fps = 60; break;

			case 4 : _fps = 50; break;
			case 5 : _fps = 48; break;

			case 10 : _fps = 25; break;

			case 11 : 
			case 13 : _fps = 24; break;

			case 12 : _fps = 1; break;

			case 15 : _fps = 96; break;
			case 16 : _fps = 72; break;

			default : _fps = 30; break;
		}

		this.framerateValue = _fps;
		this.oneFramevalue = Math.round(1000f / framerateValue);
	}

	public int getFrameRate()
	{
		return framerateValue;
	}

	public long getFramevalue()
	{
		return oneFramevalue;
	}

	/** Time modes.
	  * \remarks
	  * EMode \c eNTSCDropFrame is used for broadcasting operations where 
	  * clock time must be (almost) in sync with time code. To bring back color 
	  * NTSC time code with clock time, this mode drops 2 frames per minute
	  * except for every 10 minutes (00, 10, 20, 30, 40, 50). 108 frames are 
	  * dropped per hour. Over 24 hours the error is 2 frames and 1/4 of a 
	  * frame. A time-code of 01:00:03:18 equals a clock time of 01:00:00:00
	  * 
	  * \par
	  * EMode \c eNTSCFullFrame represents a time address and therefore is NOT 
	  * IN SYNC with clock time. A time code of 01:00:00:00 equals a clock time 
	  * of 01:00:03:18.
	  * 
	  * - \e eDefaultMode		
	  * - \e eFrames120			120 frames/s
	  * - \e eFrames100			100 frames/s
	  * - \e eFrames60          60 frames/s
	  * - \e eFrames50          50 frames/s
	  * - \e eFrames48          48 frame/s
	  * - \e eFrames30          30 frames/s (black and white NTSC)
	  * - \e eFrames30Drop		30 frames/s (use when display in frame is selected, equivalent to NTSC drop)
	  * - \e eNTSCDropFrame		~29.97 frames/s drop color NTSC
	  * - \e eNTSCFullFrame		~29.97 frames/s color NTSC
	  * - \e ePAL				25 frames/s	PAL/SECAM
	  * - \e eFrames24			24 frames/s Film/Cinema
	  * - \e eFrames1000		1000 milli/s (use for date time)
	  * - \e eFilmFullFrame		~23.976 frames/s
	  * - \e eCustom            Custom frame rate value
	  * - \e eFrames96			96 frames/s
	  * - \e eFrames72			72 frames/s
	  * - \e eFrames59dot94		~59.94 frames/s
	  * - \e eModesCount		Number of time modes
	  */
}