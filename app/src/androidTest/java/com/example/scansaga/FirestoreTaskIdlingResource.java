package com.example.scansaga;

import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

public class FirestoreTaskIdlingResource implements IdlingResource {
    private volatile ResourceCallback resourceCallback;

    // When this is 0, it means we are idle.
    private final AtomicInteger activeTasks = new AtomicInteger(0);

    // Call this method when a Firestore task starts
    public void increment() {
        activeTasks.getAndIncrement();
        setIdleState(false);
    }

    // Call this method when a Firestore task finishes
    public void decrement() {
        int counter = activeTasks.decrementAndGet();
        if (counter == 0) {
            setIdleState(true);
        }
    }

    private void setIdleState(boolean isIdleNow) {
        if (isIdleNow && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
    }

    @Override
    public String getName() {
        return FirestoreTaskIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        return activeTasks.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}
