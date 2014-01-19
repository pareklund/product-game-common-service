package com.anygine.core.common.client.serialize;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import playn.core.Assets;
import playn.java.JavaAssets;
import playn.java.JavaPlatform;

import com.anygine.core.common.client.Profile;
import com.anygine.core.common.client.Profile.Type;
import com.anygine.core.common.client.Profile_MetaModel;
import com.anygine.core.common.client.api.EntityStorage;
import com.anygine.core.common.client.api.SessionManager;
import com.anygine.core.common.client.api.UniqueConstraintViolationException;
import com.anygine.core.common.client.domain.QueryBuilder;
import com.anygine.core.common.client.inject.GameplayCommonInjectorImpl;
import com.anygine.core.common.client.inject.GameplayCommonInjectorManager;
import com.anygine.core.common.client.inject.PlayNInjector;
import com.anygine.core.common.client.inject.PlayNInjectorManager;
import com.anygine.core.common.codegen.api.EntityInternal;
import com.anygine.core.server.ProfileServlet;
import com.anygine.core.server.SessionServlet;
import com.anygine.core.server.inject.ServerInjector;
import com.anygine.core.server.inject.ServerInjectorManager;
import com.anygine.core.server.inject.ServerModule;
import com.anygine.core.server.inject.ServerPlayNModule;
import com.anygine.game.platformer.common.client.module.PlatformerCommonModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class EntityStorageImplTest {

  @Test
  public void testGetAllProfiles() {
    Injector injector = init();
    EntityStorage entityStorage = injector.getInstance(EntityStorage.class);
    Set<Profile> profiles = entityStorage.query(QueryBuilder.from(Profile.class).query());
    Assert.assertFalse(profiles.isEmpty());
  }

  @Test
  public void testAddProfile() {
    Injector injector = init();
    EntityStorage entityStorage = injector.getInstance(EntityStorage.class);
    Profile profile = new Profile(
        Type.AUTHENTICATED, "foo", "password", "foo@bar.com", false, "");
    try {
      Profile insertedProfile = entityStorage.insert(profile);
      EntityInternal<Profile> profileEntity = 
          (EntityInternal<Profile>) insertedProfile;
      Assert.assertTrue(profileEntity.getId() != 0);
      entityStorage.delete(Profile.class, profileEntity.getId());
      Profile_MetaModel p = Profile_MetaModel.META_MODEL;
      Profile removedProfile = entityStorage.getById(
          Profile.class, profileEntity.getId());
      Assert.assertNull(removedProfile);
    } catch (UniqueConstraintViolationException e) {
      Assert.fail();
    }
  }
  
  private Injector init() {
    JavaAssets assets = JavaPlatform.register().assets();
    assets.setPathPrefix("com/anygine/game/platformer/resources");

    Injector injector = Guice.createInjector(new ServerPlayNModule());
    PlayNInjectorManager.setInjector(injector);

    injector = Guice.createInjector(
        new PlatformerCommonModule(), new ServerModule());
    GameplayCommonInjectorManager.setInjector(
        new GameplayCommonInjectorImpl(injector, SessionManager.class));
    ServerInjectorManager.setInjector(getServerInjector(injector));
    return injector;
  }
  
  private static PlayNInjector getPlayNInjector(final Injector injector) {
    return new PlayNInjector() {
      
      @Override
      public Assets getAssetManager() {
        return injector.getInstance(Assets.class);
      }
    };
  }

  private static ServerInjector getServerInjector(final Injector injector) {
    return new ServerInjector() {
      
      @Override
      public SessionServlet getSessionServlet() {
        return injector.getInstance(SessionServlet.class);
      }
      
      @Override
      public ProfileServlet getProfileServlet() {
        return injector.getInstance(ProfileServlet.class);
      }

    };
  }


}
