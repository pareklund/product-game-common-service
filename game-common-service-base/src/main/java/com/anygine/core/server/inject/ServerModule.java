package com.anygine.core.server.inject;

import playn.core.Assets;

import com.anygine.core.common.client.Profile;
import com.anygine.core.common.client.api.EntityFactory;
import com.anygine.core.common.client.api.EntityService;
import com.anygine.core.common.client.api.EntityStorage;
import com.anygine.core.common.client.api.JsonWritableFactory;
import com.anygine.core.common.client.api.ProfileService;
import com.anygine.core.common.client.api.SessionManager;
import com.anygine.core.common.client.api.SessionService;
import com.anygine.core.common.client.domain.EntityFactoryImpl;
import com.anygine.core.common.client.domain.EntityServiceImpl;
import com.anygine.core.common.client.domain.EntityStorageImpl;
import com.anygine.core.common.client.domain.JsonWritableFactoryImpl;
import com.anygine.core.server.service.ServerProfileService;
import com.anygine.core.server.service.ServerSessionService;
import com.google.inject.AbstractModule;

public class ServerModule extends AbstractModule {

	@Override
	protected void configure() {
    bind(ProfileService.class).to(ServerProfileService.class);
	  bind(SessionService.class).to(ServerSessionService.class);
	  bind(SessionManager.class).to(ServerSessionService.class);
	  bind(Assets.class).to(ServerAssetManager.class);
	  bind(EntityStorage.class).to(EntityStorageImpl.class);
    bind(JsonWritableFactory.class).to(JsonWritableFactoryImpl.class);
	  bind(EntityFactory.class).to(EntityFactoryImpl.class);
    bind(EntityService.class).to(EntityServiceImpl.class);
    requestStaticInjection(Profile.class);
	}

}
