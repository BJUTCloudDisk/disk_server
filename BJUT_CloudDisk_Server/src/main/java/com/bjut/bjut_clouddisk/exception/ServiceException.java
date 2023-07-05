package com.bjut.bjut_clouddisk.exception;

public class ServiceException extends Exception {
    String errorCode;
    String errorMessage;

    public ServiceException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
