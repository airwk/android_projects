package com.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import plia.framework.FrameworkSettings;

public class MainActivity extends Activity
{
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        game = new Game(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        game.resume();;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        game.pause();;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        game.destroy();;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
