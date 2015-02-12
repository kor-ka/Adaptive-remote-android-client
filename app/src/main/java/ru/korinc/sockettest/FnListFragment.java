package ru.korinc.sockettest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

;


public class FnListFragment extends ListFragment {
	
	ArrayAdapter<String> adapter ;
	List<String> fns;
    List<String> fnpl;
    List<Integer> fnpltype;
	ButtonFnManager fnb;

    ListView pluginsLV;
    ArrayAdapter<String> pluginsAdapter ;
    List<String> pluginsList;
    Button footer;
    File folder = new File(Environment.getExternalStorageDirectory()+"/Adaptive remote plugins");
    String btnName = "";
    long btnId = -1;
    SharedPreferences shp;
    boolean itIsPlugin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            btnName = bundle.getString(FnBind.BTN_NAME, "");
            btnId = bundle.getLong(FnBind.BTN_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fn_list_lay, null);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        shp = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
	
	if(!folder.exists())folder.mkdir();

        pluginsLV = (ListView) getActivity().findViewById(R.id.plugins_list);
        pluginsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        pluginsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                return false;
            }
        });
        pluginsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pluginsLV.setItemChecked(i, true);
                if(i == 0) setAdaptiveRemoteContent();
                else{
                    String pluginName = pluginsLV.getItemAtPosition(i).toString();

                    File jsonPluginFile = new File(folder, pluginName);
                    FileInputStream stream = null;
                    try {
                        stream = new FileInputStream(jsonPluginFile);

                    String jsonStr = null;

                        FileChannel fc = stream.getChannel();
                        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                        jsonStr = Charset.defaultCharset().decode(bb).toString();

                        JSONObject pluginJson = new JSONObject(jsonStr);

                        JSONArray jArray = pluginJson.getJSONArray("array");
                           setPLuginContent(jArray);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        pluginsList = new ArrayList<String>() ;
        pluginsList.add("adaptive remote");
        for(File f:folder.listFiles()){
            String pluginName = f.getName();
            pluginsList.add(pluginName);
        }
        pluginsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, pluginsList);

        footer = new Button(getActivity());
        footer.setText("+");
        footer.setTextSize(25);
        footer.setTextColor(Color.WHITE);

        footer.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TestConnection(TestConnection.MODE_LIST, null).execute();
            }
        });
        pluginsLV.addFooterView(footer);
		pluginsLV.setAdapter(pluginsAdapter);

		fnb = new ButtonFnManager();
		fns = new ArrayList<String>() ;
        fnpl = new ArrayList<String>() ;
        fnpltype = new ArrayList<Integer>() ;
        setAdaptiveRemoteContent();
	}
	
	 public void onListItemClick(ListView l, View v, int position, long id) {
         Intent intent = new Intent();
         if(!itIsPlugin){


		        super.onListItemClick(l, v, position, id);

				int fnKey=1;
				int i= 0;
				for (Map.Entry<Integer, String> entry : fnb.fnMap.entrySet()) { 
					
					if (i==position) {
						fnKey=entry.getKey();
					}
					i++;
				}
				intent.putExtra("Name","");
				intent.putExtra("FnResult",fnKey);
				intent.putExtra("FnResultArgs","");

         }else{
             intent.putExtra("Name",fns.get(position));
             intent.putExtra("FnResult",fnpltype.get(position));
             intent.putExtra("FnResultArgs",fnpl.get(position));
         }
         intent.putExtra(FnBind.BTN_ID, btnId);
         getActivity().setResult(getActivity().RESULT_OK, intent);

         getActivity().finish();
	 }

    private void setAdaptiveRemoteContent(){
        itIsPlugin = false;
        fns.clear();
        for (Map.Entry<Integer, String> entry : fnb.fnMap.entrySet()) {
            fns.add(entry.getValue());
        }
        pluginsLV.setItemChecked(0, true);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fns);
        setListAdapter(adapter);
    }

    private void setPLuginContent(JSONArray jArray){
        itIsPlugin = true;
        fns.clear();
        fnpl.clear();
        fnpltype.clear();
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject row = null;
            try {
                row = jArray.getJSONObject(i);

            fns.add(row.getString("name"));
            fnpl.add(row.getString("cmd"));
            fnpltype.add(row.getInt("type"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fns);
        setListAdapter(adapter);
    }




    class TestConnection extends AsyncTask<String, Void, String> {

        public static final int MODE_LIST = 1;
        public static final int MODE_PLUGIN = 2;

        private String resultJSON;
        TestConnection tc= this;
        ProgressDialog progress;
        AlertDialog choosePlugin ;
        int mode;
        String name;


        public TestConnection(int mode, String name){
            super();
            this.mode = mode;
            this.name = name;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());

            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    tc.cancel(true);
                    // finish();
                }
            });

            switch (mode){
                case MODE_LIST:
                    progress.setMessage("Getting plugins list...");
                    break;

                case MODE_PLUGIN:
                    progress.setMessage("Getting plugin...");
                    break;

            }

            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {

            publishProgress(new Void[]{});


            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 7000);
            HttpConnectionParams.setSoTimeout(httpParams, 7000);
            DefaultHttpClient httpclient = new DefaultHttpClient(
                    httpParams);
            HttpPost httppost;
            switch (mode){
                case MODE_LIST:
                    httppost = new HttpPost("https://dl.dropboxusercontent.com/u/30840341/Adaptive%20remote/plugins/plugins.json");
                break;

                case MODE_PLUGIN:
                    httppost = new HttpPost(params[0]);
                break;

                default:
                    httppost = new HttpPost("https://dl.dropboxusercontent.com/u/30840341/Adaptive%20remote/plugins/plugins.json");
                    break;
            }

            // Depends on your web service
            httppost.setHeader("Content-type", "application/json");

            InputStream inputStream = null;

            try {


                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                inputStream = entity.getContent();
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                resultJSON = sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);

            }
            finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (Exception squish) {
                    squish.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);


        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

           if(progress!=null) progress.dismiss();

          switch (mode){
              case MODE_LIST:



                  JSONObject jObject;

                  try {
                      jObject = new JSONObject(resultJSON);

                      final JSONArray jArray = jObject.getJSONArray("array");

                      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                      builder.setTitle("Choose plugin");
                      final ArrayAdapter<String> serversArrayAdapter = new ArrayAdapter<String>(
                              getActivity(),
                              android.R.layout.select_dialog_singlechoice);



                      for (int i = 0; i < jArray.length(); i++) {
                          JSONObject row = jArray.getJSONObject(i);
                          String name = row.getString("name");
                          serversArrayAdapter.add(name);

                      }

                      builder.setAdapter(serversArrayAdapter, new DialogInterface.OnClickListener() {

                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              try {
                                  //Toast.makeText(getActivity(), jArray.getJSONObject(i).getString("link"), Toast.LENGTH_SHORT).show();
                                  choosePlugin.dismiss();
                                  new TestConnection(MODE_PLUGIN, jArray.getJSONObject(i).getString("name")).execute(jArray.getJSONObject(i).getString("link"));
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                      });

                      choosePlugin = builder.create();
                      choosePlugin.show();

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
                  break;

              case MODE_PLUGIN:

                  if(!folder.exists())folder.mkdir();
                  File fileToSaveJson  = new File(folder, name);
                  byte[] jsonArray = resultJSON.getBytes();


                  BufferedOutputStream bos;
                  try {
                      bos = new BufferedOutputStream(new FileOutputStream(fileToSaveJson));
                      bos.write(jsonArray);
                      bos.flush();
                      bos.close();

                  } catch (FileNotFoundException e4) {
                      // TODO Auto-generated catch block
                      e4.printStackTrace();
                  } catch (IOException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                  }

                  pluginsAdapter.notifyDataSetChanged();
                  pluginsLV.deferNotifyDataSetChanged();

                  pluginsList.remove(name);
                  pluginsList.add(name);
                  pluginsAdapter.notifyDataSetChanged();

                  JSONObject json = null;
                  try {
                      json = new JSONObject(resultJSON);
                      final JSONArray proc = json.getJSONArray("proc");
                      final JSONArray buttons= json.getJSONArray("array");
                      if(proc!=null && buttons!=null){
                          DbTool db = new DbTool();
                          int btnsCount = buttons.length()>9?9:buttons.length();
                          for (int i = 0; i < proc.length(); i++) {
                              String pr = proc.getString(i);
                              for (int j = 0; j <btnsCount ; j++) {
                                String place  = shp.getInt("ButtonId"+j, 0) + pr;

                                JSONObject btn = buttons.getJSONObject(j);

                                db.bindButtonToPlace(
                                        db.addButton(-1, btn.getString("name"), btn.getInt("type"), btn.getString("cmd"), 0, getActivity()),
                                        place,
                                        getActivity()
                                );
                              }

                          }
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }



                  pluginsLV.performItemClick(pluginsLV, pluginsList.indexOf(name), pluginsLV.getItemIdAtPosition(pluginsList.indexOf(name)));
                  break;
          }

        }

        @Override
        protected void onCancelled() {
            if(choosePlugin!=null) choosePlugin.dismiss();
        }



    }

}
