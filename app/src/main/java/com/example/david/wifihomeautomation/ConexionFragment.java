package com.example.david.wifihomeautomation;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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

    Button guardarip;
    EditText ipServer;

    public ConexionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conexion, container, false);

        guardarip = (Button)view.findViewById(R.id.ip_save);
        ipServer = (EditText)view.findViewById(R.id.ip_server);

        guardarip.setOnClickListener(connectOnClickListener);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);
        ipServer.setText(preferences.getString("number",""));
        return view;
    }

    View.OnClickListener connectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            SharedPreferences preferencias = getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);
            SharedPreferences.Editor Obj_editor = preferencias.edit();
            Obj_editor.putString("number", ipServer.getText().toString());
            Obj_editor.commit();
            Snackbar.make(arg0, "IP GUARDADA CON EXITO!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    };


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
