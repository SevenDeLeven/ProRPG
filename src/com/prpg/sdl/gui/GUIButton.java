package com.prpg.sdl.gui;

import org.joml.Vector3f;

public class GUIButton {

	private static final Vector3f baseColor = new Vector3f(0.5f, 0.5f, 0.5f);
	private static final Vector3f baseOutline = new Vector3f(0,0,0);
	
	private int x, y, width, height;
	String text;
	
	public GUIButton(int x, int y, int width, int height, String title) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = title;
	}
	
	
	
	public void draw() {
		
	}
	
}
