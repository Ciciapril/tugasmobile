package com.slamet.barlingmascakebtourism_v1;


import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
Button peta;
Button carinama;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		peta=(Button) findViewById(R.id.peta);
		carinama=(Button)findViewById(R.id.Acari_nama);
		peta.setOnClickListener(this);
		carinama.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.about) {
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
		}if (id == R.id.keluar) {
			this.finish();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.cari_nama){
			Intent intent = new Intent(this, CarinamaActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.peta){
			Intent intent = new Intent(this, PetaActivity.class);
			String pesan ="Cari peta";
			intent.putExtra("judul", pesan);
			startActivity(intent);
		}
		
	}
}
