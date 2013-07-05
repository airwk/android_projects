package airwk.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import airwk.graphics.NodePrefab;
import airwk.graphics.ScenePrefab;
import airwk.graphics.animation.Animation;
import airwk.graphics.geometry.Mesh;
import airwk.graphics.geometry.SkinnedMesh;
import airwk.graphics.shading.Material;
import airwk.graphics.shading.Texture2D;
import airwk.plugin.fbx.FbxFile;
import airwk.plugin.fbx.core.FbxGlobalSetting;
import airwk.plugin.fbx.math.FbxVector3;
import airwk.plugin.fbx.scene.FbxScene;
import airwk.plugin.fbx.scene.animation.FbxAnimCurveNode;
import airwk.plugin.fbx.scene.geometry.FbxCluster;
import airwk.plugin.fbx.scene.geometry.FbxMesh;
import airwk.plugin.fbx.scene.geometry.FbxNode;
import airwk.plugin.fbx.scene.geometry.FbxSkin;
import airwk.plugin.fbx.scene.shading.*;
import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

public final class FbxDroid
{
	private FbxDroid()
	{}
	
	public static ScenePrefab load(String fbxAssetPath, Context context)
	{
		FbxDroid fbxDroid = new FbxDroid();
		return fbxDroid.loadFbx(fbxAssetPath, context);
	}
	
