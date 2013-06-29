/*
 * Copyright (c) 2012, JFXtras
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *       * Neither the name of the <organization> nor the
 *         names of its contributors may be used to endorse or promote products
 *         derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.internal.scene.control.skin;

import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.internal.scene.control.behavior.SimpleIndicatorBehavior;
import jfxtras.labs.scene.control.gauge.SimpleIndicator;
import jfxtras.labs.util.Util;


/**
 * Created by
 * User: hansolo
 * Date: 06.03.12
 * Time: 13:53
 */
public class SimpleIndicatorSkin extends com.sun.javafx.scene.control.skin.BehaviorSkinBase<SimpleIndicator, SimpleIndicatorBehavior> {
    private static final double      PREFERRED_WIDTH  = 200;
    private static final double      PREFERRED_HEIGHT = 200;
    private static final double      MINIMUM_WIDTH    = 25;
    private static final double      MINIMUM_HEIGHT   = 25;
    private static final double      MAXIMUM_WIDTH    = 1024;
    private static final double      MAXIMUM_HEIGHT   = 1024;
    private SimpleIndicator control;
    private boolean         isDirty;
    private boolean         initialized;
    private Group           indicator;
    private Circle          main;
    private DropShadow      mainGlow;


    // ******************** Constructors **************************************
    public SimpleIndicatorSkin(final SimpleIndicator CONTROL) {
        super(CONTROL, new SimpleIndicatorBehavior(CONTROL));
        control     = CONTROL;
        initialized = false;
        isDirty     = false;
        indicator   = new Group();

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

        // Register listeners
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");
        registerChangeListener(control.innerColorProperty(), "INNER_COLOR");
        registerChangeListener(control.outerColorProperty(), "OUTER_COLOR");
        registerChangeListener(control.glowVisibleProperty(), "GLOW_VISIBILITY");

        initialized = true;
        repaint();
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("INNER_COLOR".equals(PROPERTY)) {
            updateIndicator();
        } else if ("OUTER_COLOR".equals(PROPERTY)) {
            updateIndicator();
        } else if ("GLOW_VISIBILITY".equals(PROPERTY)) {
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
        if (!isDirty) {
            return;
        }
        if (!initialized) {
            init();
        }
        if (control.getScene() != null) {
            drawIndicator();
            getChildren().setAll(indicator);
        }
        isDirty = false;
    }

    public final SimpleIndicator getControl() {
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
    private void updateIndicator() {
        main.setStyle("-fx-indicator-inner-color: " + Util.colorToCssColor(control.getInnerColor()) +
                      "-fx-indicator-outer-color: " + Util.colorToCssColor(control.getOuterColor()));
        mainGlow.setColor(control.getInnerColor());
    }

    private final void drawIndicator() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        indicator.setStyle("-fx-indicator-inner-color: " + Util.colorToCssColor(control.getInnerColor()) +
                           "-fx-indicator-outer-color: " + Util.colorToCssColor(control.getOuterColor()));

        indicator.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        indicator.getChildren().add(IBOUNDS);

        final Circle OUTER_FRAME = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.496 * WIDTH);
        OUTER_FRAME.getStyleClass().add("indicator-outer-frame-fill");
        OUTER_FRAME.setStroke(null);

        final InnerShadow OUTER_FRAME_INNER_SHADOW = new InnerShadow();
        OUTER_FRAME_INNER_SHADOW.setWidth(0.05 * OUTER_FRAME.getLayoutBounds().getWidth());
        OUTER_FRAME_INNER_SHADOW.setHeight(0.05 * OUTER_FRAME.getLayoutBounds().getHeight());
        OUTER_FRAME_INNER_SHADOW.setOffsetX(0.0);
        OUTER_FRAME_INNER_SHADOW.setOffsetY(0.0);
        OUTER_FRAME_INNER_SHADOW.setRadius(0.05 * OUTER_FRAME.getLayoutBounds().getWidth());
        OUTER_FRAME_INNER_SHADOW.setColor(Color.color(0, 0, 0, 0.9));
        OUTER_FRAME_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        OUTER_FRAME_INNER_SHADOW.inputProperty().set(null);
        OUTER_FRAME.setEffect(OUTER_FRAME_INNER_SHADOW);

        final Circle INNER_FRAME = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4 * WIDTH);
        INNER_FRAME.getStyleClass().add("indicator-inner-frame-fill");

        main = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.38 * WIDTH);
        main.setStyle("-fx-indicator-inner-color: " + Util.colorToCssColor(control.getInnerColor()) +
                      "-fx-indicator-outer-color: " + Util.colorToCssColor(control.getOuterColor()));
        main.getStyleClass().add("indicator-main-fill");

        final InnerShadow MAIN_INNER_SHADOW = new InnerShadow();
        MAIN_INNER_SHADOW.setWidth(0.2880 * main.getLayoutBounds().getWidth());
        MAIN_INNER_SHADOW.setHeight(0.2880 * main.getLayoutBounds().getHeight());
        MAIN_INNER_SHADOW.setOffsetX(0.0);
        MAIN_INNER_SHADOW.setOffsetY(0.0);
        MAIN_INNER_SHADOW.setRadius(0.2880 * main.getLayoutBounds().getWidth());
        MAIN_INNER_SHADOW.setColor(Color.BLACK);
        MAIN_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        mainGlow = new DropShadow();
        mainGlow.setWidth(0.2880 * main.getLayoutBounds().getWidth());
        mainGlow.setHeight(0.2880 * main.getLayoutBounds().getHeight());
        mainGlow.setOffsetX(0.0);
        mainGlow.setOffsetY(0.0);
        mainGlow.setRadius(0.2880 * main.getLayoutBounds().getWidth());
        mainGlow.setColor(control.getInnerColor());
        mainGlow.setBlurType(BlurType.GAUSSIAN);
        mainGlow.inputProperty().set(MAIN_INNER_SHADOW);
        if (control.isGlowVisible()) {
            main.setEffect(mainGlow);
        } else {
            main.setEffect(MAIN_INNER_SHADOW);
        }

        final Ellipse HIGHLIGHT = new Ellipse(0.504 * WIDTH, 0.294 * HEIGHT,
                                              0.26 * WIDTH, 0.15 * HEIGHT);
        HIGHLIGHT.getStyleClass().add("indicator-highlight-fill");

        indicator.getChildren().addAll(OUTER_FRAME,
                                       INNER_FRAME,
                                       main,
                                       HIGHLIGHT);
        indicator.setCache(true);
    }
}

