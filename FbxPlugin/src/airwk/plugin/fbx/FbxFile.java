package airwk.plugin.fbx;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import airwk.plugin.fbx.core.FbxGlobalSetting;
import airwk.plugin.fbx.core.FbxObject;
import airwk.plugin.fbx.math.FbxVector3;
import airwk.plugin.fbx.scene.FbxScene;
import airwk.plugin.fbx.scene.animation.*;
import airwk.plugin.fbx.scene.geometry.*;
import airwk.plugin.fbx.scene.shading.*;

public class FbxFile
{
	class FbxQueue<T>
	{
		private int index;
		private T elements[];
		
		public FbxQueue(T elements[])
		{
			this.elements = elements;
		}
		
		public boolean isEmpty()
		{
			return (index >= elements.length);
		}

		public T dequeue()
		{
			return elements[index++];
		}
	}
	
	// Attribute //
	private FbxScene scene = new FbxScene();
	private FbxQueue<String> queue = null;
	
	private short model_count = 0, geometry_count = 0, node_attribute_count = 0, anim_curve_count = 0, anim_curve_node_count = 0,
			deformer_count = 0, material_count = 0, texture_count = 0, video_count = 0;

	private int startFrame = 0, endFrame = 0, keyframeLength = 0;
	
	private HashMap<Long, FbxObject> maps = new HashMap<Long, FbxObject>();
	private ArrayList<FbxNode> rootnodes = new ArrayList<FbxNode>();
	//	//
	
	public FbxFile(InputStream inputStream)
	{
		String fbxFileString = "";
		
		try
		{
			InputStreamReader isr = new InputStreamReader(inputStream);
			char[] buffer = new char[inputStream.available()];
			isr.read(buffer);

			fbxFileString = new String(buffer);

			isr.close();
			isr = null;
		} 
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		
		
		queue = new FbxQueue<String>(fbxFileString.split("\n"));
		read();
		setup();
	}
	
	public FbxScene getScene()
	{
		return scene;
	}
	
	private void read()
	{
		while(!queue.isEmpty())
		{
			String line = queue.dequeue().trim();
			
			if(line.startsWith("FBXHeaderExtension"))
			{
				readFBXHeaderExtension();
			}
			else if(line.startsWith("GlobalSettings"))
			{
				readGlobalSettings();
			}
			else if(line.startsWith("Definitions"))
			{
				readDefinitions();
			}
			else if(line.startsWith("Objects"))
			{
				readObjects();
			}
			else if(line.startsWith("Connections"))
			{
				readConnections();
			}
		}
	}
	
	private void readFBXHeaderExtension()
	{
		
	}
	
	private void readGlobalSettings()
	{
		String line = "";
		FbxGlobalSetting globalSetting = scene.getGlobalSetting();
		while(!(line = queue.dequeue().trim()).startsWith(";"))
		{
			if(line.startsWith("P: \"TimeMode"))
			{
				globalSetting.getTimeMode().setMode(getLastCommaAsInt(line));
			}
			else if(line.startsWith("P: \"TimeSpanStart"))
			{
				globalSetting.getTimeSpan().setStart( getLastCommaAsLong(line) );
			}
			else if(line.startsWith("P: \"TimeSpanStop"))
			{
				globalSetting.getTimeSpan().setStop( getLastCommaAsLong(line) );
			}
			else if(line.startsWith("P: \"UpAxis\""))
			{
				globalSetting.setUpAxis(getLastCommaAsInt(line));
			}
			else if(line.startsWith("P: \"UpAxisSign"))
			{
				globalSetting.setUpAxisSign(getLastCommaAsInt(line));
			}
			else if(line.startsWith("P: \"FrontAxis\""))
			{
				globalSetting.setFrontAxis(getLastCommaAsInt(line));
			}
			else if(line.startsWith("P: \"FrontAxisSign"))
			{
				globalSetting.setFrontAxisSign(getLastCommaAsInt(line));
			}
		}
		
	}

	private void readDefinitions()
	{
		String line = "";
		while(!(line = queue.dequeue().trim()).startsWith(";"))
		{
			if(line.startsWith("ObjectType:"))
			{
				line = line.split("\"")[1];
				String attribute = queue.dequeue();
				short count = Short.parseShort(attribute.split(": ")[1].trim());
				
				if(line.equals("Model"))
				{
					model_count = count;
				}
				else if(line.equals("Geometry"))
				{
					geometry_count = count;
				}
				else if(line.equals("NodeAttribute"))
				{
					node_attribute_count = count;
				}
				else if(line.equals("AnimationCurve"))
				{
					anim_curve_count = count;
				}
				else if(line.equals("AnimationCurveNode"))
				{
					anim_curve_node_count = count;
				}
				else if(line.equals("Deformer"))
				{
					deformer_count = count;
				}
				else if(line.equals("Material"))
				{
					material_count = count;
				}
				else if(line.equals("Texture"))
				{
					texture_count = count;
				}
				else if(line.equals("Video"))
				{
					video_count = count;
				}
			}
		}
	}

