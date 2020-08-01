package com.prpg.sdl.gui;

import org.joml.Vector2i;

import com.prpg.sdl.render.Renderer;

public class GUIAlignment {
	
	public static enum ERelativeAlignment {
		TOPLEFT,
		TOPRIGHT,
		BOTTOMLEFT,
		BOTTOMRIGHT,
		TOPCENTER,
		LEFTCENTER,
		RIGHTCENTER,
		BOTTOMCENTER,
		CENTER
	}
	
	public static Vector2i getAlignedPosition(ERelativeAlignment screenAlignment, ERelativeAlignment anchorPoint, Vector2i elementSize) {
		int halfWWidth = Renderer.getWindowWidth()/2;
		int halfWHeight = Renderer.getWindowHeight()/2;
		int halfEWidth = elementSize.x/2;
		int halfEHeight = elementSize.y/2;
		
		int xOffset = 0;
		int yOffset = 0;
		
		int relativeXMult = 0;
		int relativeYMult = 0;
		
		//Begin by setting x and y to a center aligned position based on width and height
		switch (screenAlignment) {
		case TOPLEFT:
			xOffset -= halfWWidth;
			yOffset += halfWHeight;
			relativeXMult = 1;
			relativeYMult = -1;
			break;
		case TOPRIGHT:
			xOffset -= halfWWidth;
			yOffset -= halfWHeight;
			relativeXMult = -1;
			relativeYMult = -1;
			break;
		case BOTTOMLEFT:
			xOffset += halfWWidth;
			yOffset += halfWHeight;
			relativeXMult = 1;
			relativeYMult = 1;
			break;
		case BOTTOMRIGHT:
			xOffset += halfWWidth;
			yOffset -= halfWHeight;
			relativeXMult = -1;
			relativeYMult = 1;
			break;
		case TOPCENTER:
			yOffset += halfWHeight;
			relativeYMult = -1;
			break;
		case LEFTCENTER:
			xOffset -= halfWWidth;
			relativeXMult = 1;
			break;
		case RIGHTCENTER:
			xOffset += halfWWidth;
			relativeXMult = -1;
			break;
		case BOTTOMCENTER:
			yOffset -= halfWHeight;
			relativeYMult = 1;
			break;
		case CENTER:
			break;
		}
		
		//Then reconfigure the position based on the alignment, leaving out CENTER because it is already on the center alignment
		switch (anchorPoint) {
		case TOPLEFT:
			xOffset += (1-relativeXMult)*halfEWidth;
			yOffset += (-1-relativeYMult)*halfEHeight;
			break;
		case TOPRIGHT:
			xOffset += (-1-relativeXMult)*halfEWidth;
			yOffset += (-1-relativeYMult)*halfEHeight;
			break;
		case BOTTOMLEFT:
			xOffset += (1-relativeXMult)*halfEWidth;
			yOffset += (1-relativeYMult)*halfEHeight;
			break;
		case BOTTOMRIGHT:
			xOffset += (-1-relativeXMult)*halfEWidth;
			yOffset += (1-relativeYMult)*halfEHeight;
			break;
		case TOPCENTER:
			xOffset += (-relativeXMult)*halfEWidth;
			yOffset += (-1-relativeYMult)*halfEHeight;
			break;
		case LEFTCENTER:
			xOffset += (1-relativeXMult)*halfEWidth;
			yOffset += (-relativeYMult)*halfEHeight;
			break;
		case RIGHTCENTER:
			xOffset += (-1-relativeXMult)*halfEWidth;
			yOffset += (-relativeYMult)*halfEHeight;
			break;
		case BOTTOMCENTER:
			xOffset += (-relativeXMult)*halfEWidth;
			yOffset += (1-relativeYMult)*halfEHeight;
			break;
		case CENTER:
			xOffset += (-relativeXMult)*halfEWidth;
			yOffset += (-relativeYMult)*halfEHeight;
			break;
		}
		return new Vector2i(xOffset, yOffset);
	}
}