package com.example.pratyush.forgotpassword;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by pratyush on 11-01-2018.
 */

public class MyDialog extends DialogFragment {

    Button ok;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_send_verification,null);
        /*ok=view.findViewById(R.id.button_ok_forgotpassword);
        ok.setOnClickListener(this);*/
        setCancelable(false);
        return view;

    }

   /* @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button_ok_forgotpassword:
                dismiss();
                startActivity(new Intent(v.getContext(),MainActivity.class));
        }
    }*/
}
