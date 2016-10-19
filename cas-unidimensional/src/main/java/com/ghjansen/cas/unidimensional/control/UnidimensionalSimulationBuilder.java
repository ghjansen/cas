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

package com.ghjansen.cas.unidimensional.control;

import java.util.ArrayList;
import java.util.List;

import com.ghjansen.cas.control.parameter.SequenceParameter;
import com.ghjansen.cas.control.simulation.SimulationBuilder;
import com.ghjansen.cas.control.simulation.SimulationParameter;
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
import com.ghjansen.cas.unidimensional.ca.UnidimensionalCellularAutomaton;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalCombination;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalGeneralRule;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalState;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalTransition;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalCell;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalSpace;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalTime;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalUniverse;
import com.ghjansen.cas.unidimensional.simulation.UnidimensionalSimulation;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalSimulationBuilder extends SimulationBuilder {

	public UnidimensionalSimulationBuilder(SimulationParameter simulationParameter) {
		super(simulationParameter);
	}

	@Override
	public List<State> buildStates() {
		List<State> states = new ArrayList<State>();
		states.add(new UnidimensionalState("white", 0));
		states.add(new UnidimensionalState("black", 1));
		if (!this.getSimulationParameter().getRuleTypeParameter().isElementar()) {
			throw new IllegalArgumentException();
		}
		return states;
	}

	@Override
	public List<Combination> buildCombinations(List<State> states) throws InvalidStateException {
		List<Combination> combinations = new ArrayList<Combination>();
		State whiteState = states.get(0);
		State blackState = states.get(1);
		combinations.add(new UnidimensionalCombination(blackState, blackState, blackState));
		combinations.add(new UnidimensionalCombination(blackState, blackState, whiteState));
		combinations.add(new UnidimensionalCombination(whiteState, blackState, blackState));
		combinations.add(new UnidimensionalCombination(whiteState, blackState, whiteState));
		combinations.add(new UnidimensionalCombination(blackState, whiteState, blackState));
		combinations.add(new UnidimensionalCombination(blackState, whiteState, whiteState));
		combinations.add(new UnidimensionalCombination(whiteState, whiteState, blackState));
		combinations.add(new UnidimensionalCombination(whiteState, whiteState, whiteState));
		return combinations;
	}

	@Override
	public List<Transition> buildTransitions(List<State> states, List<Combination> combinations)
			throws InvalidCombinationException, InvalidStateException {
		List<Transition> transitions = new ArrayList<Transition>();
		State whiteState = states.get(0);
		State blackState = states.get(1);
		int value0 = this.getSimulationParameter().getRuleConfigurationParameter().getStateValues()[0];
		int value1 = this.getSimulationParameter().getRuleConfigurationParameter().getStateValues()[1];
		int value2 = this.getSimulationParameter().getRuleConfigurationParameter().getStateValues()[2];
		int value3 = this.getSimulationParameter().getRuleConfigurationParameter().getStateValues()[3];
		int value4 = this.getSimulationParameter().getRuleConfigurationParameter().getStateValues()[4];
		int value5 = this.getSimulationParameter().getRuleConfigurationParameter().getStateValues()[5];
		int value6 = this.getSimulationParameter().getRuleConfigurationParameter().getStateValues()[6];
		int value7 = this.getSimulationParameter().getRuleConfigurationParameter().getStateValues()[7];
		transitions.add(new UnidimensionalTransition(combinations.get(0), getStateFromValue(value0, whiteState, blackState)));
		transitions.add(new UnidimensionalTransition(combinations.get(1), getStateFromValue(value1, whiteState, blackState)));
		transitions.add(new UnidimensionalTransition(combinations.get(2), getStateFromValue(value2, whiteState, blackState)));
		transitions.add(new UnidimensionalTransition(combinations.get(3), getStateFromValue(value3, whiteState, blackState)));
		transitions.add(new UnidimensionalTransition(combinations.get(4), getStateFromValue(value4, whiteState, blackState)));
		transitions.add(new UnidimensionalTransition(combinations.get(5), getStateFromValue(value5, whiteState, blackState)));
		transitions.add(new UnidimensionalTransition(combinations.get(6), getStateFromValue(value6, whiteState, blackState)));
		transitions.add(new UnidimensionalTransition(combinations.get(7), getStateFromValue(value7, whiteState, blackState)));
		return transitions;
	}
	
	private State getStateFromValue(int value, State whiteState, State blackState){
		switch(value){
		case 0:
			return whiteState;
		case 1:
			return blackState;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public Rule buildRule(List<Transition> transitions) throws InvalidTransitionException {
		Transition[] transitionsArray = new Transition[transitions.size()];
		transitions.toArray(transitionsArray);
		return new UnidimensionalGeneralRule(transitionsArray);
	}

	@Override
	public CellularAutomaton buildCellularAutomaton(Rule rule) throws InvalidRuleException {
		return new UnidimensionalCellularAutomaton(rule);
	}

	@Override
	public Time buildTime()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException {
		int absoluteLimit = this.getSimulationParameter().getLimitsParameter().getIterations();
		int relativeLimit = this.getSimulationParameter().getLimitsParameter().getCells();
		return new UnidimensionalTime(absoluteLimit, relativeLimit);
	}

	@Override
	public List<Cell> buildInitialCondition(List<State> states) throws InvalidStateException {
		List<Cell> initialCondition = new ArrayList<Cell>();
		State whiteState = states.get(0);
		State blackState = states.get(1);
		List<SequenceParameter> sequences = this.getSimulationParameter().getInitialConditionParameter().getSequences();
		for (SequenceParameter sequence : sequences) {
			int amount = sequence.getFinalPosition() - sequence.getInitialPosition() + 1;
			State state;
			if (sequence.getValue() == 0) {
				state = whiteState;
			} else if (sequence.getValue() == 1) {
				state = blackState;
			} else {
				throw new IllegalArgumentException();
			}
			for (int i = 0; i < amount; i++) {
				initialCondition.add(new UnidimensionalCell(state));
			}
		}
		return initialCondition;
	}

	@Override
	public Space buildSpace(Time time, List<Cell> initialCondition) throws InvalidDimensionalAmountException,
			InvalidInitialConditionException, InvalidDimensionalSpaceException {
		return new UnidimensionalSpace(time, initialCondition);
	}

	@Override
	public Universe buildUniverse(Space space, Time time) throws InvalidSpaceException, InvalidTimeException {
		return new UnidimensionalUniverse(space, time);
	}

	@Override
	public Simulation buildSimulation(Universe universe, CellularAutomaton cellularAutomaton)
			throws InvalidUniverseException, InvalidCellularAutomataException {
		return new UnidimensionalSimulation(universe, cellularAutomaton);
	}

}
