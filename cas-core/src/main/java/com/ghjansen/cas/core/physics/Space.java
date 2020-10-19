/*
 * CAS - Cellular Automata Simulator
 * Copyright (C) 2016  Guilherme Humberto Jansen
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

package com.ghjansen.cas.core.physics;

import java.util.ArrayList;
import java.util.List;

import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidDimensionalAmountException;
import com.ghjansen.cas.core.exception.InvalidDimensionalSpaceException;
import com.ghjansen.cas.core.exception.InvalidInitialConditionException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Space<X, T extends Time, N extends Transition, O extends Combination> {

    protected List<X> initial;
    protected List<List<X>> history;
    protected List<X> last;
    protected List<X> current;
    protected int dimensionalAmount;
    protected boolean keepHistory;
    protected boolean rotating;

    protected Space(T time, List<X> initialCondition, boolean keepHistory)
            throws InvalidDimensionalAmountException, InvalidInitialConditionException, InvalidDimensionalSpaceException {
        if (time.getRelative() != null && !time.getRelative().isEmpty()) {
            this.dimensionalAmount = time.getRelative().size();
        } else {
            throw new InvalidDimensionalAmountException();
        }
        if (initialCondition != null && !initialCondition.isEmpty()) {
            validateDimensions(initialCondition, this.dimensionalAmount);
            this.initial = initialCondition;
        } else {
            throw new InvalidInitialConditionException();
        }
        this.keepHistory = keepHistory;
        this.rotating = false;
        initialize();
    }

    protected void validateDimensions(List<X> space, int dimensionalAmount) throws InvalidDimensionalSpaceException {
        if (dimensionalAmount == 1) {
            if (!space.isEmpty() && !(space.get(0) instanceof Cell)) {
                throw new InvalidDimensionalSpaceException();
            }
        } else if (!space.isEmpty() && space.get(0) instanceof List) {
            validateDimensions((List) space.get(0), dimensionalAmount--);
        } else {
            throw new InvalidDimensionalSpaceException();
        }
    }

    public O getCombination(T time) throws InvalidStateException {
        if (time.getAbsolute() == 0) {
            return getCombination(time, this.initial);
        } else {
            return getCombination(time, this.last);
        }
    }

    public void setState(T time, N transition) throws InvalidTransitionException {
        if (this.rotating) {
            this.rotating = false;
            createNewIteration(time);
        }
        createNewCell(time, transition);
        if (needsNewIteration(time)) {
            rotate();
        }

    }

    private boolean needsNewIteration(T time) {
        for (Time r : time.getRelative()) {
            if (time.getAbsolute() == time.getLimit() - 1 || r.getAbsolute() != r.getLimit() - 1) {
                return false;
            }
        }
        return true;
    }

    private void rotate() {
        this.rotating = true;
        ArrayList<X> currentClone = (ArrayList) ((ArrayList) this.current).clone();
        if (this.keepHistory) {
            this.history.add(currentClone);
        }
        this.last = currentClone;
    }

    public List<X> getInitial() {
        return this.initial;
    }

    public List<List<X>> getHistory() {
        return this.history;
    }

    public List<X> getLast() {
        return this.last;
    }

    public List<X> getCurrent() {
        return this.current;
    }

    public int getDimensionalAmount() {
        return this.dimensionalAmount;
    }

    public boolean isKeepHistory() {
        return this.keepHistory;
    }

    protected abstract void initialize();

    protected abstract O getCombination(T time, List<X> space) throws InvalidStateException;

    protected abstract void createNewIteration(T time);

    protected abstract void createNewCell(T time, N transition) throws InvalidTransitionException;

}
