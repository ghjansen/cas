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

package com.ghjansen.cas.control.simulation;

import java.util.List;

import com.ghjansen.cas.control.exception.SimulationBuilderException;
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
import com.ghjansen.cas.core.physics.Cell;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;
import com.ghjansen.cas.core.physics.Universe;
import com.ghjansen.cas.core.simulation.Simulation;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class SimulationBuilder {

	private final SimulationParameter simulationParameter;

	public SimulationBuilder(SimulationParameter simulationParameter) {
		this.simulationParameter = simulationParameter;
	}

	public SimulationParameter getSimulationParameter() {
		return simulationParameter;
	}

	public Simulation build() throws SimulationBuilderException {
		try {
			List<State> states = buildStates();
			List<Combination> combinations = buildCombinations(states);
			List<Transition> transitions = buildTransitions(states, combinations);
			Rule rule = buildRule(transitions);
			CellularAutomaton cellularAutomaton = buildCellularAutomaton(rule);
			Time time = buildTime();
			List<Cell> initialCondition = buildInitialCondition(states);
			Space space = buildSpace(time, initialCondition);
			Universe universe = buildUniverse(space, time);
			return buildSimulation(universe, cellularAutomaton);
		} catch (InvalidStateException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidCombinationException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidTransitionException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidRuleException e) {
			throw new SimulationBuilderException(e);
		} catch (CloneNotSupportedException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidAbsoluteTimeLimitException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidRelativeTimeLimitException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidDimensionalAmountException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidInitialConditionException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidDimensionalSpaceException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidSpaceException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidTimeException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidUniverseException e) {
			throw new SimulationBuilderException(e);
		} catch (InvalidCellularAutomataException e) {
			throw new SimulationBuilderException(e);
		}
	}

	public abstract List<State> buildStates();

	public abstract List<Combination> buildCombinations(List<State> states) throws InvalidStateException;

	public abstract List<Transition> buildTransitions(List<State> states, List<Combination> combinations)
			throws InvalidCombinationException, InvalidStateException;

	public abstract Rule buildRule(List<Transition> transitions) throws InvalidTransitionException;

	public abstract CellularAutomaton buildCellularAutomaton(Rule rule) throws InvalidRuleException;

	public abstract Time buildTime()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException;

	public abstract List<Cell> buildInitialCondition(List<State> states) throws InvalidStateException;

	public abstract Space buildSpace(Time time, List<Cell> initialCondition) throws InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException;

	public abstract Universe buildUniverse(Space space, Time time) throws InvalidSpaceException, InvalidTimeException;

	public abstract Simulation buildSimulation(Universe universe, CellularAutomaton cellularAutomaton)
			throws InvalidUniverseException, InvalidCellularAutomataException;

}
