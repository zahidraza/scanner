package com.jazasoft.scanner;

public class App {
    private long SLEEP_INTERVAL = 1000L; //1 second

    private void readQR(){

        while (true) {
            System.out.println("Reading camera Input");
            //Read Camera Input

            // If data received from camera, process it

            // else
            // Sleep
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readSpeedDial() {
        while (true) {
            System.out.println("Reading Dial Pad Input");
            //Read Dial Pad Input

            // If data received from dail pad, process it

            // else
            // Sleep
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        final App app = new App();

        Runnable taskQR = app::readQR;
        Runnable taskSpeedDail = app::readSpeedDial;

        //start task
        new Thread(taskQR).start();
        new Thread(taskSpeedDail).start();

        while (true) {
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
