/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.internal.scene.control.skin;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.internal.scene.control.behavior.LedBehavior;
import jfxtras.labs.scene.control.gauge.Led;
import jfxtras.labs.util.Util;


/**
 * Created by
 * User: hansolo
 * Date: 11.02.12
 * Time: 13:59
 */
public class LedSkin extends com.sun.javafx.scene.control.skin.BehaviorSkinBase<Led, LedBehavior> {
    private static final double      PREFERRED_WIDTH  = 16;
    private static final double      PREFERRED_HEIGHT = 16;
    private static final double      MINIMUM_WIDTH    = 5;
    private static final double      MINIMUM_HEIGHT   = 5;
    private static final double      MAXIMUM_WIDTH    = 1024;
    private static final double      MAXIMUM_HEIGHT   = 1024;
    public static final long BLINK_INTERVAL = 500000000l;
    private Led              control;
    private boolean          isDirty;
    private boolean          initialized;
    private Group            led;
    private Shape            ledOn;
    private boolean          on;
    private AnimationTimer   timer;
    private long             lastTimerCall;


    // ******************** Constructors **************************************
    public LedSkin(final Led CONTROL) {
        super(CONTROL, new LedBehavior(CONTROL));
        control       = CONTROL;
        initialized   = false;
        isDirty       = false;
        led           = new Group();
        timer         = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastTimerCall + BLINK_INTERVAL) {
                    on ^= true;
                    ledOn.setVisible(on);
                    lastTimerCall = currentNanoTime;
                }
            }
        };
        lastTimerCall = 0l;

        init();
    }

    private void init() {
        if (Double.compare(getSkinnable().getPrefWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getPrefHeight(), 0.0) <= 0 ||
            Double.compare(getSkinnable().getWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getHeight(), 0.0) <= 0) {
            if (getSkinnable().getPrefWidth() > 0 && getSkinnable().getPrefHeight() > 0) {
                getSkinnable().setPrefSize(getSkinnable().getPrefWidth(), getSkinnable().getPrefHeight());
            } else {
                getSkinnable().setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        if (Double.compare(getSkinnable().getMinWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getMinHeight(), 0.0) <= 0) {
            getSkinnable().setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
        }

        if (Double.compare(getSkinnable().getMaxWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getMaxHeight(), 0.0) <= 0) {
            getSkinnable().setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
        }

        led.getStyleClass().setAll("led");

        // Register listeners
        registerChangeListener(control.onProperty(), "ON");
        registerChangeListener(control.blinkingProperty(), "BLINKING");
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.typeProperty(), "TYPE");
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");

        if (control.isBlinking()) {
            timer.start();
        }

        initialized = true;
        repaint();
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("ON".equals(PROPERTY)) {
            ledOn.setVisible(control.isOn());
        } else if ("BLINKING".equals(PROPERTY)) {
            if (control.isBlinking()) {
                timer.start();
            } else {
                timer.stop();
                ledOn.setVisible(false);
            }
        } else if ("COLOR".equals(PROPERTY)) {
            repaint();
        } else if ("TYPE".equals(PROPERTY)) {
            repaint();
        } else if ("PREF_WIDTH".equals(PROPERTY)) {
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            repaint();
        }
    }

    public final void repaint() {
        isDirty = true;
        getSkinnable().requestLayout();
    }

    @Override public void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        if (!isDirty) return;

        if (!initialized) init();

        if (control.getScene() != null) {
            drawLed();
            getChildren().setAll(led);
        }
        isDirty = false;
    }

    public final Led getControl() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computeMinWidth(final double HEIGHT, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMinWidth(Math.max(MINIMUM_HEIGHT, HEIGHT - TOP_INSET - BOTTOM_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    @Override protected double computeMinHeight(final double WIDTH, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMinHeight(Math.max(MINIMUM_WIDTH, WIDTH - LEFT_INSET - RIGHT_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }

    @Override protected double computeMaxWidth(final double HEIGHT, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMaxWidth(Math.min(MAXIMUM_HEIGHT, HEIGHT - TOP_INSET - BOTTOM_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    @Override protected double computeMaxHeight(final double WIDTH, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMaxHeight(Math.min(MAXIMUM_WIDTH, WIDTH - LEFT_INSET - RIGHT_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }

    @Override protected double computePrefWidth(final double HEIGHT, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        double prefHeight = PREFERRED_HEIGHT;
        if (HEIGHT != -1) {
            prefHeight = Math.max(0, HEIGHT - TOP_INSET - BOTTOM_INSET);
        }
        return super.computePrefWidth(prefHeight, TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    @Override protected double computePrefHeight(final double WIDTH, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        double prefWidth = PREFERRED_WIDTH;
        if (WIDTH != -1) {
            prefWidth = Math.max(0, WIDTH - LEFT_INSET - RIGHT_INSET);
        }
        return super.computePrefHeight(prefWidth, TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }


    // ******************** Drawing related ***********************************
    public final void drawLed() {
        final double SIZE   = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH  = SIZE;
        final double HEIGHT = SIZE;

        led.setStyle("-fx-led: " + Util.colorToCssColor(control.getColor()));

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);

        final Shape LED_FRAME;
        final Shape LED_OFF;
        final Shape HIGHLIGHT;

        switch(control.getType()) {
            case SQUARE:
                LED_FRAME = new Rectangle(0.0625 * WIDTH, 0.0625 * HEIGHT, 0.875 * WIDTH, 0.875 * HEIGHT);
                LED_OFF   = new Rectangle(0.1875 * WIDTH, 0.1875 * HEIGHT, 0.625 * WIDTH, 0.625 * HEIGHT);
                ledOn     = new Rectangle(0.1875 * WIDTH, 0.1875 * HEIGHT, 0.625 * WIDTH, 0.625 * HEIGHT);
                HIGHLIGHT = new Rectangle(0.25 * WIDTH, 0.25 * HEIGHT, 0.5 * WIDTH, 0.1875 * HEIGHT);
                break;
            case VERTICAL:
                LED_FRAME = new Rectangle(0.25 * WIDTH, 0.0625 * HEIGHT, 0.5 * WIDTH, 0.875 * HEIGHT);
                LED_OFF   = new Rectangle(0.3125 * WIDTH, 0.125 * HEIGHT, 0.375 * WIDTH, 0.75 * HEIGHT);
                ledOn     = new Rectangle(0.3125 * WIDTH, 0.125 * HEIGHT, 0.375 * WIDTH, 0.75 * HEIGHT);
                HIGHLIGHT = new Rectangle(0.3125 * WIDTH, 0.125 * HEIGHT, 0.375 * WIDTH, 0.375 * HEIGHT);
                break;
            case HORIZONTAL:
                LED_FRAME = new Rectangle(0.0625 * WIDTH, 0.25 * HEIGHT, 0.875 * WIDTH, 0.5 * HEIGHT);
                LED_OFF   = new Rectangle(0.125 * WIDTH, 0.3125 * HEIGHT, 0.75 * WIDTH, 0.375 * HEIGHT);
                ledOn     = new Rectangle(0.125 * WIDTH, 0.3125 * HEIGHT, 0.75 * WIDTH, 0.375 * HEIGHT);
                HIGHLIGHT = new Rectangle(0.125 * WIDTH, 0.3125 * HEIGHT, 0.75 * WIDTH, 0.1875 * HEIGHT);
                break;
            case ROUND:
            default:
                LED_FRAME = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4375 * WIDTH);
                LED_OFF   = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.3125 * WIDTH);
                ledOn     = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.3125 * WIDTH);
                HIGHLIGHT = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.2 * WIDTH);
                break;
        }

        LED_FRAME.getStyleClass().add("frame");

        LED_OFF.getStyleClass().clear();
        LED_OFF.getStyleClass().add("off");
        LED_OFF.setStyle("-fx-led: " + Util.colorToCssColor(control.getColor()));

        ledOn.getStyleClass().clear();
        ledOn.getStyleClass().add("on");
        ledOn.setStyle("-fx-led: " + Util.colorToCssColor(control.getColor()));
        ledOn.setVisible(control.isOn());

        HIGHLIGHT.getStyleClass().add("highlight");

        if (LED_FRAME.visibleProperty().isBound()) {
            LED_FRAME.visibleProperty().unbind();
        }
        LED_FRAME.visibleProperty().bind(control.frameVisibleProperty());

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.180 * SIZE);
        INNER_SHADOW.setHeight(0.180 * SIZE);
        INNER_SHADOW.setRadius(0.15 * SIZE);
        INNER_SHADOW.setColor(Color.BLACK);
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        LED_OFF.setEffect(INNER_SHADOW);

        final DropShadow GLOW = new DropShadow();
        GLOW.setSpread(0.35);
        GLOW.setRadius(0.16 * ledOn.getLayoutBounds().getWidth());
        GLOW.setColor(control.getColor());
        GLOW.setBlurType(BlurType.GAUSSIAN);
        GLOW.setInput(INNER_SHADOW);
        ledOn.setEffect(GLOW);

        led.getChildren().setAll(IBOUNDS,
                                 LED_FRAME,
                                 LED_OFF,
                                 ledOn,
                                 HIGHLIGHT);

        led.setCache(true);
    }
}

