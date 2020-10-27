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

package com.ghjansen.cas.unidimensional.physics;

import java.util.ArrayList;
import java.util.List;

import com.ghjansen.cas.core.exception.InvalidDimensionalAmountException;
import com.ghjansen.cas.core.exception.InvalidDimensionalSpaceException;
import com.ghjansen.cas.core.exception.InvalidInitialConditionException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalCombination;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalState;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalTransition;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public final class UnidimensionalSpace extends Space<UnidimensionalCell, UnidimensionalTime, UnidimensionalTransition, UnidimensionalCombination> {

    public UnidimensionalSpace(UnidimensionalTime time, List<UnidimensionalCell> initialCondition) throws InvalidDimensionalAmountException,
            InvalidInitialConditionException, InvalidDimensionalSpaceException {
        super(time, initialCondition, true);
    }

    @Override
    protected UnidimensionalCombination getCombination(UnidimensionalTime time, List<UnidimensionalCell> space) throws InvalidStateException {
        final int referencePosition = time.getRelative().get(0).getAbsolute();
        final int lastPosition = time.getRelative().get(0).getLimit() - 1;
        UnidimensionalState leftCellState = getLeftCellState(referencePosition, lastPosition, space);
        UnidimensionalState referenceCellState = getReferenceCellState(referencePosition, space);
        UnidimensionalState rightCellState = getRightCellState(referencePosition, lastPosition, space);
        return new UnidimensionalCombination(referenceCellState, leftCellState, rightCellState);
    }

    private UnidimensionalState getLeftCellState(final int referencePosition, final int lastPosition, final List<UnidimensionalCell> space) {
        UnidimensionalState leftCellState;
        if (referencePosition == 0) {
            UnidimensionalCell leftCell = space.get(lastPosition);
            leftCellState = leftCell.getState();
        } else {
            UnidimensionalCell leftCell = space.get(referencePosition - 1);
            leftCellState = leftCell.getState();
        }
        return leftCellState;
    }

    private UnidimensionalState getReferenceCellState(final int referencePosition, final List<UnidimensionalCell> space) {
        UnidimensionalCell referenceCell = space.get(referencePosition);
        return referenceCell.getState();
    }

    private UnidimensionalState getRightCellState(final int referencePosition, final int lastPosition, final List<UnidimensionalCell> space) {
        UnidimensionalState rightCellState;
        if (referencePosition == lastPosition) {
            UnidimensionalCell rightCell = space.get(0);
            rightCellState = rightCell.getState();
        } else {
            UnidimensionalCell rightCell = space.get(referencePosition + 1);
            rightCellState = rightCell.getState();
        }
        return rightCellState;
    }

    @Override
    protected void initialize() {
        if (super.keepHistory) {
            super.history = new ArrayList<List<UnidimensionalCell>>();
        }
        super.current = new ArrayList<UnidimensionalCell>();
    }

    @Override
    protected void createNewIteration(UnidimensionalTime time) {
        this.current = new ArrayList<UnidimensionalCell>();
    }

    @Override
    protected void createNewCell(UnidimensionalTime time, UnidimensionalTransition transition) throws InvalidTransitionException {
        // time parameter is not used in this method because dimensional access
        // is linear for one dimension, but it should be used for more than one
        // dimension
        UnidimensionalCell cell = new UnidimensionalCell(transition);
        current.add(cell);
    }

}
