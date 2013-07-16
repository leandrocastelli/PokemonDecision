package com.lcsmobileapps.pokemon.pojo;

import android.graphics.Color;

public abstract class Pokemon {
	protected String name;
	protected int imageID;
	protected int soundID;
	protected int color;
	
	public String getName() { 
		return name;
	}
	public int getImageID(){
		return imageID;
	}
	public int getSound(){
		return soundID;
	}
	public int getColor(){
		return color;
	}
	public static final String ARGS_POSITION = "position";
	
}
