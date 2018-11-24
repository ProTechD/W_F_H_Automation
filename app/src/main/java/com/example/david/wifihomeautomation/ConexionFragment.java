package com.example.david.wifihomeautomation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConexionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ConexionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    Button connect;
    EditText ipServer;
    TextView estado;
    boolean socketStatus = false;
    Socket socket;
    MyClientTask myClientTask;
    String address;
    int port = 80;

    public ConexionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conexion, container, false);

        connect = (Button)view.findViewById(R.id.connect);
        ipServer = (EditText)view.findViewById(R.id.ip_server);
        estado = (TextView)view.findViewById(R.id.estado);

        connect.setOnClickListener(connectOnClickListener);

        return view;
    }

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
                    myClientTask.execute("apagar");
                    //encender.setEnabled(true);
                    estado.setText("IP guardada");
                }
            }
        }
    };

    public class MyClientTask extends AsyncTask<String,Void,String>{
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
                    estado.setText(p);
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
