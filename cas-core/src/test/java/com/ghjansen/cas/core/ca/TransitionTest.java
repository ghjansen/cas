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

package com.ghjansen.cas.core.ca;

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidStateException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class TransitionTest {

	@Test
	public void dimensionalTransitionConstructor() throws InvalidStateException, InvalidCombinationException {
		final DimensionalState dimensionalBlackState = new DimensionalState("black", 0);
		final DimensionalState dimensionalWhiteState = new DimensionalState("white", 1);
		final DimensionalCombination dimensionalCombination = new DimensionalCombination(dimensionalBlackState,
				dimensionalWhiteState, dimensionalBlackState);
		final DimensionalTransition dimensionalTransition = new DimensionalTransition(dimensionalCombination,
				dimensionalWhiteState);
		Assert.assertTrue(dimensionalTransition.getCombination().equals(dimensionalCombination));
		Assert.assertTrue(dimensionalTransition.getState().equals(dimensionalWhiteState));
	}

	@Test(expected = InvalidCombinationException.class)
	public void dimensionalTransitionConstructorInvalidCombination() throws InvalidStateException, InvalidCombinationException {
		final DimensionalState dimensionalWhiteState = new DimensionalState("white", 1);
		new DimensionalTransition(null, dimensionalWhiteState);
	}

	@Test(expected = InvalidStateException.class)
	public void dimensionalTransitionConstructorInvalidState() throws InvalidStateException, InvalidCombinationException {
		final DimensionalState dimensionalBlackState = new DimensionalState("black", 0);
		final DimensionalState dimensionalWhiteState = new DimensionalState("white", 1);
		final DimensionalCombination dimensionalCombination = new DimensionalCombination(dimensionalBlackState,
				dimensionalWhiteState, dimensionalBlackState);
		new DimensionalTransition(dimensionalCombination, null);
	}

}
