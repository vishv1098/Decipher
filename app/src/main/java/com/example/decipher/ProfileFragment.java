package com.example.decipher;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {
    TextView profileName,profileNative;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = view.findViewById(R.id.profileName);
        profileName.setText(getArguments().getString("Name"));
        profileNative = view.findViewById(R.id.profileNative);
        profileNative.setText(getArguments().getString("Native"));
        return view;
    }
}