	private void readObjects()
	{
		// Object properties
		int current_geometry_count = 0, current_node_attribute_count = 0, current_node_count = 0, current_deformer_count = 0,
			current_anim_curve_node_count = 0, current_anim_curve_count = 0, current_material_count = 0, current_texture_count = 0, current_video_count = 0;
		
		String line = "";
		while(!(line = queue.dequeue().trim()).startsWith(";-"))
		{
			// read Geometry
			if(current_geometry_count < geometry_count)
			{
				
				if(line.startsWith("Geometry:"))
				{
					current_geometry_count++;
					readGeometry(line);
					continue;
				}
			}
			// read nodeAttribute
			if(current_node_attribute_count < node_attribute_count)
			{
				if(line.startsWith("NodeAttribute:"))
				{
					current_node_attribute_count++;
					readNodeAttribute(line);
					continue;
				}
			}
			// read Node
			if(current_node_count < model_count)
			{
				if(line.startsWith("Model:"))
				{
					current_node_count++;
					readModel(line);
					continue;
				}
			}
			// read Deformer
			if(current_deformer_count < deformer_count)
			{
				if (line.startsWith("Deformer:"))
				{
					current_deformer_count++;
					readDeformer(line);
					continue;
				}
			}
			// read Material
			if(current_material_count < material_count)
			{
				if (line.startsWith("Material:"))
				{
					current_material_count++;
					readMaterial(line);
					continue;
				}
			}
			// read AnimCurvNode
			if(current_anim_curve_node_count < anim_curve_node_count)
			{
				
				if (line.startsWith("AnimationCurveNode:"))
				{
					current_anim_curve_node_count++;
					readAnimCurveNode(line);
					continue;
				}
			}
			// read AnimCurve
			if(current_anim_curve_count < anim_curve_count)
			{
				if (line.startsWith("AnimationCurve:"))
				{
					current_anim_curve_count++;
					readAnimCurve(line);
					continue;
				}
			}
			// read Texture
			if(current_texture_count < texture_count)
			{
				if (line.startsWith("Texture:"))
				{
					current_texture_count++;
					readTexture(line);
					continue;
				}
			}
			// read Video
			if(current_video_count < video_count)
			{
				if (line.startsWith("Video:"))
				{
					current_video_count++;
					readVideo(line);
					continue;
				}
			}
			// 
		}
	}

	private void readGeometry(String currentLine)
	{
		Object[] geoItems = getID_NameAndType(currentLine.substring(10));
		long geometry_id = (Long) geoItems[0];

		FbxMesh mesh = new FbxMesh(geometry_id);

		String line = "";
		while(true)
		{
			line = queue.dequeue().trim();
			if (line.startsWith("Vert"))
			{
				int count = toInt(line.substring(11).split(" ")[0]);
				mesh.setVertices(readFloatAttribute(queue, count));
			} 
			else if (line.startsWith("Poly"))
			{
				int count = toInt(line.substring(21).split(" ")[0]);
				mesh.setIndices(readIndicesAttribute(queue, count));
			} 
			else if (line.startsWith("Normals"))
			{
				int count = toInt(line.substring(10).split(" ")[0]);

				mesh.setNormals(readFloatAttribute(queue, count));
			} 
			else if (line.startsWith("UV:"))
			{
				int count = toInt(line.substring(5).split(" ")[0]);
				mesh.setUV(readFloatAttribute(queue, count));
			} 
			else if (line.startsWith("UVIndex"))
			{
				int count = toInt(line.substring(10).split(" ")[0]);
				mesh.setUVIndices(readUVIndicesAttribute(queue, count));
				break;
			}
		}

		maps.put(geometry_id, mesh);
	}

	private void readNodeAttribute(String currentLine)
	{
		Object[] items = getID_NameAndType(currentLine.substring(15));
		long node_attribute_id = (Long) items[0];

		int attributeType;
		FbxNodeAttribute nodeAttribute = null;

		if(items[2].equals("Null"))
		{
			attributeType = FbxNodeAttribute.Null;
			nodeAttribute = new FbxNodeAttribute(node_attribute_id, attributeType);
		}
		else if(items[2].equals("Mesh"))
		{
			attributeType = FbxNodeAttribute.Mesh;
			nodeAttribute = new FbxNodeAttribute(node_attribute_id, attributeType);
		}
		else
		{
			int skeletonType = 0;

			if(currentLine.contains("Limb")) // Limb  or LimbNode
			{
				skeletonType = FbxSkeleton.LimbNode;
			}
			else if(currentLine.contains("Root") || currentLine.contains("Effector"))
			{
				skeletonType = FbxSkeleton.Root;
			}

			nodeAttribute = new FbxSkeleton(node_attribute_id, skeletonType);
		}

		maps.put(node_attribute_id, nodeAttribute);
	}
	
