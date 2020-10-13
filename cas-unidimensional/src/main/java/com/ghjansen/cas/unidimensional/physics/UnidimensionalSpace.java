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

import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidDimensionalAmountException;
import com.ghjansen.cas.core.exception.InvalidDimensionalSpaceException;
import com.ghjansen.cas.core.exception.InvalidInitialConditionException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;
import com.ghjansen.cas.core.physics.Cell;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalCombination;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public final class UnidimensionalSpace extends Space {

	public UnidimensionalSpace(Time time, List<?> initialCondition) throws InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException {
		super(time, initialCondition, true);
	}

	@Override
	protected Combination getCombination(Time time, List<?> space) throws InvalidStateException {
		final int referencePosition = time.getRelative().get(0).getAbsolute();
		final int lastPosition = time.getRelative().get(0).getLimit() - 1;
		State leftCellState, referenceCellState, rightCellState;
		leftCellState = getLeftCellState(referencePosition, lastPosition, space);
		referenceCellState = getReferenceCellState(referencePosition, space);
		rightCellState = getRightCellState(referencePosition, lastPosition, space);
		UnidimensionalCombination uc = new UnidimensionalCombination(referenceCellState, leftCellState, rightCellState);
		return uc;
	}

	private State getLeftCellState(final int referencePosition, final int lastPosition, final List<?> space) {
		State leftCellState;
		if (referencePosition == 0) {
			Cell leftCell = (Cell) space.get(lastPosition);
			leftCellState = leftCell.getState();
		} else {
			Cell leftCell = (Cell) space.get(referencePosition - 1);
			leftCellState = leftCell.getState();
		}
		return leftCellState;
	}

	private State getReferenceCellState(final int referencePosition, final List<?> space) {
		Cell referenceCell = (Cell) space.get(referencePosition);
		return referenceCell.getState();
	}

	private State getRightCellState(final int referencePosition, final int lastPosition, final List<?> space) {
		State rightCellState;
		if (referencePosition == lastPosition) {
			Cell rightCell = (Cell) space.get(0);
			rightCellState = rightCell.getState();
		} else {
			Cell rightCell = (Cell) space.get(referencePosition + 1);
			rightCellState = rightCell.getState();
		}
		return rightCellState;
	}

	@Override
	protected void initialize() {
		if (super.keepHistory) {
			super.history = new ArrayList<List<?>>();
		}
		super.current = new ArrayList<Cell>();
	}

	@Override
	protected void createNewIteration(Time time) {
		this.current = new ArrayList<Cell>();
	}

	@Override
	protected void createNewCell(Time time, Transition transition) throws InvalidTransitionException {
		// time parameter is not used in this method because dimensional access
		// is linear for one dimension, but it should be used for more than one
		// dimension
		Cell cell = new UnidimensionalCell(transition);
		((ArrayList<Cell>) current).add(cell);
	}

}
