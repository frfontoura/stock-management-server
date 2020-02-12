package com.stockmanagement.infra.exception;

/**
 * Generic exception for StockManagement Application, exception for the StockManagement application, should be extended to a more specific one.
 *
 * @author frfontoura
 * @version 1.0 08/01/2020
 */
public class StockManagementException extends RuntimeException {

    public StockManagementException(final String message) {
        super(message);
    }

}
