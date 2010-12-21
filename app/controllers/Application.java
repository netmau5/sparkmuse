package controllers;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

public class Application extends SparkmuseController {

  public static void logout() {
    session.clear();
    Landing.index();
  }

  //@todo create credits page
  public static void credits(){
    render();
  }

  public static void about() {
    render();
  }

  public static void feedback(String appName) {
    render(appName);
  }

  public static void favicon() throws FileNotFoundException {
    File icon = play.Play.getFile("public/images/favicon.png");
    InputStream is = new FileInputStream(icon);
    response.setHeader("Content-Length", icon.length() + "");
    response.cacheFor("2h");
    response.contentType = "image/x-icon";
    response.direct = is; // renderBinary() will override any caching headers.
  }


  public static void get(String blobKey) {
    redirect(Blob.BLOB_SERVER + "/serve/" + blobKey);
  }

}