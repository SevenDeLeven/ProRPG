package com.prpg.sdl.gui;

import java.util.HashMap;

import org.joml.Vector4i;

import com.prpg.sdl.resources.ResourceManager;

public class TextAtlas {
	
	public static HashMap<String, Vector4i> charMap = new HashMap<String, Vector4i>();
	
	private static int atlasTexture;
	
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
		int x = -6;
		int y = 9;
		String alphanumericCharset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		for (Character c : alphanumericCharset.toCharArray()) {
			charMap.put(c.toString(),new Vector4i(x+=6, y, 6, 7));
		}

		String specialCharset = "!@#%^&*()-_+=[]{};:'\",.<>/?\\|`~";
		x = 0; //Negative 6 because parameter is post-edit value
		y = 1;
		for (Character c : specialCharset.toCharArray()) {
			charMap.put(c.toString(),new Vector4i(x+=6, y, 6, 7));
		}

		charMap.put("leftArrow", new Vector4i(x+=6,y,6,7));
		charMap.put("rightArrow", new Vector4i(x+=6,y,6,7));
		charMap.put("upArrow", new Vector4i(x+=6,y,6,7));
		charMap.put("downArrow", new Vector4i(x+=6,y,6,7));
	}
	
}
