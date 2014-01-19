package com.anygine.core.server;

import javax.security.auth.login.FailedLoginException;

import playn.core.Json;

import com.anygine.core.common.client.Session;
import com.anygine.core.common.client.Session_Storable;
import com.anygine.core.common.client.api.SessionService;
import com.anygine.core.common.client.domain.JsonObjects;
import com.anygine.core.common.codegen.api.JsonWritableInternal;
import com.google.inject.Inject;

public class SessionServlet extends BaseServlet {

  private final SessionService sessionService;

  @Inject
  public SessionServlet(
      SessionService sessionService, JsonObjects jsonObjects) {
    super(jsonObjects);
    this.sessionService = sessionService;
  }

  @Override
  protected JsonWritableInternal doOperation(Operation operation, Json.Object data)
    throws Exception {
    switch (operation) {
      case Login:
        return (Session_Storable) login(data);
    }
    return super.doOperation(operation, data);
  }

  private Session login(Json.Object jsonData) throws FailedLoginException {
    String username = jsonData.getString("username");
    String password = jsonData.getString("password");
    Session session = sessionService.login(username, password);
    return session;
  }

}
