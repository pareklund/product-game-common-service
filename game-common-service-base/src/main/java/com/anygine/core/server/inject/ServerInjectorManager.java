package com.anygine.core.server.inject;

public class ServerInjectorManager {

	private static ServerInjector injector;

	public static ServerInjector getInjector() {
		return injector;
	}

	public static void setInjector(ServerInjector injector) {
		ServerInjectorManager.injector = injector;
	}

}
