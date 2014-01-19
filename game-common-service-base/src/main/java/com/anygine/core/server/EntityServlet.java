package com.anygine.core.server;

import playn.core.Json;

import com.anygine.core.common.client.api.EntityFactory;
import com.anygine.core.common.client.api.EntityService;
import com.anygine.core.common.client.api.EntityStorage;
import com.anygine.core.common.client.api.JsonWritableFactory;
import com.anygine.core.common.client.domain.JsonObjects;
import com.anygine.core.common.codegen.api.EntityInternal;
import com.anygine.core.common.codegen.api.JsonWritableInternal;
import com.google.inject.Inject;

public class EntityServlet extends BaseServlet {

  private final EntityStorage storage;
  private final EntityFactory factory;
  private final EntityService service;
  
  @Inject
  public EntityServlet(
      EntityStorage storage, JsonObjects jsonObjects,
      EntityFactory factory, EntityService service) {
    super(jsonObjects);
    this.storage = storage;
    this.factory = factory;
    this.service = service;
  }

  // TODO: Extract entity first, then get id - now getInt("id") will fail
  @Override
  protected JsonWritableInternal doOperation(
      Operation operation, Json.Object entityObj) throws Exception {
    switch (operation) {
      case Store:
        return storage.insert((EntityInternal) factory.newInstance(
            EntityInternal.class, entityObj));
      case Update:
        return storage.update(entityObj);
      case Delete:
        return storage.delete(
            (Class<? extends EntityInternal>) service.getClass(entityObj), 
            (long) entityObj.getInt("id"));
      case GetById:
        return storage.getById(
            (Class<? extends EntityInternal>) service.getClass(entityObj), 
            (long) entityObj.getInt("id"));
    }
    return super.doOperation(operation, entityObj);  
  }
  
}
