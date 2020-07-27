package com.prpg.sdl.render;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2fv;
import static org.lwjgl.opengl.GL20.glUniform2iv;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniform3iv;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniform4iv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.joml.Vector4i;

import com.prpg.sdl.resources.ResourceManager;

public class ShaderProgram {
	
	private static ShaderProgram activeShader;
	
	private int program;
	
	public ShaderProgram() {
		this.program = glCreateProgram();
	}
	
	public static ShaderProgram createShaderProgram(String vertex, String fragment) {
		ShaderProgram shaderProgram = new ShaderProgram();
		shaderProgram.loadShader(GL_VERTEX_SHADER, vertex);
		shaderProgram.loadShader(GL_FRAGMENT_SHADER, fragment);
		shaderProgram.link();
		ResourceManager.registerShader(shaderProgram);
		return shaderProgram;
	}
	
	public static ShaderProgram createShaderProgram(String vertex, String geometry, String fragment) {
		ShaderProgram shaderProgram = new ShaderProgram();
		shaderProgram.loadShader(GL_VERTEX_SHADER, vertex);
		shaderProgram.loadShader(GL_GEOMETRY_SHADER, geometry);
		shaderProgram.loadShader(GL_FRAGMENT_SHADER, fragment);
		shaderProgram.link();
		ResourceManager.registerShader(shaderProgram);
		return shaderProgram;
	}
	
	public static ShaderProgram getActiveShader() {
		return activeShader;
	}
	
	public int getProgram() {
		return this.program;
	}
	
	public void link() {
		glLinkProgram(program);
		int status = glGetProgrami(program, GL_LINK_STATUS);
		if (status != GL_TRUE) {
			String log = glGetProgramInfoLog(program);
			throw new RuntimeException(log);
		}
	}
	
	public void loadShader(int shaderType, String fileName) {
		String shaderSource;
		try {
			shaderSource = new String(Files.readAllBytes(new File(ResourceManager.CURRENT_WORKING_DIRECTORY + "/assets/shaders/" + fileName).toPath())
					, Charset.forName("UTF-8"));

			int shaderName = glCreateShader(shaderType);
			glShaderSource(shaderName, shaderSource);
			glCompileShader(shaderName);
			
			System.out.println(glGetShaderInfoLog(shaderName));
			
			glAttachShader(program, shaderName);
			glDeleteShader(shaderName);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("File " + fileName + " was unreadable!");
			System.exit(-1);
		}
	}
	
	public void uniformInt(int location, int i) {
		glUniform1i(location, i);
	}
	
	public void uniformVec2i(int location, Vector2i v) {
		glUniform2iv(location, new int[]{v.x, v.y});
	}
	
	public void uniformVec3i(int location, Vector3i v) {
		glUniform3iv(location, new int[]{v.x, v.y, v.z});
	}
	
	public void uniformVec4i(int location, Vector4i v) {
		glUniform4iv(location, new int[]{v.x, v.y, v.z, v.w});
	}
	
	public void uniformFloat(int location, float f) {
		glUniform1f(location, f);
	}
	
	public void uniformVec2(int location, Vector2f v) {
		glUniform2fv(location, new float[]{v.x, v.y});
	}
	
	public void uniformVec3(int location, Vector3f v) {
		glUniform3fv(location, new float[]{v.x, v.y, v.z});
	}
	
	public void uniformVec4(int location, Vector4f v) {
		glUniform4fv(location, new float[]{v.x, v.y, v.z, v.w});
	}
	
	public void uniformMatrix(int location, Matrix4f mat) {
		FloatBuffer buffer = memAllocFloat(16);
		mat.get(buffer);
		glUniformMatrix4fv(location, false, buffer);
		memFree(buffer);
	}
	
	public void uniformInt(String name, int i) {
		uniformInt(getUniformLocation(name), i);
	}
	
	public void uniformVec2i(String name, Vector2i v) {
		uniformVec2i(getUniformLocation(name), v);
	}
	
	public void uniformVec3i(String name, Vector3i v) {
		uniformVec3i(getUniformLocation(name), v);
	}
	
	public void uniformVec4i(String name, Vector4i v) {
		uniformVec4i(getUniformLocation(name), v);
	}
	
	public void uniformFloat(String name, float f) {
		uniformFloat(getUniformLocation(name), f);
	}

	public void uniformVec2(String name, Vector2f v) {
		uniformVec2(getUniformLocation(name), v);
	}
	
	public void uniformVec3(String name, Vector3f v) {
		uniformVec3(getUniformLocation(name), v);
	}
	
	public void uniformVec4(String name, Vector4f v) {
		uniformVec4(getUniformLocation(name), v);
	}
	
	public void uniformMatrix(String name, Matrix4f mat) {
		uniformMatrix(getUniformLocation(name), mat);
	}
	
	public void use() {
		activeShader = this;
		glUseProgram(program);
	}
	
	public int getUniformLocation(String name) {
		return glGetUniformLocation(program, name);
	}
	
	public int getAttribLocation(String name) {
		return glGetAttribLocation(program, name);
	}
	
	
	
}
