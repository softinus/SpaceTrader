package com.raimsoft.spacetrade;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.raimsoft.spacetrade.core.SpaceTrade;

public class SpaceTrade_androidActivity extends AndroidApplication
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialize(new SpaceTrade(), false); 
    }
}