	private void readModel(String currentLine)
	{
		StringBuilder[] item = new StringBuilder[2];
		item[0] = new StringBuilder();
		item[1] = new StringBuilder();
		int inner_index = 0;
		boolean begin = false;
		char temp = 0;
		
		String line = currentLine;

		for (int j = 0; j < line.length(); j++)
		{
			char c = line.charAt(j);

			if (temp == ':')
			{
				if (c == ' ')
				{
					begin = true;
					inner_index = 0;
					continue;
				} 
				else if (c == ':')
				{
					begin = true;
					inner_index = 1;
					continue;
				}
			}

			if (c == '"' || c == ',')
			{
				begin = false;

				if (inner_index == 1)
				{
					break;
				}
			}

			if (begin)
			{
				item[inner_index].append(c);
			}

			temp = c;
		}

		long nodeID = toLong(item[0].toString().trim());
		String nodeName = item[1].toString();
		
		FbxNode node = new FbxNode(nodeID);
		node.setName(nodeName);

		FbxVector3 lclT = new FbxVector3();
		FbxVector3 lclR = new FbxVector3();
		FbxVector3 lclS = new FbxVector3(1,1,1);

		while(!(line = queue.dequeue()).contains("}"))
		{
			if (line.contains("Lcl Translation"))
			{
				lclT = new FbxVector3(splitCommaToFloatArray(line, 3));
			} else if (line.contains("Lcl Rotation"))
			{
				lclR = new FbxVector3(splitCommaToFloatArray(line, 3));
			} else if (line.contains("Lcl Scaling"))
			{
				lclS = new FbxVector3(splitCommaToFloatArray(line, 3));
			}
		}

		node.setLclTranslation(lclT);
		node.setLclRotation(lclR);
		node.setLclScaling(lclS);

		maps.put(nodeID, node);
	}
	
	private void readDeformer(String currentLine)
	{
		String line = currentLine;
		
		Object[] items = getID_NameAndType(line.substring(10));
		long deformer_id = (Long) items[0];

		if (items[2].equals("Skin"))
		{
			FbxSkin skin = new FbxSkin(deformer_id);
			maps.put(deformer_id, skin);
		}
		else
		{
			FbxCluster cluster = new FbxCluster(deformer_id);

			while(true)
			{
				line = queue.dequeue().trim();
				if (line.startsWith("Indexes"))
				{
					int count = toInt(line.substring(10).split(" ")[0]);
					cluster.setIndices(readIntAttribute(queue, count));
				} 
				else if (line.startsWith("Weights"))
				{
					int count = toInt(line.substring(10).split(" ")[0]);
					cluster.setWeights(readFloatAttribute(queue, count));
				} 
				else if (line.startsWith("Transform:"))
				{
					int count = toInt(line.substring(12).split(" ")[0]);
					cluster.setTransform(readFloatAttribute(queue, count));
				}
				else if (line.startsWith("TransformLink"))
				{
					int count = toInt(line.substring(16).split(" ")[0]);
					cluster.setTransformLink(readFloatAttribute(queue, count));
					break;
				}
			}

			maps.put(deformer_id, cluster);
		}
	}
	
