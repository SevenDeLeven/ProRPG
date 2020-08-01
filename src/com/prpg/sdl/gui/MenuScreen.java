package com.prpg.sdl.gui;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.GL_TEXTURE6;
import static org.lwjgl.opengl.GL13.GL_TEXTURE7;
import static org.lwjgl.opengl.GL13.GL_TEXTURE8;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.prpg.sdl.render.Renderer;
import com.prpg.sdl.render.ShaderProgram;
import com.prpg.sdl.resources.ResourceManager;

public class MenuScreen extends Screen {
	
	private ShaderProgram textShader;
	private ShaderProgram scaleShader;
	
	private GUIText titleText;
	private GUIText subtitleText;
	private GUIText copyrightText;
	private GUIButton beginButton;
	
	
	public MenuScreen() {
		textShader = Renderer.getTextShader();
		scaleShader = Renderer.getScaleShader();
		titleText = new GUIText(0, 200, 7, "ProRPG");
		subtitleText = new GUIText(0, 160, 2, "The programmer's RPG");
		copyrightText = new GUIText();
		beginButton = new GUIButton(0, 0, 200, 40, 3, "Begin");
		addElement(titleText);
		addElement(subtitleText);
		addElement(beginButton);
	}
	
	public ArrayList<GUIText> additionalText;
	
	@Override
	public void draw() {
		
		additionalText = new ArrayList<GUIText>();
		
		scaleShader.use();
		scaleShader.uniformMatrix("projection", Renderer.loadProjectionMatrix());
		Renderer.bindTexture(GL_TEXTURE0, ResourceManager.getTexture("buttonTL.png"));
		Renderer.bindTexture(GL_TEXTURE1, ResourceManager.getTexture("buttonT.png"));
		Renderer.bindTexture(GL_TEXTURE2, ResourceManager.getTexture("buttonTR.png"));
		Renderer.bindTexture(GL_TEXTURE3, ResourceManager.getTexture("buttonL.png"));
		Renderer.bindTexture(GL_TEXTURE4, ResourceManager.getTexture("buttonC.png"));
		Renderer.bindTexture(GL_TEXTURE5, ResourceManager.getTexture("buttonR.png"));
		Renderer.bindTexture(GL_TEXTURE6, ResourceManager.getTexture("buttonBL.png"));
		Renderer.bindTexture(GL_TEXTURE7, ResourceManager.getTexture("buttonB.png"));
		Renderer.bindTexture(GL_TEXTURE8, ResourceManager.getTexture("buttonBR.png"));
		scaleShader.uniformVec2("atlasSize", new Vector2f(24,24));
		for (GUIElement button : elemsByClass.get(GUIButton.class)) {
			button.draw(this);
		}
		
		textShader.use();
		textShader.uniformMatrix("projection", Renderer.loadProjectionMatrix());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, TextAtlas.getTexture());
		textShader.uniformInt("atlas", TextAtlas.getTexture());
		textShader.uniformVec2("atlasSize", new Vector2f(216, 16));
		for (GUIElement text : elemsByClass.get(GUIText.class)) {
			text.draw(this);
		}
		for (GUIText text : additionalText) {
			text.draw(this);
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
		for (GUIElement elem : elemsByClass.get(GUIButton.class)) {
			GUIButton button = (GUIButton) elem;
			if (button.getHovered()) {
				button.action();
			}
		}
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
