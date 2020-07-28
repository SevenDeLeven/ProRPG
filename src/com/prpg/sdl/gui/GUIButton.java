package com.prpg.sdl.gui;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4i;

import com.prpg.sdl.render.Renderer;
import com.prpg.sdl.render.ShaderProgram;

public class GUIButton {

	private static final Vector3f baseColor = new Vector3f(0.5f, 0.5f, 0.5f);
	private static final Vector3f baseOutline = new Vector3f(0,0,0);
	
	private static final Vector4i TL_UVS = new Vector4i(0,21,3,3);
	private static final Vector4i T_UVS = new Vector4i(3,21,18,3);
	private static final Vector4i TR_UVS = new Vector4i(21,21,3,3);
	private static final Vector4i L_UVS = new Vector4i(0,3,3,18);
	private static final Vector4i C_UVS = new Vector4i(3,3,18,18);
	private static final Vector4i R_UVS = new Vector4i(21,3,3,18);
	private static final Vector4i BL_UVS = new Vector4i(0,0,3,3);
	private static final Vector4i B_UVS = new Vector4i(3,0,18,3);
	private static final Vector4i BR_UVS = new Vector4i(21,0,3,3);
	
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
	
	private Matrix4f makeMatrix(float x, float y, float width, float height) {
		Matrix4f ret = new Matrix4f();
		ret.translate(x, y, 0);
		ret.scale(width,height,1);
		return ret;
	}
	
	public void draw() {
		
		float cornerWidth = scale;
		float cornerHeight = scale;
		float udEdgeWidth = width-(scale*2);
		float udEdgeHeight = scale;
		float lrEdgeWidth = scale;
		float lrEdgeHeight = height-(scale*2);
		
		Renderer.bindQuad();
		buttonShader.uniformInt("scale", this.scale);
		//Top-left corner

		buttonShader.uniformVec4i("sector", TL_UVS);
		buttonShader.uniformMatrix("model", makeMatrix(x-((cornerWidth+lrEdgeWidth)/2),y+((cornerHeight+lrEdgeHeight)/2),cornerWidth,cornerHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformVec4i("sector", T_UVS);
		buttonShader.uniformMatrix("model", makeMatrix(x,y+((cornerHeight+lrEdgeHeight)/2),udEdgeWidth,udEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformVec4i("sector", TR_UVS);
		buttonShader.uniformMatrix("model", makeMatrix(x+((cornerWidth+lrEdgeWidth)/2),y+((cornerHeight+lrEdgeHeight)/2),cornerWidth,cornerHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformVec4i("sector", L_UVS);
		buttonShader.uniformMatrix("model", makeMatrix(x-((cornerWidth+lrEdgeWidth)/2),y,lrEdgeWidth,lrEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformVec4i("sector", C_UVS);
		buttonShader.uniformMatrix("model", makeMatrix(x,y,udEdgeWidth,lrEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformVec4i("sector", R_UVS);
		buttonShader.uniformMatrix("model", makeMatrix(x+((cornerWidth+lrEdgeWidth)/2),y,lrEdgeWidth,lrEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformVec4i("sector", BL_UVS);
		buttonShader.uniformMatrix("model", makeMatrix(x-((cornerWidth+lrEdgeWidth)/2),y+((cornerHeight+lrEdgeHeight)/2),cornerWidth,cornerHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformVec4i("sector", B_UVS);
		buttonShader.uniformMatrix("model", makeMatrix(x,y-((cornerHeight+lrEdgeHeight)/2),udEdgeWidth,udEdgeHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);

		buttonShader.uniformVec4i("sector", BR_UVS);
		buttonShader.uniformMatrix("model", makeMatrix(x+((cornerWidth+lrEdgeWidth)/2),y-((cornerHeight+lrEdgeHeight)/2),cornerWidth,cornerHeight));
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
	}
	
}
