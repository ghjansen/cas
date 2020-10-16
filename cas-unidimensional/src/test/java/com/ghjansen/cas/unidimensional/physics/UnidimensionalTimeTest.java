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

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.exception.InvalidAbsoluteTimeLimitException;
import com.ghjansen.cas.core.exception.InvalidRelativeTimeLimitException;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;
import com.ghjansen.cas.core.physics.Time;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalTimeTest {

	@Test
	public void unidimensionalTimeConstructor()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException {
		final int absoluteLimit = 1000;
		final int relativeLimit = 1000;
		final UnidimensionalTime unidimensionalTime = new UnidimensionalTime(absoluteLimit, relativeLimit);
		Assert.assertTrue(unidimensionalTime.getLimit() == absoluteLimit);
		Assert.assertTrue(unidimensionalTime.getRelative().size() == 1);
		Assert.assertTrue(unidimensionalTime.getRelative().get(0).getLimit() == relativeLimit);
	}

	@Test
	public void unidimensionalTimeincrease()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, TimeLimitReachedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = 1000;
		final int timeEvolutionLimit = (absoluteLimit * relativeLimit) - 1;
		final UnidimensionalTime dimensionalTime = new UnidimensionalTime(absoluteLimit, relativeLimit);
		for (int i = 0; i < timeEvolutionLimit; i++) {
			dimensionalTime.increase();
		}
		Assert.assertTrue(dimensionalTime.getAbsolute() == absoluteLimit - 1);
		Assert.assertTrue(dimensionalTime.getRelative().get(0).getAbsolute() == relativeLimit - 1);
	}

}
