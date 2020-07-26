package com.prpg.sdl.entity;

public class Updater implements Runnable {
	
	private Thread thread;
	public static boolean running = false;
	
	public void start() {
		running = true;
		thread = new Thread(this,"Updater/01");
		thread.start();
	}
	
	public void run() {
		Updater.create();
		Updater.loop();
		Updater.destroy();
	}
	
	public static void create() {
		
	}
	
	public static void loop() {
		while (running) {
			
		}
	}
	
	public static void destroy() {
		
	}
	
}
