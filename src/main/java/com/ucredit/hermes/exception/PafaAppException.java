package com.ucredit.hermes.exception;

public class PafaAppException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -7940682874050334627L;

    public PafaAppException(String code, Exception e) {
        System.out.println("code:" + code + ";exception:" + e);
    }

    public PafaAppException(Exception e) {
        System.out.println("exception:" + e);
    }

    public PafaAppException(String code) {
        System.out.println("code:" + code);
    }
}
