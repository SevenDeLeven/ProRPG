package com.prpg.sdl.resources;

import static org.lwjgl.opengl.GL33.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
		String shaderSource;
		try {
			shaderSource = new String(Files.readAllBytes(new File(CURRENT_WORKING_DIRECTORY + "/assets/shaders/" + fileName).toPath())
					, Charset.forName("UTF-8"));

			int shaderName = glCreateShader(shaderType);
			glShaderSource(shaderName, shaderSource);
			glCompileShader(shaderName);
			
			System.out.println(glGetShaderInfoLog(shaderName));
			return shaderName;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("File " + fileName + " was unreadable!");
			System.exit(-1);
			return -1;
		}
	}

}
