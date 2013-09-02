package com.game;

import android.app.Activity;

import plia.framework.Framework;
import plia.framework.GameTime;
import plia.node.group.Level;

/**
 * Created by Wirune on 1/9/2556.
 */
public class Game extends Framework
{
    private Level level;
    private Scene1 scene1;

    public Game(Activity activity)
    {
        super(activity);
        trace("Constructor");
    }

    @Override
    public void onCreate()
    {
        trace("OnCreate");
        level = new Level();
        scene1 = new Scene1();
        level.addChild(scene1);
        setLevel(level);
    }

    @Override
    public void onResume()
    {
        trace("OnResume");
    }

    @Override
    public void onPause()
    {
        trace("OnPause");
    }

    @Override
    public void onDestroy()
    {
        trace("OnDestroy");
    }
}
