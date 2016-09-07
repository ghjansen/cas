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

package com.ghjansen.cas.unidimensional.physics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidAbsoluteTimeLimit;
import com.ghjansen.cas.core.exception.InvalidCombination;
import com.ghjansen.cas.core.exception.InvalidDimensionalAmount;
import com.ghjansen.cas.core.exception.InvalidDimensionalSpace;
import com.ghjansen.cas.core.exception.InvalidInitialCondition;
import com.ghjansen.cas.core.exception.InvalidRelativeTimeLimit;
import com.ghjansen.cas.core.exception.InvalidState;
import com.ghjansen.cas.core.exception.InvalidTransition;
import com.ghjansen.cas.core.exception.TimeLimitReached;
import com.ghjansen.cas.core.physics.Cell;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalCombination;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalState;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalTransition;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalSpaceTest {

	@Test
	public void unidimensionalSpaceConstructor() throws CloneNotSupportedException, InvalidAbsoluteTimeLimit,
			InvalidRelativeTimeLimit, InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace,
			InvalidState, InvalidCombination, InvalidTransition {
		final Time unidimensionalTime = new UnidimensionalTime(1000, 1000);
		final Cell unidimensionalCell = getNewWhiteUnidimensionalCell(new UnidimensionalState("black", 0),
				new UnidimensionalState("white", 1));
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(unidimensionalCell);
		final Space unidimensionalSpace = new UnidimensionalSpace(unidimensionalTime, initialCondition);
		Assert.assertTrue(unidimensionalSpace.getInitial().equals(initialCondition));
		Assert.assertNotNull(unidimensionalSpace.getHistory());
		Assert.assertNull(unidimensionalSpace.getLast());
		Assert.assertNotNull(unidimensionalSpace.getCurrent());
		Assert.assertTrue(unidimensionalSpace.getDimensionalAmout() == 1);
		Assert.assertTrue(unidimensionalSpace.isKeepHistory());
	}

	@Test
	public void unidimensionalGetCombination() throws CloneNotSupportedException, InvalidAbsoluteTimeLimit,
			InvalidRelativeTimeLimit, InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace,
			TimeLimitReached, InvalidState, InvalidCombination, InvalidTransition {
		final Time unidimensionalTime = new UnidimensionalTime(3, 3);
		final State unidimensionalBlackState = new UnidimensionalState("black", 0);
		final State unidimensionalWhiteState = new UnidimensionalState("white", 1);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState, unidimensionalWhiteState));
		initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalBlackState, unidimensionalWhiteState));
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState, unidimensionalWhiteState));
		final Space unidimensionalSpace = new UnidimensionalSpace(unidimensionalTime, initialCondition);
		// Assert initial condition
		Combination resultCombination = unidimensionalSpace.getCombination(unidimensionalTime);
		Assert.assertTrue(resultCombination.getReferenceState().equals(unidimensionalBlackState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(unidimensionalBlackState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(unidimensionalWhiteState));
		unidimensionalSpace.setState(unidimensionalTime,
				getNewWhiteUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		resultCombination = unidimensionalSpace.getCombination(unidimensionalTime);
		Assert.assertTrue(resultCombination.getReferenceState().equals(unidimensionalWhiteState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(unidimensionalBlackState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(unidimensionalBlackState));
		unidimensionalSpace.setState(unidimensionalTime,
				getNewBlackUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		resultCombination = unidimensionalSpace.getCombination(unidimensionalTime);
		Assert.assertTrue(resultCombination.getReferenceState().equals(unidimensionalBlackState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(unidimensionalWhiteState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(unidimensionalBlackState));
		unidimensionalSpace.setState(unidimensionalTime,
				getNewWhiteUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		// Assert first iteration
		resultCombination = unidimensionalSpace.getCombination(unidimensionalTime);
		Assert.assertTrue(resultCombination.getReferenceState().equals(unidimensionalWhiteState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(unidimensionalWhiteState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(unidimensionalBlackState));
		unidimensionalSpace.setState(unidimensionalTime,
				getNewBlackUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		resultCombination = unidimensionalSpace.getCombination(unidimensionalTime);
		Assert.assertTrue(resultCombination.getReferenceState().equals(unidimensionalBlackState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(unidimensionalWhiteState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(unidimensionalWhiteState));
		unidimensionalSpace.setState(unidimensionalTime,
				getNewWhiteUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		resultCombination = unidimensionalSpace.getCombination(unidimensionalTime);
		Assert.assertTrue(resultCombination.getReferenceState().equals(unidimensionalWhiteState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(unidimensionalBlackState));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(unidimensionalWhiteState));
		unidimensionalSpace.setState(unidimensionalTime,
				getNewBlackUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		// Assert second iteration
		Assert.assertTrue(((Cell) unidimensionalSpace.getCurrent().get(0)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getCurrent().get(1)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getCurrent().get(2)).getState().equals(unidimensionalBlackState));
	}

	@Test
	public void unidimensionalSetState() throws CloneNotSupportedException, InvalidAbsoluteTimeLimit,
			InvalidRelativeTimeLimit, InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace,
			TimeLimitReached, InvalidState, InvalidCombination, InvalidTransition {
		final Time unidimensionalTime = new UnidimensionalTime(3, 3);
		final State unidimensionalBlackState = new UnidimensionalState("black", 0);
		final State unidimensionalWhiteState = new UnidimensionalState("white", 1);
		final List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState, unidimensionalWhiteState));
		initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalBlackState, unidimensionalWhiteState));
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState, unidimensionalWhiteState));
		final Space unidimensionalSpace = new UnidimensionalSpace(unidimensionalTime, initialCondition);
		unidimensionalSpace.setState(unidimensionalTime,
				getNewWhiteUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		unidimensionalSpace.setState(unidimensionalTime,
				getNewBlackUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		unidimensionalSpace.setState(unidimensionalTime,
				getNewWhiteUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		unidimensionalSpace.setState(unidimensionalTime,
				getNewBlackUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		unidimensionalSpace.setState(unidimensionalTime,
				getNewWhiteUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		unidimensionalSpace.setState(unidimensionalTime,
				getNewBlackUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		unidimensionalSpace.setState(unidimensionalTime,
				getNewWhiteUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		unidimensionalSpace.setState(unidimensionalTime,
				getNewBlackUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		unidimensionalTime.increase();
		unidimensionalSpace.setState(unidimensionalTime,
				getNewWhiteUnidimensionalTransition(unidimensionalBlackState, unidimensionalWhiteState));
		// Assert initial condition
		Assert.assertTrue(((Cell) unidimensionalSpace.getInitial().get(0)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getInitial().get(1)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getInitial().get(2)).getState().equals(unidimensionalBlackState));
		// Assert history
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(0).get(0)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(0).get(1)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(0).get(2)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(1).get(0)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(1).get(1)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(
				((Cell) unidimensionalSpace.getHistory().get(1).get(2)).getState().equals(unidimensionalBlackState));
		// Assert last
		Assert.assertTrue(((Cell) unidimensionalSpace.getLast().get(0)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getLast().get(1)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getLast().get(2)).getState().equals(unidimensionalBlackState));
		// Assert current
		Assert.assertTrue(((Cell) unidimensionalSpace.getCurrent().get(0)).getState().equals(unidimensionalWhiteState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getCurrent().get(1)).getState().equals(unidimensionalBlackState));
		Assert.assertTrue(((Cell) unidimensionalSpace.getCurrent().get(2)).getState().equals(unidimensionalWhiteState));

	}

	private Cell getNewWhiteUnidimensionalCell(State black, State white)
			throws InvalidState, InvalidCombination, InvalidTransition {
		final Combination unidimensionalCombination = new UnidimensionalCombination(black, white, white);
		final Transition unidimensionalTransition = new UnidimensionalTransition(unidimensionalCombination, white);
		return new UnidimensionalCell(unidimensionalTransition);
	}

	private Cell getNewBlackUnidimensionalCell(State black, State white)
			throws InvalidState, InvalidCombination, InvalidTransition {
		final Combination unidimensionalCombination = new UnidimensionalCombination(white, black, black);
		final Transition unidimensionalTransition = new UnidimensionalTransition(unidimensionalCombination, black);
		return new UnidimensionalCell(unidimensionalTransition);
	}

	private Transition getNewWhiteUnidimensionalTransition(State black, State white)
			throws InvalidState, InvalidCombination {
		final Combination unidimensionalCombination = new UnidimensionalCombination(black, white, white);
		return new UnidimensionalTransition(unidimensionalCombination, white);
	}

	private Transition getNewBlackUnidimensionalTransition(State black, State white)
			throws InvalidState, InvalidCombination {
		final Combination unidimensionalCombination = new UnidimensionalCombination(white, black, black);
		return new UnidimensionalTransition(unidimensionalCombination, black);
	}

}
