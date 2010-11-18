package net.sparkmuse.common;

public enum ResponseCode {
  //success codes
  OK(200),
  CREATED(201),
  ACCEPTED(202),
  NO_CONTENT(204),

  //redirection codes
  MOVED_PERMENANTLY(301),
  FOUND(302),
  SEE_OTHER(303),
  NOT_MODIFIED(304),

  //client error
  BAD_REQUEST(400),
  UNAUTHORIZED(401),
  FORBIDDEN(403),
  NOT_FOUND(404),

  //server error
  INTERNAL_SERVER_ERROR(500),
  NOT_IMPLEMENTED(501);

  private final int status;

  ResponseCode(int status) {
    this.status = status;
  }

  public int getStatusCode() {
    return status;
  }

  public static ResponseCode forStatusCode(int statusCode) {
    for (ResponseCode response : values()) {
      if (response.status == statusCode) return response;
    }
    return INTERNAL_SERVER_ERROR;
  }
}
