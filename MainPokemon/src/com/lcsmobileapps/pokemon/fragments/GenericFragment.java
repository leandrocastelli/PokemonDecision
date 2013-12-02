package com.lcsmobileapps.pokemon.fragments;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.lcsmobileapps.pokemon.PokemonActivity;
import com.lcsmobileapps.pokemon.R;
import com.lcsmobileapps.pokemon.factory.PokemonFactory;
import com.lcsmobileapps.pokemon.pojo.Pokemon;
import com.lcsmobileapps.pokemon.util.FileManager;
import com.lcsmobileapps.pokemon.util.FileManager.Props;
import com.lcsmobileapps.pokemon.util.ImageHelper;

public class GenericFragment extends Fragment{

	Pokemon pokemon;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View)inflater.inflate(R.layout.fragment_layout, container,false);
		setHasOptionsMenu(true);
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
		//adRequest.addTestDevice("5A873CD5069A96C1FCBBEB66EB7CBC5A");
		
		//adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		AdView adView = (AdView)view.findViewById(R.id.ad);
		adRequest.addKeyword(pokemon.getKeyword());
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		adRequest.setLocation(lastKnownLocation);
		adView.loadAd(adRequest);
		return view;
	};
	private void loadImage(ImageView imgView, int id) {
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
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_pokemon, menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		final InputStream in = getResources().openRawResource(pokemon.getSound());
		switch(item.getItemId()){
		case R.id.menu_setas: {


			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getString(R.string.dialog_set_title));

			builder.setNegativeButton(getString(R.string.cancel), new AlertDialog.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

				}
			});

			List<String> listString = new ArrayList<String>(3);
			listString.add(getString(R.string.ringtone));
			listString.add(getString(R.string.notification));
			listString.add(getString(R.string.alarm));
			
			
			
			final ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, android.R.id.text1, listString);
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					
						String path;
						boolean result = false;
												
						Props selection = FileManager.map.get(arg1);
						path = FileManager.getInstance().copyFile(adapter.getContext(), selection, in,pokemon);
						if((path!=null && path.length()>0)) { //API Lvl 8 doesnt have isEmpty
							result = FileManager.getInstance().setAs(path,selection, adapter.getContext());
						}
						
						if(result) {
							Toast.makeText(adapter.getContext(), adapter.getItem(arg1)+getString(R.string.set_success),Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(adapter.getContext(), adapter.getItem(arg1)+getString(R.string.set_fail),Toast.LENGTH_SHORT).show();
						}
					
					arg0.dismiss();

				}
			});
			builder.show();
		}
		}
		return true;
	}
}
