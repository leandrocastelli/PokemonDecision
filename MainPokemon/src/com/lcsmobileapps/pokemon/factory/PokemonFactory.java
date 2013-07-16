package com.lcsmobileapps.pokemon.factory;

import com.lcsmobileapps.pokemon.pojo.Bulbasaur;
import com.lcsmobileapps.pokemon.pojo.Charmander;
import com.lcsmobileapps.pokemon.pojo.Pikachu;
import com.lcsmobileapps.pokemon.pojo.Pokemon;
import com.lcsmobileapps.pokemon.pojo.Squirtle;

public class PokemonFactory {

	public static Pokemon  getPokemon(int position) {
		Pokemon pokemon = null;
		switch(position) {
			case 0: {
				pokemon = new Pikachu();
				
			}
			break;
			case 1: {
				pokemon = new Squirtle();
				
			}
			break;
			case 2: {
				pokemon = new Charmander();
				
			}
			break;
			
			
			case 3: {
				pokemon = new Bulbasaur();
			}
			break;
		}
		
		return pokemon;
	}
}
