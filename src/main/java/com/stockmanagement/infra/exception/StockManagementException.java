package com.stockmanagement.infra.exception;

import lombok.Getter;

/**
 * Generic exception for StockManagement Application, exception for the StockManagement application, should be extended to a more specific one.
 *
 * @author frfontoura
 * @version 1.0 08/01/2020
 */
public class StockManagementException extends RuntimeException {

    @Getter
    private boolean showMessage = false;

    public StockManagementException(final String message) {
        super(message);
    }

    public StockManagementException(final String message, final boolean showMessage) {
        super(message);
        this.showMessage = showMessage;
    }

}
