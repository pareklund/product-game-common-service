package com.anygine.core.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import playn.core.Json;
import playn.core.json.JsonImpl;

import com.anygine.core.common.client.AnygineException;
import com.anygine.core.common.client.AnygineException_Embeddable;
import com.anygine.core.common.client.domain.JsonObjects;
import com.anygine.core.common.codegen.api.JsonWritableInternal;

// TODO: Add support for filter chains (checking account balance, offers, ...)
public abstract class BaseServlet extends HttpServlet {

  private final static Json JSON = new JsonImpl();

  private final JsonObjects jsonObjects;
  
  public BaseServlet(JsonObjects jsonObjects) {
    this.jsonObjects = jsonObjects;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Json.Object jsonObj = getJsonObj(request.getInputStream());
    Operation operation = Operation.valueOf(jsonObj.getString("operation"));
    Json.Object data = jsonObj.getObject("data");
    JsonWritableInternal jsonWritable = null;
    try {
      jsonWritable = doOperation(operation, data);
    } catch (Throwable t) {
      if (t instanceof AnygineException_Embeddable) {
        jsonWritable = (AnygineException_Embeddable) t;
      } else {
        jsonWritable = new AnygineException_Embeddable(t);
      }
    }
    response.getWriter().write(jsonObjects.serializeJson(jsonWritable));
  }

  protected JsonWritableInternal doOperation(Operation operation, Json.Object data) 
    throws Exception {
    return new AnygineException_Embeddable(new UnsupportedOperationException(
        "Operation " + operation + " not yet supported"));
  }

  protected Json.Object getJsonObj(ServletInputStream inputStream) {
    if (inputStream == null) {
      return null;
    }
    try {
      Writer writer = new StringWriter();
      char[] buffer = new char[1024];
      Reader reader = new BufferedReader(
          new InputStreamReader(inputStream, "UTF-8"));
      int n;
      while ((n = reader.read(buffer)) != -1) {
        writer.write(buffer, 0, n);
      }
      String jsonStr = writer.toString();
      return JSON.parse(jsonStr);
    } catch (Exception e) {
      return null;
    }
    finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
}
