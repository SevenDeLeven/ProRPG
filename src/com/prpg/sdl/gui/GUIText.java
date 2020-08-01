package com.prpg.sdl.gui;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4i;

import com.prpg.sdl.render.Renderer;
import com.prpg.sdl.render.ShaderProgram;

public class GUIText extends GUIElement {
	
	public static enum EAlignment {
		CENTER,
		LEFT,
		RIGHT
	};
	
	private ShaderProgram textShader;
	
	private int x, y, scale;
	private int lines;
	
	private String unformattedText;
	private String[] formattedText;
	private Vector2i[] formattedTextPositions;
	
	private EAlignment alignment = EAlignment.CENTER;
	
	public GUIText(int x, int y, int scale, String text) {
		this.textShader = Renderer.getTextShader();
		this.x = x;
		this.y = y;
		this.scale = scale;
		setText(text, EAlignment.CENTER);
	}
	
	public static int getTextWidth(int scale, String text) {
		return 6*scale*text.length();
	}
	
	public void setAlignment(EAlignment alignment) {
		this.setText(this.unformattedText, alignment);
	}
	
	public void setText(String text, EAlignment alignment) {
		this.unformattedText = text;
		this.alignment = alignment;
		text = text.toUpperCase();
		this.formattedText = text.split("\n");
		lines = formattedText.length;
		this.formattedTextPositions = new Vector2i[lines];
		int decenterOffsetX = 3*scale;
		int decenterOffsetY = 4*scale;
		
		switch (alignment) {
		case LEFT:
			for (int i = 0; i < formattedText.length; i++) {
				formattedTextPositions[i] = new Vector2i(x+decenterOffsetX,y-(i*scale*8)+decenterOffsetY);
			}
			break;
		case RIGHT:
			for (int i = 0; i < formattedText.length; i++) {
				formattedTextPositions[i] = new Vector2i(x-getTextWidth(this.scale, formattedText[i])-decenterOffsetX, y-(i*scale*8)-decenterOffsetY);
			}
			break;
		case CENTER:
			for (int i = 0; i < formattedText.length; i++) {
				formattedTextPositions[i] = new Vector2i(x-(getTextWidth(this.scale, formattedText[i])/2)-decenterOffsetX, y-(i*scale*8));
			}
			break;
		}
	}
	
	public void draw(MenuScreen screen) {
		textShader.uniformInt("atlas", 0);
		boolean escape = false;
		
		for (int t = 0; t < lines; t++) {
			String text = formattedText[t];
			Vector2i position = formattedTextPositions[t];
			int x = position.x;
			int y = position.y;
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				boolean drawCharacter = true;
				
				if (!escape) {
					if (c == ' ') {
						x += 6*scale;
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
	
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
