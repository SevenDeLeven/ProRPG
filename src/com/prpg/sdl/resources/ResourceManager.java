package com.prpg.sdl.resources;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class ResourceManager {
	
	public static final String CURRENT_WORKING_DIRECTORY = System.getProperty("user.dir");
	
	private static final List<Integer> shaderPrograms = new ArrayList<Integer>();
	
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
	
	public static int loadTexture(String fileName) {
		fileName = CURRENT_WORKING_DIRECTORY+"/assets/textures/"+fileName;
		int tex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, tex);
		int[] x = new int[1];
		int[] y = new int[1];
		int[] comp = new int[1];
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		ByteBuffer img = STBImage.stbi_load(fileName, x, y, comp, 4);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, x[0], y[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, img);
		
		return tex;
	}
	
	public static int createShaderProgram(String vertexShaderFileName, String fragmentShaderFileName) {
		int vertexShader = loadShader(GL_VERTEX_SHADER, vertexShaderFileName);
		int fragmentShader = loadShader(GL_FRAGMENT_SHADER, fragmentShaderFileName);
		int program = glCreateProgram();
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		glLinkProgram(program);
		
		int status = glGetProgrami(program, GL_LINK_STATUS);
		if (status != GL_TRUE) {
			String log = glGetProgramInfoLog(program);
			throw new RuntimeException(log);
		}
		
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		
		shaderPrograms.add(program);
		
		return program;
	}
	
	public static int createShaderProgram(String geometryShaderFileName, String vertexShaderFileName, String fragmentShaderFileName) {
		int geometryShader = loadShader(GL_GEOMETRY_SHADER, geometryShaderFileName);
		int vertexShader = loadShader(GL_VERTEX_SHADER, vertexShaderFileName);
		int fragmentShader = loadShader(GL_FRAGMENT_SHADER, fragmentShaderFileName);
		int program = glCreateProgram();
		glAttachShader(program, geometryShader);
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		glLinkProgram(program);
		
		int status = glGetProgrami(program, GL_LINK_STATUS);
		if (status != GL_TRUE) {
			String log = glGetProgramInfoLog(program);
			throw new RuntimeException(log);
		}

		glDeleteShader(geometryShader);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		
		shaderPrograms.add(program);
		
		return program;
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
	
	public static int loadShader(int shaderType, String fileName) {
		byte[] shaderData = null;
		try (FileInputStream fis = new FileInputStream(CURRENT_WORKING_DIRECTORY + "/assets/shaders/" + fileName)) {
			shaderData = new byte[fis.available()];
			fis.read(shaderData);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Shader file " + fileName + " was unreadable!");
			System.exit(-1);
		}
		String shaderSource = new String(shaderData, Charset.forName("UTF-8"));
		int shaderName = glCreateShader(shaderType);
		glShaderSource(shaderName, shaderSource);
		glCompileShader(shaderName);
		return shaderName;
	}

}
