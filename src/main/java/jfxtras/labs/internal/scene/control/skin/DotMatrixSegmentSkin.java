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
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.internal.scene.control.behavior.DotMatrixSegmentBehavior;
import jfxtras.labs.scene.control.gauge.DotMatrixSegment;
import jfxtras.labs.util.Util;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by
 * User: hansolo
 * Date: 15.03.12
 * Time: 12:15
 */
public class DotMatrixSegmentSkin extends com.sun.javafx.scene.control.skin.BehaviorSkinBase<DotMatrixSegment, DotMatrixSegmentBehavior> {
    private static final double      PREFERRED_WIDTH  = 40;
    private static final double      PREFERRED_HEIGHT = 56;
    private static final double      MINIMUM_WIDTH    = 10;
    private static final double      MINIMUM_HEIGHT   = 14;
    private static final double      MAXIMUM_WIDTH    = 400;
    private static final double      MAXIMUM_HEIGHT   = 560;
    private DotMatrixSegment                 control;
    private boolean                          isDirty;
    private boolean                          initialized;
    private Group                            dots;
    private Map<DotMatrixSegment.Dot, Shape> dotMap;


    // ******************** Constructors **************************************
    public DotMatrixSegmentSkin(final DotMatrixSegment CONTROL) {
        super(CONTROL, new DotMatrixSegmentBehavior(CONTROL));
        control     = CONTROL;
        initialized = false;
        isDirty     = false;
        dots        = new Group();
        dotMap      = new HashMap<DotMatrixSegment.Dot, Shape>(17);

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

        createDots();
        updateCharacter();

        // Register listeners
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");
        registerChangeListener(control.characterProperty(), "CHARACTER");
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.plainColorProperty(), "PLAIN_COLOR");
        registerChangeListener(control.customDotMappingProperty(), "CUSTOM_MAPPING");
        registerChangeListener(control.dotOnProperty(), "DOT_ON");

        initialized = true;
        repaint();
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

