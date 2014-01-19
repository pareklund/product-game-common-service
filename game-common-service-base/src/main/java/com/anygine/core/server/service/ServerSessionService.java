package com.anygine.core.server.service;

import java.util.Collection;

import javax.security.auth.login.FailedLoginException;

import playn.core.Json;

import com.anygine.core.common.client.Profile;
import com.anygine.core.common.client.Profile_MetaModel;
import com.anygine.core.common.client.Session;
import com.anygine.core.common.client.Session_MetaModel;
import com.anygine.core.common.client.api.EntityStorage;
import com.anygine.core.common.client.api.SessionService;
import com.anygine.core.common.client.api.UniqueConstraintViolationException;
import com.anygine.core.common.client.domain.GameComponentFactory;
import com.anygine.core.common.client.domain.Player;
import com.anygine.core.common.client.domain.QueryBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ServerSessionService implements SessionService {

  private final static long TIMEOUT = 10 * 60 * 1000;
  
  private final EntityStorage storage;
  private final GameComponentFactory factory;

  @Inject
  public ServerSessionService(
      EntityStorage storage, GameComponentFactory factory) {
    this.storage = storage;
    this.factory = factory;
  }
  
  @Override 
  public <P extends Player<?, ?, ?, ?>> Session<P> login(
      Class<P> klass, String username, String password) 
    throws FailedLoginException {
    Profile_MetaModel p = Profile_MetaModel.META_MODEL;
    Profile profile = storage.uniqueQuery(
        QueryBuilder.from(Profile.class).where(p.username).equalTo(username).
        and(p.password).equalTo(password).query());
    if (profile == null) {
      throw new FailedLoginException(
          "Wrong combination of username and password");
    }
    // TODO: Move id- and version generation to storage layer
    Session session = null;
    try {
      session = storage.insert(new Session(profile, factory.createPlayer(profile)));
    } catch (UniqueConstraintViolationException e) {}
    return session;
  }

  @Override
  public Collection<Session<? extends Player<?, ?, ?, ?>>> getSessions() {
    return storage.query(QueryBuilder.from(Session.class).query());
  }

  @Override
  public Session<?> getSingleSession() {
    return getSingleSession(Player.class);
  }

  @Override
  public <P extends Player<?, ?, ?, ?>> Session<P> getSingleSession(Class<P> klass) {
    return storage.uniqueQuery(QueryBuilder.from(Session.class).query());
  }

  // TODO: Implement join functionality - cross entity queries not working now
  @Override
  public boolean isLoggedIn(Profile profile) {
//    com.anygine.core.common.client.metamodel.Session session = MetaModel.getSession();
    // TODO: Generate MetaModel:s as separate classes to avoid exposing _Storable:s
    com.anygine.core.common.client.Session_MetaModel session = Session_MetaModel.META_MODEL;
    return (storage.uniqueQuery(
        QueryBuilder.from(Session.class).
        where(session.profile).equalTo(profile).
        and(session.lastActive).greaterThan(System.currentTimeMillis() - TIMEOUT).
        query()) != null);
  }

}
