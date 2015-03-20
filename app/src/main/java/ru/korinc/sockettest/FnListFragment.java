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

import net.dinglisch.android.tasker.PluginBundleManager;
import net.dinglisch.android.tasker.TaskerPlugin;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
    List<String[]> fnpl;
    List<Integer[]> fnpltype;
	ButtonFnManager fnb;

    ListView pluginsLV;
    ArrayAdapter<String> pluginsAdapter ;
    List<String> pluginsList;
    Button footer;
    public static final String PLUGINS_FOLDER_PATH = Environment.getExternalStorageDirectory()+"/Adaptive remote plugins";
    File pluginsFolder = new File(PLUGINS_FOLDER_PATH);
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
	
	if(!pluginsFolder.exists()) pluginsFolder.mkdir();

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
                if (i == 0) setAdaptiveRemoteContent();
                else {
                    String pluginName = pluginsLV.getItemAtPosition(i).toString();

                    File jsonPluginFile = new File(pluginsFolder, pluginName);
                    FileInputStream stream = null;
                    try {
                        stream = new FileInputStream(jsonPluginFile);

                        String jsonStr = null;

                        FileChannel fc = stream.getChannel();
                        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                        jsonStr = Charset.defaultCharset().decode(bb).toString();

                        JSONObject pluginJson = new JSONObject(jsonStr);

                        JSONArray jArray = pluginJson.getJSONArray("array");

                        int color = 0;
                        try {

                            color = Color.parseColor(pluginJson.getString("color"));}catch (JSONException e) {}
                        setPLuginContent(jArray, pluginName, color);

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
        for(File f: pluginsFolder.listFiles()){
            if(!f.getName().contains(".")){
                String pluginName = f.getName();
                pluginsList.add(pluginName);
            }

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

		fnb = new ButtonFnManager(getActivity(), null);
		fns = new ArrayList<String>() ;
        fnpl = new ArrayList<String[]>() ;
        fnpltype = new ArrayList<Integer[]>() ;
        setAdaptiveRemoteContent();
	}
	
	 public void onListItemClick(ListView l, View v, int position, long id) {
         Intent intent = new Intent();
         final Bundle resultBundle;
         final String blurb;
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

                resultBundle = PluginBundleManager.generateBundle(getActivity().getApplicationContext(), "", "", fnKey);
                blurb  = ""+fnKey;

         }else{
             intent.putExtra("Name",fns.get(position));
             intent.putExtra("FnResult",fnpltype.get(position)[0]);
             intent.putExtra("FnResultArgs",fnpl.get(position)[0]);
             intent.putExtra("plugin",fnpl.get(position)[1]);
             intent.putExtra("color",fnpltype.get(position)[1]);

             resultBundle = PluginBundleManager.generateBundle(getActivity().getApplicationContext(), fns.get(position), fnpl.get(position)[0], fnpltype.get(position)[0]);
             blurb  = fns.get(position) + " | " + fnpl.get(position)[0] + " | " + fnpltype.get(position)[0];
         }
         intent.putExtra(FnBind.BTN_ID, btnId);


         if ( TaskerPlugin.Setting.hostSupportsOnFireVariableReplacement( getActivity() ) )
             TaskerPlugin.Setting.setVariableReplaceKeys( resultBundle, new String [] { FnBind.BTN_CMD } );

         intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);
         intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb);

         if (  TaskerPlugin.Setting.hostSupportsSynchronousExecution( getActivity().getIntent().getExtras() ) )
             TaskerPlugin.Setting.requestTimeoutMS( intent, 3000 );

         if ( TaskerPlugin.hostSupportsRelevantVariables( getActivity().getIntent().getExtras() ) )
             TaskerPlugin.addRelevantVariableList( intent, new String [] {
                     "%context\nContext\nCurrent PC process in focus",
                     "%btnids()\nContext Button id\nContext Button id",
             } );

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

    private void setPLuginContent(JSONArray jArray, String plugin, int color){
        itIsPlugin = true;
        fns.clear();
        fnpl.clear();
        fnpltype.clear();
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject row = null;
            try {
                row = jArray.getJSONObject(i);

            fns.add(row.getString("name"));
            fnpl.add(new String[]{row.getString("cmd"), plugin});
            fnpltype.add(new Integer[]{row.getInt("type"), color});

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
        public static final int MODE_ICO_LINK = 3;


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
                    httppost = new HttpPost("https://raw.githubusercontent.com/kor-ka/Adaptive-remote-server/mouse/plugins/plugins.json");
                break;

                case MODE_PLUGIN:
                    httppost = new HttpPost(params[0]);
                break;

                default:
                    httppost = new HttpPost("https://raw.githubusercontent.com/kor-ka/Adaptive-remote-server/mouse/plugins/plugins.json");
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

                //���� ���� ������ �� ������
                if(mode == MODE_PLUGIN && params[1]!=null){
                    try {
                        URL url = new URL(params[1]);
                        URLConnection conection = url.openConnection();
                        conection.connect();

                        // this will be useful so that you can show a tipical 0-100%
                        // progress bar
                        int lenghtOfFile = conection.getContentLength();

                        // download the file
                        InputStream input = new BufferedInputStream(url.openStream(),
                                8192);

                        // Output stream
                        if(!pluginsFolder.exists()) pluginsFolder.mkdir();
                        File ico = new File(pluginsFolder, name+".png");
                        OutputStream output = new FileOutputStream(ico);

                        byte data[] = new byte[1024];

                        long total = 0;
                        int count;
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            output.write(data, 0, count);
                        }

                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();
                    }catch (Exception e){
                        //OOops
                    }

                }

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
                                  new TestConnection(MODE_PLUGIN, jArray.getJSONObject(i).getString("name")).execute(jArray.getJSONObject(i).getString("link"), jArray.getJSONObject(i).getString("icoLink"));
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

                  if(!pluginsFolder.exists()) pluginsFolder.mkdir();
                  File fileToSaveJson  = new File(pluginsFolder, name);
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

                      JSONArray proc = null;
                      try {
                          proc  = json.getJSONArray("proc");
                      }catch (JSONException e){
                            proc = new JSONArray();
                            proc.put("");
                      }

                      int color = 0;
                      try {

                          color = Color.parseColor(json.getString("color"));
                      }catch (JSONException e){

                      }

                      JSONArray buttons= json.getJSONArray("array");
                      DbTool db = new DbTool();
                      int btnsCount = buttons.length()>9?9:buttons.length();


                          for (int i = 0; i < proc.length(); i++) {
                              String pr = proc.getString(i);
                              for (int j = 0; j <btnsCount ; j++) {
                                JSONObject btn = buttons.getJSONObject(j);
                                long id = db.addButton(-1, btn.getString("name"), btn.getInt("type"), btn.getString("cmd"), 0, getActivity(), name, color);
                                if(!pr.isEmpty()){
                                    String place  = shp.getInt("ButtonId"+j, 0) + pr;
                                    db.bindButtonToPlace(id, place, getActivity());
                                }
                              }
                          }

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }



                  pluginsLV.performItemClick(pluginsLV, pluginsList.indexOf(name), pluginsLV.getItemIdAtPosition(pluginsList.indexOf(name)));
                  break;

              case MODE_ICO_LINK:

                  break;
          }

        }

        @Override
        protected void onCancelled() {
            if(choosePlugin!=null) choosePlugin.dismiss();
        }



    }

}
