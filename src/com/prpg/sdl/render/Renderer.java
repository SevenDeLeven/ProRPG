package com.prpg.sdl.render;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.prpg.sdl.gui.MenuScreen;
import com.prpg.sdl.gui.Screen;
import com.prpg.sdl.gui.TextAtlas;
import com.prpg.sdl.resources.ResourceManager;

public class Renderer {
	
	static long glfwWindow;
	
	static int quadVAO;
	static int quadVBO;
	
	private static ShaderProgram basicShaderProgram;
	private static ShaderProgram textShaderProgram;
	private static ShaderProgram scaleShaderProgram;
	
	private static List<Screen> screens = new ArrayList<Screen>();
	
	public static LinkedBlockingQueue<Runnable> eventQueue = new LinkedBlockingQueue<Runnable>();
	
	public static List<Screen> getScreens() {
		return screens;
	}
	
	public static void queueEvent(Runnable event) {
		eventQueue.add(event);
	}
	
	public static void create() {
		
		glfwSetErrorCallback(new GLFWErrorCallbackI() {

			@Override
			public void invoke(int arg0, long arg1) {
				System.err.println("GLFWERRORCALLBACK: " + arg0 + " " + arg1);
			}
			
		});
		
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindow = glfwCreateWindow(640, 480, "name", NULL, NULL);
		glfwMakeContextCurrent(glfwWindow);
		
		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(glfwWindow, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				glfwWindow,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
			
		}
		
		GL.createCapabilities();

		glViewport(0,0,640,480);
		
		//Text Loading
		
		TextAtlas.init();
		
		//Load Shaders

		basicShaderProgram = ShaderProgram.createShaderProgram("vertShader.vert", "fragShader.frag");
		textShaderProgram = ShaderProgram.createShaderProgram("textShader.vert", "textShader.frag");
		scaleShaderProgram = ShaderProgram.createShaderProgram("scaleShader.vert", "scaleShader.frag");
		
		
		quadVAO = glGenVertexArrays();
		glBindVertexArray(quadVAO);
		
		float[] vertexPositionData = {	
				-0.5f,-0.5f,
				0.5f,0.5f,
				-0.5f,0.5f,
				-0.5f,-0.5f,
				0.5f,-0.5f,
				0.5f,0.5f,
				0,0,
				1,1,
				0,1,
				0,0,
				1,0,
				1,1,
		};
		
		
		quadVBO = ResourceManager.createVBO(vertexPositionData);
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		
		screens.add(new MenuScreen());
		
		
	}
	
	public static void pollEvents() {
		
		while (!eventQueue.isEmpty()) {
			Runnable evt = eventQueue.poll();
			evt.run();
		}
		
	}
	
	public static void loop() {

		Matrix4f model = new Matrix4f();
		model.scale(215*2, 14*2, 1);
		
		glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		while (!glfwWindowShouldClose(glfwWindow)) {
			pollEvents();
			
			glfwPollEvents();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			//RENDER

			for (Screen screen : screens) {
				screen.draw();
			}
			
			//TEST RENDER STUFF
//			basicShaderProgram.use();
//			glActiveTexture(GL_TEXTURE0);
//			glBindTexture(GL_TEXTURE_2D, TextAtlas.getTexture());
//			basicShaderProgram.uniformMatrix(basicShaderProgram.getUniformLocation("model"), model);
//			basicShaderProgram.uniformMatrix(basicShaderProgram.getUniformLocation("projection"), loadProjectionMatrix());
//			bindQuad();
//			glDrawArrays(GL_TRIANGLES, 0, 6);
			
			//STOP RENDER
			
			glfwSwapBuffers(glfwWindow);
		}
	}
	
	public static void destroy() {
		glDeleteVertexArrays(quadVAO);
		glDeleteBuffers(quadVBO);
		ResourceManager.cleanPrograms();
		glfwTerminate();
	}
	
	public static Matrix4f loadProjectionMatrix() {
		Matrix4f projection = new Matrix4f();
		projection.scale(1f/640f,1f/480f,1);
		return projection;
	}
	
	public static void bindQuad() {
		ShaderProgram activeShader = ShaderProgram.getActiveShader();
		int positionLocation = activeShader.getAttribLocation("position");
		int uvLocation = activeShader.getAttribLocation("v_uv");
		glBindVertexArray(quadVAO);
		glEnableVertexAttribArray(positionLocation);
		glVertexAttribPointer(positionLocation, 2, GL_FLOAT, false, 2*Float.BYTES, 0);
		glEnableVertexAttribArray(uvLocation);
		glVertexAttribPointer(uvLocation, 2, GL_FLOAT, false, 2*Float.BYTES, 12*Float.BYTES);
	}
	
	public static int getQuadVAO() {
		return Renderer.quadVAO;
	}
	
	public static ShaderProgram getBasicShader() {
		return basicShaderProgram;
	}
	
	public static ShaderProgram getTextShader() {
		return textShaderProgram;
	}
	
	public static ShaderProgram getScaleShader() {
		return scaleShaderProgram;
	}
	
}
