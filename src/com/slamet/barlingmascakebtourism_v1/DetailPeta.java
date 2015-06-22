package com.slamet.barlingmascakebtourism_v1;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.slamet.barlingmascakebtourism_v1.PetaActivity.MyInfoWindowAdapter;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailPeta extends Activity implements OnMapLongClickListener, OnInfoWindowClickListener {

	ArrayList<HashMap<String, String>> dataMap = new ArrayList<HashMap<String, String>>();
	private ProgressDialog pDialog;
	JSONParser jParser = new JSONParser();
	String link_url = "";
	JSONArray str_json = null;
	Koneksi lo_Koneksi = new Koneksi();
	String isi = lo_Koneksi.isi_koneksi();
	static final LatLng AWAL = new LatLng(-7.430286,109.231849);
	TextView judul_set,lokasi_set, keterangan_set;
	ImageView gambar_set;

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_peta);

        keterangan_set = (TextView) findViewById(R.id.keterangan);
        lokasi_set = (TextView) findViewById(R.id.lokasi);
        judul_set = (TextView) findViewById(R.id.TextViewdet);
        gambar_set = (ImageView) findViewById(R.id.gambar);

        Bundle b = getIntent().getExtras();
		String nama_wisata = b.getString("nama_wisata");
		//Intent i = getIntent();
		//String nama_wisata = i.getStringExtra(CarinamaActivity.);
    	link_url = isi + "detailwisata.php?nama_wisata="+nama_wisata;
    	
    	///////////map//
    	FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment 
        	= (MapFragment)myFragmentManager.findFragmentById(R.id.map1);
        myMap = myMapFragment.getMap();
        
        myMap.setMyLocationEnabled(true);
         
         myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
       
         
         myMap.getUiSettings().setZoomControlsEnabled(true);
         myMap.getUiSettings().setCompassEnabled(true);
         myMap.getUiSettings().setMyLocationButtonEnabled(true);
         
         myMap.getUiSettings().setAllGesturesEnabled(true);
         
         myMap.setTrafficEnabled(true);
         
         myMap.setOnMapLongClickListener(this);
        
         myMap.setOnInfoWindowClickListener(this);

         myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AWAL, 15));
 	
         myMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    	/////////////
        new getListInfo().execute();
	}
	
	class getListInfo extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DetailPeta.this);
			pDialog.setMessage("Menghubungkan ke server...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			JSONObject json = jParser.AmbilJson(link_url);

			try {
				str_json = json.getJSONArray("detailwisata");
				
				for(int i = 0; i < str_json.length(); i++)
				{
					JSONObject ar = str_json.getJSONObject(i);
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("nama_wisata", ar.getString("nama_wisata"));
					map.put("koordinat_lat",  ar.getString("lat"));
					map.put("koordinat_long",  ar.getString("long"));
					map.put("lokasi",  ar.getString("lokasi"));
					map.put("deskripsi",  ar.getString("deskripsi"));
					map.put("gambar",  ar.getString("nama_gambar"));

					dataMap.add(map);
				}
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Maaf tidak ada koneksi", Toast.LENGTH_LONG).show();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
                public void run() {
                	
                    for (int i = 0; i < dataMap.size(); i++)
                    {
                    	HashMap<String, String> map = new HashMap<String, String>();
                    	map = dataMap.get(i);
                    	judul_set.setText("Detail " + map.get("nama_wisata"));
                    	lokasi_set.setText(map.get("lokasi"));
                    	keterangan_set.setText(map.get("deskripsi"));
                    	LatLng POSISI = new LatLng(Double.parseDouble(map.get("koordinat_lat")),Double.parseDouble(map.get("koordinat_long")));
                    	
                    	myMap.addMarker(new MarkerOptions()
                        .position(POSISI)
                        .title(map.get("nama_wisata"))
                        .snippet(map.get("lokasi")));
                    	new DownloadImagesTask().execute(isi+"images/"+map.get("gambar"));
                    }
                }
            });
		}

	}
	public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... urls) {
		    return download_Image(urls[0]);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			gambar_set.setImageBitmap(result);
		}


		private Bitmap download_Image(String url) {
		    Bitmap bm = null;
		    try {
		        URL aURL = new URL(url);
		        URLConnection conn = aURL.openConnection();
		        conn.connect();
		        InputStream is = conn.getInputStream();
		        BufferedInputStream bis = new BufferedInputStream(is);
		        bm = BitmapFactory.decodeStream(bis);
		        bis.close();
		        is.close();
		    } catch (IOException e) {
		        Log.e("Hub","Error getting the image from server : " + e.getMessage().toString());
		    } 
		    return bm;
		}
	}
	
	 PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED); 
		final int RQS_GooglePlayServices = 1;
		private GoogleMap myMap;
		TextView tvLocInfo;

		@Override
		public void onMapLongClick(LatLng arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onInfoWindowClick(Marker arg0) {
			// TODO Auto-generated method stub
			
		}
}
