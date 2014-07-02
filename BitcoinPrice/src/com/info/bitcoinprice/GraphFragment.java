package com.info.bitcoinprice;

import java.text.FieldPosition;
import java.text.ParsePosition;

import android.os.AsyncTask;
import android.os.Bundle;

import com.info.bitcoinprice.adapter.*;
import com.androidplot.xy.*;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class GraphFragment extends Fragment {
	XYPlot plot;
	View rootView;
	ArrayList<String> timeStamps = new ArrayList<String>();
	ArrayList<Number> priceHistory = new ArrayList<Number>();
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		
		new PriceNumbers().execute();
        rootView = inflater.inflate(R.layout.fragment_graph, container, false);
     
		return rootView;
    }
	    
         
    
	@SuppressWarnings("serial")
	public void drawGraph(){
        plot = (XYPlot) rootView.findViewById(R.id.mySimpleXYPlot);
   
        XYSeries series1 = new SimpleXYSeries(
       		 priceHistory,          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "");                             // Set the display title of the series
        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:

    	LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getActivity().getApplicationContext(),
               R.xml.line_point_formatter_with_plf1);

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
              plot.getGraphWidget().setDomainLabelOrientation(-45);
              plot.setRangeLabel("USD");
              plot.setDomainLabel("");
              plot.setDomainValueFormat(new NumberFormat() {
                  // 
                  @Override
                  public StringBuffer format(Object object, StringBuffer stringBuffer, FieldPosition fieldPosition) {
                	  int parsedInt = Math.round(Float.parseFloat(object.toString()));
                	  String labelString = timeStamps.get(parsedInt).replaceAll("^\\d{4}-", "");
                	  return stringBuffer.append(labelString.toString()); // shortcut to convert d+1 into a String;
                  }

				@Override
				public StringBuffer format(double arg0, StringBuffer arg1,
						FieldPosition arg2) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public StringBuffer format(long arg0, StringBuffer arg1,
						FieldPosition arg2) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Number parse(String string, ParsePosition position) {
					// TODO Auto-generated method stub
					return null;
				}
      });
              plot.redraw();        
	} 
	
  
  
    private class PriceNumbers extends AsyncTask<String, String, JSONObject> {
	     @Override
	     protected void onPreExecute() {
	          super.onPreExecute();
	     }
	     @Override
	     protected JSONObject doInBackground(String... args) {
	    	 JSONParser jParser = new JSONParser();
	    	 // Getting JSON from URL
	    	 JSONObject json = jParser.getJSONFromUrl("https://api.coindesk.com/v1/bpi/historical/close.json");
	    	 return json;
	     }
	     @Override
	     protected void onPostExecute(JSONObject json) {
	    	 try {
	    		 // Getting JSON Array
	    		 JSONObject a = json.getJSONObject("bpi");
	    		 Iterator<?> keys = a.keys();
	    		 HashMap<String, Number> priceUnorder = new HashMap<String, Number>();
	    		 while( keys.hasNext() ){
	    			 String key = (String)keys.next();
	    			 timeStamps.add(key);
	    			 Number value = Double.valueOf(a.getString(key).replaceAll(",", "")).intValue();
	    			 priceUnorder.put(key, value);
	    		 }
	    		 Collections.sort(timeStamps, new Comparator<String>(){
    		 
	    			 @Override
	    			 public int compare(String o1, String o2) {
	    				 return o1.compareTo(o2);
	    			 } });
    	 
	    		 for(int i = 0; i < timeStamps.size(); i++){
	    			 Number value = Double.valueOf(priceUnorder.get(timeStamps.get(i)).toString().replaceAll(",", "")).intValue();
	    			 priceHistory.add(value);		 
	    		 }
	    		 drawGraph();
    
	    	 } catch (JSONException e) {
	    		 e.printStackTrace();
	    	 } 
	     }
    }   
    
}