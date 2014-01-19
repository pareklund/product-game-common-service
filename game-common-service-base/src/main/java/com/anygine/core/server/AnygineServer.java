package com.anygine.core.server;

import playn.java.JavaAssets;
import playn.java.JavaPlatform;
import playn.server.GameServer;

import com.anygine.core.common.client.api.SessionManager;
import com.anygine.core.common.client.inject.GameplayCommonInjectorImpl;
import com.anygine.core.common.client.inject.GameplayCommonInjectorManager;
import com.anygine.core.common.client.inject.PlayNInjectorManager;
import com.anygine.core.server.inject.ServerInjector;
import com.anygine.core.server.inject.ServerInjectorManager;
import com.anygine.core.server.inject.ServerModule;
import com.anygine.core.server.inject.ServerPlayNModule;
import com.anygine.game.platformer.common.client.module.PlatformerCommonModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class AnygineServer extends GameServer {

	public AnygineServer(int port) {
		super(port);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	  JavaAssets assets = JavaPlatform.register().assets();
    assets.setPathPrefix("com/anygine/game/platformer/resources");

    Injector injector = Guice.createInjector(new ServerPlayNModule());
    PlayNInjectorManager.setInjector(injector);

    injector = Guice.createInjector(
        new PlatformerCommonModule(), new ServerModule());
    GameplayCommonInjectorManager.setInjector(
        new GameplayCommonInjectorImpl(injector, SessionManager.class));
    ServerInjectorManager.setInjector(getServerInjector(injector));
		
    AnygineServer server = new AnygineServer(8080);
		ServerInjector serverInjector = ServerInjectorManager.getInjector();
		server.addServlet("/session", serverInjector.getSessionServlet());
    server.addServlet("/profile", serverInjector.getProfileServlet());
		
    try {
			server.run();
		} catch (Exception e) {
			System.err.println(e);
		}
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
