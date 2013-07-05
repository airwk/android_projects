package airwk.core;

import java.io.IOException;
import java.io.InputStream;

import airwk.graphics.ScenePrefab;
import airwk.plugin.fbx.FbxFile;
import airwk.plugin.fbx.core.FbxGlobalSetting;
import airwk.plugin.fbx.scene.FbxScene;
import android.content.Context;
import android.util.Log;

public class FbxDroid2
{

	public FbxDroid2(String fbxAssetPath, Context context)
	{
		FbxScene fbxScene = getScene(fbxAssetPath, context);
		if(fbxScene != null)
		{
			
		}
	}
	
	private FbxScene getScene(String path, Context context)
	{
		try
		{
			InputStream inputStream = context.getAssets().open(path);

			FbxFile fbxFile = new FbxFile(inputStream);
			FbxScene fbxScene = fbxFile.getScene();
			
			return fbxScene;

		} 
		catch (IOException e)
		{
			Log.e("Error", e.getMessage());
		}
		
		return null;
	}

	private String getSceneName(String path)
	{
		String filename = path.substring(0, path.length() - 4);
		int indexOfSlash = filename.lastIndexOf("/");
		if(indexOfSlash > -1)
		{
			filename = filename.substring(indexOfSlash+1, filename.length());
		}
		return filename;
	}

	private float[] getAxisRotation(FbxGlobalSetting globalSetting)
	{
		float[] axisRotation = new float[16];

		axisRotation[globalSetting.getFrontAxis() + 4] = globalSetting.getFrontAxisSign();
		axisRotation[globalSetting.getUpAxis() + 8] = globalSetting.getUpAxisSign();

		// cross [4,5,6] = forward, [8,9,10] = up.
		axisRotation[0] = (axisRotation[5] * axisRotation[10]) - (axisRotation[6] * axisRotation[9]);
		axisRotation[1] = (axisRotation[6] * axisRotation[8]) - (axisRotation[4] * axisRotation[10]);
		axisRotation[2] = (axisRotation[4] * axisRotation[9]) - (axisRotation[5] * axisRotation[8]);
		
		axisRotation[15] = 1;
		
		return axisRotation;
	}
	
	

}
