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
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidStateException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalTransitionTest {

	@Test
	public void unidimensionalTransitionConstructor() throws InvalidStateException, InvalidCombinationException {
		final State unidimensionalBlackState = new UnidimensionalState("black", 0);
		final State unidimensionalWhiteState = new UnidimensionalState("white", 1);
		final Combination unidimensionalCombination0 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalBlackState);
		final Transition unidimensionalTransition0 = new UnidimensionalTransition(unidimensionalCombination0,
				unidimensionalWhiteState);
		final Combination unidimensionalCombination1 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final Transition unidimensionalTransition1 = new UnidimensionalTransition(unidimensionalCombination1,
				unidimensionalBlackState);
		final Combination unidimensionalCombination2 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalBlackState);
		final Transition unidimensionalTransition2 = new UnidimensionalTransition(unidimensionalCombination2,
				unidimensionalBlackState);
		final Combination unidimensionalCombination3 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final Transition unidimensionalTransition3 = new UnidimensionalTransition(unidimensionalCombination3,
				unidimensionalWhiteState);
		final Combination unidimensionalCombination4 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalBlackState);
		final Transition unidimensionalTransition4 = new UnidimensionalTransition(unidimensionalCombination4,
				unidimensionalBlackState);
		final Combination unidimensionalCombination5 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final Transition unidimensionalTransition5 = new UnidimensionalTransition(unidimensionalCombination5,
				unidimensionalBlackState);
		final Combination unidimensionalCombination6 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalBlackState);
		final Transition unidimensionalTransition6 = new UnidimensionalTransition(unidimensionalCombination6,
				unidimensionalBlackState);
		final Combination unidimensionalCombination7 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final Transition unidimensionalTransition7 = new UnidimensionalTransition(unidimensionalCombination7,
				unidimensionalWhiteState);
		Assert.assertTrue(unidimensionalTransition0.getCombination().equals(unidimensionalCombination0));
		Assert.assertTrue(unidimensionalTransition0.getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalTransition1.getCombination().equals(unidimensionalCombination1));
		Assert.assertTrue(unidimensionalTransition1.getState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalTransition2.getCombination().equals(unidimensionalCombination2));
		Assert.assertTrue(unidimensionalTransition2.getState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalTransition3.getCombination().equals(unidimensionalCombination3));
		Assert.assertTrue(unidimensionalTransition3.getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalTransition4.getCombination().equals(unidimensionalCombination4));
		Assert.assertTrue(unidimensionalTransition4.getState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalTransition5.getCombination().equals(unidimensionalCombination5));
		Assert.assertTrue(unidimensionalTransition5.getState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalTransition6.getCombination().equals(unidimensionalCombination6));
		Assert.assertTrue(unidimensionalTransition6.getState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalTransition7.getCombination().equals(unidimensionalCombination7));
		Assert.assertTrue(unidimensionalTransition7.getState().equals(unidimensionalWhiteState));
	}

}
