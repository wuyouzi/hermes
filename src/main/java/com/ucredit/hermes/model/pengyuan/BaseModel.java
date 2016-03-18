/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import com.ucredit.hermes.model.IDOwner;
import com.ucredit.hermes.model.NonEqualsBaseModel;

/**
 * @author ijay
 * @param <S>
 */
public abstract class BaseModel<S> extends NonEqualsBaseModel<BaseModel<S>>
        implements IDOwner<S> {
    private static final long serialVersionUID = -3910829862996014763L;

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result
            + (this.getId() == null ? 0 : this.getId().hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        BaseModel<S> other = (BaseModel<S>) obj;
        if (this.getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!this.getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    public BaseModel<S> cloneWithoutCascade() {
        return this.clone();
    }
}
