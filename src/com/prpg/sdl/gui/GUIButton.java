package com.prpg.sdl.gui;

import org.joml.Vector3f;
import org.joml.Vector4i;

import com.prpg.sdl.render.Renderer;
import com.prpg.sdl.render.ShaderProgram;

public class GUIButton {

	private static final Vector3f baseColor = new Vector3f(0.5f, 0.5f, 0.5f);
	private static final Vector3f baseOutline = new Vector3f(0,0,0);
	
	private int x, y, width, height;
	private int scale;
	String text;
	
	private Vector3f color = baseColor;
	private Vector3f outline = baseOutline;
	
	ShaderProgram buttonShader = Renderer.getScaleShader();
	
	public GUIButton(int x, int y, int width, int height, int scale, String title) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = title;
	}
	
	
	
	public void draw() {
		
		
		//Top-left corner
		Vector4i uvs = new Vector4i();
		
	}
	
}
