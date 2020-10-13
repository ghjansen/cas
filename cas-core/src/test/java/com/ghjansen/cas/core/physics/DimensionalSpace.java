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

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class DimensionalSpace extends Space {

	public DimensionalSpace(Time time, List<?> initialCondition, boolean keepHistory)
			throws InvalidDimensionalAmountException, InvalidInitialConditionException, InvalidDimensionalSpaceException {
		super(time, initialCondition, keepHistory);
	}

	@Override
	protected Combination getCombination(Time time, List<?> space) {
		// Since this method is declared as abstract in core module, its
		// implementation and tests should be created inside each dimensional
		// module. The implementation here (if any) is used since required to
		// test other structures.
		return null;
	}

	@Override
	protected void initialize() {
		// Since this method is declared as abstract in core module, its
		// implementation and tests should be created inside each dimensional
		// module. The implementation here (if any) is used since required to
		// test other structures.
		if (super.keepHistory) {
			super.history = new ArrayList<List<?>>();
		}
		super.current = new ArrayList<Cell>();
	}

	@Override
	protected void createNewIteration(Time time) {
		// Since this method is declared as abstract in core module, its
		// implementation and tests should be created inside each dimensional
		// module. The implementation here (if any) is used since required to
		// test other structures.
	}

	@Override
	protected void createNewCell(Time time, Transition transition) {
		// Since this method is declared as abstract in core module, its
		// implementation and tests should be created inside each dimensional
		// module. The implementation here (if any) is used since required to
		// test other structures.
	}

}
