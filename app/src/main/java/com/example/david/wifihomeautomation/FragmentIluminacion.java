package com.example.david.wifihomeautomation;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentIluminacion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentIluminacion extends Fragment {

    private OnFragmentInteractionListener mListener;

    Button connect;
    EditText ipServer;
    TextView estado;
    Switch btnSwitchled1,btnSwitchled2;
    SeekBar seekbarled1, seekbarled2;
    boolean socketStatus = false;
    Socket socket;
    MyClientTask myClientTask;
    String address;
    int port = 80;

    int intensidad1 = 0;
    int intensidad2 = 0;

    public FragmentIluminacion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_iluminacion, container, false);
        connect = (Button)view.findViewById(R.id.connect);
        ipServer = (EditText)view.findViewById(R.id.ip_server);
        estado = (TextView)view.findViewById(R.id.estado);
        btnSwitchled1 = (Switch) view.findViewById(R.id.leduno);
        btnSwitchled2 = (Switch) view.findViewById(R.id.leddos);
        seekbarled1 = (SeekBar) view.findViewById(R.id.seekBarled1);
        seekbarled1.setMax(256);
        seekbarled1.setEnabled(false);
        seekbarled2 = (SeekBar) view.findViewById(R.id.seekBarled2);
        seekbarled2.setMax(256);
        seekbarled2.setEnabled(false);

        btnSwitchled1.setOnClickListener(OnOffLedClickListener);
        btnSwitchled2.setOnClickListener(OnOffLedClickListener);
        connect.setOnClickListener(connectOnClickListener);

        seekbarled1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String intensisin = "";
                intensidad1 = progress;
                intensisin="luminosidadled1" + "?lumi=" + String.valueOf(intensidad1);
                MyClientTask taskEsp = new MyClientTask(address);
                taskEsp.execute(intensisin);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarled2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String intensisin = "";
                intensidad1 = progress;
                intensisin="luminosidadled2" + "?lumi=" + String.valueOf(intensidad2);
                MyClientTask taskEsp = new MyClientTask(address);
                taskEsp.execute(intensisin);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    View.OnClickListener OnOffLedClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            String onoff = "";
            //CONDICION LED 1
            if(v.getId()==R.id.leduno){

                if(btnSwitchled1.isChecked()){
                    onoff="encenderled" + "?led=1";
                    seekbarled1.setEnabled(true);
                    seekbarled1.setProgress(256);
                }else{
                    onoff="apagarled" + "?led=1";
                    seekbarled1.setProgress(0);
                    seekbarled1.setEnabled(false);
                }
            }
            //CONDICION LED 2
            if(v.getId()==R.id.leddos){
                if(btnSwitchled2.isChecked()){
                    onoff="encenderled" + "?led=2";
                    seekbarled2.setEnabled(true);
                    seekbarled2.setProgress(256);
                }else{
                    onoff="apagarled" + "?led=2";
                    seekbarled2.setProgress(0);
                    seekbarled2.setEnabled(false);
                }
            }
            MyClientTask taskEsp = new MyClientTask(address);
            taskEsp.execute(onoff);
        }
    };

    View.OnClickListener connectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if(socketStatus)
                Toast.makeText(getContext(),"Already talking to a Socket!! Disconnect and try again!", Toast.LENGTH_LONG).show();
            else {
                socket = null;
                address = ipServer.getText().toString();
                if (address == null)
                    Toast.makeText(getContext(), "Please enter valid address", Toast.LENGTH_LONG).show();
                else {
                    myClientTask = new MyClientTask(address);
                    ipServer.setEnabled(false);
                    connect.setEnabled(false);
                    //myClientTask.execute("apagarled");
                    //encender.setEnabled(true);
                    estado.setText("IP guardada");
                }
            }
        }
    };

    public class MyClientTask extends AsyncTask<String,Void,String> {
        String server;
        MyClientTask(String server){
            this.server = server;
        }
        @Override
        protected String doInBackground(String... params) {
            StringBuffer chaine = new StringBuffer("");
            final String val = params[0];
            final String p = "http://"+ server+"/"+val;
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    //estado.setText(p);
                }
            });
            String serverResponse = "";
            try {
                URL url = new URL(p);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    chaine.append(line);
                }
                inputStream.close();
                System.out.println("chaine: " + chaine.toString());
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            }
            return serverResponse;
        }
        @Override
        protected void onPostExecute(String s) {
        }
    }

    /*
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i("Instance state","onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        Log.i("Instance state","onViewStateRestored");
    }
    */

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
