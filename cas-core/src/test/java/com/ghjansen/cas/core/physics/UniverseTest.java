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

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.DimensionalCombination;
import com.ghjansen.cas.core.ca.DimensionalState;
import com.ghjansen.cas.core.ca.DimensionalTransition;
import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidAbsoluteTimeLimitException;
import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidDimensionalAmountException;
import com.ghjansen.cas.core.exception.InvalidDimensionalSpaceException;
import com.ghjansen.cas.core.exception.InvalidInitialConditionException;
import com.ghjansen.cas.core.exception.InvalidRelativeTimeLimitException;
import com.ghjansen.cas.core.exception.InvalidSpaceException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTimeException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UniverseTest {

	@Test
	public void dimensionalUniverseConstructor() throws CloneNotSupportedException, InvalidAbsoluteTimeLimitException,
			InvalidRelativeTimeLimitException, InvalidStateException, InvalidTransitionException, InvalidCombinationException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, InvalidSpaceException, InvalidTimeException {
		final Time dimensionalTime = new DimensionalTime(1000, 1000);
		final Cell dimensionalCell = getNewValidDimensionalCell();
		final ArrayList<Cell> firstDimension = new ArrayList<Cell>();
		firstDimension.add(dimensionalCell);
		final Space dimensionalSpace = new DimensionalSpace(dimensionalTime, firstDimension, true);
		Universe dimensionalUniverse = new DimensionalUniverse(dimensionalSpace, dimensionalTime);
		Assert.assertTrue(dimensionalUniverse.getSpace().equals(dimensionalSpace));
		Assert.assertTrue(dimensionalUniverse.getTime().equals(dimensionalTime));
	}
	
	@Test(expected = InvalidSpaceException.class)
	public void dimensionalUniverseConstructorInvalidSpace() throws CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidSpaceException, InvalidTimeException{
		final Time dimensionalTime = new DimensionalTime(1000, 1000);
		new DimensionalUniverse(null, dimensionalTime);
	}
	
	@Test(expected = InvalidTimeException.class)
	public void dimensionalUniverseConstructorInvalidTime() throws CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidStateException, InvalidTransitionException, InvalidCombinationException, InvalidDimensionalAmountException, InvalidInitialConditionException, InvalidDimensionalSpaceException, InvalidSpaceException, InvalidTimeException{
		final Time dimensionalTime = new DimensionalTime(1000, 1000);
		final Cell dimensionalCell = getNewValidDimensionalCell();
		final ArrayList<Cell> firstDimension = new ArrayList<Cell>();
		firstDimension.add(dimensionalCell);
		final Space dimensionalSpace = new DimensionalSpace(dimensionalTime, firstDimension, true);
		new DimensionalUniverse(dimensionalSpace, null);
	}

	private Cell getNewValidDimensionalCell() throws InvalidStateException, InvalidTransitionException, InvalidCombinationException {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		final State dimensionalWhiteState = new DimensionalState("white", 1);
		final Combination dimensionalCombination = new DimensionalCombination(dimensionalWhiteState,
				dimensionalBlackState, dimensionalBlackState);
		final Transition dimensionalTransition = new DimensionalTransition(dimensionalCombination,
				dimensionalBlackState);
		return new DimensionalCell(dimensionalTransition);
	}

}
