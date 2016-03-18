/**
 * Copyright(c) 2011-2012 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model;

/**
 * @author ijay
 * @param <T>
 */
public interface IDOwner<T> {
    T getId();

    void setId(T id);
}
