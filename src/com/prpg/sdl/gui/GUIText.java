package com.prpg.sdl.gui;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform4fv;

import org.joml.Matrix4f;
import org.joml.Vector4i;

import com.prpg.sdl.render.Renderer;

public class GUIText extends GUIElement {
	
	private int u_xywh = 0;
	private int u_model = 1;
	private int u_atlas = 2;
	
	private int x, y, scale;
	private String text;
	
	public GUIText(int x, int y, int scale, String text) {
		this.x = x;
		this.y = y;
		this.scale = scale;
		this.text = text.toUpperCase();
	}
	
	public void draw() {
		int x = this.x;
		int y = this.y;
		
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			boolean drawCharacter = true;
			
			if (c == ' ') {
				x += 6*scale;
				drawCharacter = false;
			} else if (c == '\n') {
				x = this.x;
				y -= 8*scale;
				drawCharacter = false;
			}
			
			if (drawCharacter) {
				Vector4i xywh = TextAtlas.getCharacter(c);
				if (xywh == null) continue;
				glUniform4fv(u_xywh, new float[]{xywh.x, xywh.y, xywh.z, xywh.w});
				
				Matrix4f model = new Matrix4f();
				model.translate(x+=(6*scale), y, 0);
				model.scale(6*scale,7*scale,1);
				Renderer.uniformMatrix(u_model, model);
				
				glUniform1i(u_atlas, 0);
				
				Renderer.bindQuad();
				glDrawArrays(GL_TRIANGLES, 0, 6);
			}
		}
	}

}
