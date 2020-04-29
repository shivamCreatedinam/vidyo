package com.createdinam.vidyo.customloder;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.createdinam.vidyo.R;

public class CustomLoader {

    Activity mActivity;
    AlertDialog mAlertDialog;

    public CustomLoader(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void startLoadingAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loader_view,null));
        builder.setCancelable(false);
        // show alert
        mAlertDialog = builder.create();
        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlertDialog.show();
    }

    public void setCancelAlertDailog(){
        mAlertDialog.dismiss();
    }
}
