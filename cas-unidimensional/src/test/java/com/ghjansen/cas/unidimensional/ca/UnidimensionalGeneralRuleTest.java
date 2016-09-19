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
import com.ghjansen.cas.core.ca.Rule;
import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalGeneralRuleTest {

	@Test
	public void unidimensionalGeneralRuleConstructor()
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException {
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

		Rule unidimensionalRule = new UnidimensionalGeneralRule(unidimensionalTransition0, unidimensionalTransition1,
				unidimensionalTransition2, unidimensionalTransition3, unidimensionalTransition4,
				unidimensionalTransition5, unidimensionalTransition6, unidimensionalTransition7);

		Assert.assertNotNull(unidimensionalRule.getTransitions());
		Assert.assertTrue(unidimensionalRule.getTransitions().size() > 0);
		Assert.assertTrue(unidimensionalRule.getTransitions().size() == 2);
		Assert.assertTrue(unidimensionalRule.getTransitions().get(unidimensionalBlackState).get(0)
				.equals(unidimensionalTransition0));
		Assert.assertTrue(unidimensionalRule.getTransitions().get(unidimensionalBlackState).get(1)
				.equals(unidimensionalTransition1));
		Assert.assertTrue(unidimensionalRule.getTransitions().get(unidimensionalBlackState).get(2)
				.equals(unidimensionalTransition4));
		Assert.assertTrue(unidimensionalRule.getTransitions().get(unidimensionalBlackState).get(3)
				.equals(unidimensionalTransition5));
		Assert.assertTrue(unidimensionalRule.getTransitions().get(unidimensionalWhiteState).get(0)
				.equals(unidimensionalTransition2));
		Assert.assertTrue(unidimensionalRule.getTransitions().get(unidimensionalWhiteState).get(1)
				.equals(unidimensionalTransition3));
		Assert.assertTrue(unidimensionalRule.getTransitions().get(unidimensionalWhiteState).get(2)
				.equals(unidimensionalTransition6));
		Assert.assertTrue(unidimensionalRule.getTransitions().get(unidimensionalWhiteState).get(3)
				.equals(unidimensionalTransition7));
	}

	@Test
	public void unidimensionalGeneralRuleGetNextState()
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException {
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

		Rule unidimensionalRule = new UnidimensionalGeneralRule(unidimensionalTransition0, unidimensionalTransition1,
				unidimensionalTransition2, unidimensionalTransition3, unidimensionalTransition4,
				unidimensionalTransition5, unidimensionalTransition6, unidimensionalTransition7);

		Assert.assertTrue(
				unidimensionalRule.getTransition(unidimensionalCombination0).equals(unidimensionalWhiteState));
		Assert.assertTrue(
				unidimensionalRule.getTransition(unidimensionalCombination1).equals(unidimensionalBlackState));
		Assert.assertTrue(
				unidimensionalRule.getTransition(unidimensionalCombination2).equals(unidimensionalBlackState));
		Assert.assertTrue(
				unidimensionalRule.getTransition(unidimensionalCombination3).equals(unidimensionalWhiteState));
		Assert.assertTrue(
				unidimensionalRule.getTransition(unidimensionalCombination4).equals(unidimensionalBlackState));
		Assert.assertTrue(
				unidimensionalRule.getTransition(unidimensionalCombination5).equals(unidimensionalBlackState));
		Assert.assertTrue(
				unidimensionalRule.getTransition(unidimensionalCombination6).equals(unidimensionalBlackState));
		Assert.assertTrue(
				unidimensionalRule.getTransition(unidimensionalCombination7).equals(unidimensionalWhiteState));
	}

}
