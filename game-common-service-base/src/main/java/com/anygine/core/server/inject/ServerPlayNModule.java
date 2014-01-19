package com.anygine.core.server.inject;

import playn.core.Assets;
import playn.core.PlayN;

import com.google.inject.AbstractModule;

public class ServerPlayNModule extends AbstractModule {

	@Override
	protected void configure() {
	  bind(Assets.class).toInstance(PlayN.assets());
	}

}
