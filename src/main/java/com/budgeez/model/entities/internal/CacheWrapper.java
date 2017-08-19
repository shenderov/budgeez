package com.budgeez.model.entities.internal;

public class CacheWrapper {

    private Object object;
    private long expirationTime;

    public CacheWrapper() {
    }

    public CacheWrapper(Object object, long expireInMills) {
        this.object = object;
        this.expirationTime = System.currentTimeMillis() + expireInMills;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
