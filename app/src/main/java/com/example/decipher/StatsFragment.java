package com.example.decipher;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class StatsFragment extends Fragment {
    TextView textStatsCount,imageStatsCount;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        textStatsCount = view.findViewById(R.id.textStatsCount);
        textStatsCount.setText(getArguments().getString("Text"));
        imageStatsCount = view.findViewById(R.id.imageStatsCount);
        imageStatsCount.setText(getArguments().getString("OCR"));
        return view;
    }
}