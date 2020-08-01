package com.prpg.sdl.render;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.prpg.sdl.gui.MenuScreen;
import com.prpg.sdl.gui.Screen;
import com.prpg.sdl.gui.TextAtlas;
import com.prpg.sdl.input.Keyboard;
import com.prpg.sdl.input.Mouse;
import com.prpg.sdl.resources.ResourceManager;

public class Renderer {
	
	static long glfwWindow;
	
	static int quadVAO;
	static int quadVBO;
	
	static float windowWidth;
	static float windowHeight;
	
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
		
		//Window and context setup
		
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
		windowWidth = 640;
		windowHeight = 480;
		glfwWindow = glfwCreateWindow(640, 480, "name", NULL, NULL);
		glfwMakeContextCurrent(glfwWindow);
		Mouse.init(glfwWindow);
		
		glfwSetCursorPosCallback(glfwWindow, new GLFWCursorPosCallback()  {
			@Override
			public void invoke(long window, double x, double y) {
				Mouse.mouseX = (int)x;
				Mouse.mouseY = (int)y;
			}
		});
		
		glfwSetWindowSizeCallback(glfwWindow, new GLFWWindowSizeCallback() {
			public void invoke(long window, int width, int height) {
				glViewport(0,0,width,height);
				Mouse.update(width, height);
				Renderer.windowWidth = width;
				Renderer.windowHeight = height;
			}
		});
		
		glfwSetKeyCallback(glfwWindow, new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW_PRESS)
					Keyboard.keyPress(key, mods);
			}
		});
		
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
		
		Mouse.update((int)windowWidth, (int)windowHeight);
		
		GL.createCapabilities();

		glViewport(0,0,(int)windowWidth,(int)windowHeight);
		
		//Text Loading
		
		TextAtlas.init();
		
		//Load Shaders

		basicShaderProgram = ShaderProgram.createShaderProgram("vertShader.vert", "fragShader.frag");
		textShaderProgram = ShaderProgram.createShaderProgram("textShader.vert", "textShader.frag");
		scaleShaderProgram = ShaderProgram.createShaderProgram("scaleShader.vert", "scaleShader.frag");
		
		//Creation of the basic quad VAO
		
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
		glEnable(GL_DEPTH_TEST);
		
		//Load textures
		ResourceManager.loadTexture("textAtlas.png");
		ResourceManager.loadTexture("buttonTL.png", GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE);
		ResourceManager.loadTexture("buttonT.png", GL_REPEAT, GL_CLAMP_TO_EDGE);
		ResourceManager.loadTexture("buttonTR.png", GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE);
		ResourceManager.loadTexture("buttonL.png", GL_CLAMP_TO_EDGE, GL_REPEAT);
		ResourceManager.loadTexture("buttonC.png", GL_REPEAT, GL_REPEAT);
		ResourceManager.loadTexture("buttonR.png", GL_CLAMP_TO_EDGE, GL_REPEAT);
		ResourceManager.loadTexture("buttonBL.png", GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE);
		ResourceManager.loadTexture("buttonB.png", GL_REPEAT, GL_CLAMP_TO_EDGE);
		ResourceManager.loadTexture("buttonBR.png", GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE);
		
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
		projection.scale(2f/((float)windowWidth),2f/((float)windowHeight),1);
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
	
	public static void bindTexture(int location, int texture) {
		glActiveTexture(location);
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public static int getQuadVAO() {
		return Renderer.quadVAO;
	}
	
	public static int getWindowWidth() {
		return (int) windowWidth;
	}
	
	public static int getWindowHeight() {
		return (int) windowHeight;
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
