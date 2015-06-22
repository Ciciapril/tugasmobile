package com.slamet.barlingmascakebtourism_v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.slamet.barlingmascakebtourism_v1.DetailPeta.DownloadImagesTask;
import com.slamet.barlingmascakebtourism_v1.PetaActivity.getListInfo;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CarinamaActivity extends Activity implements OnClickListener{
	
	Button btcari;
	ListView listwisata;
	TextView judul_set;
	Spinner kab;
	private String array_spinner[];
	String id_kab;
	EditText keyword;
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	ArrayList<HashMap<String, String>> Daftrawisatanya;
	// url daftar rumah sakit
	Koneksi lo_Koneksi = new Koneksi();
	String isi = lo_Koneksi.isi_koneksi();
	String url = "";
	String lokasi=isi+"images/";
	private ProgressDialog pDialog;
	
	public static final String TAG_NAMA_WISATA = "nama_wisata";
	public static final String TAG_ID_WISATA = "id_wisata";
	public static final String TAG_LOKASI = "lokasi";
	public static final String TAG_LINK_IMAGE = "nama_gambar";
	public static final String TAG_DESKRIPSI= "deskripsi";
	public final static String KIRIM_KEY="";
	public final static String KIRIM_ID_KAB="";
	String link_url = "";
	JSONArray str_json = null;
	ListView list;
	ListwisataActivity adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carinama);
		
		btcari=(Button)findViewById(R.id.btcarinama);
		list=(ListView)findViewById(R.id.listcariwisata);
		btcari.setOnClickListener(this);
		judul_set=(TextView)findViewById(R.id.textView1);
		keyword=(EditText)findViewById(R.id.edcarinama);
		kab=(Spinner)findViewById(R.id.spinner1);
		array_spinner=new String[6];
		  array_spinner[0]="--pilih kabupaten--";
	        array_spinner[1]="Banyumas";
	        array_spinner[2]="Purbalingga";
	        array_spinner[3]="Banjarnegara";
	        array_spinner[4]="Kebumen";
	        array_spinner[5]="Cilacap";

	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,array_spinner);
	        kab.setAdapter(adapter);
	        
			
		Daftrawisatanya = new ArrayList<HashMap<String, String>>();
			
		// Loading daftar Barang in Background Thread 
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String k=extras.getString("kirimkey");
			String i=extras.getString("kirimid");
			url=isi + "cariwisata.php?keyword="+k+"&id_kab="+i;
			//Toast.makeText(getApplicationContext(), "ok"+k+i, Toast.LENGTH_LONG).show();
		}else{
			
			url=isi + "cariwisata.php";
		}
		
		
		new LoadDaftarWisata().execute();
				// Get listview
				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.carinama, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		String keywordnya=keyword.getText().toString().replace(" ", "_");
		String kabupaten = kab.getSelectedItem().toString();
		
		if(kabupaten.equalsIgnoreCase("Purbalingga")){
			id_kab= "1";
		}else if(kabupaten.equalsIgnoreCase("Banyumas")){
			id_kab= "2";
		}else if(kabupaten.equalsIgnoreCase("Banjarnegara")){
			id_kab= "3";
		}else if(kabupaten.equalsIgnoreCase("Kebumen")){
			id_kab= "4";
		}else if(kabupaten.equalsIgnoreCase("Cilacap")){
			id_kab= "5";
		}else{
			id_kab="";
		}
		//url=isi + "cariwisata.php?keyword="+keywordnya+"&id_kab="+id_kab;
		
		//new LoadDaftarWisata().execute();
		//Toast.makeText(getApplicationContext(), "ok"+keywordnya+id_kab, Toast.LENGTH_LONG).show();
		Intent i = new Intent(CarinamaActivity.this, CarinamaActivity.class);
		i.putExtra("kirimkey", keywordnya);
		i.putExtra("kirimid", id_kab);
		startActivity(i);
		this.finish();
	}
	
	


public void SetListViewAdapter(ArrayList<HashMap<String, String>> daftar) {
	adapter = new ListwisataActivity(this, daftar);
	list.setAdapter(adapter);
	list.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			String nama_wisata = ((TextView) view.findViewById(R.id.nama_wisata)).getText().toString();
			//Toast.makeText(getApplicationContext(), "ok"+id_wisata, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(), DetailPeta.class);

			String replace_string_first = nama_wisata.replace(" ", "_");;
			intent.putExtra("nama_wisata", replace_string_first);
			startActivity(intent);
		}
	});
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	// Jika result code 100
	if (resultCode == 100) {
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
}
class LoadDaftarWisata extends AsyncTask<String, String, String> {
	/**
	* Sebelum Memulai Proses Secara Background, Tampilkan Progress Dialog
	* */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(CarinamaActivity.this);
		pDialog.setMessage("Menghubungkan ke server...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		
	}
	
	/**
	 * Ambil Semua Data Dari URL
	* */
	protected String doInBackground(String... args) {
		

		JSONObject json = jParser.AmbilJson(url);

		try {
			str_json = json.getJSONArray("peta");
			
			for(int i = 0; i < str_json.length(); i++)
			{
				JSONObject c = str_json.getJSONObject(i);
				// looping through All daftar_rs
				
					// Storing each json item in variable
					String id_wisata = c.getString(TAG_ID_WISATA);
					String nama_wisata = c.getString(TAG_NAMA_WISATA);
					String link_image = lokasi + c.getString(TAG_LINK_IMAGE);
					String lokasi = c.getString(TAG_LOKASI);
					String deskripsi = c.getString(TAG_DESKRIPSI);
					
					// Membuat HashMap Baru
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(TAG_ID_WISATA, id_wisata);
					map.put(TAG_NAMA_WISATA, nama_wisata);
					map.put(TAG_LINK_IMAGE, link_image);
					map.put(TAG_LOKASI, lokasi);
					map.put(TAG_DESKRIPSI, deskripsi);
					// Menambahkan HashList ke ArrayList
					Daftrawisatanya.add(map);
				}

		// Tidak Ada Record Data (SUCCESS = 0)
		// Akan menutup aplikasi
		//finish();
		
	} catch (JSONException e) {
		Toast.makeText(getApplicationContext(), "Maaf tidak ada koneksi", Toast.LENGTH_LONG).show();
		}
		return null;
	}
		protected void onPostExecute(String file_url) {
			
			// update UI dari Background
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					// update listview
					//for (int i = 0; i < Daftrawisatanya.size(); i++)
                    //{
                    	//HashMap<String, String> map = new HashMap<String, String>();
                    	//map = Daftrawisatanya.get(i);
                    	//judul_set.setText("Detail " + map.get("nama_wisata"));
                    	
                   // }
					
					SetListViewAdapter(Daftrawisatanya);
				}
				
			});
		}

}
}

