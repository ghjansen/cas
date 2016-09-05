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

package com.ghjansen.cas.core.physics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.ghjansen.cas.core.ca.Combination;
import com.ghjansen.cas.core.ca.DimensionalCombination;
import com.ghjansen.cas.core.ca.DimensionalState;
import com.ghjansen.cas.core.ca.DimensionalTransition;
import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.physics.exception.space.InvalidDimensionalAmount;
import com.ghjansen.cas.core.physics.exception.space.InvalidDimensionalSpace;
import com.ghjansen.cas.core.physics.exception.space.InvalidInitialCondition;
import com.ghjansen.cas.core.physics.exception.time.InvalidAbsoluteTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.InvalidRelativeTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.TimeLimitReached;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class SpaceTest {

	@Test
	public void dimensionalSpaceConstructor() throws CloneNotSupportedException, InvalidAbsoluteTimeLimit,
			InvalidRelativeTimeLimit, InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace {
		Time time = new DimensionalTime(1000, 1000);
		Cell cell = getNewValidDimensionalCell();
		ArrayList<Cell> firstDimension = new ArrayList<Cell>();
		firstDimension.add(cell);
		new DimensionalSpace(time, firstDimension, true);
	}

	@Test(expected = InvalidInitialCondition.class)
	public void dimensionalSpaceConstructorInvalidInitialCondition()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit,
			InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace {
		Time time = new DimensionalTime(1000, 1000);
		new DimensionalSpace(time, null, true);
	}

	@Test(expected = InvalidDimensionalSpace.class)
	public void dimensionalSpaceConstructorInvalidDimensionalSpace()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit,
			InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace {
		Time time = new DimensionalTime(1000, 1000);
		Cell cell = getNewValidDimensionalCell();
		ArrayList<List> firstDimension = new ArrayList<List>();
		ArrayList<Cell> secondDimension = new ArrayList<Cell>();
		firstDimension.add(secondDimension);
		secondDimension.add(cell);
		new DimensionalSpace(time, firstDimension, true);
	}

	@Test(expected = InvalidDimensionalAmount.class)
	public void dimensionalSpaceConstructorInvalidDimensionalAmount() throws InvalidAbsoluteTimeLimit,
			InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace {
		Time time = new LimitedTime(1000);
		new DimensionalSpace(time, null, false);
	}

	@Test
	public void getCombination() throws CloneNotSupportedException, InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit,
			InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace, TimeLimitReached {
		Time time = new DimensionalTime(3, 1);
		Cell cell = getNewValidDimensionalCell();
		ArrayList<Cell> firstDimension = new ArrayList<Cell>();
		firstDimension.add(cell);
		Space mockedSpace = spy(new DimensionalSpace(time, firstDimension, true));
		mockedSpace.getCombination(time);
		time.increase();
		mockedSpace.getCombination(time);
		time.increase();
		mockedSpace.getCombination(time);
		ArgumentCaptor<Time> argumentTime = ArgumentCaptor.forClass(Time.class);
		ArgumentCaptor<List> argumentList = ArgumentCaptor.forClass(List.class);
		verify(mockedSpace, times(3)).getCombination(argumentTime.capture(), argumentList.capture());
		List expected = mockedSpace.getInitial();
		assertEquals(expected, argumentList.getAllValues().get(0));
		expected = mockedSpace.getLast();
		assertEquals(expected, argumentList.getAllValues().get(1));
		assertEquals(expected, argumentList.getAllValues().get(2));
	}

	@Test
	public void setState() throws CloneNotSupportedException, InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit,
			InvalidDimensionalAmount, InvalidInitialCondition, InvalidDimensionalSpace, TimeLimitReached {
		Time time = new DimensionalTime(3, 2);
		State black = new DimensionalState("black", 0);
		State white = new DimensionalState("white", 1);
		Combination combination = new DimensionalCombination(white, black, black);
		Transition transition = new DimensionalTransition(combination, black);
		Cell cell = new DimensionalCell(transition);
		ArrayList<Cell> firstDimension = new ArrayList<Cell>();
		firstDimension.add(cell);
		Space mockedDimensionalSpace = spy(new DimensionalSpace(time, firstDimension, true));
		mockedDimensionalSpace.setState(time, transition);
		time.increase();
		mockedDimensionalSpace.setState(time, transition);
		time.increase();
		mockedDimensionalSpace.setState(time, transition);
		time.increase();
		mockedDimensionalSpace.setState(time, transition);
		time.increase();
		mockedDimensionalSpace.setState(time, transition);
		time.increase();
		mockedDimensionalSpace.setState(time, transition);
		List<List> history = mockedDimensionalSpace.getHistory();
		List<?> current = mockedDimensionalSpace.getCurrent();
		List<?> last = mockedDimensionalSpace.getLast();
		boolean keepHistory = mockedDimensionalSpace.isKeepHistory();
//		verify(mockedDimensionalSpace, times(1)).initialize();
		verify(mockedDimensionalSpace, times(2)).createNewIteration(time);
		verify(mockedDimensionalSpace, times(6)).createNewCell(time, transition);
		//TODO refactor this test and make sure space rotation is working
	}

	private Cell getNewValidDimensionalCell() {
		State black = new DimensionalState("black", 0);
		State white = new DimensionalState("white", 1);
		Combination combination = new DimensionalCombination(white, black, black);
		Transition transition = new DimensionalTransition(combination, black);
		return new DimensionalCell(transition);
	}

}