        if ("CHARACTER".equals(PROPERTY)) {
            updateCharacter();
        } else if ("COLOR".equals(PROPERTY)) {
           updateSegmentColor();
        } else if ("PLAIN_COLOR".equals(PROPERTY)) {
            updateCharacter();
        } else if ("CUSTOM_MAPPING".equals(PROPERTY)) {
            updateCharacter();
        } else if ("DOT_ON".equals(PROPERTY)) {
            updateCharacter();
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
            updateCharacter();
            getChildren().setAll(dots);
        }
        isDirty = false;
    }

    public final DotMatrixSegment getControl() {
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
    private void updateSegmentColor() {
        control.setStyle("-fx-segment-color-on: " + Util.colorToCssColor(control.getColor()) +
                         "-fx-segment-color-off: " + Util.colorToCssColor(Color.color(control.getColor().getRed(), control.getColor().getGreen(), control.getColor().getBlue(), 0.075)));
    }

    private void updateCharacter() {
        updateSegmentColor();
        final int ASCII = control.getCharacter().isEmpty() ? 20 : control.getCharacter().toUpperCase().charAt(0);
        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setRadius(0.05 * control.getPrefWidth());
        INNER_SHADOW.setColor(Color.hsb(control.getColor().getHue(), control.getColor().getSaturation(), 0.2));

        final String ON_STYLE = control.isPlainColor() ? "plain-on" : "on";

        if (control.getCustomDotMapping().isEmpty()) {
            for (DotMatrixSegment.Dot dot : dotMap.keySet()) {
                if (control.getDotMapping().containsKey(ASCII)) {
                    if (control.getDotMapping().get(ASCII).contains(dot)) {
                        dotMap.get(dot).getStyleClass().setAll(ON_STYLE);
                        dotMap.get(dot).setEffect(INNER_SHADOW);
                    } else {
                        dotMap.get(dot).getStyleClass().setAll("off");
                        dotMap.get(dot).setEffect(null);
                    }
                } else {
                    dotMap.get(dot).getStyleClass().setAll("off");
                    dotMap.get(dot).setEffect(null);
                }
            }
        } else {
            for (DotMatrixSegment.Dot dot : dotMap.keySet()) {
                if (control.getCustomDotMapping().containsKey(ASCII)) {
                    if (control.getCustomDotMapping().get(ASCII).contains(dot)) {
                        dotMap.get(dot).getStyleClass().setAll(ON_STYLE);
                        dotMap.get(dot).setEffect(INNER_SHADOW);
                    } else {
                        dotMap.get(dot).getStyleClass().setAll("off");
                        dotMap.get(dot).setEffect(null);
                    }
                } else {
                    dotMap.get(dot).getStyleClass().setAll("off");
                    dotMap.get(dot).setEffect(null);
                }
            }
        }
    }

    private final void createDots() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        updateSegmentColor();

        dots.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        dots.getChildren().add(IBOUNDS);

        final Circle D57 = new Circle(0.8902439024390244 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D57, D57);

        final Circle D47 = new Circle(0.6951219512195121 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D47, D47);

        final Circle D37 = new Circle(0.5 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D37, D37);

        final Circle D27 = new Circle(0.3048780487804878 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D27, D27);

        final Circle D17 = new Circle(0.10975609756097561 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D17, D17);

        final Circle D56 = new Circle(0.8902439024390244 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D56, D56);

        final Circle D46 = new Circle(0.6951219512195121 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D46, D46);

        final Circle D36 = new Circle(0.5 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D36, D36);

        final Circle D26 = new Circle(0.3048780487804878 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D26, D26);

        final Circle D16 = new Circle(0.10975609756097561 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D16, D16);

        final Circle D55 = new Circle(0.8902439024390244 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D55, D55);

        final Circle D45 = new Circle(0.6951219512195121 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D45, D45);

        final Circle D35 = new Circle(0.5 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D35, D35);

        final Circle D25 = new Circle(0.3048780487804878 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D25, D25);

        final Circle D15 = new Circle(0.10975609756097561 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D15, D15);

        final Circle D54 = new Circle(0.8902439024390244 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D54, D54);

        final Circle D44 = new Circle(0.6951219512195121 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D44, D44);

        final Circle D34 = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D34, D34);

        final Circle D24 = new Circle(0.3048780487804878 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D24, D24);

        final Circle D14 = new Circle(0.10975609756097561 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D14, D14);

        final Circle D53 = new Circle(0.8902439024390244 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D53, D53);

        final Circle D43 = new Circle(0.6951219512195121 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D43, D43);

        final Circle D33 = new Circle(0.5 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D33, D33);

        final Circle D23 = new Circle(0.3048780487804878 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D23, D23);

        final Circle D13 = new Circle(0.10975609756097561 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D13, D13);

        final Circle D52 = new Circle(0.8902439024390244 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D52, D52);

        final Circle D42 = new Circle(0.6951219512195121 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D42, D42);

        final Circle D32 = new Circle(0.5 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D32, D32);

        final Circle D22 = new Circle(0.3048780487804878 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D22, D22);

        final Circle D12 = new Circle(0.10975609756097561 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D12, D12);

        final Circle D51 = new Circle(0.8902439024390244 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D51, D51);

        final Circle D41 = new Circle(0.6951219512195121 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D41, D41);

        final Circle D31 = new Circle(0.5 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D31, D31);

        final Circle D21 = new Circle(0.3048780487804878 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D21, D21);

        final Circle D11 = new Circle(0.10975609756097561 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        dotMap.put(DotMatrixSegment.Dot.D11, D11);

        for (Shape dot : dotMap.values()) {
            dot.getStyleClass().add("off");
        }

        dots.getChildren().addAll(D57,
                                 D47,
                                 D37,
                                 D27,
                                 D17,
                                 D56,
                                 D46,
                                 D36,
                                 D26,
                                 D16,
                                 D55,
                                 D45,
                                 D35,
                                 D25,
                                 D15,
                                 D54,
                                 D44,
                                 D34,
                                 D24,
                                 D14,
                                 D53,
                                 D43,
                                 D33,
                                 D23,
                                 D13,
                                 D52,
                                 D42,
                                 D32,
                                 D22,
                                 D12,
                                 D51,
                                 D41,
                                 D31,
                                 D21,
                                 D11);
    }
}
