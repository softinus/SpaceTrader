package com.raimsoft.spacetrade.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.raimsoft.spacetrade.screens.MainMenuScreen;

public class SpaceTrade extends Game
{
	public static void main(String[] args)
	{
		new JoglApplication(new SpaceTrade(),
							"space trade",
							480, 800, 
							false);
	}

	@Override
	public void create()
	{
		setScreen(new MainMenuScreen(this));
	}
}
