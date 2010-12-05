package controllers;

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

}