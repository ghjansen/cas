/*
 * CAS - Cellular Automata Simulator
 * Copyright (C) 2020  Guilherme Humberto Jansen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ghjansen.cas.ui.desktop.processing;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class ScaleCalibration {

    private float emptySpaceX;
    private float emptySpaceY;
    private double deltaScaleLimitX;
    private double deltaScaleLimitY;
    private float newScale;

    public ScaleCalibration(float emptySpaceX, float emptySpaceY, double deltaScaleLimitX, double deltaScaleLimitY, float newScale) {
        this.emptySpaceX = emptySpaceX;
        this.emptySpaceY = emptySpaceY;
        this.deltaScaleLimitX = deltaScaleLimitX;
        this.deltaScaleLimitY = deltaScaleLimitY;
        this.newScale = newScale;
    }

    public float getEmptySpaceX() {
        return emptySpaceX;
    }

    public void setEmptySpaceX(float emptySpaceX) {
        this.emptySpaceX = emptySpaceX;
    }

    public float getEmptySpaceY() {
        return emptySpaceY;
    }

    public void setEmptySpaceY(float emptySpaceY) {
        this.emptySpaceY = emptySpaceY;
    }

    public double getDeltaScaleLimitX() {
        return deltaScaleLimitX;
    }

    public void setDeltaScaleLimitX(double deltaScaleLimitX) {
        this.deltaScaleLimitX = deltaScaleLimitX;
    }

    public double getDeltaScaleLimitY() {
        return deltaScaleLimitY;
    }

    public void setDeltaScaleLimitY(double deltaScaleLimitY) {
        this.deltaScaleLimitY = deltaScaleLimitY;
    }

    public float getNewScale() {
        return newScale;
    }

    public void setNewScale(float newScale) {
        this.newScale = newScale;
    }
}
