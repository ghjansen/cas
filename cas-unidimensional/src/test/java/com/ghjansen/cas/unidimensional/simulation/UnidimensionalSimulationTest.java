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

package com.ghjansen.cas.unidimensional.simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;
import com.ghjansen.cas.core.physics.Universe;
import com.ghjansen.cas.core.simulation.Simulation;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalCellularAutomaton;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalCombination;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalGeneralRule;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalState;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalTransition;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalCell;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalSpace;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalTime;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalUniverse;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalSimulationTest {

	@Test
	public void unidimensionalSimulationConstructor()
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException, InvalidRuleException,
			CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException,
			InvalidDimensionalAmountException, InvalidInitialConditionException, InvalidDimensionalSpaceException,
			InvalidSpaceException, InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException {
		final State unidimensionalWhiteState = new UnidimensionalState("white", 0);
		final State unidimensionalBlackState = new UnidimensionalState("black", 1);
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
		final Universe unidimensionalUniverse = new UnidimensionalUniverse(unidimensionalSpace, unidimensionalTime);
		final Simulation unidimensionalSimulation = new UnidimensionalSimulation(unidimensionalUniverse,
				unidimensionalCellularAutomaton);
		Assert.assertTrue(unidimensionalSimulation.getUniverse().equals(unidimensionalUniverse));
		Assert.assertTrue(unidimensionalSimulation.getCellularAutomata().equals(unidimensionalCellularAutomaton));
	}

	@Test
	public void unidimensionalSimulationValidateRule30() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, InvalidRuleException, CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, InvalidSpaceException,
			InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException, IOException {
		final State unidimensionalWhiteState = new UnidimensionalState("white", 0);
		final State unidimensionalBlackState = new UnidimensionalState("black", 1);
		final Combination unidimensionalCombination0 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalBlackState);
		final Transition unidimensionalTransition0 = new UnidimensionalTransition(unidimensionalCombination0,
				unidimensionalWhiteState);
		final Combination unidimensionalCombination1 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final Transition unidimensionalTransition1 = new UnidimensionalTransition(unidimensionalCombination1,
				unidimensionalWhiteState);
		final Combination unidimensionalCombination2 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalBlackState);
		final Transition unidimensionalTransition2 = new UnidimensionalTransition(unidimensionalCombination2,
				unidimensionalWhiteState);
		final Combination unidimensionalCombination3 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final Transition unidimensionalTransition3 = new UnidimensionalTransition(unidimensionalCombination3,
				unidimensionalBlackState);
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
		final int absoluteTimeLimit = 1000;
		final int relativeTimeLimit = 1000;
		final Time unidimensionalTime = new UnidimensionalTime(absoluteTimeLimit, relativeTimeLimit);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		for (int i = 0; i < (relativeTimeLimit / 2) - 1; i++) {
			initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalWhiteState));
		}
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState));
		for (int i = 0; i < (relativeTimeLimit / 2); i++) {
			initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalWhiteState));
		}
		final Space unidimensionalSpace = new UnidimensionalSpace(unidimensionalTime, initialCondition);
		final Universe unidimensionalUniverse = new UnidimensionalUniverse(unidimensionalSpace, unidimensionalTime);
		final Simulation unidimensionalSimulation = new UnidimensionalSimulation(unidimensionalUniverse,
				unidimensionalCellularAutomaton);
		try {
			unidimensionalSimulation.simulateComplete();
		} catch (TimeLimitReachedException e) {
			// End of simulation signal
		}
		Assert.assertTrue(unidimensionalSimulation.getUniverse().getTime().getRelative().get(0)
				.getAbsolute() == relativeTimeLimit - 1);
		Assert.assertTrue(unidimensionalSimulation.getUniverse().getTime().getAbsolute() == absoluteTimeLimit - 1);
		Assert.assertTrue(
				validateWithMathematicaOutput("src/test/resources/mathematicaRule30.txt", unidimensionalSimulation));
		// Mathematica input: Export["mathematicaRule30.txt", CellularAutomaton[30, PadRight[PadLeft[{1}, 500], 1000], 1000]]
	}

	@Test
	public void unidimensionalSimulationValidateRule90() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, InvalidRuleException, CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, InvalidSpaceException,
			InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException, IOException {
		final State unidimensionalWhiteState = new UnidimensionalState("white", 0);
		final State unidimensionalBlackState = new UnidimensionalState("black", 1);
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
				unidimensionalWhiteState);
		final Combination unidimensionalCombination3 = new UnidimensionalCombination(unidimensionalWhiteState,
				unidimensionalBlackState, unidimensionalWhiteState);
		final Transition unidimensionalTransition3 = new UnidimensionalTransition(unidimensionalCombination3,
				unidimensionalBlackState);
		final Combination unidimensionalCombination4 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalBlackState);
		final Transition unidimensionalTransition4 = new UnidimensionalTransition(unidimensionalCombination4,
				unidimensionalBlackState);
		final Combination unidimensionalCombination5 = new UnidimensionalCombination(unidimensionalBlackState,
				unidimensionalWhiteState, unidimensionalWhiteState);
		final Transition unidimensionalTransition5 = new UnidimensionalTransition(unidimensionalCombination5,
				unidimensionalWhiteState);
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
		final int absoluteTimeLimit = 1000;
		final int relativeTimeLimit = 1000;
		final Time unidimensionalTime = new UnidimensionalTime(absoluteTimeLimit, relativeTimeLimit);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		for (int i = 0; i < (relativeTimeLimit / 2) - 1; i++) {
			initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalWhiteState));
		}
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState));
		for (int i = 0; i < (relativeTimeLimit / 2); i++) {
			initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalWhiteState));
		}
		final Space unidimensionalSpace = new UnidimensionalSpace(unidimensionalTime, initialCondition);
		final Universe unidimensionalUniverse = new UnidimensionalUniverse(unidimensionalSpace, unidimensionalTime);
		final Simulation unidimensionalSimulation = new UnidimensionalSimulation(unidimensionalUniverse,
				unidimensionalCellularAutomaton);
		try {
			unidimensionalSimulation.simulateComplete();
		} catch (TimeLimitReachedException e) {
			// End of simulation signal
		}
		Assert.assertTrue(unidimensionalSimulation.getUniverse().getTime().getRelative().get(0)
				.getAbsolute() == relativeTimeLimit - 1);
		Assert.assertTrue(unidimensionalSimulation.getUniverse().getTime().getAbsolute() == absoluteTimeLimit - 1);
		Assert.assertTrue(
				validateWithMathematicaOutput("src/test/resources/mathematicaRule90.txt", unidimensionalSimulation));
		// Mathematica input: Export["mathematicaRule90.txt", CellularAutomaton[90, PadRight[PadLeft[{1}, 500], 1000], 1000]]
	}

	@Test
	public void unidimensionalSimulationValidateRule110() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, InvalidRuleException, CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException, InvalidSpaceException,
			InvalidTimeException, InvalidUniverseException, InvalidCellularAutomataException, IOException {
		final State unidimensionalWhiteState = new UnidimensionalState("white", 0);
		final State unidimensionalBlackState = new UnidimensionalState("black", 1);
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
		final int absoluteTimeLimit = 1000;
		final int relativeTimeLimit = 1000;
		final Time unidimensionalTime = new UnidimensionalTime(absoluteTimeLimit, relativeTimeLimit);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		for (int i = 0; i < relativeTimeLimit - 1; i++) {
			initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalWhiteState));
		}
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState));
		final Space unidimensionalSpace = new UnidimensionalSpace(unidimensionalTime, initialCondition);
		final Universe unidimensionalUniverse = new UnidimensionalUniverse(unidimensionalSpace, unidimensionalTime);
		final Simulation unidimensionalSimulation = new UnidimensionalSimulation(unidimensionalUniverse,
				unidimensionalCellularAutomaton);
		try {
			unidimensionalSimulation.simulateComplete();
		} catch (TimeLimitReachedException e) {
			// End of simulation signal
		}
		Assert.assertTrue(unidimensionalSimulation.getUniverse().getTime().getRelative().get(0)
				.getAbsolute() == relativeTimeLimit - 1);
		Assert.assertTrue(unidimensionalSimulation.getUniverse().getTime().getAbsolute() == absoluteTimeLimit - 1);
		Assert.assertTrue(
				validateWithMathematicaOutput("src/test/resources/mathematicaRule110.txt", unidimensionalSimulation));
		// Mathematica input: Export["mathematicaRule110.txt", CellularAutomaton[110, PadLeft[{1}, 1000], 1000]]
	}

	private boolean validateWithMathematicaOutput(String filePath, Simulation simulation) throws IOException {
		final int absoluteLimit = simulation.getUniverse().getTime().getLimit();
		final int relativeLimit = simulation.getUniverse().getTime().getRelative().get(0).getLimit();
		final BufferedReader buffer = new BufferedReader(new FileReader(filePath));
		List<Integer> line = convertLine(buffer.readLine());
		// Validate initial condition
		for (int i = 0; i < relativeLimit; i++) {
			Cell cell = (Cell) simulation.getUniverse().getSpace().getInitial().get(i);
			if (line.get(i) != cell.getState().getValue()) {
				return false;
			}
		}
		// Validate history
		for (int i = 0; i < absoluteLimit - 1; i++) {
			line = convertLine(buffer.readLine());
			List<Cell> cells = (List<Cell>) simulation.getUniverse().getSpace().getHistory().get(i);
			for (int j = 0; j < relativeLimit; j++) {
				Cell cell = cells.get(j);
				if (line.get(j) != cell.getState().getValue()) {
					return false;
				}
			}
		}
		// Validate current (last processed)
		line = convertLine(buffer.readLine());
		for (int i = 0; i < relativeLimit; i++) {
			Cell cell = (Cell) simulation.getUniverse().getSpace().getCurrent().get(i);
			if (line.get(i) != cell.getState().getValue()) {
				return false;
			}
		}
		return true;
	}

	private List<Integer> convertLine(String line) {
		String temp = line.substring(1, line.length() - 1);
		String array[] = temp.split(",\\s");
		List<Integer> list = new ArrayList<Integer>();
		for (String s : array) {
			list.add(Integer.valueOf(s));
		}
		return list;
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
