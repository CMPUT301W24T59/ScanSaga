package com.example.scansaga;

import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An {@link IdlingResource} for Espresso tests that waits for asynchronous operations
 * to complete in Firestore.
 */
public class FirestoreTaskIdlingResource implements IdlingResource {
    private volatile ResourceCallback resourceCallback;

    // When this is 0, it means we are idle.
    private final AtomicInteger activeTasks = new AtomicInteger(0);

    /**
     * Increments the count of active tasks. Call this method when a Firestore task starts.
     */
    public void increment() {
        activeTasks.getAndIncrement();
        setIdleState(false);
    }

    /**
     * Decrements the count of active tasks. Call this method when a Firestore task finishes.
     * If the active task count reaches 0, it notifies the {@link ResourceCallback} that it's idle.
     */
    public void decrement() {
        int counter = activeTasks.decrementAndGet();
        if (counter == 0) {
            setIdleState(true);
        }
    }

    /**
     * Sets the idle state of the resource. If idle, notifies the registered {@link ResourceCallback}.
     *
     * @param isIdleNow Whether the resource is now idle.
     */
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
