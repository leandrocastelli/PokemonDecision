package com.lcsmobileapps.pokemon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.Window;

import com.lcsmobileapps.pokemon.fragments.GenericFragment;
import com.lcsmobileapps.pokemon.pojo.Pokemon;
import com.lcsmobileapps.pokemon.service.MediaService;
import com.lcsmobileapps.pokemon.service.MediaService.LocalBinder;
import com.lcsmobileapps.pokemon.service.SoundPlayer;
import com.lcsmobileapps.pokemon.util.AppRater;
import com.lcsmobileapps.pokemon.util.FileManager;
import com.lcsmobileapps.pokemon.util.FileManager.Props;
import com.lcsmobileapps.pokemon.util.ImageHelper;

public class PokemonActivity extends FragmentActivity implements ServiceConnection{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private SoundPlayer soundPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bindService(new Intent(this,MediaService.class), this, Context.BIND_AUTO_CREATE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);
		
		int cacheSize;
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			cacheSize = maxMemory / 8;
		}
		else {
			cacheSize = maxMemory / 6;
		}
		
		ImageHelper.mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
		setContentView(R.layout.activity_pokemon);

		AppRater.app_launched(this);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int currentItem = mViewPager.getCurrentItem();
				PagerTitleStrip pageTitle = (PagerTitleStrip)findViewById(R.id.pager_title_strip);
				switch(currentItem){
				case 0: {
					pageTitle.setBackgroundColor(Color.YELLOW);
					pageTitle.setTextColor(Color.BLACK);
					

				}
				break;
				case 1: {
					pageTitle.setBackgroundColor(Color.BLUE);
					pageTitle.setTextColor(Color.WHITE);

				}
				break;
				case 2: {
					pageTitle.setBackgroundColor(Color.RED);
					pageTitle.setTextColor(Color.WHITE);

				}
				break;


				case 3: {
					pageTitle.setBackgroundColor(Color.GREEN);
					pageTitle.setTextColor(Color.BLACK);
				}
				break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		FileManager.map.put(0,Props.RINGTONE);
		FileManager.map.put(1,Props.NOTIFICATION);
		FileManager.map.put(2,Props.ALARM);
	}

	@Override
	protected void onResume() {
		super.onResume();



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.activity_pokemon, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new GenericFragment();
			Bundle args = new Bundle();
			args.putInt(Pokemon.ARGS_POSITION, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.pikachu).toUpperCase();
			case 1:
				return getString(R.string.squirtle).toUpperCase();
			case 2:
				return getString(R.string.charmander).toUpperCase();
			case 3:
				return getString(R.string.bulbasaur).toUpperCase();
			}
			return null;
		}

	}


	public void onServiceConnected(ComponentName arg0, IBinder service) {
		LocalBinder localBinder = (LocalBinder) service;
		soundPlayer = localBinder.getSoundPlayer();

	}
	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		soundPlayer = null;

	}
	public SoundPlayer getPlayer() {
		return soundPlayer;
	}

}
