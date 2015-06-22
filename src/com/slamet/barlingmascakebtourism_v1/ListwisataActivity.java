package com.slamet.barlingmascakebtourism_v1;

import java.util.ArrayList;
import java.util.HashMap;


import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListwisataActivity extends BaseAdapter {
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	
	public ListwisataActivity(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}
	
	public int getCount() {
		return data.size();
	}
	
	public Object getItem(int position) {
		return position;
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.item_list_wisata, null);
		//Set Masing2 pallete yang ada di item_list_barang
		TextView id_wisata = (TextView)vi.findViewById(R.id.id_wisata); 
		TextView nama_wisata = (TextView)vi.findViewById(R.id.nama_wisata);
		TextView link_image_wisata = (TextView)vi.findViewById(R.id.link_image_wisata);
		TextView lokasi = (TextView)vi.findViewById(R.id.alamat); 
		TextView deskripsi = (TextView) vi.findViewById(R.id.deskripsi);
		ImageView thumb_image = (ImageView)vi.findViewById(R.id.image); 
		
		//Ambil data dari masing-masing TAG yang ada di SemuaBarang Activity
		HashMap<String, String> daftar_rs = new HashMap<String, String>();
		daftar_rs = data.get(position);
		id_wisata.setText(daftar_rs.get(CarinamaActivity.TAG_ID_WISATA));
		nama_wisata.setText(daftar_rs.get(CarinamaActivity.TAG_NAMA_WISATA));
		link_image_wisata.setText(daftar_rs.get(CarinamaActivity.TAG_LINK_IMAGE));
		lokasi.setText(daftar_rs.get(CarinamaActivity.TAG_LOKASI));
		//deskripsi.setText(daftar_rs.get(CarinamaActivity.TAG_DESKRIPSI));
		imageLoader.DisplayImage(daftar_rs.get(CarinamaActivity.TAG_LINK_IMAGE), thumb_image);
		return vi;
	}
}
