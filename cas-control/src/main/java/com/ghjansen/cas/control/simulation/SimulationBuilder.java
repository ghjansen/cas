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
public abstract class SimulationBuilder<A extends State,O extends Combination,N extends Transition,R extends Rule,
		E extends Cell,C extends CellularAutomaton,T extends Time,S extends Space,U extends Universe> {

	private final SimulationParameter simulationParameter;

	public SimulationBuilder(SimulationParameter simulationParameter) {
		this.simulationParameter = simulationParameter;
	}

	public SimulationParameter getSimulationParameter() {
		return simulationParameter;
	}

	public Simulation build() throws SimulationBuilderException {
		try {
			List<A> states = buildStates();
			List<O> combinations = buildCombinations(states);
			List<N> transitions = buildTransitions(states, combinations);
			R rule = buildRule(transitions);
			C cellularAutomaton = buildCellularAutomaton(rule);
			T time = buildTime();
			List<E> initialCondition = buildInitialCondition(states);
			S space = buildSpace(time, initialCondition);
			U universe = buildUniverse(space, time);
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

	public abstract List<A> buildStates();

	public abstract List<O> buildCombinations(List<A> states) throws InvalidStateException;

	public abstract List<N> buildTransitions(List<A> states, List<O> combinations)
			throws InvalidCombinationException, InvalidStateException;

	public abstract R buildRule(List<N> transitions) throws InvalidTransitionException;

	public abstract C buildCellularAutomaton(R rule) throws InvalidRuleException;

	public abstract T buildTime()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException;

	public abstract List<E> buildInitialCondition(List<A> states) throws InvalidStateException;

	public abstract S buildSpace(T time, List<E> initialCondition) throws InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException;

	public abstract U buildUniverse(S space, T time) throws InvalidSpaceException, InvalidTimeException;

	public abstract Simulation buildSimulation(U universe, C cellularAutomaton)
			throws InvalidUniverseException, InvalidCellularAutomataException;

}
