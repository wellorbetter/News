package com.example.news.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.news.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class NetworkDisconnected extends Fragment {
    private SharedPreferences preferences;
    private String token;
    private Button reconnect;
    @Nullable
    @Override
    //用于处理断网
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network_disconnected, container, false);
        reconnect = view.findViewById(R.id.reconnect);
        reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token","Null");
    }
}
