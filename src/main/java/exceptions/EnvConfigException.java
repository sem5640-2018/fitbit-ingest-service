package exceptions;

/**
 * Possible Exception class for use when starting application to check that we have a correctly configured enviroment.
 * My thinking is that at startup we can check that all the environment variables needed for the application are set so
 * if the app fails the reason is more explicit.
 */
public class EnvConfigException extends RuntimeException {

    public EnvConfigException(String message) {
        super(message);
    }

}
