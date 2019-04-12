package com.jazasoft.scanner;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class PiezoKeyPad {

    private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
    public static final String KEY = "key";
    private char keyPressed;

    /**
     * The Constant KEYPAD.
     */
    private static final char keypad[][] = {
            {'1', '2', '3', 'A'},
            {'4', '5', '6', 'B'},
            {'7', '8', '9', 'C'},
            {'*', '0', '#', 'D'}
    };

    /**
     * The gpio.
     */
    private final GpioController theGpio = GpioFactory.getInstance();

    /**
     * The Constant PIN_1_IN.
     */
    private static final Pin PIN_1_IN = RaspiPin.GPIO_07;

    /**
     * The Constant PIN_2_IN.
     */
    private static final Pin PIN_2_IN = RaspiPin.GPIO_00;

    /**
     * The Constant PIN_3_IN.
     */
    private static final Pin PIN_3_IN = RaspiPin.GPIO_02;

    /**
     * The Constant PIN_4_IN.
     */
    private static final Pin PIN_4_IN = RaspiPin.GPIO_03;

    /**
     * The Constant PIN_5_OUT.
     */
    private static final Pin PIN_5_OUT = RaspiPin.GPIO_01;

    /**
     * The Constant PIN_6_OUT.
     */
    private static final Pin PIN_6_OUT = RaspiPin.GPIO_04;

    /**
     * The Constant PIN_7_OUT.
     */
    private static final Pin PIN_7_OUT = RaspiPin.GPIO_05;

    /**
     * The Constant PIN_8_OUT.
     */
    private static final Pin PIN_8_OUT = RaspiPin.GPIO_06;

    /**
     * The pin1.
     */
    private final GpioPinDigitalInput thePin1 = theGpio
            .provisionDigitalInputPin(PIN_1_IN, PinPullResistance.PULL_UP);

    /**
     * The pin2.
     */
    private final GpioPinDigitalInput thePin2 = theGpio
            .provisionDigitalInputPin(PIN_2_IN, PinPullResistance.PULL_UP);

    /**
     * The pin3.
     */
    private final GpioPinDigitalInput thePin3 = theGpio
            .provisionDigitalInputPin(PIN_3_IN, PinPullResistance.PULL_UP);

    /**
     * The pin4.
     */
    private final GpioPinDigitalInput thePin4 = theGpio
            .provisionDigitalInputPin(PIN_4_IN, PinPullResistance.PULL_UP);

    /**
     * The pin5.
     */
    private final GpioPinDigitalOutput thePin5 = theGpio
            .provisionDigitalOutputPin(PIN_5_OUT);

    /**
     * The pin6.
     */
    private final GpioPinDigitalOutput thePin6 = theGpio
            .provisionDigitalOutputPin(PIN_6_OUT);

    /**
     * The pin7.
     */
    private final GpioPinDigitalOutput thePin7 = theGpio
            .provisionDigitalOutputPin(PIN_7_OUT);

    /**
     * The pin8.
     */
    private final GpioPinDigitalOutput thePin8 = theGpio
            .provisionDigitalOutputPin(PIN_8_OUT);

    /**
     * The outputs.
     */
    private final GpioPinDigitalOutput theOutputs[] = {thePin5, thePin6,
            thePin7, thePin8};

    /**
     * The input.
     */
    private GpioPinDigitalInput theInput;

    /**
     * The in id.
     */
    private int theLin;

    /**
     * The in id.
     */
    private int theCol;

    /**
     * Instantiates a new piezo keypad.
     */
    public PiezoKeyPad() {
        initListeners();
    }

    /**
     * Find output.
     * <p>
     * Sets output lines to high and then to low one by one. Then the input line
     * is tested. If its state is low, we have the right output line and
     * therefore a mapping to a key on the keypad.
     */
    private void findOutput() {
        // now test the inuts by setting the outputs from high to low
        // one by one
        for (int myO = 0; myO < theOutputs.length; myO++) {
            for (final GpioPinDigitalOutput myTheOutput : theOutputs) {
                myTheOutput.high();
            }

            theOutputs[myO].low();

            // input found?
            if (theInput.isLow()) {
                theCol = myO;
                checkPins();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
                break;
            }
        }

        for (final GpioPinDigitalOutput myTheOutput : theOutputs) {
            myTheOutput.low();
        }
    }

    /**
     * Check pins.
     * <p>
     * Determins the pressed key based on the activated GPIO pins.
     */
    private synchronized void checkPins() {

        notifyListeners(this, KEY, this.keyPressed,
                this.keyPressed = keypad[theLin - 1][theCol]);

        // System.out.println(keypad[theLin - 1][theCol]);
    }

    /**
     * Inits the listeners.
     */
    private void initListeners() {
        thePin1.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    final GpioPinDigitalStateChangeEvent aEvent) {
                if (aEvent.getState() == PinState.LOW) {
                    theInput = thePin1;
                    theLin = 1;
                    findOutput();
                }
            }
        });

        thePin1.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    final GpioPinDigitalStateChangeEvent aEvent) {
                if (aEvent.getState() == PinState.LOW) {
                    theInput = thePin1;
                    theLin = 1;
                    findOutput();
                }
            }
        });
        thePin2.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    final GpioPinDigitalStateChangeEvent aEvent) {
                if (aEvent.getState() == PinState.LOW) {
                    theInput = thePin2;
                    theLin = 2;
                    findOutput();
                }
            }
        });
        thePin3.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    final GpioPinDigitalStateChangeEvent aEvent) {
                if (aEvent.getState() == PinState.LOW) {
                    theInput = thePin3;
                    theLin = 3;
                    findOutput();
                }
            }
        });
        thePin4.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    final GpioPinDigitalStateChangeEvent aEvent) {
                if (aEvent.getState() == PinState.LOW) {
                    theInput = thePin4;
                    theLin = 4;
                    findOutput();
                }
            }
        });
    }

    private void notifyListeners(Object object, String property, char oldValue,
                                 char newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property,
                    oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }
}