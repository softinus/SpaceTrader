package com.raimsoft.spacetrader.util;

import com.raimsoft.spacetrader.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class AlertDialogMaker extends Activity
{

	@Override
	protected Dialog onCreateDialog(int id, Bundle args)
	{
		LayoutInflater factory = LayoutInflater.from(this);
	    final View textEntryView = factory.inflate(R.layout.login_dialog, null);
	    return new AlertDialog.Builder(AlertDialogMaker.this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("로그인")
	        .setView(textEntryView)
	        .setPositiveButton("확인", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int whichButton)
	            {

	                /* User clicked OK so do some stuff */
	            }
	        })
	        .setNegativeButton("닫기", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int whichButton)
	            {

	                /* User clicked cancel so do some stuff */
	            }
	        })
	        .create();
	}
	
}
