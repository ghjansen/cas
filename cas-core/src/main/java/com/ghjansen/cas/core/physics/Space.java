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
public abstract class Space {

	protected List<?> initial;
	protected List<List> history;
	protected List<?> last;
	protected List<?> current;
	protected int dimensionalAmount;
	protected boolean keepHistory;
	protected boolean rotating;

	public Space(Time time, List<?> initialCondition, boolean keepHistory)
			throws InvalidDimensionalAmountException, InvalidInitialConditionException, InvalidDimensionalSpaceException {
		if (time.getRelative() != null && time.getRelative().size() > 0) {
			this.dimensionalAmount = time.getRelative().size();
		} else {
			throw new InvalidDimensionalAmountException();
		}
		if (initialCondition != null && initialCondition.size() > 0) {
			validateDimensions(initialCondition, this.dimensionalAmount);
			this.initial = initialCondition;
		} else {
			throw new InvalidInitialConditionException();
		}
		this.keepHistory = keepHistory;
		this.rotating = false;
		initialize();
	}

	protected void validateDimensions(List<?> space, int dimensionalAmount) throws InvalidDimensionalSpaceException {
		if (dimensionalAmount == 1) {
			if (space.size() > 0 && !(space.get(0) instanceof Cell)) {
				throw new InvalidDimensionalSpaceException();
			}
		} else if (space.size() > 0 && space.get(0) instanceof List) {
			validateDimensions((List) space.get(0), dimensionalAmount--);
		} else {
			throw new InvalidDimensionalSpaceException();
		}
	}

	public Combination getCombination(Time time) throws InvalidStateException {
		if (time.getAbsolute() == 0) {
			return getCombination(time, this.initial);
		} else {
			return getCombination(time, this.last);
		}
	}

	public void setState(Time time, Transition transition) throws InvalidTransitionException {
		if (this.rotating) {
			this.rotating = false;
			createNewIteration(time);
		}
		createNewCell(time, transition);
		if (needsNewIteration(time)) {
			rotate();
		}

	}

	private boolean needsNewIteration(Time time) {
		for (Time r : time.getRelative()) {
			if (time.getAbsolute() == time.getLimit() - 1 || r.getAbsolute() != r.getLimit() - 1) {
				return false;
			}
		}
		return true;
	}

	private void rotate() {
		this.rotating = true;
		ArrayList currentClone = (ArrayList) ((ArrayList) this.current).clone();
		if (this.keepHistory) {
			this.history.add(currentClone);
		}
		this.last = currentClone;
	}

	public List<?> getInitial() {
		return this.initial;
	}

	public List<List> getHistory() {
		return this.history;
	}

	public List<?> getLast() {
		return this.last;
	}

	public List<?> getCurrent() {
		return this.current;
	}

	public int getDimensionalAmout() {
		return this.dimensionalAmount;
	}

	public boolean isKeepHistory() {
		return this.keepHistory;
	}
	
	protected abstract void initialize();
	
	protected abstract Combination getCombination(Time time, List<?> space) throws InvalidStateException;

	protected abstract void createNewIteration(Time time);

	protected abstract void createNewCell(Time time, Transition transition) throws InvalidTransitionException;

}
