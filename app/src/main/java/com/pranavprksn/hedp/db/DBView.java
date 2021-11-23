package com.pranavprksn.hedp.db;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pranavprksn.hedp.R;

public class DBView extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.db_list_view,
                container, false);

        TextView data = v.findViewById(R.id.data);



        return v;
    }
}
