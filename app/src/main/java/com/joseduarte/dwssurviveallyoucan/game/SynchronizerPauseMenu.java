package com.joseduarte.dwssurviveallyoucan.game;

public class SynchronizerPauseMenu {

    public void notifyThreads() {
        synchronized (this) {
            notifyAll();
        }
    }

}
