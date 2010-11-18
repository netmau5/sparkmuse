package net.sparkmuse.user;

/**
 * Exception used to mark that a user attempted to authenticate
 * via oauth without first passing through the application to
 * receive an OAuth request token.  This token is required to
 * identify the user when they return from the OAuth authentication
 * service.
 *
 * @author neteller
 * @created: Aug 21, 2010
 */
public class InvalidOAuthRequestToken extends Exception {


}
