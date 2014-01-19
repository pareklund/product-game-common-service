package com.anygine.core.server;

import playn.core.Json;

import com.anygine.core.common.client.Profile;
import com.anygine.core.common.client.api.ProfileService;
import com.anygine.core.common.client.api.UniqueConstraintViolationException;
import com.anygine.core.common.client.domain.JsonObjects;
import com.anygine.core.common.codegen.api.JsonWritableInternal;
import com.google.inject.Inject;

public class ProfileServlet extends BaseServlet {

  private final ProfileService profileService;

  @Inject
  public ProfileServlet(
      ProfileService profileService, JsonObjects jsonObjects) {
    super(jsonObjects);
    this.profileService = profileService;
  }

  @Override
  protected JsonWritableInternal doOperation(Operation operation, Json.Object data) 
    throws Exception {
    switch (operation) {
      case AddProfile:
        return (JsonWritableInternal) addProfile(data);
    }
    return super.doOperation(operation, data);
  }

  private Profile addProfile(Json.Object jsonData) 
    throws UniqueConstraintViolationException {
    String username = jsonData.getString("username");
    String password = jsonData.getString("password");
    String email = jsonData.getString("email");
    boolean autoLogin = jsonData.getBoolean("autoLogin");
    String pictureUrl = jsonData.getString("pictureUrl");
    Profile profile = profileService.addProfile(
        username, password, email, autoLogin, pictureUrl);
    return profile;

  }

}
