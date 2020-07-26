package com.prpg.sdl.main;

import com.prpg.sdl.render.Renderer;

public class Main {

	public static void main(String[] args) {
		System.out.println("CWD: "+System.getProperty("user.dir"));
		Renderer.create();
		Renderer.loop();
		Renderer.destroy();
		
	}
	
}
