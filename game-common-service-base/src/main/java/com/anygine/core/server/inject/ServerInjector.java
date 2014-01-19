package com.anygine.core.server.inject;

import com.anygine.core.server.ProfileServlet;
import com.anygine.core.server.SessionServlet;


public interface ServerInjector {
	SessionServlet getSessionServlet();
  ProfileServlet getProfileServlet();
}
