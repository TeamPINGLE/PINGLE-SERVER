package org.pingle.pingleserver.service;

public interface LockManager {
    void executeWithLock(String key, Runnable runnable);
}
