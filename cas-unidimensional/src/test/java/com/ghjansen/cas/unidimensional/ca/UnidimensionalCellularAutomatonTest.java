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
		final Rule unidimensionalRule = new UnidimensionalGeneralRule(unidimensionalTransition0,
				unidimensionalTransition1, unidimensionalTransition2, unidimensionalTransition3,
				unidimensionalTransition4, unidimensionalTransition5, unidimensionalTransition6,
				unidimensionalTransition7);
		final CellularAutomaton unidimensionalCellularAutomaton = new UnidimensionalCellularAutomaton(
				unidimensionalRule);
		Assert.assertTrue(unidimensionalCellularAutomaton.getRule().equals(unidimensionalRule));
	}

	@Test
	public void unidimensionalCellularAutomatonExecuteRule()
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException, InvalidRuleException,
			CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException,
			InvalidDimensionalAmountException, InvalidInitialConditionException, InvalidDimensionalSpaceException {
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
		final Rule unidimensionalRule = new UnidimensionalGeneralRule(unidimensionalTransition0,
				unidimensionalTransition1, unidimensionalTransition2, unidimensionalTransition3,
				unidimensionalTransition4, unidimensionalTransition5, unidimensionalTransition6,
				unidimensionalTransition7);
		final CellularAutomaton unidimensionalCellularAutomaton = new UnidimensionalCellularAutomaton(
				unidimensionalRule);
		final int absoluteTimeLimit = 3;
		final int relativeTimeLimit = 3;
		final Time unidimensionalTime = new UnidimensionalTime(absoluteTimeLimit, relativeTimeLimit);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalWhiteState));
		initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalWhiteState));
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState));
		final Space unidimensionalSpace = new UnidimensionalSpace(unidimensionalTime, initialCondition);
		final int ruleExecutions = absoluteTimeLimit * relativeTimeLimit;
		try {
			for (int i = 0; i < ruleExecutions; i++) {
				unidimensionalCellularAutomaton.executeRule(unidimensionalSpace, unidimensionalTime);
			}
		} catch (TimeLimitReachedException e) {
			// End of simulation signal
		}
		// Assert initial condition
		Assert.assertTrue(((Cell) unidimensionalSpace.getInitial().get(0)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getInitial().get(1)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getInitial().get(2)).getState().equals(unidimensionalBlackState));
		// Assert history
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(0).get(0)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(0).get(1)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(0).get(2)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(1).get(0)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(1).get(1)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(1).get(2)).getState().equals(unidimensionalBlackState));
		// Assert last
		Assert.assertTrue(((Cell) unidimensionalSpace.getLast().get(0)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getLast().get(1)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getLast().get(2)).getState().equals(unidimensionalBlackState));
		// Assert current
		Assert.assertTrue(((Cell) unidimensionalSpace.getCurrent().get(0)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getCurrent().get(1)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getCurrent().get(2)).getState().equals(unidimensionalWhiteState));

	}

	private Cell getNewBlackUnidimensionalCell(State unidimensionalBlackState)
			throws InvalidStateException, InvalidTransitionException, InvalidCombinationException {
		final Combination unidimensionalCombination = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalBlackState);
		final Transition unidimensionalTransition = new UnidimensionalTransition(unidimensionalCombination,
				unidimensionalBlackState);
		return new UnidimensionalCell(unidimensionalTransition);
	}

	private Cell getNewWhiteUnidimensionalCell(State unidimensionalWhiteState)
			throws InvalidStateException, InvalidTransitionException, InvalidCombinationException {
		final Combination unidimensionalCombination = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final Transition unidimensionalTransition = new UnidimensionalTransition(unidimensionalCombination,
				unidimensionalWhiteState);
		return new UnidimensionalCell(unidimensionalTransition);
	}

}
