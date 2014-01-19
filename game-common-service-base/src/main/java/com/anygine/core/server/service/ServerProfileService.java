package com.anygine.core.server.service;

import com.anygine.core.common.client.Profile;
import com.anygine.core.common.client.Profile.Type;
import com.anygine.core.common.client.api.EntityStorage;
import com.anygine.core.common.client.api.ProfileService;
import com.anygine.core.common.client.api.UniqueConstraintViolationException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ServerProfileService implements ProfileService {

  private final EntityStorage storage;
  
  @Inject
  public ServerProfileService(EntityStorage storage) {
    this.storage = storage;
  }
  
  @Override
  public Profile addProfile(
      String username, String password, String email, boolean autoLogin,
      String pictureUrl) throws UniqueConstraintViolationException {
    Profile profile = new Profile(
        Type.AUTHENTICATED, username, password, email, autoLogin, pictureUrl);
    return storage.insert(profile);
  }

}
