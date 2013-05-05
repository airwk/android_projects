package airwk.graphics.animation;

import java.util.HashMap;

import airwk.core.GameTime;

public class Animation
{
	private int frameRate;
	private float interval;

	private int startFrame;
	private int stopFrame;
	private int totalFrame;

	private float currentFrame;

	private float playbackSpeed = 1;

	private AnimationClip currentAnimationClip;
	private HashMap<String, AnimationClip> animationClips = new HashMap<String, AnimationClip>();
	private OnAnimationStateChangeListener animationStateChangeListener = null;

	private boolean playing = false;
	
	public Animation(int start, int total)
	{
		startFrame = start;
		stopFrame = (total + start) - 1;
		totalFrame = total;

		frameRate = 60;
		interval = 1000f / frameRate;

		currentAnimationClip = new AnimationClip("idle", startFrame, stopFrame);
		animationClips.put(currentAnimationClip.getName(), currentAnimationClip);
	}

	@Override
	public Animation clone()
	{
		Animation animation = new Animation(startFrame, totalFrame);

		animation.frameRate = frameRate;
		animation.interval = interval;
		animation.startFrame = startFrame;
		animation.stopFrame = stopFrame;
		animation.totalFrame = totalFrame;
		animation.currentFrame = currentFrame;
		animation.playbackSpeed = playbackSpeed;

		animation.currentAnimationClip = currentAnimationClip;
		animation.animationClips = animationClips;

		animation.playing = playing;

		return animation;
	}
	
	public void update()
	{
		if(playing)
		{
			if(currentFrame >= currentAnimationClip.getEnd())
			{
				if(currentAnimationClip.getPlaybackMode() == PlaybackMode.LOOP)
				{
					currentFrame = currentAnimationClip.getStart();
					onAnimationStateChanged(AnimationState.BEGIN);
				}
				else
				{
					stop();
					onAnimationStateChanged(AnimationState.END);
				}
			}
			else
			{
				float step = ((float)GameTime.getElapsedGameTime().getMilliseconds() / interval);
				currentFrame += step * playbackSpeed;
			}
		}
	}


	public int getFrameRate()
	{
		return frameRate;
	}

	public void setFrameRate(int frameRate)
	{
		this.frameRate = frameRate;
		this.interval = 1000f / frameRate;
	}

	public float getPlaybackSpeed()
	{
		return playbackSpeed;
	}

	public void setPlaybackSpeed(float playbackSpeed)
	{
		if(playbackSpeed > 2.0f)
		{
			playbackSpeed = 2.0f;
		}
		else if(playbackSpeed < 0.4f)
		{
			playbackSpeed = 0.4f;
		}

		this.playbackSpeed = playbackSpeed;
	}

	public int getStartFrame()
	{
		return startFrame;
	}

	public int getStopFrame()
	{
		return stopFrame;
	}

	public int getTotalFrame()
	{
		return totalFrame;
	}

	public int getCurrentFrame()
	{
		return (int) currentFrame;
	}

	public AnimationClip getAnimationClip(String clipName)
	{
		return animationClips.get(clipName);
	}

	public void addAnimationClip(String clipName, int start, int end)
	{
		this.animationClips.put(clipName, new AnimationClip(clipName, start, end));
	}

	public void addAnimationClip(String clipName, int start, int end, PlaybackMode playbackMode)
	{
		AnimationClip clip = new AnimationClip(clipName, start, end);
		clip.setPlaybackMode(playbackMode);
		this.animationClips.put(clipName, clip);
	}

	public void removeAnimationClip(String clipName)
	{
		this.animationClips.remove(clipName);
	}

	public void play(String clipName)
	{
		//
		if(animationClips.containsKey(clipName))
		{
			currentAnimationClip = animationClips.get(clipName);
			currentFrame = currentAnimationClip.getStart();
			playing = true;
			onAnimationStateChanged(AnimationState.BEGIN);
		}
	}

	public void stop()
	{
		//
		playing = false;
	}

	public boolean isPlaying(String clipName)
	{
		return currentAnimationClip.getName() == clipName;
	}
	
	public void setOnAnimationStateChangeListener(OnAnimationStateChangeListener listener)
	{
		animationStateChangeListener = listener;
	}
	
	private void onAnimationStateChanged(AnimationState state)
	{
		if(animationStateChangeListener != null)
		{
			animationStateChangeListener.onAnimationStateChanged(state);
		}
	}
}
