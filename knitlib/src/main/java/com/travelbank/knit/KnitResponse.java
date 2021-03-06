package com.travelbank.knit;

/**
 *
 * This class wraps the body of a generated response type or an error from a {@link KnitModel}.
 * Generated by a {@link com.travelbank.knit.generators.ValueGenerator}.
 *
 * @param <A> response body type.
 *
 * @see com.travelbank.knit.generators.ValueGenerator
 *
 * @author Omer Ozer
 */

public class KnitResponse<A> {
    /**
     * Error instance.
     */
    private Throwable e;

    /**
     * Error message in {@link String} format.
     */
    private String errorMessage;

    /**
     * Body of the response.
     */
    private A body;

    public KnitResponse(A body){
        this.body = body;
    }

    /**
     * Method that returns the error instance.
     * @return error instance.
     */
    public Throwable getError() {
        return e;
    }

    /**
     * Sets error instance and the error message.
     * @param e error instance that contains the message .
     */
    public void setError(Throwable e) {
        this.e = e;
        this.errorMessage = e.getMessage();
    }

    /**
     * Returns the response body.
     * @return response body.
     */
    public A getBody() {
        return body;
    }

    /**
     * Sets the response body.
     * @param body response body to be set.
     */
    public void setBody(A body) {
        this.body = body;
    }

    /**
     * Returns the error message.
     * @return error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message. This won't set the error instance. That requires a {@link Throwable} to be given through {@link this#setError(Throwable)}.
     * @param errorMessage error message to be set.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Returns whether the response was successful or not. Returns {@code true} if {@link this#errorMessage} is null and {@code false} if not.
     * @return whether the response was successful or not
     */
    public boolean isSuccessfull() {
        return errorMessage == null;
    }
}