	private void readMaterial(String currentLine)
	{
		String line = currentLine;
		
		Object[] items = getID_NameAndType(line.substring(10));

		long material_id = (Long) items[0];
		String material_name = ((String)items[1]).substring(10);

		FbxSurfacePhong material = new FbxSurfacePhong(material_id);
		material.setName(material_name);

		// Read Properties
		while(!line.contains("}"))
		{
			line = queue.dequeue().trim();

			if(line.startsWith("P: \"Emissive\""))
			{
				material.getEmissive().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"Ambient\""))
			{
				material.getAmbient().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"Diffuse\""))
			{
				material.getDiffuse().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"NormalMap\""))
			{
				material.getNormalMap().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"Bump\""))
			{
				material.getBump().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"TransparentColor\""))
			{
				material.getTransparentColor().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"DisplacementColor\""))
			{
				material.getDisplacementColor().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"VectorDisplacementColor\""))
			{
				material.getVectorDisplacementColor().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"Specular\""))
			{
				material.getSpecular().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"Reflection\""))
			{
				material.getReflection().set(new FbxVector3(splitCommaToFloatArray(line, 3)));
			}
			else if(line.startsWith("P: \"Shinniness\""))
			{
				material.setShinniness( splitCommaToFloat(line) );
			}
			else if(line.startsWith("P: \"EmissiveFactor"))
			{
				material.setEmissiveFactor( splitCommaToFloat(line) );
			}
			else if(line.startsWith("P: \"AmbientFactor"))
			{
				material.setAmbientFactor( splitCommaToFloat(line) );
			}
			else if(line.startsWith("P: \"DiffuseFactor"))
			{
				material.setDiffuseFactor( splitCommaToFloat(line) );
			}
			else if(line.startsWith("P: \"BumpFactor"))
			{
				material.setBumpFactor( splitCommaToFloat(line) );
			}
			else if(line.startsWith("P: \"TransparencyFactor"))
			{
				material.setTransparencyFactor( splitCommaToFloat(line) );
			}
			else if(line.startsWith("P: \"DisplacementFactor"))
			{
				material.setDisplacementFactor( splitCommaToFloat(line) );
			}
			else if(line.startsWith("P: \"VectorDisplacementFactor"))
			{
				material.setVectorDisplacementFactor( splitCommaToFloat(line) );
			}
			else if(line.startsWith("P: \"SpecularFactor"))
			{
				material.setSpecularFactor( splitCommaToFloat(line) );
			}
			else if(line.startsWith("P: \"ReflectionFactor"))
			{
				material.setReflectionFactor( splitCommaToFloat(line) );
			}

