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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

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
import com.ghjansen.cas.core.physics.DimensionalCell;
import com.ghjansen.cas.core.physics.DimensionalSpace;
import com.ghjansen.cas.core.physics.DimensionalTime;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class CellularAutomatonTest {

	@Test
	public void dimensionalCellularAutomataConstructor() throws CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidStateException,
			InvalidTransitionException, InvalidCombinationException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, InvalidRuleException {
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
		final CellularAutomaton dimensionalCellularAutomaton = new DimensionalCellularAutomaton(dimensionalRule);
		Assert.assertTrue(dimensionalCellularAutomaton.getRule().equals(dimensionalRule));
	}

	@Test(expected = InvalidRuleException.class)
	public void dimensionalCellularAutomatonConstructorInvalidRule() throws InvalidRuleException {
		new DimensionalCellularAutomaton(null);
	}

	@Test
	public void dimensionalCellularAutomatonExecuteRule() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, InvalidRuleException, CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, TimeLimitReachedException {
		final DimensionalState dimensionalBlackState = new DimensionalState("black", 0);
		final DimensionalState dimensionalWhiteState = new DimensionalState("white", 1);
		final DimensionalCombination dimensionalCombination0 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final DimensionalTransition dimensionalTransition0 = new DimensionalTransition(dimensionalCombination0,
				dimensionalWhiteState);
		final DimensionalRule mockedDimensionalRule = spy(new DimensionalRule(dimensionalTransition0));
		final DimensionalCellularAutomaton mockedDimensionalCellularAutomaton = spy(
				new DimensionalCellularAutomaton(mockedDimensionalRule));
		final DimensionalTime mockedDimensionalTime = spy(new DimensionalTime(1, 3));
		final Cell dimensionalCell = getNewBlackDimensionalCell(dimensionalBlackState);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		final DimensionalSpace mockedDimensionalSpace = spy(new DimensionalSpace(mockedDimensionalTime, initialCondition, true));
		when(mockedDimensionalSpace.getCombination(mockedDimensionalTime)).thenReturn(dimensionalCombination0);
		when(mockedDimensionalRule.getTransition(dimensionalCombination0)).thenReturn(dimensionalTransition0);
		mockedDimensionalCellularAutomaton.executeRule(mockedDimensionalSpace, mockedDimensionalTime);
		verify(mockedDimensionalSpace, times(2)).getCombination(mockedDimensionalTime);
		verify(mockedDimensionalRule, times(1)).getTransition(dimensionalCombination0);
		verify(mockedDimensionalSpace, times(1)).setState(mockedDimensionalTime, dimensionalTransition0);
		verify(mockedDimensionalTime, times(1)).increase();
	}

	private Cell getNewBlackDimensionalCell(DimensionalState dimensionalBlackState)
			throws InvalidStateException, InvalidTransitionException, InvalidCombinationException {
		final DimensionalCombination dimensionalCombination = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final DimensionalTransition dimensionalTransition = new DimensionalTransition(dimensionalCombination,
				dimensionalBlackState);
		return new DimensionalCell(dimensionalTransition);
	}

}
