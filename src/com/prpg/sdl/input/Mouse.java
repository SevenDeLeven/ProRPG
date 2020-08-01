package com.prpg.sdl.input;

import static org.lwjgl.glfw.GLFW.*;
import org.joml.*;

public class Mouse {
	
	private static long window;
	
	private static int windowWidth, windowHeight;
	
	public static int mouseX = 0, mouseY = 0;
	
	public static void init(long window) {
		Mouse.window = window;
	}
	
	public static void update(int width, int height) {
		windowWidth = width;
		windowHeight = height;
	}
	
	public static Vector2i getMousePos() {
		return new Vector2i(mouseX - (windowWidth/2), mouseY - (windowHeight/2));
	}
	
}
