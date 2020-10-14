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

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.DimensionalCombination;
import com.ghjansen.cas.core.ca.DimensionalState;
import com.ghjansen.cas.core.ca.DimensionalTransition;
import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class CellTest {

	@Test
	public void dimensionalCellConstructor()
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException {
		final DimensionalState dimensionalBlackState = new DimensionalState("black", 0);
		final DimensionalState dimensionalWhiteState = new DimensionalState("white", 1);
		final DimensionalCombination dimensionalCombination = new DimensionalCombination(dimensionalBlackState,
				dimensionalWhiteState, dimensionalBlackState);
		final DimensionalTransition dimensionalTransition = new DimensionalTransition(dimensionalCombination,
				dimensionalWhiteState);
		final Cell dimensionalCell = new DimensionalCell(dimensionalTransition);
		Assert.assertTrue(dimensionalCell.getTransition().equals(dimensionalTransition));
		Assert.assertTrue(dimensionalCell.getState().equals(dimensionalTransition.getState()));
	}

	@Test(expected = InvalidTransitionException.class)
	public void dimensionalCellConstructorInvalidTransition() throws InvalidTransitionException {
		DimensionalTransition t = null;
		new DimensionalCell(t);
	}

	@Test(expected = InvalidStateException.class)
	public void dimensionalCellConstructorInvalidState() throws InvalidStateException {
		DimensionalState s = null;
		new DimensionalCell(s);
	}

}
