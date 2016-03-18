package com.ucredit.hermes.exception;

public class ServicesException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 2655301563943644375L;

    public ServicesException(String code, Exception e) {
        System.out.println("code:" + code + ";exception:" + e);
    }

    public ServicesException(Exception e) {
        System.out.println("exception:" + e);
    }
}
