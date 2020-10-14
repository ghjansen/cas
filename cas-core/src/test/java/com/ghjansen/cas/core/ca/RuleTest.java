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
import com.ghjansen.cas.core.exception.InvalidTransitionException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class RuleTest {

	@Test
	public void dimensionalRuleConstructor()
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException {
		final DimensionalState dimensionalBlackState = new DimensionalState("black", 0);
		final DimensionalState dimensionalWhiteState = new DimensionalState("white", 1);
		final DimensionalCombination dimensionalCombination0 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final DimensionalTransition dimensionalTransition0 = new DimensionalTransition(dimensionalCombination0,
				dimensionalWhiteState);
		final DimensionalCombination dimensionalCombination1 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalWhiteState);
		final DimensionalTransition dimensionalTransition1 = new DimensionalTransition(dimensionalCombination1,
				dimensionalBlackState);
		final DimensionalCombination dimensionalCombination2 = new DimensionalCombination(dimensionalWhiteState,
				dimensionalBlackState, dimensionalBlackState);
		final DimensionalTransition dimensionalTransition2 = new DimensionalTransition(dimensionalCombination2,
				dimensionalBlackState);
		final DimensionalRule dimensionalRule = new DimensionalRule(dimensionalTransition0, dimensionalTransition1,
				dimensionalTransition2);
		Assert.assertNotNull(dimensionalRule.getTransitions());
		Assert.assertTrue(dimensionalRule.getTransitions().size() > 0);
		Assert.assertTrue(dimensionalRule.getTransitions().size() == 2);
		Assert.assertTrue(
				dimensionalRule.getTransitions().get(dimensionalBlackState).get(0).equals(dimensionalTransition0));
		Assert.assertTrue(
				dimensionalRule.getTransitions().get(dimensionalBlackState).get(1).equals(dimensionalTransition1));
		Assert.assertTrue(
				dimensionalRule.getTransitions().get(dimensionalWhiteState).get(0).equals(dimensionalTransition2));
	}

	@Test(expected = InvalidTransitionException.class)
	public void dimensionalRuleConstructorInvalidTransition() throws InvalidTransitionException {
		new DimensionalRule(null);
	}

	@Test
	public void dimensionalRuleGetNextState()
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException {
		final DimensionalState dimensionalBlackState = new DimensionalState("black", 0);
		final DimensionalState dimensionalWhiteState = new DimensionalState("white", 1);
		final DimensionalCombination dimensionalCombination0 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final DimensionalTransition dimensionalTransition0 = new DimensionalTransition(dimensionalCombination0,
				dimensionalWhiteState);
		final DimensionalCombination dimensionalCombination1 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalWhiteState);
		final DimensionalTransition dimensionalTransition1 = new DimensionalTransition(dimensionalCombination1,
				dimensionalBlackState);
		final DimensionalCombination dimensionalCombination2 = new DimensionalCombination(dimensionalWhiteState,
				dimensionalBlackState, dimensionalBlackState);
		final DimensionalTransition dimensionalTransition2 = new DimensionalTransition(dimensionalCombination2,
				dimensionalBlackState);
		final DimensionalRule dimensionalRule = new DimensionalRule(dimensionalTransition0, dimensionalTransition1,
				dimensionalTransition2);
		Assert.assertTrue(dimensionalRule.getTransition(dimensionalCombination0).equals(dimensionalTransition0));
		Assert.assertTrue(dimensionalRule.getTransition(dimensionalCombination1).equals(dimensionalTransition1));
		Assert.assertTrue(dimensionalRule.getTransition(dimensionalCombination2).equals(dimensionalTransition2));
	}

}
