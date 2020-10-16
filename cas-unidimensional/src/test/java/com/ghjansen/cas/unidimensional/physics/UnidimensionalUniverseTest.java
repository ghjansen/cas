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
import com.ghjansen.cas.core.exception.InvalidAbsoluteTimeLimitException;
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
import com.ghjansen.cas.core.physics.Cell;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;
import com.ghjansen.cas.core.physics.Universe;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalCombination;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalState;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalTransition;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalUniverseTest {

	@Test
	public void unidimensionalUniverseConstructor()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException,
			InvalidStateException, InvalidCombinationException, InvalidTransitionException,
			InvalidDimensionalAmountException, InvalidInitialConditionException, InvalidDimensionalSpaceException,
			InvalidRuleException, InvalidSpaceException, InvalidTimeException {
		final UnidimensionalTime unidimensionalTime = new UnidimensionalTime(3, 3);
		final UnidimensionalState unidimensionalBlackState = new UnidimensionalState("black", 0);
		final UnidimensionalState unidimensionalWhiteState = new UnidimensionalState("white", 1);
		final List<UnidimensionalCell> initialCondition = new ArrayList<UnidimensionalCell>();
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState, unidimensionalWhiteState));
		initialCondition.add(getNewWhiteUnidimensionalCell(unidimensionalBlackState, unidimensionalWhiteState));
		initialCondition.add(getNewBlackUnidimensionalCell(unidimensionalBlackState, unidimensionalWhiteState));
		final UnidimensionalSpace unidimensionalSpace = new UnidimensionalSpace(unidimensionalTime, initialCondition);
		final UnidimensionalUniverse unidimensionalUniverse = new UnidimensionalUniverse(unidimensionalSpace, unidimensionalTime);
		Assert.assertTrue(unidimensionalUniverse.getSpace().equals(unidimensionalSpace));
		Assert.assertTrue(unidimensionalUniverse.getTime().equals(unidimensionalTime));
	}

	private UnidimensionalCell getNewWhiteUnidimensionalCell(UnidimensionalState black, UnidimensionalState white)
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException {
		final UnidimensionalCombination unidimensionalCombination = new UnidimensionalCombination(black, white, white);
		final UnidimensionalTransition unidimensionalTransition = new UnidimensionalTransition(unidimensionalCombination, white);
		return new UnidimensionalCell(unidimensionalTransition);
	}

	private UnidimensionalCell getNewBlackUnidimensionalCell(UnidimensionalState black, UnidimensionalState white)
			throws InvalidStateException, InvalidCombinationException, InvalidTransitionException {
		final UnidimensionalCombination unidimensionalCombination = new UnidimensionalCombination(white, black, black);
		final UnidimensionalTransition unidimensionalTransition = new UnidimensionalTransition(unidimensionalCombination, black);
		return new UnidimensionalCell(unidimensionalTransition);
	}

}
