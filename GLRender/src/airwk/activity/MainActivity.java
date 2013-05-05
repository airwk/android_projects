package airwk.activity;

import java.io.IOException;
import java.io.InputStream;

import airwk.activity.R;
import airwk.core.FbxDroid;
import airwk.graphics.Node;
import airwk.graphics.ScenePrefab;
import airwk.plugin.fbx.FbxFile;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Node node = new Node();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			long start = System.nanoTime();
			ScenePrefab prefab = FbxDroid.load("elementalist31.FBX", this);
			float end = (System.nanoTime() - start) / 1000000f;
			Log.e("Usage Time", end+" ms");
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
