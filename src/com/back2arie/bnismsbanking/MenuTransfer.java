package com.back2arie.bnismsbanking;

import com.back2arie.bnismsbanking.R;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.view.LayoutInflater;
import java.text.NumberFormat;

public class MenuTransfer extends ListActivity {
	private String[] menu;
	private String kode_master;
	private String[] kode_transfer;
	private AlertDialog konfirmasi;
	private DialogInterface.OnClickListener ok_listener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       
       kode_master = this.getIntent().getExtras().getString("kode");
       kode_transfer = getResources().getStringArray(R.array.kode_transfer);
       menu = getResources().getStringArray(R.array.menu_transfer);

       final DialogInterface.OnClickListener cancel_listener = new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
               }
           };
           
       setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, menu));
       
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				Integer pilihan = ((Long) id).intValue();
				
				if(pilihan == 0)
				{
					LayoutInflater factory = LayoutInflater.from(MenuTransfer.this);
					final View textEntryView = factory.inflate(R.layout.input_transfer, null);
					
					konfirmasi = new AlertDialog.Builder(MenuTransfer.this).create();
					final EditText input_no_rek_tujuan = (EditText) textEntryView .findViewById(R.id.no_rek_tujuan); 
					final EditText input_jumlah_trf = (EditText) textEntryView .findViewById(R.id.jumlah_trf);						       
					final EditText input_no_referensi = (EditText) textEntryView .findViewById(R.id.no_referensi);
					
					ok_listener = new DialogInterface.OnClickListener() {
			              @Override
						public void onClick(DialogInterface dialog, int id) {
			            	  String no_rek_tujuan = input_no_rek_tujuan.getText().toString().trim();
			            	  String jumlah_trf = input_jumlah_trf.getText().toString().trim();
			            	  String no_referensi = input_no_referensi.getText().toString().trim();
			            	  String delimiter = getResources().getString(R.string.delimiter);
		    	              final String message = kode_master + delimiter + kode_transfer[0] + delimiter + no_rek_tujuan + delimiter + jumlah_trf + delimiter + '#' + no_referensi + '#';
			            	  
		        	      		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		        	                @Override
									public void onClick(DialogInterface dialog, int id) {				        	                	
		        	                	// Kirim SMS
				      		   			Intent sms = new Intent(view.getContext(), KirimSMS.class);
				      		   			sms.putExtra("message", message);
				      		   			startActivity(sms);
		        	                }
		        	            };
		    	              
		        	            // Format jumlah transfer ke format currency
		        	            NumberFormat nf = NumberFormat.getInstance();
		        	            String jumlah_trf_hum = nf.format(Integer.parseInt(jumlah_trf));
		        	            
		        	      		AlertDialog alert = new AlertDialog.Builder(MenuTransfer.this).create();
		        	    		alert.setTitle(getResources().getString(R.string.konfirmasi));
		        	    		String isi_konfirmasi = getResources().getString(R.string.konf_no_rek_tujuan) + "\n" + no_rek_tujuan + "\n\n" +   
		        	    								getResources().getString(R.string.konf_jumlah_trf) + "\n" + jumlah_trf_hum + "\n\n" +   
		        	    								getResources().getString(R.string.konf_no_referensi) + "\n" + no_referensi;
		        	    		
		        	    		alert.setMessage(isi_konfirmasi);
		        	    		alert.setCancelable(false);
		        	    		alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.tombol_lanjut), listener);
		        	    		alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.tombol_batal), cancel_listener);
		        	    		alert.show();
			              }
					};
					
					konfirmasi.setView(textEntryView);
					konfirmasi.setTitle(getResources().getString(R.string.title_transfer));
					konfirmasi.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.tombol_lanjut), ok_listener);
					konfirmasi.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.tombol_batal), cancel_listener);
					konfirmasi.show();
				}
				else if(pilihan == 1)
				{
					Intent antar_bank = new Intent(view.getContext(), TransferAntarBank.class);
					antar_bank.putExtra("kode_master", kode_master);
					startActivity(antar_bank);
				}
			}
		});
		
   }
}
