package com.prpg.sdl.gui;

import java.util.HashMap;

import org.joml.Vector4i;

import com.prpg.sdl.resources.ResourceManager;

public class TextAtlas {
	
	public static HashMap<String, Vector4i> charMap = new HashMap<String, Vector4i>();
	
	private static int atlasTexture;
	
	private static void a(Character c, Vector4i coord) {
		charMap.put(c.toString(),coord);
	}
	
	private static void a(String s, Vector4i coord) {
		charMap.put(s,coord);
	}
	
	public static int getTexture() {
		return atlasTexture;
	}
	
	public static Vector4i getCharacter(Character c) {
		return charMap.get(c.toString());
	}
	
	public static Vector4i getCharacter(String s) {
		return charMap.get(s);
	}
	
	public static void init() {
		atlasTexture = ResourceManager.loadTexture("textAtlas.png");
		int x = 0;
		int y = 9;
		a('A', new Vector4i(0,y,6,7));
		a('B', new Vector4i(x+=6,y,6,7));
		a('C', new Vector4i(x+=6,y,6,7));
		a('D', new Vector4i(x+=6,y,6,7));
		a('E', new Vector4i(x+=6,y,6,7));
		a('F', new Vector4i(x+=6,y,6,7));
		a('G', new Vector4i(x+=6,y,6,7));
		a('H', new Vector4i(x+=6,y,6,7));
		a('I', new Vector4i(x+=6,y,6,7));
		a('J', new Vector4i(x+=6,y,6,7));
		a('K', new Vector4i(x+=6,y,6,7));
		a('L', new Vector4i(x+=6,y,6,7));
		a('M', new Vector4i(x+=6,y,6,7));
		a('N', new Vector4i(x+=6,y,6,7));
		a('O', new Vector4i(x+=6,y,6,7));
		a('P', new Vector4i(x+=6,y,6,7));
		a('Q', new Vector4i(x+=6,y,6,7));
		a('R', new Vector4i(x+=6,y,6,7));
		a('S', new Vector4i(x+=6,y,6,7));
		a('T', new Vector4i(x+=6,y,6,7));
		a('U', new Vector4i(x+=6,y,6,7));
		a('V', new Vector4i(x+=6,y,6,7));
		a('W', new Vector4i(x+=6,y,6,7));
		a('X', new Vector4i(x+=6,y,6,7));
		a('Y', new Vector4i(x+=6,y,6,7));
		a('Z', new Vector4i(x+=6,y,6,7));
		a('1', new Vector4i(x+=6,y,6,7));
		a('2', new Vector4i(x+=6,y,6,7));
		a('3', new Vector4i(x+=6,y,6,7));
		a('4', new Vector4i(x+=6,y,6,7));
		a('5', new Vector4i(x+=6,y,6,7));
		a('6', new Vector4i(x+=6,y,6,7));
		a('7', new Vector4i(x+=6,y,6,7));
		a('8', new Vector4i(x+=6,y,6,7));
		a('9', new Vector4i(x+=6,y,6,7));
		a('0', new Vector4i(x+=6,y,6,7));
		
		x = 0;
		y = 1;
		a('!', new Vector4i(x,y,6,7));
		a('@', new Vector4i(x+=6,y,6,7));
		a('#', new Vector4i(x+=6,y,6,7));
		a('$', new Vector4i(x+=6,y,6,7));
		a('%', new Vector4i(x+=6,y,6,7));
		a('^', new Vector4i(x+=6,y,6,7));
		a('&', new Vector4i(x+=6,y,6,7));
		a('*', new Vector4i(x+=6,y,6,7));
		a('(', new Vector4i(x+=6,y,6,7));
		a(')', new Vector4i(x+=6,y,6,7));
		a('-', new Vector4i(x+=6,y,6,7));
		a('_', new Vector4i(x+=6,y,6,7));
		a('+', new Vector4i(x+=6,y,6,7));
		a('=', new Vector4i(x+=6,y,6,7));
		a('[', new Vector4i(x+=6,y,6,7));
		a(']', new Vector4i(x+=6,y,6,7));
		a('{', new Vector4i(x+=6,y,6,7));
		a('}', new Vector4i(x+=6,y,6,7));
		a(';', new Vector4i(x+=6,y,6,7));
		a(':', new Vector4i(x+=6,y,6,7));
		a('\'', new Vector4i(x+=6,y,6,7));
		a('"', new Vector4i(x+=6,y,6,7));
		a(',', new Vector4i(x+=6,y,6,7));
		a('.', new Vector4i(x+=6,y,6,7));
		a('<', new Vector4i(x+=6,y,6,7));
		a('>', new Vector4i(x+=6,y,6,7));
		a('/', new Vector4i(x+=6,y,6,7));
		a('?', new Vector4i(x+=6,y,6,7));
		a('\\', new Vector4i(x+=6,y,6,7));
		a('|', new Vector4i(x+=6,y,6,7));
		a('`', new Vector4i(x+=6,y,6,7));
		a('~', new Vector4i(x+=6,y,6,7));

		a("leftArrow", new Vector4i(x+=6,y,6,7));
		a("rightArrow", new Vector4i(x+=6,y,6,7));
		a("upArrow", new Vector4i(x+=6,y,6,7));
		a("downArrow", new Vector4i(x+=6,y,6,7));
	}
	
}
