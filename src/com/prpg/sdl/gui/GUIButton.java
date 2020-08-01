package com.prpg.sdl.gui;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import com.prpg.sdl.input.Mouse;
import com.prpg.sdl.render.Renderer;
import com.prpg.sdl.render.ShaderProgram;

public class GUIButton extends GUIElement{

	private static final Vector3f baseColor = new Vector3f(0.7f, 0.4f, 0.1f);
	private static final Vector3f baseOutline = new Vector3f(0.2f,0.05f,0.01f);
	private static final Vector3f baseHoverColor = baseOutline;
	private static final Vector3f baseHoverOutline = baseColor;
	
	private int x, y, width, height;
	private int scale;
	private GUIText text;
	
	private static final Vector2f cornerSize = new Vector2f(4,4);
	private static final Vector2f udSize = new Vector2f(24,4);
	private static final Vector2f lrSize = new Vector2f(4,24);
	private static final Vector2f centerSize = new Vector2f(24,24);
	
	private Vector3f color = baseColor;
	private Vector3f outline = baseOutline;
	private Vector3f hoverColor = baseHoverColor;
	private Vector3f hoverOutline = baseHoverOutline;
	
	ShaderProgram buttonShader = Renderer.getScaleShader();
	
	public GUIButton(int x, int y, int width, int height, int scale, String title) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.text = new GUIText(this.x, this.y, this.scale, title);
	}
	
	private Matrix4f makeMatrix(float x, float y, float width, float height) {
		Matrix4f ret = new Matrix4f();
		ret.translate(x, y, 0);
		ret.scale(width,height,1);
		return ret;
	}
	
	public boolean getHovered() {
		Vector2i pos = Mouse.getMousePos();
		int minX = x-(width/2), maxX = x+(width/2), minY = y-(height/2), maxY = y+(height/2);
		return pos.x>=minX && pos.y>=minY && pos.x<=maxX && pos.y<=maxY;
	}
	
	public void draw(MenuScreen screen) {
		boolean hovered = getHovered();
		
		screen.additionalText.add(this.text);
		float cornerWidth = 4*scale;
		float cornerHeight = 4*scale;
		float udEdgeWidth = width-(cornerWidth*2);
		float udEdgeHeight = 4*scale;
		float lrEdgeWidth = 4*scale;
		float lrEdgeHeight = height-(cornerHeight*2);
		
		Renderer.bindQuad();
		buttonShader.uniformInt("scale", this.scale);
		if (!hovered) {
			buttonShader.uniformVec3("color", color);
			buttonShader.uniformVec3("outline", outline);
		} else {
			buttonShader.uniformVec3("color", hoverColor);
			buttonShader.uniformVec3("outline", hoverOutline);
		}
		//Top-left corner
		buttonShader.uniformInt("tex", 0);
		buttonShader.uniformVec2("atlasSize", cornerSize);
		buttonShader.uniformVec2("scaleSize", new Vector2f(cornerWidth/scale,cornerHeight/scale));
		buttonShader.uniformMatrix("model", makeMatrix(x-((cornerWidth+udEdgeWidth)/2),y+((cornerHeight+lrEdgeHeight)/2),cornerWidth,cornerHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformInt("tex", 1);
		buttonShader.uniformVec2("atlasSize", udSize);
		buttonShader.uniformVec2("scaleSize", new Vector2f(udEdgeWidth/scale,udEdgeHeight/scale));
		buttonShader.uniformMatrix("model", makeMatrix(x,y+((cornerHeight+lrEdgeHeight)/2),udEdgeWidth,udEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformInt("tex", 2);
		buttonShader.uniformVec2("atlasSize", cornerSize);
		buttonShader.uniformVec2("scaleSize", new Vector2f(cornerWidth/scale,cornerHeight/scale));
		buttonShader.uniformMatrix("model", makeMatrix(x+((cornerWidth+udEdgeWidth)/2),y+((cornerHeight+lrEdgeHeight)/2),cornerWidth,cornerHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformInt("tex", 3);
		buttonShader.uniformVec2("atlasSize", lrSize);
		buttonShader.uniformVec2("scaleSize", new Vector2f(lrEdgeWidth/scale,lrEdgeHeight/scale));
		buttonShader.uniformMatrix("model", makeMatrix(x-((cornerWidth+udEdgeWidth)/2),y,lrEdgeWidth,lrEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		buttonShader.uniformInt("tex",4);
		buttonShader.uniformVec2("atlasSize", centerSize);
		buttonShader.uniformVec2("scaleSize", new Vector2f(udEdgeWidth/scale,lrEdgeHeight/scale));
		buttonShader.uniformMatrix("model", makeMatrix(x,y,udEdgeWidth,lrEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformInt("tex", 5);
		buttonShader.uniformVec2("atlasSize", lrSize);
		buttonShader.uniformVec2("scaleSize", new Vector2f(lrEdgeWidth/scale,lrEdgeHeight/scale));
		buttonShader.uniformMatrix("model", makeMatrix(x+((cornerWidth+udEdgeWidth)/2),y,lrEdgeWidth,lrEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformInt("tex", 6);
		buttonShader.uniformVec2("atlasSize", cornerSize);
		buttonShader.uniformVec2("scaleSize", new Vector2f(cornerWidth/scale,cornerHeight/scale));
		buttonShader.uniformMatrix("model", makeMatrix(x-((cornerWidth+udEdgeWidth)/2),y-((cornerHeight+lrEdgeHeight)/2),cornerWidth,cornerHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformInt("tex", 7);
		buttonShader.uniformVec2("atlasSize", udSize);
		buttonShader.uniformVec2("scaleSize", new Vector2f(udEdgeWidth/scale,udEdgeHeight/scale));
		buttonShader.uniformMatrix("model", makeMatrix(x,y-((cornerHeight+lrEdgeHeight)/2),udEdgeWidth,udEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformInt("tex", 8);
		buttonShader.uniformVec2("atlasSize", cornerSize);
		buttonShader.uniformVec2("scaleSize", new Vector2f(cornerWidth/scale,cornerHeight/scale));
		buttonShader.uniformMatrix("model", makeMatrix(x+((cornerWidth+udEdgeWidth)/2),y-((cornerHeight+lrEdgeHeight)/2),cornerWidth,cornerHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
	}
	
}