	public ScenePrefab loadFbx(String fbxAssetPath, Context context)
	{
		ScenePrefab scenePrefab = new ScenePrefab();
		
		FbxScene fbxScene = getScene(fbxAssetPath, context);
		if(fbxScene != null)
		{
			String filename = getFileNameFromPath(fbxAssetPath);
			//

			loadAxisRotation(fbxScene, scenePrefab);
			loadMaterials(fbxScene, scenePrefab);
			loadNodePrefab(fbxScene, scenePrefab);
		}
		
		return scenePrefab;
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
	
	private String getFileNameFromPath(String path)
	{
		String filename = path.substring(0, path.length() - 4);
		int indexOfSlash = filename.lastIndexOf("/");
		if(indexOfSlash > -1)
		{
			filename = filename.substring(indexOfSlash+1, filename.length());
		}
		return filename;
	}

	private void loadAxisRotation(FbxScene scene,ScenePrefab scenePrefab)
	{
		FbxGlobalSetting globalSetting = scene.getGlobalSetting();
		float[] axisRotation = { 1,0,0,0,	0,1,0,0,	0,0,1,0,	0,0,0,1 };
		float[] forward = new float[3];
		float[] up = new float[3];
		float[] right = new float[3];
		
		int frontAxisSign = globalSetting.getFrontAxisSign();

		switch(globalSetting.getFrontAxis())
		{
			case 0 : forward[0] = frontAxisSign; break;
			case 1 : forward[1] = frontAxisSign; break;
			case 2 : forward[2] = frontAxisSign; break;
		}

		int upAxisSign = globalSetting.getUpAxisSign();

		switch(globalSetting.getUpAxis())
		{
			case 0 : up[0] = upAxisSign; break;
			case 1 : up[1] = upAxisSign; break;
			case 2 : up[2] = upAxisSign; break;
		}

		// cross
		right[0] = (forward[1] * up[2]) - (forward[2] * up[1]);
		right[1] = (forward[2] * up[0]) - (forward[0] * up[2]);
		right[2] = (forward[0] * up[1]) - (forward[1] * up[0]);
		
		axisRotation[0] = right[0];
		axisRotation[1] = right[1];
		axisRotation[2] = right[2];

		axisRotation[4] = forward[0];
		axisRotation[5] = forward[1];
		axisRotation[6] = forward[2];

		axisRotation[8] = up[0];
		axisRotation[9] = up[1];
		axisRotation[10] = up[2];
		
		scenePrefab.setAxisRotation(axisRotation);
	}
	
	private void loadMaterials(FbxScene scene, ScenePrefab scenePrefab)
	{
		int materialCount = scene.getMaterialCount();
		Material[] materials = new Material[materialCount];
		
		for (int i = 0; i < materialCount; i++)
		{
			FbxSurfaceMaterial surfaceMaterial = scene.getMaterial(i);
			materials[i] = new Material();
			//materials[i].setShader(Shader.DIFFUSE);

			if(surfaceMaterial instanceof FbxSurfaceLambert)
			{
				FbxVector3 diffuse = ((FbxSurfaceLambert) surfaceMaterial).getDiffuse();
				materials[i].setBaseColor(diffuse.x, diffuse.y, diffuse.z);

				FbxTexture texture = ((FbxSurfaceLambert) surfaceMaterial).getDiffuseTexture();
				if(texture instanceof FbxFileTexture)
				{
					String textureFileName = ((FbxFileTexture) texture).getFileName();

//					Texture2D texture2d = GameObjectManager.loadTexture2DWithFileName(textureFileName);

//					if(texture2d != null)
//					{
//						materials[i].setBaseTexture(texture2d);
//					}
				}
			}
		}
		
		scenePrefab.setMaterials(materials);
	}
	
	private void loadNodePrefab(FbxScene scene, ScenePrefab scenePrefab)
	{
		boolean hasAnimation = false;
		
		int geometryCount = scene.getGeometryCount();
		NodePrefab[] nodePrefabs = new NodePrefab[geometryCount];
		scenePrefab.setNodePrefabs(nodePrefabs);
		
		if(geometryCount > 0)
		{
			FbxDroidData[] datas = new FbxDroidData[geometryCount];
			for (int i = 0; i < datas.length; i++)
			{
				datas[i] = new FbxDroidData(scene, scenePrefab, (FbxMesh)scene.getGeometry(i));
				
				nodePrefabs[i] = datas[i].nodePrefab;
				
				if(datas[i].hasAnimation)
				{
					hasAnimation = true;
				}
			}
			
			if(hasAnimation)
			{
				int fps = scene.getGlobalSetting().getTimeMode().getFrameRate();
				Animation animation = new Animation(0, 100);
				animation.setFrameRate(fps);

				scenePrefab.setAnimation(animation);
			}
		}
	}
	
	class FbxDroidData
	{
		//
		private FbxMesh mesh;

		// Scene
		private FbxScene scene;

		// Type
		private boolean isSkinnedMesh = false;
		private boolean hasAnimation = false;

		// Mesh
		private float[] vertices;
		private float[] normals;
		private float[] uv;
		private int[] indices;

		private Mesh meshObject;

		// Skinned Mesh
		private float[] boneWeights;
		private short[] boneIndices;

		// Animation
		private int startFrame;
		private int endFrame;
		private int totalFrame;
		private float[][] matrixPalette;

		// Transform Default
		private FbxVector3 defaultTranslation = new FbxVector3();
		private FbxVector3 defaultRotation = new FbxVector3();
		private FbxVector3 defaultScaling = new FbxVector3();

		// Bounding Box
		private FbxVector3 max = new FbxVector3();
		private FbxVector3 min = new FbxVector3();

		// Additional
		private ArrayList<Integer> oi = new ArrayList<Integer>();
		private ArrayList<Integer> ni = new ArrayList<Integer>();
		private FbxCluster[] clustersArr;
		private ArrayList<FbxNode> rootnode = new ArrayList<FbxNode>();
		private HashMap<FbxNode, Integer> map = new HashMap<FbxNode, Integer>();

		// Skinned
		private ArrayList<FbxCluster> clusters;

		// Prefab
		private NodePrefab nodePrefab;
		private ScenePrefab scenePrefab;
		
		private FbxDroidData(FbxScene scene, ScenePrefab scenePrefab, FbxMesh mesh)
		{
			this.scene = scene;
			this.scenePrefab = scenePrefab;
			this.mesh = mesh;

			this.loadMesh();
		}

		private void loadMesh()
		{
			ArrayList<Float> nv = new ArrayList<Float>();

			float[] vertices1 = mesh.getVertices();
			float[] vertices2 = null;
			float[] normals1 = mesh.getNormals();
			float[] normals2 = null;
			float[] uv1 = mesh.getUV();
			float[] uv2 = null;
			int[] indices = mesh.getIndices();
			int[] uvIndices = mesh.getUVIndices();

			// Identify Vertices Indices
			for (int i = 0; i < indices.length; i++)
			{
				if(indices[i] < 0)
				{
					indices[i] = ((indices[i] * -1) - 1);
				}
			}

			// Flip Y of UV
			for (int i = 1; i < uv1.length; i += 2)
			{
				uv1[i] = 1 - uv1[i];
			}

			int last = vertices1.length / 3;
			int count = 0;
			int[] t = new int[uvIndices.length];

			for (int i = 0; i < t.length; i++)
			{
				t[indices[i]] = uvIndices[i];
			}

			for (int i = 0; i < t.length; i++)
			{
				if (t[indices[i]] != uvIndices[i])
				{
					count++;

					int indx0 = indices[i] * 3;
					nv.add(vertices1[indx0]);
					nv.add(vertices1[indx0 + 1]);
					nv.add(vertices1[indx0 + 2]);

					oi.add(indices[i]);
					ni.add(last);

					indices[i] = last;
					last++;
				}
			}

			int v1length = vertices1.length;
			int uvlength = (v1length / 3) * 2;

			int length = v1length + (count * 3);
			vertices2 = new float[length];
			normals2 = new float[length];
			uv2 = new float[uvlength + (count * 2)];

			System.arraycopy(vertices1, 0, vertices2, 0, v1length);

			for (int i = v1length; i < vertices2.length; i++)
			{
				vertices2[i] = nv.get(i - v1length);
			}

			for(int i = 0; i < indices.length; i++)
			{
				int index1 = indices[i] * 3;
				int index2 = i * 3;

				normals2[index1] = normals1[index2];
				normals2[index1 + 1] = normals1[index2 + 1];
				normals2[index1 + 2] = normals1[index2 + 2];

				int index3 = indices[i] * 2;
				int index4 = uvIndices[i] * 2;

				uv2[index3] = uv1[index4];
				uv2[index3 + 1] = uv1[index4 + 1];
			}

			//
			this.vertices = vertices2;
			this.normals = normals2;
			this.uv = uv2;
			this.indices = indices;

			FbxNode mn = mesh.getNode(0);
			FbxVector3 defaultT = mn.getLclTranslation();
			FbxVector3 defaultR = mn.getLclRotation();
			FbxVector3 defaultS = mn.getLclScaling();

			defaultTranslation = new FbxVector3(defaultT.x, defaultT.y, defaultT.z);
			defaultRotation = new FbxVector3(defaultR.x, defaultR.y, defaultR.z);
			defaultScaling = new FbxVector3(defaultS.x, defaultS.y, defaultS.z);

			// Find Max Bounds
			for (int i = 0; i < vertices2.length / 3; i++)
			{
				int indx = i * 3;
				float x = vertices2[indx];
				float y = vertices2[indx + 1];
				float z = vertices2[indx + 2];

				max.x = Math.max(x, max.x);
				max.y = Math.max(y, max.y);
				max.z = Math.max(z, max.z);

				min.x = Math.min(x, min.x);
				min.y = Math.min(y, min.y);
				min.z = Math.min(z, min.z);

//				Log.e("Vertices : "+i, x+", "+ y+", "+z);
			}

//			// Material Zone
			FbxSurfaceMaterial surfaceMaterial = mesh.getNode(0).getMaterial();
			Material material = null;

			int sceneMaterialCount = scene.getMaterialCount();
			for (int j = 0; j < sceneMaterialCount; j++)
			{
				if(scene.getMaterial(j) == surfaceMaterial)
				{
					material = scenePrefab.getMaterials()[j];
				}
			}

			if(material == null)
			{
				material = scenePrefab.getDefaultMaterial();
			}

			// Skinned + Animation Zone
			int deformerCount = mesh.getDeformerCount();
			if(deformerCount > 0)
			{
				isSkinnedMesh = true;
				loadSkin();
			}
			else
			{
				FbxNode n = mesh.getNode(0);

				if(n != null)
				{
					FbxAnimCurveNode aT = n.getAnimCurveNodeT();
					FbxAnimCurveNode aR = n.getAnimCurveNodeR();
					FbxAnimCurveNode aS = n.getAnimCurveNodeS();

					int tt = 0, tr = 0, ts = 0, st = 0, sr = 0, ss = 0;

					if(aT != null)
					{
						tt = aT.getTotalFrame();
						st = aT.getStartFrame();
					}
					if(aR != null)
					{
						tr = aR.getTotalFrame();
						sr = aR.getStartFrame();
					}
					if(aS != null)
					{
						ts = aS.getTotalFrame();
						ss = aS.getStartFrame();
					}

					totalFrame = Math.max(Math.max(tt, tr), ts);
					if(totalFrame == tt)
					{
						startFrame = st;
					}
					else if(totalFrame == tr)
					{
						startFrame = sr;
					}
					else if(totalFrame == ts)
					{
						startFrame = ss;
					}

					if(totalFrame > 0)
					{
						endFrame = (totalFrame + startFrame) - 1;
						hasAnimation = true;

						matrixPalette = new float[totalFrame][16];
						for (int frame = startFrame; frame < endFrame+1; frame++)
						{
							int i = frame - startFrame;

							if((n.getAnimCurveNodeT() == null))
							{
								T[0] = 0;
								T[1] = 0;
								T[2] = 0;
							}
							else
							{
								n.getAnimCurveNodeT().getValue(T, frame);
							}

							if((n.getAnimCurveNodeR() == null))
							{
								R[0] = 0;
								R[1] = 0;
								R[2] = 0;
							}
							else
							{
								n.getAnimCurveNodeR().getValue(R, frame);
							}

							if((n.getAnimCurveNodeS() == null))
							{
								S[0] = 1;
								S[1] = 1;
								S[2] = 1;
							}
							else
							{
								n.getAnimCurveNodeS().getValue(S, frame);
							}

							translation = createTranslation(T[0], T[1], T[2]);
							rx = createRotationX(R[0]);
							ry = createRotationY(R[1]);
							rz = createRotationZ(R[2]);
							
							Matrix.multiplyMM(rzy, 0, rz, 0, ry, 0);
							Matrix.multiplyMM(rotation, 0, rzy, 0, rx, 0);

							scaling = createScale(S[0], S[1], S[2]);

							Matrix.multiplyMM(TR, 0, translation, 0, rotation, 0);
							Matrix.multiplyMM(TRS, 0, TR, 0, scaling, 0);

							for (int j = 0; j < TRS.length; j++)
							{
								matrixPalette[i][j] = TRS[j];
							}
						}
					}
				}
				else
				{
					totalFrame = 0;
					startFrame = 0;
					hasAnimation = false;
				}
			}


			// Create Mesh
			if(isSkinnedMesh)
			{
				meshObject = new SkinnedMesh(vertices, normals, uv, indices, boneWeights, boneIndices);
//				meshObject.setBuffer(2, boneBuffers[0]);
//				meshObject.setBuffer(3, boneBuffers[1]);
			}
			else
			{
				meshObject = new Mesh(vertices, normals, uv, indices);
			}

//			meshObject.setBuffer(0, meshBuffers[0]);
//			meshObject.setBuffer(1, meshBuffers[1]);

			if(hasAnimation)
			{
				meshObject.setMatrixPalette(getMatrixPalette());
				meshObject.setMatrixPaletteIndexOffset(getStartFrame());
			}

			float[] minf = {min.x, min.y, min.z};
			float[] maxf = {max.x, max.y, max.z};
			meshObject.setMin(minf);
			meshObject.setMax(maxf);

			nodePrefab = new NodePrefab();
			//nodePrefab.setName(getName());
			nodePrefab.setMesh(meshObject);
			nodePrefab.setMaterial(material);
			nodePrefab.setHasAnimation(hasAnimation);
		}
		
		private void loadSkin()
		{
			for (int d = 0; d < mesh.getDeformerCount(); d++)
			{
				FbxSkin skin = (FbxSkin)mesh.getDeformer(d);

				clusters = skin.getClusters();

				ArrayList<Integer> addOnIndices = new ArrayList<Integer>();
				ArrayList<Float> addOnWeights = new ArrayList<Float>();

				for (FbxCluster cluster : clusters)
				{
					int[] indices1 = cluster.getIndices();
					float[] weights1 = cluster.getWeights();

					if(indices1 == null)
					{
//						removableCluster.add(cluster);
						continue;
					}

					for (int i = 0; i < indices1.length; i++)
					{
						int key = indices1[i];

						for (int j = 0; j < oi.size(); j++)
						{
							if(key == oi.get(j))
							{
								addOnIndices.add(ni.get(j));
								addOnWeights.add(weights1[i]);
							}
						}
					}

					int size = addOnIndices.size() + indices1.length;

					int[] lastIndices = new int[size];
					float[] lastWeights = new float[size];

					System.arraycopy(indices1, 0, lastIndices, 0, indices1.length);
					System.arraycopy(weights1, 0, lastWeights, 0, weights1.length);

					for (int i = weights1.length; i < lastWeights.length; i++)
					{
						int indx = i - weights1.length;
						lastIndices[i] = addOnIndices.get(indx);
						lastWeights[i] = addOnWeights.get(indx);
					}

					cluster.setIndices(lastIndices);
					cluster.setWeights(lastWeights);

					addOnIndices.clear();
					addOnWeights.clear();
				}
			}

//			clusters.removeAll(removableCluster);

			int tableSize = (vertices.length / 3) * 4;
			short[] boneIndexTable = new short[tableSize];
			float[] boneWeightTable = new float[tableSize];

			for (int i = 0; i < clusters.size(); i++)
			{
				FbxCluster fbxCluster = clusters.get(i);
				FbxNode n = fbxCluster.getAssociateModel();

				if(n != null)
				{
					FbxAnimCurveNode T = n.getAnimCurveNodeT();
					FbxAnimCurveNode R = n.getAnimCurveNodeR();
					FbxAnimCurveNode S = n.getAnimCurveNodeS();

					int t = 0, r = 0, s = 0, st = 0, sr = 0, ss = 0;

					if(T != null)
					{
						t = T.getTotalFrame();
						st = T.getStartFrame();
					}
					if(R != null)
					{
						r = R.getTotalFrame();
						sr = R.getStartFrame();
					}
					if(S != null)
					{
						s = S.getTotalFrame();
						ss = S.getStartFrame();
					}

					totalFrame = Math.max(Math.max(t, r), s);
					if(totalFrame == t)
					{
						startFrame = st;
					}
					else if(totalFrame == r)
					{
						startFrame = sr;
					}
					else if(totalFrame == s)
					{
						startFrame = ss;
					}

					if(totalFrame > 0)
					{
						endFrame = (totalFrame + startFrame) - 1;
						hasAnimation = true;
					}
				}
				else
				{
					totalFrame = 0;
					startFrame = 0;
					hasAnimation = false;
				}

				map.put(n, i);

				if(n != null)
				{
					if(n.isRoot())
					{
						FbxNode root = n;
						if(!rootnode.contains(root))
						{
							rootnode.add(root);
						}
					}
					else
					{
						FbxNode root = findRootBone(n);
						if(!rootnode.contains(root))
						{
							rootnode.add(root);
						}
					}
				}

				int[] cIndices = fbxCluster.getIndices();

				if(cIndices == null)
				{
					continue;
				}

				for (int j = 0; j < cIndices.length; j++)
				{
					int location = cIndices[j] * 4;
					for (int k = location; k < location + 4; k++)
					{
						if(boneWeightTable[k] <= 0.00000001f)
						{
							boneIndexTable[k] = (short) i;
							boneWeightTable[k] = fbxCluster.getWeights()[j];
							break;
						}
					}
				}
			}

			this.boneWeights = boneWeightTable;
			this.boneIndices = boneIndexTable;

			clustersArr = new FbxCluster[clusters.size()];
			clusters.toArray(clustersArr);

			if(hasAnimation && totalFrame > 0)
			{
				matrixPalette = new float[totalFrame][];

				for (int frame = startFrame; frame < endFrame+1; frame++)
				{
					int i = frame - startFrame;
					matrixPalette[i] = new float[clusters.size() * 16];
					for (int j = 0; j < rootnode.size(); j++)
					{
						FbxNode node = rootnode.get(j);
						recursive(frame, node, createIdentityMatrix4());
					}
				}
			}

			// Gen Bone Buffer
//			boneBuffers = genBonesBuffer(boneWeights, boneIndices);
		}

		private FbxNode findRootBone(FbxNode node)
		{
			FbxNode parent = node.getParent();

			if(parent != null)
			{
				return findRootBone(parent);
			}

			return node;
		}

		

		private void recursive(int frame, FbxNode node, float[] parentWorld)
		{
			Integer i = map.get(node);

			if((node.getAnimCurveNodeT() == null))
			{
				translation = createIdentityMatrix4();
			}
			else
			{
				node.getAnimCurveNodeT().getValue(T, frame);
				translation[12] = T[0];
				translation[13] = T[1];
				translation[14] = T[2];
			}

			if((node.getAnimCurveNodeR() == null))
			{
				rotation = createIdentityMatrix4();
			}
			else
			{
				node.getAnimCurveNodeR().getValue(R, frame);
				rx = createRotationX(R[0]);
				ry = createRotationY(R[1]);
				rz = createRotationZ(R[2]);

				Matrix.multiplyMM(rzy, 0, rz, 0, ry, 0);
				Matrix.multiplyMM(rotation, 0, rzy, 0, rx, 0);

			}

			if((node.getAnimCurveNodeS() == null))
			{
				scaling = createIdentityMatrix4();
			}
			else
			{
				node.getAnimCurveNodeS().getValue(S, frame);
				scaling = createScale(S[0], S[1], S[2]);
			}

			Matrix.multiplyMM(TR, 0, translation, 0, rotation, 0);
			Matrix.multiplyMM(TRS, 0, TR, 0, scaling, 0);

//			Matrix4.createTRS_ZYX(TRS, T[0], T[1], T[2], R[0], R[1], R[2], S[0], S[1], S[2]);
			Matrix.multiplyMM(AbsoluteTransform, 0, parentWorld, 0, TRS, 0);

			if(i != null)
			{
				System.arraycopy(clustersArr[i].getTransform(), 0, clusterTransform, 0, 16);

				float[] Transform = createIdentityMatrix4();
				Matrix.multiplyMM(Transform, 0, AbsoluteTransform, 0, clusterTransform, 0);

				System.arraycopy(Transform, 0, temp, 0, 16);
				System.arraycopy(temp, 0, matrixPalette[(frame - startFrame)], i * 16, 16);
			}

			for (int j = 0; j < node.getChildCount(); j++)
			{
				float[] mAbsolute = new float[16];
				System.arraycopy(AbsoluteTransform, 0, mAbsolute, 0, 16);
				recursive(frame, node.getChild(j), mAbsolute);
			}
		}

		public Mesh getMeshObject()
		{
			return meshObject;
		}

		public String getName()
		{
			return mesh.getNode(0).getName();
		}

		public boolean hasAnimation()
		{
			return hasAnimation;
		}

		public boolean isSkinnedMesh()
		{
			return isSkinnedMesh;
		}

		public int getStartFrame()
		{
			return startFrame;
		}

		public int getEndFrame()
		{
			return endFrame;
		}

		public int getTotalFrame()
		{
			return totalFrame;
		}

		public float[][] getMatrixPalette()
		{
			return matrixPalette;
		}

		public int getNormalOffset()
		{
			return vertices.length * 4;
		}

		public int getUVOffset()
		{
			return (vertices.length + normals.length) * 4;
		}

		public FbxVector3 getDefaultTranslation()
		{
			return defaultTranslation;
		}

		public FbxVector3 getDefaultRotation()
		{
			return defaultRotation;
		}

		public FbxVector3 getDefaultScaling()
		{
			return defaultScaling;
		}

		public FbxVector3 getMax()
		{
			return max;
		}

		public FbxVector3 getMin()
		{
			return min;
		}
		
		
		////
		
		
	}
	
	private static float[] createIdentityMatrix4()
	{
		return new float[] {1,0,0,0,	0,1,0,0,	0,0,1,0,	0,0,0,1};
	}
	
	private static float[] createTranslation(float x, float y, float z)
	{
		return new float[]{ 1,0,0,0, 0,1,0,0, 0,0,1,0, x, y, z, 1};
	}
	
	private static float[] createRotationX(float degree)
	{
		float radian = degree * 0.0174533f;
		float cos = (float) Math.cos(radian);
		float sin = (float) Math.sin(radian);
		
		float[] result = { 1,0,0,0, 0,cos,sin,0, 0,-sin,cos,0, 0,0,0,1 };
		return result;
	}
	
	private static float[] createRotationY(float degree)
	{
		float radian = degree * 0.0174533f;
		float cos = (float) Math.cos(radian);
		float sin = (float) Math.sin(radian);
		
		float[] result = { cos,0,-sin,0, 0,1,0,0, sin,0,cos,0, 0,0,0,1 };
		return result;
	}
	
	private static float[] createRotationZ(float degree)
	{
		float radian = degree * 0.0174533f;
		float cos = (float) Math.cos(radian);
		float sin = (float) Math.sin(radian);
		
		float[] result = { cos,sin,0,0, -sin,cos,0,0, 0,0,1,0, 0,0,0,1 };
		return result;
	}

	private static float[] createScale(float x, float y, float z)
	{
		float[] result = { x,0,0,0, 0,y,0,0, 0,0,z,0, 0,0,0,1 };
		return result;
	}
	
	private static float[] AbsoluteTransform = createIdentityMatrix4();
//	private static Matrix4 Transform = new Matrix4();
	private static float[] TRS = createIdentityMatrix4();
	private static float[] T = createIdentityMatrix4();
	private static float[] R = createIdentityMatrix4();
	private static float[] S = createIdentityMatrix4();

	private static float[] rx = createIdentityMatrix4();
	private static float[] ry = createIdentityMatrix4();
	private static float[] rz = createIdentityMatrix4();
	private static float[] rzy = createIdentityMatrix4();

	private static float[] translation = createIdentityMatrix4();
	private static float[] rotation = createIdentityMatrix4();
	private static float[] scaling = createIdentityMatrix4();

	private static float[] TR = createIdentityMatrix4();
	private static float[] clusterTransform = createIdentityMatrix4();
	private static float[] temp = createIdentityMatrix4();
	
	
	
}
