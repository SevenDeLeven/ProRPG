package com.prpg.sdl.gui;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Screen {
	
	protected final ArrayList<GUIElement> elems = new ArrayList<GUIElement>();
	
	protected final HashMap<Class<?>, ArrayList<GUIElement>> elemsByClass = new HashMap<Class<?>, ArrayList<GUIElement>>();
	
	public Screen() {
		elemsByClass.put(GUIButton.class, new ArrayList<GUIElement>());
		elemsByClass.put(GUIText.class, new ArrayList<GUIElement>());
	}
	
	public final void addElement(GUIElement e) {
		this.elems.add(e);
		this.elemsByClass.get(e.getClass()).add(e);
	}
	
	public final void removeElement(GUIElement e) {
		elems.remove(e);
		elemsByClass.get(e.getClass()).remove(e);
	}
	
	public abstract void draw();
	public abstract void update();
	
	public abstract void mouseMove(int x, int y);
	public abstract void mousePress(int x, int y);
	public abstract void mouseRelease(int x, int y);
	public abstract void mouseClick(int x, int y);
	
	public abstract void keyPress(int key, byte mods);
	public abstract void keyRelease(int key);
	
	public abstract void screenSizeChange(int width, int height);
	
}
