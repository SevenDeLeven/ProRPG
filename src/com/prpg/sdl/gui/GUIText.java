package com.prpg.sdl.gui;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector4i;

import com.prpg.sdl.render.Renderer;
import com.prpg.sdl.render.ShaderProgram;

public class GUIText extends GUIElement {
	
	private ShaderProgram textShader;
	
	private int x, y, scale;
	private String text;
	
	public GUIText(int x, int y, int scale, String text) {
		this.textShader = Renderer.getTextShader();
		this.x = x;
		this.y = y;
		this.scale = scale;
		this.text = text.toUpperCase();
	}
	
	public void draw() {
		int x = this.x-(6*scale);
		int y = this.y;
		textShader.uniformInt("atlas", 0);
		
		boolean escape = false;
		
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			boolean drawCharacter = true;
			
			if (!escape) {
				if (c == ' ') {
					x += 6*scale;
					drawCharacter = false;
				} else if (c == '\n') {
					x = this.x;
					y -= 8*scale;
					drawCharacter = false;
				} else if (c == '\\') {
					escape = true;
					drawCharacter = false;
				}
			}
			
			if (drawCharacter) {
				Vector4i xywh = TextAtlas.getCharacter(c);
				if (xywh == null) continue;
				textShader.uniformVec4i("xywh", xywh);
				Matrix4f model = new Matrix4f();
				model.translate(x+=(6*scale), y, 0);
				model.scale(6*scale,7*scale,1);
				textShader.uniformMatrix("model", model);
				
				Renderer.bindQuad();
				glDrawArrays(GL_TRIANGLES, 0, 6);
			}
			escape = false;
		}
	}

}
