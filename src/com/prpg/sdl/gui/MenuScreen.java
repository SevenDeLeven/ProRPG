package com.prpg.sdl.gui;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;

import com.prpg.sdl.render.Renderer;

public class MenuScreen extends Screen {
	
	private int u_atlasSize = 3;
	private int u_projection = 4;
	
	public MenuScreen() {
		addElement(new GUIText(-400,0,10,"The quick, brown\nfox 'jumps' over\nthe lazy dog\nyou're"));
	}
	
	@Override
	public void draw() {
		
		for (GUIElement button : elemsByClass.get(GUIButton.class)) {
			
		}
		
		int textShader = Renderer.getTextShader();
		glUseProgram(textShader);
		Renderer.uniformMatrix(u_projection, Renderer.loadProjectionMatrix());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, TextAtlas.getTexture());
		glUniform2f(u_atlasSize, 215, 16);
		for (GUIElement text : elemsByClass.get(GUIText.class)) {
			text.draw();
		}
		
		
	}

	@Override
	public void update() {
	}

	@Override
	public void mouseMove(int x, int y) {
	}

	@Override
	public void mousePress(int x, int y) {
	}

	@Override
	public void mouseRelease(int x, int y) {
	}

	@Override
	public void mouseClick(int x, int y) {
	}

	@Override
	public void keyPress(int key, byte mods) {
	}

	@Override
	public void keyRelease(int key) {
	}

	@Override
	public void screenSizeChange(int width, int height) {
	}
	
	
	
}
