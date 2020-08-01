package com.prpg.sdl.gui;

public abstract class GUIElement {
	public abstract void draw(MenuScreen screen);
	public abstract int getWidth();
	public abstract int getHeight();
	public void update() {}
	public void mouseEnter() {}
	public void mouseLeave() {}
	public void click() {}
}
