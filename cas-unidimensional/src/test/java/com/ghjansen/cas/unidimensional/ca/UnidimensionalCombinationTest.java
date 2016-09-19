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

package com.ghjansen.cas.unidimensional.ca;

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.exception.InvalidStateException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalCombinationTest {

	@Test
	public void unidimensionalCombinationConstructor() throws InvalidStateException {
		final State unidimensionalBlackState = new UnidimensionalState("black", 0);
		final State unidimensionalWhiteState = new UnidimensionalState("white", 1);
		final Combination unidimensionalCombination0 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalBlackState);
		final Combination unidimensionalCombination1 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final Combination unidimensionalCombination2 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalBlackState);
		final Combination unidimensionalCombination3 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final Combination unidimensionalCombination4 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalBlackState);
		final Combination unidimensionalCombination5 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final Combination unidimensionalCombination6 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalBlackState);
		final Combination unidimensionalCombination7 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		Assert.assertTrue(unidimensionalCombination0.getReferenceState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination0.getNeighborhood().get(0).equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination0.getNeighborhood().get(1).equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination1.getReferenceState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination1.getNeighborhood().get(0).equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination1.getNeighborhood().get(1).equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination2.getReferenceState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination2.getNeighborhood().get(0).equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination2.getNeighborhood().get(1).equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination3.getReferenceState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination3.getNeighborhood().get(0).equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination3.getNeighborhood().get(1).equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination4.getReferenceState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination4.getNeighborhood().get(0).equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination4.getNeighborhood().get(1).equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination5.getReferenceState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination5.getNeighborhood().get(0).equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination5.getNeighborhood().get(1).equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination6.getReferenceState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination6.getNeighborhood().get(0).equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination6.getNeighborhood().get(1).equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalCombination7.getReferenceState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination7.getNeighborhood().get(0).equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalCombination7.getNeighborhood().get(1).equals(unidimensionalWhiteState));
	}

}
