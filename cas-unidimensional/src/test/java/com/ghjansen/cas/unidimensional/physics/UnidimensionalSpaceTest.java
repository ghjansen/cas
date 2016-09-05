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
import com.ghjansen.cas.core.physics.Cell;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;
import com.ghjansen.cas.core.physics.exception.space.InvalidDimensionalAmount;
import com.ghjansen.cas.core.physics.exception.space.InvalidDimensionalSpace;
import com.ghjansen.cas.core.physics.exception.space.InvalidInitialCondition;
import com.ghjansen.cas.core.physics.exception.time.InvalidAbsoluteTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.InvalidRelativeTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.TimeLimitReached;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalCombination;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalState;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalTransition;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalSpaceTest {

	@Test
	public void unidimensionalConstructor() throws CloneNotSupportedException, InvalidAbsoluteTimeLimit,
			InvalidRelativeTimeLimit, InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace {
		Time time = new UnidimensionalTime(1000, 1000);
		Cell cell = getNewWhiteUnidimensionalCell(new UnidimensionalState("black", 0),
				new UnidimensionalState("white", 1));
		List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(cell);
		Space space = new UnidimensionalSpace(time, initialCondition);
		Assert.assertTrue(space.getInitial().equals(initialCondition));
		Assert.assertNull(space.getHistory());
		Assert.assertNull(space.getLast());
		Assert.assertNull(space.getCurrent());
		Assert.assertTrue(space.getDimensionalAmout() == 1);
		Assert.assertTrue(space.isKeepHistory());
	}

	@Test
	public void undimensionalGetCombination()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit,
			InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace, TimeLimitReached {
		Time time = new UnidimensionalTime(3, 3);
		State black = new UnidimensionalState("black", 0);
		State white = new UnidimensionalState("white", 1);
		List<Cell> initialCondition = new ArrayList<Cell>();
		initialCondition.add(getNewBlackUnidimensionalCell(black, white));
		initialCondition.add(getNewWhiteUnidimensionalCell(black, white));
		initialCondition.add(getNewBlackUnidimensionalCell(black, white));
		Space space = new UnidimensionalSpace(time, initialCondition);
		Combination resultCombination = space.getCombination(time);
		Assert.assertTrue(resultCombination.getReferenceState().equals(black));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(black));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(white));
		space.setState(time, getNewWhiteUnidimensionalTransition(black, white));
		time.increase();
		resultCombination = space.getCombination(time);
		Assert.assertTrue(resultCombination.getReferenceState().equals(white));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(black));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(black));
		space.setState(time, getNewBlackUnidimensionalTransition(black, white));
		time.increase();
		resultCombination = space.getCombination(time);
		Assert.assertTrue(resultCombination.getReferenceState().equals(black));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(white));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(black));
		space.setState(time, getNewWhiteUnidimensionalTransition(black, white));
		time.increase();
		
		/*
		resultCombination = space.getCombination(time);
		Assert.assertTrue(resultCombination.getReferenceState().equals(cell01.getState()));
		Assert.assertTrue(resultCombination.getNeighborhood().get(0).equals(cell00.getState()));
		Assert.assertTrue(resultCombination.getNeighborhood().get(1).equals(cell02.getState()));
		*/
		
	}

	private Cell getNewWhiteUnidimensionalCell(State black, State white) {
		Combination combination = new UnidimensionalCombination(black, white, white);
		Transition transition = new UnidimensionalTransition(combination, white);
		return new UnidimensionalCell(transition);
	}

	private Cell getNewBlackUnidimensionalCell(State black, State white) {
		Combination combination = new UnidimensionalCombination(white, black, black);
		Transition transition = new UnidimensionalTransition(combination, black);
		return new UnidimensionalCell(transition);
	}
	
	private Transition getNewWhiteUnidimensionalTransition(State black, State white) {
		Combination combination = new UnidimensionalCombination(black, white, white);
		return new UnidimensionalTransition(combination, white);
	}

	private Transition getNewBlackUnidimensionalTransition(State black, State white) {
		Combination combination = new UnidimensionalCombination(white, black, black);
		return new UnidimensionalTransition(combination, black);
	}

}
