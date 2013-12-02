package com.lcsmobileapps.pokemon.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.lcsmobileapps.pokemon.pojo.Pokemon;

public class FileManager {

	public enum Props{
		RINGTONE,
		NOTIFICATION,
		ALARM,
		SEND
	};
	private static final String TAG = "FileManager";
	private static FileManager instance;
	private FileManager(){};
	
	public static Map<Integer,Props> map = new HashMap<Integer, Props>();
	
	public static FileManager getInstance(){
		if(instance==null)
			instance = new FileManager();
		return instance;
	}



	public String copyFile(Context ctx, Props where, InputStream in, Pokemon pokemon) {

		OutputStream out;

		String path = whereTo(ctx,where,pokemon.getName());
		if(!fileExists(ctx,path))
		{

			try{
				//			File file = new File(path);
				//			file.mkdir();
				out = new FileOutputStream(path);
				byte[] buffer = new byte[1024];
				int read;
				while ((read = in.read(buffer)) != -1) {
					out.write(buffer, 0, read);
				}

				in.close();
				in = null;
				out.flush();
				out.close();
			}
			catch (FileNotFoundException ex)
			{
				Log.e(TAG, ex.getMessage());
				return null;
			}
			catch (IOException e) {
				Log.e(TAG, e.getMessage());
				return null;
			}
		}
		return path;
	}

	private String whereTo(Context ctx,Props where,String name) {

		String state = Environment.getExternalStorageState();
		String path = "";
		if(Environment.MEDIA_MOUNTED.equals(state)){
			switch(where) {
			case RINGTONE:
				path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES)+"/"+name+".mp3";
				break;
			case NOTIFICATION:
				path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS)+"/"+name+".mp3";
				break;
			case ALARM:
				path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS)+"/"+name+".mp3";
				break;
			case SEND:
				File externalFilesDir = ctx.getExternalFilesDir(null);
				if (externalFilesDir != null) {
					path = externalFilesDir.getAbsolutePath()+"/"+name+".mp3";
				}
				else {
					path = ctx.getFilesDir().getAbsolutePath()+"/"+where.name()+"/"+name+".mp3";
				}
				
				break;
			}


		}
		else {
			path = ctx.getFilesDir().getAbsolutePath()+"/"+where.name()+"/"+name+".mp3";

			
		}
		File createDir = new File(path.substring(0, path.length()-(name.length()+5)));
		if(!createDir.exists())
			createDir.mkdirs();
		return path;

	}

	private boolean fileExists(Context ctx,String path) {
		File file ;

		file = new File(path);
		return file.exists();
	}

	public boolean setAs (String path,Props prop, Context ctx) {

		
		File file = new File(path);
		
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, file.getName().substring(0, file.getName().lastIndexOf('.')));
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		switch(prop){
		case RINGTONE:
			values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
			break;
		case NOTIFICATION:
			values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
			break;
		case ALARM:
			values.put(MediaStore.Audio.Media.IS_ALARM, true );
			break;
		}

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
		ctx.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + file.getAbsolutePath() + "\"", null);
		Uri newUri = ctx.getContentResolver().insert(uri, values);


		switch(prop){
		case RINGTONE:
			RingtoneManager.setActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_RINGTONE, newUri);
			break;
		case NOTIFICATION:
			RingtoneManager.setActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_NOTIFICATION, newUri);
			break;
		case ALARM:
			RingtoneManager.setActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_ALARM, newUri);
			break;
		}
		return true;

	}
}
