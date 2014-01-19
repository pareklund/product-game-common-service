package com.anygine.core.server.inject;

import playn.core.Assets;
import playn.core.Image;
import playn.core.Sound;
import playn.core.util.Callback;

public class ServerAssetManager implements Assets {

  @Override
  public Image getImage(String path) {
    return null;
  }

  @Override
  public Sound getSound(String path) {
    return null;
  }

  @Override
  public void getText(String path, Callback<String> callback) {
    callback.onSuccess("");
  }

  @Override
  public boolean isDone() {
    return true;
  }

  @Override
  public int getPendingRequestCount() {
    return 0;
  }

  @Override
  public Image getImageSync(String path) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Image getRemoteImage(String url) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Image getRemoteImage(String url, float width, float height) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getTextSync(String path) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }
}
