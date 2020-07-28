package com.prpg.sdl.gui;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Vector2f;

import com.prpg.sdl.render.Renderer;
import com.prpg.sdl.render.ShaderProgram;
import com.prpg.sdl.resources.ResourceManager;

public class MenuScreen extends Screen {
	
	private ShaderProgram textShader;
	private ShaderProgram scaleShader;
	
	public MenuScreen() {
		textShader = Renderer.getTextShader();
		scaleShader = Renderer.getScaleShader();
		addElement(new GUIText(-400,0,10,"The quick,brown\nfox 'jumps' over\nthe lazy dog\nyou're"));
	}
	
	@Override
	public void draw() {
		
		scaleShader.use();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, ResourceManager.getTexture("button.png"));
		scaleShader.uniformInt("tex", 0);
		for (GUIElement button : elemsByClass.get(GUIButton.class)) {
			button.draw();
		}
		
		textShader.use();
		textShader.uniformMatrix("projection", Renderer.loadProjectionMatrix());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, TextAtlas.getTexture());
		textShader.uniformInt("atlas", TextAtlas.getTexture());
		textShader.uniformVec2("atlasSize", new Vector2f(215, 16));
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
