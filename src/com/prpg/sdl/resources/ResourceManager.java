package com.prpg.sdl.resources;

import static org.lwjgl.opengl.GL20.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import com.prpg.sdl.render.ShaderProgram;

public class ResourceManager {
	
	public static final String CURRENT_WORKING_DIRECTORY = System.getProperty("user.dir");
	
	private static final List<Integer> shaderPrograms = new ArrayList<Integer>();
	private static final HashMap<String, Integer> textures = new HashMap<String, Integer>();
	
	public static int createVBO(float[] data) {
		MemoryStack stack = MemoryStack.stackPush();
		FloatBuffer verts = stack.mallocFloat(data.length*Float.BYTES);
		for (float f : data) {
			verts.put(f);
		}
		verts.flip();
		
		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
		
		stack.pop();
		
		return vbo;
	}
	
	public static int loadTexture(String fName, int wrap_s, int wrap_t) {
		String fileName = CURRENT_WORKING_DIRECTORY+"/assets/textures/"+fName;
		int tex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, tex);
		int[] x = new int[1];
		int[] y = new int[1];
		int[] comp = new int[1];
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		ByteBuffer img = STBImage.stbi_load(fileName, x, y, comp, 4);
		if (img == null) {
			throw new NullPointerException("Image does not exist or there was an error loading the texture " + fileName);
		}
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap_t);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap_s);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, x[0], y[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, img);
		
		textures.put(fName, tex);
		return tex;
	}
	
	public static int loadTexture(String fName) {
		return loadTexture(fName, GL_REPEAT, GL_REPEAT);
	}
	
	public static int getTexture(String fileName) {
		if (!textures.containsKey(fileName)) {
			loadTexture(fileName);
		}
		return textures.get(fileName);
	}
	
	
	/**
	 * Registers a shader into the resource manager.
	 * Used for deleting shaders when cleaning up
	 * @param program
	 */
	public static void registerShader(ShaderProgram program) {
		shaderPrograms.add(program.getProgram());
	}
	
	
	public static void deleteShaderProgram(int program) {
		glDeleteProgram(program);
		shaderPrograms.remove(program);
	}
	
	public static void cleanPrograms() {
		while (shaderPrograms.size() > 0) {
			int program = shaderPrograms.get(0);
			glDeleteProgram(program);
			shaderPrograms.remove(0);
		}
	}
	
	
	/**
	 * Must use filename if deleting textures
	 * @param fileName The name of the file which the texture was loaded from
	 */
	public static void deleteTexture(String fileName) {
		glDeleteTextures(textures.get(fileName));
		textures.remove(fileName);
	}
	
	public static void cleanTextures() {
		for (Entry<String, Integer> entry : textures.entrySet()) {
			glDeleteTextures(entry.getValue());
		}
		textures.clear();
	}
	

}
