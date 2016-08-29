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

import com.ghjansen.cas.core.physics.exception.space.InvalidInitialCondition;
import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.physics.exception.space.InvalidDimensionalAmount;
import com.ghjansen.cas.core.physics.exception.space.InvalidDimensionalSpace;

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

	protected Space(Time time, List<?> initialCondition, boolean keepHistory)
			throws InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace {
		if (time.getRelative() != null && time.getRelative().size() > 0) {
			this.dimensionalAmount = time.getRelative().size();
		} else {
			throw new InvalidDimensionalAmount();
		}
		if (initialCondition != null && initialCondition.size() > 0) {
			validateDimensions(initialCondition, this.dimensionalAmount);
		} else {
			throw new InvalidInitialCondition();
		}
		this.keepHistory = keepHistory;
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
	
	public Combination getCombination(Time time){
		if(time.getAbsolute() == 0){
			return getCombination(time, this.initial);
		} else {
			return getCombination(time, this.last);
		}
	}
	
	protected abstract Combination getCombination(Time time, List<?> space);

}
