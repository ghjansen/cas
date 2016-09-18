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

package com.ghjansen.cas.core.simulation;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.ca.CellularAutomaton;
import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.DimensionalCellularAutomaton;
import com.ghjansen.cas.core.ca.DimensionalCombination;
import com.ghjansen.cas.core.ca.DimensionalRule;
import com.ghjansen.cas.core.ca.DimensionalState;
import com.ghjansen.cas.core.ca.DimensionalTransition;
import com.ghjansen.cas.core.ca.Rule;
import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidAbsoluteTimeLimitException;
import com.ghjansen.cas.core.exception.InvalidCellularAutomataException;
import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidDimensionalAmountException;
import com.ghjansen.cas.core.exception.InvalidDimensionalSpaceException;
import com.ghjansen.cas.core.exception.InvalidInitialConditionException;
import com.ghjansen.cas.core.exception.InvalidRelativeTimeLimitException;
import com.ghjansen.cas.core.exception.InvalidRuleException;
import com.ghjansen.cas.core.exception.InvalidSpaceException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTimeException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;
import com.ghjansen.cas.core.exception.InvalidUniverseException;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;
import com.ghjansen.cas.core.physics.Cell;
import com.ghjansen.cas.core.physics.DimensionalCell;
import com.ghjansen.cas.core.physics.DimensionalSpace;
import com.ghjansen.cas.core.physics.DimensionalTime;
import com.ghjansen.cas.core.physics.DimensionalUniverse;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;
import com.ghjansen.cas.core.physics.Universe;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class SimulationTest {

	@Test
	public void dimensionalSimulationConstructor() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, InvalidRuleException, CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, TimeLimitReachedException,
			InvalidSpaceException, InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		final State dimensionalWhiteState = new DimensionalState("white", 1);
		final Combination dimensionalCombination0 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final Transition dimensionalTransition0 = new DimensionalTransition(dimensionalCombination0,
				dimensionalWhiteState);
		final Rule dimensionalRule = new DimensionalRule(dimensionalTransition0);
		final CellularAutomaton dimensionalCellularAutomaton = new DimensionalCellularAutomaton(dimensionalRule);
		final Time dimensionalTime = new DimensionalTime(1, 3);
		final Cell dimensionalCell = getNewBlackDimensionalCell(dimensionalBlackState);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		final Space dimensionalSpace = new DimensionalSpace(dimensionalTime, initialCondition, true);
		final Universe dimensionalUniverse = new DimensionalUniverse(dimensionalSpace, dimensionalTime);
		final Simulation dimensionalSimulation = new DimensionalSimulation(dimensionalUniverse,
				dimensionalCellularAutomaton);
		Assert.assertTrue(dimensionalSimulation.getUniverse().equals(dimensionalUniverse));
		Assert.assertTrue(dimensionalSimulation.getCellularAutomata().equals(dimensionalCellularAutomaton));
		Assert.assertTrue(dimensionalSimulation.isActive() == false);
	}

	@Test(expected = InvalidUniverseException.class)
	public void dimensionalSimulationConstructorInvalidUniverse() throws InvalidStateException,
			InvalidCombinationException, InvalidTransitionException, InvalidRuleException, CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, TimeLimitReachedException,
			InvalidSpaceException, InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		final State dimensionalWhiteState = new DimensionalState("white", 1);
		final Combination dimensionalCombination0 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final Transition dimensionalTransition0 = new DimensionalTransition(dimensionalCombination0,
				dimensionalWhiteState);
		final Rule dimensionalRule = new DimensionalRule(dimensionalTransition0);
		final CellularAutomaton dimensionalCellularAutomaton = new DimensionalCellularAutomaton(dimensionalRule);
		final Cell dimensionalCell = getNewBlackDimensionalCell(dimensionalBlackState);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		new DimensionalSimulation(null, dimensionalCellularAutomaton);
	}

	@Test(expected = InvalidCellularAutomataException.class)
	public void dimensionalSimulationConstructorInvalidCellularAutomata()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException,
			InvalidStateException, InvalidTransitionException, InvalidCombinationException,
			InvalidDimensionalAmountException, InvalidInitialConditionException, InvalidDimensionalSpaceException,
			InvalidSpaceException, InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		final Time dimensionalTime = new DimensionalTime(1, 3);
		final Cell dimensionalCell = getNewBlackDimensionalCell(dimensionalBlackState);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		final Space dimensionalSpace = new DimensionalSpace(dimensionalTime, initialCondition, true);
		final Universe dimensionalUniverse = new DimensionalUniverse(dimensionalSpace, dimensionalTime);
		new DimensionalSimulation(dimensionalUniverse, null);
	}

	@Test
	public void dimensionalSimulationSimulateComplete() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, InvalidRuleException, CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, TimeLimitReachedException,
			InvalidSpaceException, InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		final State dimensionalWhiteState = new DimensionalState("white", 1);
		final Combination dimensionalCombination0 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final Transition dimensionalTransition0 = new DimensionalTransition(dimensionalCombination0,
				dimensionalWhiteState);
		final Rule mockedDimensionalRule = spy(new DimensionalRule(dimensionalTransition0));
		final CellularAutomaton mockedDimensionalCellularAutomaton = spy(
				new DimensionalCellularAutomaton(mockedDimensionalRule));
		final int absoluteTime = 3;
		final int relativeTime = 3;
		final Time mockedDimensionalTime = spy(new DimensionalTime(absoluteTime, relativeTime));
		final Cell dimensionalCell = getNewBlackDimensionalCell(dimensionalBlackState);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		final Space mockedDimensionalSpace = spy(new DimensionalSpace(mockedDimensionalTime, initialCondition, true));
		final Universe dimensionalUniverse = new DimensionalUniverse(mockedDimensionalSpace, mockedDimensionalTime);
		final Simulation dimensionalSimulation = new DimensionalSimulation(dimensionalUniverse,
				mockedDimensionalCellularAutomaton);
		when(mockedDimensionalSpace.getCombination(mockedDimensionalTime)).thenReturn(dimensionalCombination0);
		when(mockedDimensionalRule.getTransition(dimensionalCombination0)).thenReturn(dimensionalTransition0);
		try {
			dimensionalSimulation.simulateComplete();
		} catch (InvalidStateException e) {
			throw e;
		} catch (InvalidCombinationException e) {
			throw e;
		} catch (InvalidTransitionException e) {
			throw e;
		} catch (TimeLimitReachedException e) {
			//throw e; // End of simulation signal
		}
		final int ruleExecutions = absoluteTime * relativeTime;
		verify(mockedDimensionalCellularAutomaton, times(ruleExecutions)).executeRule(mockedDimensionalSpace,
				mockedDimensionalTime);
		Assert.assertTrue(dimensionalSimulation.isActive() == false);
	}
	
	@Test
	public void dimensionalSimulationSimulateIteration() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, InvalidRuleException, CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, TimeLimitReachedException,
			InvalidSpaceException, InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		final State dimensionalWhiteState = new DimensionalState("white", 1);
		final Combination dimensionalCombination0 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final Transition dimensionalTransition0 = new DimensionalTransition(dimensionalCombination0,
				dimensionalWhiteState);
		final Rule mockedDimensionalRule = spy(new DimensionalRule(dimensionalTransition0));
		final CellularAutomaton mockedDimensionalCellularAutomaton = spy(
				new DimensionalCellularAutomaton(mockedDimensionalRule));
		final int absoluteTime = 3;
		final int relativeTime = 3;
		final Time mockedDimensionalTime = spy(new DimensionalTime(absoluteTime, relativeTime));
		final Cell dimensionalCell = getNewBlackDimensionalCell(dimensionalBlackState);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		final Space mockedDimensionalSpace = spy(new DimensionalSpace(mockedDimensionalTime, initialCondition, true));
		final Universe dimensionalUniverse = new DimensionalUniverse(mockedDimensionalSpace, mockedDimensionalTime);
		final Simulation dimensionalSimulation = new DimensionalSimulation(dimensionalUniverse,
				mockedDimensionalCellularAutomaton);
		when(mockedDimensionalSpace.getCombination(mockedDimensionalTime)).thenReturn(dimensionalCombination0);
		when(mockedDimensionalRule.getTransition(dimensionalCombination0)).thenReturn(dimensionalTransition0);
		try {
			dimensionalSimulation.simulateIteration();
		} catch (InvalidStateException e) {
			throw e;
		} catch (InvalidCombinationException e) {
			throw e;
		} catch (InvalidTransitionException e) {
			throw e;
		} catch (TimeLimitReachedException e) {
			//throw e; // End of simulation signal
		}
		final int ruleExecutions = relativeTime;
		verify(mockedDimensionalCellularAutomaton, times(ruleExecutions)).executeRule(mockedDimensionalSpace,
				mockedDimensionalTime);
		Assert.assertTrue(dimensionalSimulation.isActive() == false);
	}
	
	@Test
	public void dimensionalSimulationSimulateCell() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, InvalidRuleException, CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, TimeLimitReachedException,
			InvalidSpaceException, InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException {
		final State dimensionalBlackState = new DimensionalState("black", 0);
		final State dimensionalWhiteState = new DimensionalState("white", 1);
		final Combination dimensionalCombination0 = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final Transition dimensionalTransition0 = new DimensionalTransition(dimensionalCombination0,
				dimensionalWhiteState);
		final Rule mockedDimensionalRule = spy(new DimensionalRule(dimensionalTransition0));
		final CellularAutomaton mockedDimensionalCellularAutomaton = spy(
				new DimensionalCellularAutomaton(mockedDimensionalRule));
		final int absoluteTime = 3;
		final int relativeTime = 3;
		final Time mockedDimensionalTime = spy(new DimensionalTime(absoluteTime, relativeTime));
		final Cell dimensionalCell = getNewBlackDimensionalCell(dimensionalBlackState);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		initialCondition.add(dimensionalCell);
		final Space mockedDimensionalSpace = spy(new DimensionalSpace(mockedDimensionalTime, initialCondition, true));
		final Universe dimensionalUniverse = new DimensionalUniverse(mockedDimensionalSpace, mockedDimensionalTime);
		final Simulation dimensionalSimulation = new DimensionalSimulation(dimensionalUniverse,
				mockedDimensionalCellularAutomaton);
		when(mockedDimensionalSpace.getCombination(mockedDimensionalTime)).thenReturn(dimensionalCombination0);
		when(mockedDimensionalRule.getTransition(dimensionalCombination0)).thenReturn(dimensionalTransition0);
		try {
			dimensionalSimulation.simulateUnit();
		} catch (InvalidStateException e) {
			throw e;
		} catch (InvalidCombinationException e) {
			throw e;
		} catch (InvalidTransitionException e) {
			throw e;
		} catch (TimeLimitReachedException e) {
			//throw e; // End of simulation signal
		}
		final int ruleExecutions = 1;
		verify(mockedDimensionalCellularAutomaton, times(ruleExecutions)).executeRule(mockedDimensionalSpace,
				mockedDimensionalTime);
		Assert.assertTrue(dimensionalSimulation.isActive() == false);
	}

	private Cell getNewBlackDimensionalCell(State dimensionalBlackState)
			throws InvalidStateException, InvalidTransitionException, InvalidCombinationException {
		final Combination dimensionalCombination = new DimensionalCombination(dimensionalBlackState,
				dimensionalBlackState, dimensionalBlackState);
		final Transition dimensionalTransition = new DimensionalTransition(dimensionalCombination,
				dimensionalBlackState);
		return new DimensionalCell(dimensionalTransition);
	}

}
