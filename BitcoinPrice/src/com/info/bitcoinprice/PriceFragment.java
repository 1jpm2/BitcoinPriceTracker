package com.info.bitcoinprice;

import java.text.NumberFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.info.bitcoinprice.adapter.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.info.bitcoinprice.CurrencyFragment;


public class PriceFragment extends Fragment {
	private static View rootView;
	private static TextView price = null;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_price, container, false);
        new JSONParse().execute(); 
         
        return rootView;
    }
	
	
	 private class JSONParse extends AsyncTask<String, String, JSONObject> {
	    @Override
	      protected void onPreExecute() {
	    	 DisplayMetrics metrics = getResources().getDisplayMetrics();
	   	  int width = metrics.widthPixels;
	   	//  int height = metrics.heightPixels;
	          super.onPreExecute();
	           price = (TextView) rootView.findViewById(R.id.topR);
	           price.setText("Loading...");
	           price.setTextSize			(TypedValue.COMPLEX_UNIT_PX, 1);

	           int size = 1; 

	           do
	           {
	             float textWidth = price.getPaint().measureText("Loading...");

	             if (textWidth < width)
	            	 price.setTextSize(++size);
	             else
	             {
	            	 price.setTextSize(--size);
	               break;
	             }
	           }while(true);
	           
	    }
	    @Override
	      protected JSONObject doInBackground(String... args) {
	      JSONParser jParser = new JSONParser();
	      // Getting JSON from URL
	      JSONObject json = jParser.getJSONFromUrl("https://api.coindesk.com/v1/bpi/currentprice/" + CurrencyFragment.getSelectedCurrency() + ".json");
	      return json;
	    }
	     @Override
	       protected void onPostExecute(JSONObject json) {
	       try {
	    	   // Getting JSON Array
	    	   JSONObject a = json.getJSONObject("bpi");
	    	   String curr = CurrencyFragment.getSelectedCurrency();
	    	   JSONObject c = a.getJSONObject(curr);
	    	   // Storing  JSON item in a Variable
	    	   String strNum =  NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(c.getString("rate").replaceAll(",", "")).intValue());
	    	   if (curr.equals("USD")){
	    		   strNum = "\u0024"+strNum;}
	    	   else if(curr.equals("JPY")){
	    		   strNum = "\u00A5"+strNum;}
	    	   else if(curr.equals("EUR")){
	    		   strNum = "\u20ac"+strNum;}
	    	   else if(curr.equals("CAD")){
	    		   strNum = "\u0024"+strNum;}
	    	   else { //GBP
	    		   strNum = "\u00a3"+strNum;}
	         
	    	   price.setText(strNum);

	      } catch (JSONException e) {
	    	  e.printStackTrace();
	      }
	     }
	  }
}
