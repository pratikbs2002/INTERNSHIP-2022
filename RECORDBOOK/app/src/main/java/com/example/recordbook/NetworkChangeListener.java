package com.example.recordbook;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!InernetConnection.isConnectedToInternet(context)) {  //internet is not connected
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View noICDialog = LayoutInflater.from(context).inflate(R.layout.check_internet_connection_dialog, null);
            builder.setView(noICDialog);

            AppCompatButton btnRetry = noICDialog.findViewById(R.id.btnRetry);

            //show dialog
            AlertDialog dialog = builder.create();

            dialog.show();
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

     //       ContextCompat.getDrawable(this, R.drawable.your_drawable);
//            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(R.drawable.dialog_background,null));
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            });
        }
    }
}