//			P: "EmissiveFactor", "double", "Number", "",0
//			P: "AmbientColor", "ColorRGB", "Color", "",0.317647069692612,0.317647069692612,0.317647069692612
//			P: "DiffuseColor", "ColorRGB", "Color", "",0.317647069692612,0.317647069692612,0.317647069692612
//			P: "TransparentColor", "ColorRGB", "Color", "",1,1,1
//			P: "SpecularColor", "ColorRGB", "Color", "",0.899999976158142,0.899999976158142,0.899999976158142
//			P: "SpecularFactor", "double", "Number", "",0
//			P: "ShininessExponent", "double", "Number", "",1.99999991737042
//			P: "Emissive", "Vector3D", "Vector", "",0,0,0
//			P: "Ambient", "Vector3D", "Vector", "",0.317647069692612,0.317647069692612,0.317647069692612
//			P: "Diffuse", "Vector3D", "Vector", "",0.317647069692612,0.317647069692612,0.317647069692612
//			P: "Specular", "Vector3D", "Vector", "",0,0,0
//			P: "Shininess", "double", "Number", "",1.99999991737042
//			P: "Opacity", "double", "Number", "",1
//			P: "Reflectivity", "double", "Number", "",0
		}

		maps.put(material_id, material);
	}
	
	private void readAnimCurveNode(String currentLine)
	{
		String line = currentLine;
		
		long anim_curve_node_id = toLong(line.substring(20).split(",")[0]);
		FbxAnimCurveNode animCurveNode = new FbxAnimCurveNode(anim_curve_node_id);

		maps.put(anim_curve_node_id, animCurveNode);
	}
	
	private void readAnimCurve(String currentLine)
	{
		String line = currentLine;
		
		long anim_curve_id = toLong(line.substring(16).split(",")[0]);
		FbxAnimCurve animCurve = new FbxAnimCurve(anim_curve_id);
		
		long[] times = null;
		float[] values = null;

		while(true)
		{
			line = queue.dequeue().trim();
			if (line.startsWith("KeyTime:"))
			{
				int count = toInt(line.substring(10).split(" ")[0]);
				times = readLongAttribute(queue, count);
				if(times.length > 0)
				{
					int length = (int) ( (times[times.length - 1] - times[0])  / 1539538600L) + 1;
					if(length > keyframeLength)
					{
						keyframeLength = length;
						startFrame = (int) (times[0] / 1539538600L);
						endFrame = (int) (times[times.length - 1] / 1539538600L);
					}
				}
			}
			else if (line.startsWith("KeyValueFloat:"))
			{
				int count = toInt(line.substring(16).split(" ")[0]);
				values = readFloatAttribute(queue, count);
				break;
			}
		}

		animCurve.set(times, values);
		maps.put(anim_curve_id, animCurve);
	}
	
	private void readTexture(String currentLine)
	{
		String line = currentLine;
		
		String[] t = line.substring(9).split(",");
		long texture_id = toLong(t[0]);
		String texture_name = t[1].split("\"")[1].substring(9);

		FbxFileTexture texture = new FbxFileTexture(texture_id);
		texture.setName(texture_name);

		while(!line.contains("}"))
		{
			line = queue.dequeue().trim();
		}
		line = queue.dequeue().trim();
		// Read Properties
		while(!line.contains("}"))
		{
			line = queue.dequeue().trim();
			String[] tmp = line.split("\"");

			if(line.startsWith("FileName:"))
			{
				String tt = tmp[tmp.length-1];
				String fileName = tt;

				int indexOfSlash = tt.lastIndexOf("\\");

				if(indexOfSlash > -1)
				{
					fileName = tt.substring(indexOfSlash+1);
				}

				texture.setFileName(fileName);
			}
			else if(line.startsWith("RelativeFilename:"))
			{
				String tt = tmp[tmp.length-1];
				texture.setRelativeFileName(tt);
			}
			else if(line.startsWith("Media:"))
			{

			}
		}

		maps.put(texture_id, texture);
	}
	
	private void readVideo(String currentLine)
	{
		
	}
	
	private void readConnections()
	{
		String line = "";
		while(!queue.isEmpty())
		{
			line = queue.dequeue().trim();
			if(line.endsWith("RootNode"))
			{
				line = queue.dequeue().trim();
				String attribute = line.substring(8);

				long root_node_id = toLong(attribute.substring(0, attribute.length()-2));

				FbxNode node = (FbxNode) maps.get(root_node_id);

				rootnodes.add(node);
			}
			else if (line.endsWith(", SubDeformer::"))
			{
				line = queue.dequeue().trim();
				String[] attribute = line.substring(8).split(",");

				long node_id = toLong(attribute[0]);
				long sub_deformer_id = toLong(attribute[1]);

				FbxNode associateModel = (FbxNode) maps.get(node_id);
				FbxCluster cluster = (FbxCluster) maps.get(sub_deformer_id);

				cluster.setAssociateModel(associateModel);
			}
			
			else if (line.startsWith(";Model"))
			{
				if(line.split(",")[1].startsWith(" Model:"))
				{
					line = queue.dequeue().trim();
					String[] attribute = line.substring(8).split(",");

					long child_node_id = toLong(attribute[0]);
					long parent_node_id = toLong(attribute[1]);

					FbxNode child_node = (FbxNode) maps.get(child_node_id);
					FbxNode parent_node = (FbxNode) maps.get(parent_node_id);

					parent_node.addChild(child_node);
					child_node.setParent(parent_node);
				}
			}
			else if (line.startsWith(";Geometry"))
			{
				line = queue.dequeue().trim();
				String[] attribute = line.substring(8).split(",");

				long geometry_id = toLong(attribute[0]);
				long node_id = toLong(attribute[1]);

				
				FbxNode node = (FbxNode) maps.get(node_id);
				FbxGeometry geometry = (FbxGeometry) maps.get(geometry_id);

				node.setNodeAttribute(geometry);
			}
			else if (line.startsWith(";AnimCurveNode:"))
			{

				String[] tmp0 = line.split(",");
				if(tmp0[1].startsWith(" Model:"))
				{
					line = queue.dequeue().trim();

					String[] attribute = line.substring(8).split(",");

					long anim_curve_node_id = toLong(attribute[0]);
					long node_id = toLong(attribute[1]);

					FbxNode node = (FbxNode) maps.get(node_id);
					FbxAnimCurveNode animCurveNode = (FbxAnimCurveNode) maps.get(anim_curve_node_id);

					char type = tmp0[0].charAt(tmp0[0].length()-1);
					if(type == 'T')
					{
						node.setLclTranslation(animCurveNode);
					}
					else if(type == 'R')
					{
						node.setLclRotation(animCurveNode);
					}
					else
					{
						node.setLclScaling(animCurveNode);
					}
					
				}
			}
			else if (line.startsWith(";NodeAtt"))
			{
				line = queue.dequeue().trim();
				String[] attribute = line.substring(8).split(",");

				long node_attribute_id = toLong(attribute[0]);
				long node_id = toLong(attribute[1]);

				FbxNode node = (FbxNode) maps.get(node_id);
				FbxNodeAttribute nodeAttribute = (FbxNodeAttribute) maps.get(node_attribute_id);

				node.setNodeAttribute(nodeAttribute);
			}
			else if (line.startsWith(";Deform"))
			{
				line = queue.dequeue().trim();
				String[] attribute = line.substring(8).split(",");

				long deformer_id = toLong(attribute[0]);
				long geometry_id = toLong(attribute[1]);

				FbxDeformer deformer = (FbxDeformer) maps.get(deformer_id);
				FbxGeometry geometry = (FbxGeometry) maps.get(geometry_id);

				geometry.addDeformer(deformer);
			}
			else if (line.startsWith(";AnimCurve:"))
			{
				line = queue.dequeue().trim();
				String[] first_line = line.substring(8).split(",");
				queue.dequeue();
				queue.dequeue();
				line = queue.dequeue().trim();
				String[] second_line = line.substring(8).split(",");
				queue.dequeue();
				queue.dequeue();
				line = queue.dequeue().trim();
				String[] third_line = line.substring(8).split(",");

				long anim_curve_node_id = toLong(first_line[1]);
				long x_id = toLong(first_line[0]);
				long y_id = toLong(second_line[0]);
				long z_id = toLong(third_line[0]);
				
				FbxAnimCurveNode animCurveNode = (FbxAnimCurveNode) maps.get(anim_curve_node_id);
				FbxAnimCurve animCurveX = (FbxAnimCurve) maps.get(x_id);
				FbxAnimCurve animCurveY = (FbxAnimCurve) maps.get(y_id);
				FbxAnimCurve animCurveZ = (FbxAnimCurve) maps.get(z_id);

				animCurveNode.set(animCurveX, animCurveY, animCurveZ);
			}
			else if (line.startsWith(";SubDef"))
			{
				line = queue.dequeue().trim();
				String[] attribute = line.substring(8).split(",");

				long sub_deformer_id = toLong(attribute[0]);
				long deformer_id = toLong(attribute[1]);

				FbxCluster cluster = (FbxCluster) maps.get(sub_deformer_id);
				FbxSkin deformer = (FbxSkin) maps.get(deformer_id);

				deformer.addCluster(cluster);
			}
			else if(line.startsWith(";Material"))
			{
				line = queue.dequeue().trim();
				String[] attribute = line.substring(8).split(",");

				long material_id = toLong(attribute[0]);
				long node_id = toLong(attribute[1]);

				FbxSurfaceMaterial material = (FbxSurfaceMaterial) maps.get(material_id);
				FbxNode node = (FbxNode) maps.get(node_id);

				node.setMaterial(material);
			}
			else if(line.startsWith(";Texture"))
			{
				line = queue.dequeue().trim();
				String[] attribute = line.substring(8).split(",");

				long texture_id = toLong(attribute[0]);
				long material_id = toLong(attribute[1]);

				String target = attribute[2].trim().split("\"")[1];

				FbxTexture texture = (FbxTexture) maps.get(texture_id);
				FbxSurfacePhong material = (FbxSurfacePhong) maps.get(material_id);

				
				if(target.equalsIgnoreCase("DiffuseColor"))
				{
					material.setDiffuseTexture(texture);
				}
				else if(target.equalsIgnoreCase("TransparentColor"))
				{

				}
			}
			
			
		}
	}

	private void setup()
	{
		for (FbxObject fbxObject : maps.values())
		{
			if(fbxObject instanceof FbxGeometry)
			{
				scene.addGeometry((FbxGeometry) fbxObject);
			}
			else if(fbxObject instanceof FbxNode)
			{
				scene.addNode((FbxNode) fbxObject);
			}
			else if(fbxObject instanceof FbxSurfaceMaterial)
			{
				scene.addMaterial((FbxSurfaceMaterial) fbxObject);
			}
		}

		FbxNode sceneRootNode = scene.getRootnodes();

		for (FbxNode rootnode : rootnodes)
		{
			sceneRootNode.addChild(rootnode);
		}
		
		scene.setKeyframe(startFrame, endFrame, keyframeLength);
	}
	
	// Static Definition //
	private static StringBuilder sb = new StringBuilder();

	private static float[] readFloatAttribute(FbxQueue<String> queue, int count)
	{
		while(true)
		{
			String line = queue.dequeue().trim();
			if(line.startsWith("}"))
			{
				break;
			}
			sb.append(line);
		}

		float[] items = new float[count];

		double result = 0;
		double p = 1;
		byte sign = 1;

		int index = count-1;

		for (int i = sb.length()-1; i >= 0; i--)
		{
			char c = sb.charAt(i);

			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == '.')
			{
				result /= p;
				p = 1;
			}
			else if(c == 'e' || c == 'E')
			{
				i = convertToFloatWithE(items, index--, (i-1), result, sign);
				result = 0;
				p = 1;
				sign = 1;

				if(index < 0)
				{
					break;
				}
			}
			else if(c == '-')
			{
				sign = -1;
			}
			else
			{
				items[index--] = (float) (result * sign);
				result = 0;
				p = 1;
				sign = 1;

				if(index < 0)
				{
					break;
				}
			}
		}

		sb.delete(0, sb.length());

		return items;
	}

	private static int convertToFloatWithE(float[] items, int index, int start, double eValue, byte eSign)
	{
		long result = 0;
		long p = 1;
		double lastPow = 1;
		byte sign = 1;

		if(eSign == 1)
		{
			lastPow = getMaxPow10(eValue);
		}
		else
		{
			lastPow = getMinPow10(-eValue);
		}

		for (int i = start; i >= 0; i--)
		{
			char c = sb.charAt(i);

			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == '.')
			{
				result /= p;
				p = 1;
			}
			else if(c == '-')
			{
				sign = -1;
			}
			else
			{
				items[index] = (float) (result * lastPow * sign);
				return i;
			}
		}

		return 0;
	}

	private static int[] readIndicesAttribute(FbxQueue<String> queue, int count)
	{
		while(true)
		{
			String line = queue.dequeue().trim();
			if(line.startsWith("}"))
			{
				break;
			}

			sb.append(line);
		}

		int[] items = new int[count];

		int result = 0;
		int p = 1;
		byte sign = 1;

		int index = count-1;

		for (int i = sb.length()-1; i >= 0; i--)
		{
			char c = sb.charAt(i);

			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == '-')
			{
				sign = -1;
//				result = ((result * -1) - 1);
			}
			else
			{
				items[index--] = result * sign;
				result = 0;
				p = 1;
				sign = 1;

				if(index < 0)
				{
					break;
				}
			}
		}

		sb.delete(0, sb.length());

		return items;
	}

	private static int[] readUVIndicesAttribute(FbxQueue<String> queue, int count)
	{
		while(true)
		{
			String line = queue.dequeue().trim();
			if(line.startsWith("}"))
			{
				break;
			}

			sb.append(line);
		}

		int[] items = new int[count];

		int result = 0;
		int p = 1;
		byte sign = 1;

		int index = count-1;

		for (int i = sb.length()-1; i >= 0; i--)
		{
			char c = sb.charAt(i);

			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == '-')
			{
				sign = -1;
			}
			else
			{
				items[index--] = result * sign;
				result = 0;
				p = 1;
				sign = 1;

				if(index < 0)
				{
					break;
				}
			}
		}

		sb.delete(0, sb.length());

		return items;
	}

	private static int[] readIntAttribute(FbxQueue<String> queue, int count)
	{
		while(true)
		{
			String line = queue.dequeue().trim();
			if(line.startsWith("}"))
			{
				break;
			}

			sb.append(line);
		}

		int[] items = new int[count];

		int result = 0;
		int p = 1;
		byte sign = 1;

		int index = count-1;

		for (int i = sb.length()-1; i >= 0; i--)
		{
			char c = sb.charAt(i);

			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == '-')
			{
				sign = -1;
			}
			else
			{
				items[index--] = result * sign;
				result = 0;
				p = 1;
				sign = 1;

				if(index < 0)
				{
					break;
				}
			}
		}

		sb.delete(0, sb.length());

		return items;
	}

	private static long[] readLongAttribute(FbxQueue<String> queue, int count)
	{
		while(true)
		{
			String line = queue.dequeue().trim();
			if(line.startsWith("}"))
			{
				break;
			}

			sb.append(line);
		}

		long[] items = new long[count];

		long result = 0;
		long p = 1;
		byte sign = 1;

		int index = count-1;

		for (int i = sb.length()-1; i >= 0; i--)
		{
			char c = sb.charAt(i);

			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == '-')
			{
				sign = -1;
			}
			else
			{
				items[index--] = result * sign;
				result = 0;
				p = 1;
				sign = 1;

				if(index < 0)
				{
					break;
				}
			}
		}

		sb.delete(0, sb.length());

		return items;
	}

	private static long getLastCommaAsLong(String line)
	{
		long result = 0;
		long p = 1;
		byte sign = 1;

		for (int i = line.length()-1; i >= 0; i--)
		{
			char c = line.charAt(i);

			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == ',' || i == 0)
			{
				return result * sign;
			}
			else if(c == '-')
			{
				sign = -1;
			}
		}

		return 0;
	}

	private static int getLastCommaAsInt(String line)
	{
		int result = 0;
		int p = 1;
		int sign = 1;

		for (int i = line.length()-1; i >= 0; i--)
		{
			char c = line.charAt(i);

			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == ',' || i == 0)
			{
				return result * sign;
			}
			else if(c == '-')
			{
				sign = -1;
			}
		}

		return 0;
	}

	private static float[] splitCommaToFloatArray(String line, int count)
	{
		float[] items = new float[count];

		float result = 0;
		float p = 1;
		float lastPow = 1;
		int sign = 1;

		int index = count-1;

		for (int i = line.length()-1; i >= 0; i--)
		{
			char c = line.charAt(i);

			if(c == '-')
			{
				sign = -1;
			}
			else if(c == ',')
			{
				items[index--] = result * lastPow * sign;
				result = 0;
				p = 1;
				lastPow = 1;
				sign = 1;

				if(index < 0)
				{
					break;
				}
			}
			else if(c == '.')
			{
				result /= p;
				p = 1;
			}
			else if(c == 'e' || c == 'E')
			{
				if(sign == 1)
				{
					lastPow = (float) getMaxPow10(result);
				}
				else
				{
					lastPow = (float) getMinPow10(-result);
					sign = 1;
				}

				result = 0;
				p = 1;
			}
			else if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
		}

		return items;
	}

	private static float splitCommaToFloat(String line)
	{
		float result = 0;
		float p = 1;
		float lastPow = 1;
		int sign = 1;

		for (int i = line.length()-1; i >= 0; i--)
		{
			char c = line.charAt(i);

			if(c == '-')
			{
				sign = -1;
			}
			else if(c == ',')
			{
				return result * lastPow * sign;
			}
			else if(c == '.')
			{
				result /= p;
				p = 1;
			}
			else if(c == 'e' || c == 'E')
			{
				if(sign == 1)
				{
					lastPow = (float) getMaxPow10(result);
				}
				else
				{
					lastPow = (float) getMinPow10(-result);
					sign = 1;
				}

				result = 0;
				p = 1;
			}
			else if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
		}

		return 0;
	}

	private static Object[] getID_NameAndType(String line)
	{
		Object[] items = new Object[3];

		int indexOfComma = -1;
		int[] indexOfQuote = new int[4];
		int index = 0;

		for (int i = 0; i < line.length(); i++)
		{
			char c = line.charAt(i);

			if(c == ',' && indexOfComma == -1)
			{
				indexOfComma = i;
			}
			else if(c == '\"')
			{
				indexOfQuote[index++] = i;

				if(index > 3)
				{
					break;
				}
			}
		}

		items[0] = toLong(line.substring(0, indexOfComma));
		items[1] = line.substring(indexOfQuote[0]+1, indexOfQuote[1]);
		items[2] = line.substring(indexOfQuote[2]+1, indexOfQuote[3]);

		return items;
	}
	
	private static int toInt(String s)
	{
		int result = 0;
		int p = 1;
		int lastPow = 1;
		byte sign = 1;

		for (int i = s.length()-1; i >= 0; i--)
		{
			char c = s.charAt(i);
			
			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == '-')
			{
				sign = -1;
			}
			else if(c == '.')
			{
				result = 0;
				p = 1;
			}
			else if(c == 'e' || c == 'E')
			{
				return (int) toLongWithE(s, (byte) (i-1), result, sign);
			}
		}

		return (result * lastPow) * sign;
	}
	
	private static long toLong(String s)
	{
		long result = 0;
		long p = 1;
		long lastPow = 1;
		byte sign = 1;

		for (int i = s.length()-1; i >= 0; i--)
		{
			char c = s.charAt(i);
			
			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == '-')
			{
				sign = -1;
			}
			else if(c == '.')
			{
				result = 0;
				p = 1;
			}
			else if(c == 'e' || c == 'E')
			{
				return toLongWithE(s, (byte) (i-1), result, sign);
			}
		}

		return (result * lastPow) * sign;
	}

	private static long toLongWithE(String s, byte start, long eValue, byte eSign)
	{
		long result = 0;
		long p = 1;
		long dotValue = 0;
		
		double lastPow = 1;
		byte sign = 1;
		
		if(eSign == 1)
		{
			lastPow = (double) getMaxPow10(eValue);
		}
		else
		{
			lastPow = (double) getMinPow10(-eValue);
		}

		for (int i = start; i >= 0; i--)
		{
			char c = s.charAt(i);
			
			if(c >= '0' && c <= '9')
			{
				result += (c - 48) * p;
				p *= 10;
			}
			else if(c == '.')
			{
				float r = (float)result / (float)p;
				dotValue = (int) (r * lastPow);
				result = 0;
				p = 1;
			}
			else if(c == '-')
			{
				sign = -1;
			}
		}

		return (long)((result * lastPow) + dotValue) * sign;
	}

	private static double getMaxPow10(double positiveY)
	{
		int cast = (int) positiveY;
		double decimal = Math.exp((positiveY - cast) * 2.302585092994046);
		return Math.pow(10, cast) * decimal;
	}

	private static double getMinPow10(double negativeY)
	{
		int cast = (int) -negativeY;
		double decimal = Math.exp((negativeY + cast) * 2.302585092994046);
		return Math.pow(10, -cast) * decimal;
	}
}

