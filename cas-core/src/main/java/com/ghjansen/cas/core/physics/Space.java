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

import java.util.List;

import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.physics.exception.space.InvalidDimensionalAmount;
import com.ghjansen.cas.core.physics.exception.space.InvalidDimensionalSpace;
import com.ghjansen.cas.core.physics.exception.space.InvalidInitialCondition;

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
			throws InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace {
		if (time.getRelative() != null && time.getRelative().size() > 0) {
			this.dimensionalAmount = time.getRelative().size();
		} else {
			throw new InvalidDimensionalAmount();
		}
		if (initialCondition != null && initialCondition.size() > 0) {
			validateDimensions(initialCondition, this.dimensionalAmount);
			this.initial = initialCondition;
		} else {
			throw new InvalidInitialCondition();
		}
		this.keepHistory = keepHistory;
		this.rotating = false;
		// initialize(this.history, this.current, this.keepHistory);
		initialize();
	}

	protected void validateDimensions(List<?> space, int dimensionalAmount) throws InvalidDimensionalSpace {
		if (dimensionalAmount == 1) {
			if (space.size() > 0 && !(space.get(0) instanceof Cell)) {
				throw new InvalidDimensionalSpace();
			}
		} else if (space.size() > 0 && space.get(0) instanceof List) {
			validateDimensions((List) space.get(0), dimensionalAmount--);
		} else {
			throw new InvalidDimensionalSpace();
		}
	}

	public Combination getCombination(Time time) {
		if (time.getAbsolute() == 0) {
			return getCombination(time, this.initial);
		} else {
			return getCombination(time, this.last);
		}
	}

	protected abstract Combination getCombination(Time time, List<?> space);

	public void setState(Time time, Transition transition) {
		/*
		if (isNewIteration(time)) {
			// createNewIteration(time, this.history, this.last, this.current,
			// this.keepHistory);
			createNewIteration(time);
		}*/
		// createNewCell(time, transition, this.current);
		if(this.rotating){
			this.rotating = false;
			createNewIteration(time);
		}
		createNewCell(time, transition);
		if(isEndOfIteration(time)){
			rotate();
			createNewIteration(time);
		}
	}

	private boolean isNewIteration(Time time) {
		for (Time r : time.getRelative()) {
			if (r.getAbsolute() != 0) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isEndOfIteration(Time time){
		for (Time r : time.getRelative()) {
			if (r.getAbsolute() != r.getLimit() - 1) {
				return false;
			}
		}
		return false;
	}
	
	private void rotate(){
		this.rotating = true;
		List currentClone = this.current.subList(0, this.current.size());
		if (this.keepHistory) {
			this.history.add(currentClone);
		}
		this.last = currentClone;
	}

	// protected abstract void initialize(List<List> history, List<?> current,
	// boolean keepHistory);
	protected abstract void initialize();

	// protected abstract void createNewIteration(Time time, List<List> history,
	// List<?> last, List<?> current,
	// boolean keepHistory);
	protected abstract void createNewIteration(Time time);

	// protected abstract void createNewCell(Time time, Transition transition,
	// List<?> current);
	protected abstract void createNewCell(Time time, Transition transition);

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

}
