package com.info.bitcoinprice;

import com.info.bitcoinprice.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;
 
public class CurrencyFragment extends Fragment {
    private Spinner currencyspinner;   
    private static String selection = "USD";   
    final CurrencyFragment context = this;  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_currency, container, false);
        currencyspinner = (Spinner) rootView.findViewById(R.id.spinner1);  
        listenerMethod();        
        return rootView;
    }
    
 public static String getSelectedCurrency(){
	return selection; 
 }

    private void listenerMethod() {  
         // TODO Auto-generated method stub  
       currencyspinner.setOnItemSelectedListener(new OnItemSelectedListener()   
       {  
     @Override  
     public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)   
     { 
    	if (!(selection.equals((String) parentView.getItemAtPosition(position)))) {
    		selection = (String) parentView.getItemAtPosition(position);  
    		Toast.makeText(getActivity(), "You seleceted " + selection + ".", Toast.LENGTH_LONG).show();
    	}   
     }  
     @Override  
     public void onNothingSelected(AdapterView<?> parentView)   
           {}});  
    }    
}