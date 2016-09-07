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

import com.ghjansen.cas.core.exception.InvalidState;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class CombinationTest {

	@Test
	public void dimensionalCombinationConstructor() throws InvalidState {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		final State dimensionalWhiteState = new DimensionalState("white", 1);
		final Combination dimensionalCombination = new DimensionalCombination(dimensionalBlackState,
				dimensionalWhiteState, dimensionalBlackState);
		Assert.assertTrue(dimensionalCombination.getReferenceState().equals(dimensionalBlackState));
		Assert.assertTrue(dimensionalCombination.getNeighborhood().get(0).equals(dimensionalWhiteState));
		Assert.assertTrue(dimensionalCombination.getNeighborhood().get(1).equals(dimensionalBlackState));
	}

	@Test(expected = InvalidState.class)
	public void dimensionalCombinationConstructorInvalidReference() throws InvalidState {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		final State dimensionalWhiteState = new DimensionalState("white", 1);
		new DimensionalCombination(null, dimensionalWhiteState, dimensionalBlackState);
	}

	@Test(expected = InvalidState.class)
	public void dimensionalCombinationConstructorInvalidNeighborhood() throws InvalidState {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		new DimensionalCombination(dimensionalBlackState, null);
	}

}
