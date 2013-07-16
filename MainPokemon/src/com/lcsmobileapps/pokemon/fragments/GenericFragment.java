package com.lcsmobileapps.pokemon.fragments;


import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.lcsmobileapps.pokemon.PokemonActivity;
import com.lcsmobileapps.pokemon.R;
import com.lcsmobileapps.pokemon.factory.PokemonFactory;
import com.lcsmobileapps.pokemon.pojo.Pokemon;
import com.lcsmobileapps.pokemon.util.ImageHelper;

public class GenericFragment extends Fragment{

	Pokemon pokemon;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View)inflater.inflate(R.layout.fragment_layout, container,false);
		
		pokemon = PokemonFactory.getPokemon(getArguments().getInt(Pokemon.ARGS_POSITION));
		ImageView imgView = (ImageView)view.findViewById(R.id.pokemonImage);
		loadImage(imgView, pokemon.getImageID());
		//imgView.setImageResource(pokemon.getImageID());
		imgView.setScaleType(ScaleType.FIT_XY);
		imgView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PokemonActivity mainActivity = (PokemonActivity)getActivity();
				mainActivity.getPlayer().startPlaying(pokemon.getSound());
			}
		});
		AdRequest adRequest = new AdRequest();
		//adRequest.addTestDevice("89E1AAF3C3FB0B29BA39B0E77040BDEF");
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		AdView adView = (AdView)view.findViewById(R.id.ad);
		adRequest.addKeyword("pokemon");
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		adRequest.setLocation(lastKnownLocation);
		adView.loadAd(adRequest);
		return view;
	};
	public void loadImage(ImageView imgView, int id) {
		final String imageKey = String.valueOf(id);
		
		
		ImageHelper imageHelper = new ImageHelper(imgView, getActivity());
		final Bitmap bitmap = imageHelper.getBitmapFromMemCache(imageKey);
		
		if (bitmap != null) {
			imgView.setImageBitmap(bitmap);
	    }
		else {
			imageHelper.execute(id);
		}
		
	}
	
}
