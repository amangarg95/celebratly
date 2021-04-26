package com.kiprosh.optimizeprime.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kiprosh.optimizeprime.R;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    BottomSheetDialog.onItemClickListener listener;

    public BottomSheetDialog(BottomSheetDialog.onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_behaviour,
                container, false);

        LinearLayout llCool = v.findViewById(R.id.ll_bg_cool);
        LinearLayout llSummer = v.findViewById(R.id.ll_bg_summer);
        LinearLayout llLights = v.findViewById(R.id.ll_bg_lights);

        llCool.setOnClickListener(v1 -> listener.onItemClick(1));

        llSummer.setOnClickListener(v1 -> listener.onItemClick(2));

        llLights.setOnClickListener(v1 -> listener.onItemClick(3));
        return v;
    }

    public interface onItemClickListener {
        void onItemClick(int pos);
    }
}