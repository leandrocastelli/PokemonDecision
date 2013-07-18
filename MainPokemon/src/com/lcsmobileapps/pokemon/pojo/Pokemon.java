package com.lcsmobileapps.pokemon.pojo;

import android.graphics.Color;

public abstract class Pokemon {
	protected String name;
	protected int imageID;
	protected int soundID;
	protected int color;
	protected String keyword;
	
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
	public String getKeyword() { 
		return keyword;
	}
	public static final String ARGS_POSITION = "position";
	
}
