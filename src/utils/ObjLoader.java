package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class ObjLoader {
	public static Model loadObj(String path, boolean hasTexture) {
		
		List<float[]> vertices = new ArrayList<float[]>();
		List<float[]> uvs = new ArrayList<float[]>();
		List<float[]> normals = new ArrayList<float[]>();
		
		List<int[]> indices = new ArrayList<int[]>();
		List<Integer> indexOrder = new ArrayList<Integer>();
		
		BufferedReader reader;
		
		try {
			reader = FileUtils.getReader(path);
			String line = reader.readLine();
			
			while (line != null) {
				String[] data = line.split(" ");
				
				if (data.length > 2) {
					if (data[0].equals("v")) {
						float[] vertex = new float[] {Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])};
						vertices.add(vertex);
					}
					else if (data[0].equals("vt")) {
						float[] uvCoord = new float[] {Float.parseFloat(data[1]), Float.parseFloat(data[2])};
						uvs.add(uvCoord);
					}
					else if (data[0].equals("vn")) {
						float[] normal = new float[] {Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])};
						normals.add(normal);
					}
					else if (data[0].equals("f")) {
						if (data.length > 4) { 
							int[] curIndices = new int[4];
							
							for(byte i = 1; i < data.length; i++) {
								String[] faceData = data[i].split("/");
								
								int[] index;
								if (hasTexture) index = new int[] {Integer.parseInt(faceData[0])-1, Integer.parseInt(faceData[1])-1, Integer.parseInt(faceData[2])-1};
								else index = new int[] {Integer.parseInt(faceData[0])-1, 0, Integer.parseInt(faceData[2])-1};
	
								int indexPosition = -1;
								
								for(int j = 0; j < indices.size(); j++) {
									int[] check = indices.get(j);
									if (check[0] == index[0] && check[1] == index[1] && check[2] == index[2]) {
										indexPosition = j;
										break;
									}
								}
								
								if (indexPosition == -1) {
									indices.add(index);
									curIndices[i-1] = indices.size()-1;
								}
								else {
									curIndices[i-1] = indexPosition;
								}
							}
							
							indexOrder.add(curIndices[0]);
							indexOrder.add(curIndices[1]);
							indexOrder.add(curIndices[3]);
							indexOrder.add(curIndices[3]);
							indexOrder.add(curIndices[1]);
							indexOrder.add(curIndices[2]);
						}
						else {
							for(byte i = 1; i < data.length; i++) {
								String[] faceData = data[i].split("/");
								int[] index = new int[] {Integer.parseInt(faceData[0])-1, Integer.parseInt(faceData[1])-1, Integer.parseInt(faceData[2])-1};
								
								int indexPosition = -1;
								
								for(int j = 0; j < indices.size(); j++) {
									int[] check = indices.get(j);
									if (check[0] == index[0] && check[1] == index[1] && check[2] == index[2]) {
										indexPosition = j;
										break;
									}
								}
								
								if (indexPosition == -1) {
									indices.add(index);
									indexOrder.add(indices.size()-1);	//index[0]
								}
								else {
									//indices.add(indices.get(indexPosition));
									indexOrder.add(indexPosition);	//indices.get(indexPosition)[0]
								}
							}
						}
					}
				}
				
				line = reader.readLine();
			}
			
			reader.close();
			
			float[] vertexArray = new float[indices.size()*3];
			float[] uvArray = new float[indices.size()*2];
			float[] normalArray = new float[indices.size()*3];
			int[] indexArray = new int[indexOrder.size()];
			
			for(int i = 0; i < indexArray.length; i++) {
				indexArray[i] = indexOrder.get(i);//indices.get(indexOrder.get(i))[0];
			}
			
			for(int i = 0; i < vertexArray.length/3; i ++) {
				float[] vertex = vertices.get(indices.get(i)[0]);
				vertexArray[i*3+0] = vertex[0];
				vertexArray[i*3+1] = vertex[1];
				vertexArray[i*3+2] = vertex[2];
				
				float[] uv = uvs.get(indices.get(i)[1]);
				uvArray[i*2+0] = uv[0];
				uvArray[i*2+1] = 1-uv[1];
				
				float[] normal = normals.get(indices.get(i)[2]);
				normalArray[i*3+0] = normal[0];
				normalArray[i*3+1] = normal[1];
				normalArray[i*3+2] = normal[2];
				
			}

			Model model = Model.create();
			model.bind();
			model.createIndexBuffer(indexArray);
			model.createAttribute(0, vertexArray, 3);
			model.createAttribute(1, uvArray, 2);
			model.createAttribute(2, normalArray, 3);
			model.unbind();
			
			return model;
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
