package com.pranavprksn.hedp.db;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pranavprksn.hedp.R;
import com.pranavprksn.hedp.models.DataModel;

import java.util.ArrayList;

public class DBView extends BottomSheetDialogFragment {

    TextView data;
    String dataString = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.db_list_view,
                container, false);

        data = v.findViewById(R.id.data);
        data.setText(dataString);

        return v;
    }

    public void fillData(ArrayList<DataModel> dataList){
        for (DataModel dataModel: dataList){
            dataString += dataModel.getInputText() + ":\n" + dataModel.getEncryptedText() + "\n\n";
        }

    }
}
