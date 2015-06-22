package com.slamet.barlingmascakebtourism_v1;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class PetaActivity extends Activity implements OnMapLongClickListener, OnInfoWindowClickListener{
	
	static final LatLng AWAL = new LatLng(-7.430286,109.231849);
	ArrayList<HashMap<String, String>> dataMap = new ArrayList<HashMap<String, String>>();
	private ProgressDialog pDialog;
	JSONParser jParser = new JSONParser();
	Koneksi lo_Koneksi = new Koneksi();
	String isi = lo_Koneksi.isi_koneksi();
	String link_url = isi + "peta.php";
	JSONArray str_json = null;
	
	String id;
	class MyInfoWindowAdapter implements InfoWindowAdapter{
		
		private final View myContentsView;
		
		MyInfoWindowAdapter(){
			myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);	
		}

		@Override
		public View getInfoContents(Marker marker) {
			TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());
   
            return myContentsView;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
		
	}
	
	class getListInfo extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PetaActivity.this);
			pDialog.setMessage("Menghubungkan ke server...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			JSONObject json = jParser.AmbilJson(link_url);

			try {
				str_json = json.getJSONArray("peta");
				
				for(int i = 0; i < str_json.length(); i++)
				{
					JSONObject ar = str_json.getJSONObject(i);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id_wisata", ar.getString("id_wisata"));
					map.put("nama_wisata", ar.getString("nama_wisata"));
					map.put("koordinat_lat",  ar.getString("lat"));
					map.put("koordinat_long",  ar.getString("long"));
					map.put("lokasi",  ar.getString("lokasi"));

					dataMap.add(map);
				}
				 ////////////////////////////
		        LatLng TENDEAN = new LatLng(-6.248000,106.8322);
		        LatLng MONAS = new LatLng(-6.1755, 106.8273); 
		         
		        // inisialisasi kelas
		         
		        //MapDirection md = new MapDirection();         
		        //LatLng source = TENDEAN;           
		         
		            //download document XML
		        //Document doc = md.getDocument(source, MONAS, MapDirection.MODE_DRIVING);
		            // menggunakan mode driving 
		         
		        // method untuk mendapatkan point direction / rute         
		        //ArrayList<LatLng> directionPoint = md.getDirection(doc); 
		         
		        // konfigurasi rute, warna garis, ketebalan, dan sebagainya            
		       
		         
		        // looping pada array koordinat direction point, dan menambahkannya ke PolyLineOptions         
		         
		        //for (int i = 0; i < directionPoint.size(); i++)
		        //{              
		          //  rectLine.add(directionPoint.get(i));           
		        //} 
		         
		        // menampilkan rute di peta yang kita buat         
		       

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
                    	LatLng POSISI = new LatLng(Double.parseDouble(map.get("koordinat_lat")),Double.parseDouble(map.get("koordinat_long")));
                    	id=map.get("id_wisata");
                    	myMap.addMarker(new MarkerOptions()
                        .position(POSISI)
                        .title(map.get("nama_wisata"))
                        .snippet(map.get("lokasi")));

                    }
                   
                   // myMap.addPolyline(rectLine); 
                }
            });
		}

	}
	 PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED); 
	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;
	TextView tvLocInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peta_online);
        
        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment 
        	= (MapFragment)myFragmentManager.findFragmentById(R.id.mapdetail);
        myMap = myMapFragment.getMap();
        
        myMap.setMyLocationEnabled(true);
        
        myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
      
        
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
        
        myMap.getUiSettings().setAllGesturesEnabled(true);
        
        myMap.setTrafficEnabled(true);
        
        myMap.setOnMapLongClickListener(this);
        myMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        myMap.setOnInfoWindowClickListener(this);

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AWAL, 15));
	
        myMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        
       

        new getListInfo().execute();

    }

	@Override
	protected void onResume() {
		super.onResume();
		
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		
		if (resultCode != ConnectionResult.SUCCESS)
		{
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		}
	}

	@Override
	public void onMapLongClick(LatLng point) {
		
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Intent intent = new Intent(this, DetailPeta.class);

		String replace_string_first = marker.getTitle().replace(" ", "_");
		intent.putExtra("nama_wisata", replace_string_first);
		startActivity(intent);
		
	}
    
}
