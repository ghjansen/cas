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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.ca.CellularAutomaton;
import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.Rule;
import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidAbsoluteTimeLimitException;
import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidDimensionalAmountException;
import com.ghjansen.cas.core.exception.InvalidDimensionalSpaceException;
import com.ghjansen.cas.core.exception.InvalidInitialConditionException;
import com.ghjansen.cas.core.exception.InvalidRelativeTimeLimitException;
import com.ghjansen.cas.core.exception.InvalidRuleException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;
import com.ghjansen.cas.core.physics.Cell;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalCell;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalSpace;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalTime;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalCellularAutomatonTest {

	@Test
	public void unidimensionalCellularAutomatonConstructor() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, InvalidRuleException {
		final UnidimensionalState unidimensionalBlackState = new UnidimensionalState("black", 0);
		final UnidimensionalState unidimensionalWhiteState = new UnidimensionalState("white", 1);
		final UnidimensionalCombination unidimensionalCombination0 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalBlackState);
		final UnidimensionalTransition unidimensionalTransition0 = new UnidimensionalTransition(unidimensionalCombination0,
				unidimensionalWhiteState);
		final UnidimensionalCombination unidimensionalCombination1 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final UnidimensionalTransition unidimensionalTransition1 = new UnidimensionalTransition(unidimensionalCombination1,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination2 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalBlackState);
		final UnidimensionalTransition unidimensionalTransition2 = new UnidimensionalTransition(unidimensionalCombination2,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination3 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final UnidimensionalTransition unidimensionalTransition3 = new UnidimensionalTransition(unidimensionalCombination3,
				unidimensionalWhiteState);
		final UnidimensionalCombination unidimensionalCombination4 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalBlackState);
		final UnidimensionalTransition unidimensionalTransition4 = new UnidimensionalTransition(unidimensionalCombination4,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination5 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final UnidimensionalTransition unidimensionalTransition5 = new UnidimensionalTransition(unidimensionalCombination5,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination6 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalBlackState);
		final UnidimensionalTransition unidimensionalTransition6 = new UnidimensionalTransition(unidimensionalCombination6,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination7 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final UnidimensionalTransition unidimensionalTransition7 = new UnidimensionalTransition(unidimensionalCombination7,
				unidimensionalWhiteState);
		final UnidimensionalGeneralRule unidimensionalRule = new UnidimensionalGeneralRule(unidimensionalTransition0,
				unidimensionalTransition1, unidimensionalTransition2, unidimensionalTransition3,
				unidimensionalTransition4, unidimensionalTransition5, unidimensionalTransition6,
				unidimensionalTransition7);
		final UnidimensionalCellularAutomaton unidimensionalCellularAutomaton = new UnidimensionalCellularAutomaton(
				unidimensionalRule);
		Assert.assertTrue(unidimensionalCellularAutomaton.getRule().equals(unidimensionalRule));
	}

	@Test
	public void unidimensionalCellularAutomatonExecuteRule()
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException, InvalidRuleException,
			CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException,
			InvalidDimensionalAmountException, InvalidInitialConditionException, InvalidDimensionalSpaceException {
		final UnidimensionalState unidimensionalBlackState = new UnidimensionalState("black", 0);
		final UnidimensionalState unidimensionalWhiteState = new UnidimensionalState("white", 1);
		final UnidimensionalCombination unidimensionalCombination0 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalBlackState);
		final UnidimensionalTransition unidimensionalTransition0 = new UnidimensionalTransition(unidimensionalCombination0,
				unidimensionalWhiteState);
		final UnidimensionalCombination unidimensionalCombination1 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final UnidimensionalTransition unidimensionalTransition1 = new UnidimensionalTransition(unidimensionalCombination1,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination2 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalBlackState);
		final UnidimensionalTransition unidimensionalTransition2 = new UnidimensionalTransition(unidimensionalCombination2,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination3 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final UnidimensionalTransition unidimensionalTransition3 = new UnidimensionalTransition(unidimensionalCombination3,
				unidimensionalWhiteState);
		final UnidimensionalCombination unidimensionalCombination4 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalBlackState);
		final UnidimensionalTransition unidimensionalTransition4 = new UnidimensionalTransition(unidimensionalCombination4,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination5 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final UnidimensionalTransition unidimensionalTransition5 = new UnidimensionalTransition(unidimensionalCombination5,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination6 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalBlackState);
		final UnidimensionalTransition unidimensionalTransition6 = new UnidimensionalTransition(unidimensionalCombination6,
				unidimensionalBlackState);
		final UnidimensionalCombination unidimensionalCombination7 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final UnidimensionalTransition unidimensionalTransition7 = new UnidimensionalTransition(unidimensionalCombination7,
				unidimensionalWhiteState);
		final UnidimensionalGeneralRule unidimensionalRule = new UnidimensionalGeneralRule(unidimensionalTransition0,
				unidimensionalTransition1, unidimensionalTransition2, unidimensionalTransition3,
				unidimensionalTransition4, unidimensionalTransition5, unidimensionalTransition6,
				unidimensionalTransition7);
		final UnidimensionalCellularAutomaton unidimensionalCellularAutomaton = new UnidimensionalCellularAutomaton(
				unidimensionalRule);
		final int absoluteTimeLimit = 3;
		final int relativeTimeLimit = 3;
		final UnidimensionalTime unidimensionalTime = new UnidimensionalTime(absoluteTimeLimit, relativeTimeLimit);
		final List<UnidimensionalCell> initialCondition = new ArrayList<UnidimensionalCell>();
		initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalWhiteState));
		initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalWhiteState));
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState));
		final UnidimensionalSpace unidimensionalSpace = new UnidimensionalSpace(unidimensionalTime, initialCondition);
		final int ruleExecutions = absoluteTimeLimit * relativeTimeLimit;
		try {
			for (int i = 0; i < ruleExecutions; i++) {
				unidimensionalCellularAutomaton.executeRule(unidimensionalSpace, unidimensionalTime);
			}
		} catch (TimeLimitReachedException e) {
			// End of simulation signal
		}
		// Assert initial condition
		Assert.assertTrue(unidimensionalSpace.getInitial().get(0).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalSpace.getInitial().get(1).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalSpace.getInitial().get(2).getState().equals(unidimensionalBlackState));
		// Assert history
		Assert.assertTrue(
				unidimensionalSpace.getHistory().get(0).get(0).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(
				unidimensionalSpace.getHistory().get(0).get(1).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				unidimensionalSpace.getHistory().get(0).get(2).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				unidimensionalSpace.getHistory().get(1).get(0).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				unidimensionalSpace.getHistory().get(1).get(1).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				unidimensionalSpace.getHistory().get(1).get(2).getState().equals(unidimensionalBlackState));
		// Assert last
		Assert.assertTrue(unidimensionalSpace.getLast().get(0).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalSpace.getLast().get(1).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(unidimensionalSpace.getLast().get(2).getState().equals(unidimensionalBlackState));
		// Assert current
		Assert.assertTrue(unidimensionalSpace.getCurrent().get(0).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalSpace.getCurrent().get(1).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(unidimensionalSpace.getCurrent().get(2).getState().equals(unidimensionalWhiteState));

	}

	private UnidimensionalCell getNewBlackUnidimensionalCell(UnidimensionalState unidimensionalBlackState)
			throws InvalidStateException, InvalidTransitionException, InvalidCombinationException {
		final UnidimensionalCombination unidimensionalCombination = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalBlackState);
		final UnidimensionalTransition unidimensionalTransition = new UnidimensionalTransition(unidimensionalCombination,
				unidimensionalBlackState);
		return new UnidimensionalCell(unidimensionalTransition);
	}

	private UnidimensionalCell getNewWhiteUnidimensionalCell(UnidimensionalState unidimensionalWhiteState)
			throws InvalidStateException, InvalidTransitionException, InvalidCombinationException {
		final UnidimensionalCombination unidimensionalCombination = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final UnidimensionalTransition unidimensionalTransition = new UnidimensionalTransition(unidimensionalCombination,
				unidimensionalWhiteState);
		return new UnidimensionalCell(unidimensionalTransition);
	}

}
