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

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.exception.InvalidAbsoluteTimeLimitException;
import com.ghjansen.cas.core.exception.InvalidRelativeTimeLimitException;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class TimeTest {

	@Test
	public void unlimitedTimeContructor() {
		final Time unlimitedTime = new UnlimitedTime();
		Assert.assertTrue(unlimitedTime.getAbsolute() == 0);
		Assert.assertNull(unlimitedTime.getRelative());
	}

	@Test
	public void unlimitedTimeIncreasing() throws TimeLimitReachedException {
		final int amount = 1000;
		final Time unlimitedTime = new UnlimitedTime();
		for (int i = 0; i < amount; i++) {
			unlimitedTime.increase();
		}
		Assert.assertTrue(unlimitedTime.getAbsolute() == amount);
		Assert.assertNull(unlimitedTime.getRelative());
	}

	@Test
	public void unlimitedTimeReset() throws TimeLimitReachedException {
		final int amount = 1000;
		final int reset = 0;
		final Time unlimitedTime = new UnlimitedTime();
		for (int i = 0; i < amount; i++) {
			unlimitedTime.increase();
		}
		Assert.assertTrue(unlimitedTime.getAbsolute() == amount);
		unlimitedTime.resetAbsoluteTime();
		Assert.assertTrue(unlimitedTime.getAbsolute() == reset);
		Assert.assertNull(unlimitedTime.getRelative());
	}

	@Test
	public void limitedTimeConstructor() throws InvalidAbsoluteTimeLimitException {
		final int limit = 10;
		final Time limitedTime = new LimitedTime(limit);
		Assert.assertNotNull(limitedTime);
		Assert.assertTrue(limitedTime.getAbsolute() == 0);
		Assert.assertNull(limitedTime.getRelative());
	}

	@Test(expected = InvalidAbsoluteTimeLimitException.class)
	public void limitedTimeConstructorZero() throws InvalidAbsoluteTimeLimitException {
		final int limit = 0;
		new LimitedTime(limit);
	}

	@Test(expected = InvalidAbsoluteTimeLimitException.class)
	public void limitedTimeConstructorNegative() throws InvalidAbsoluteTimeLimitException {
		final int limit = -1;
		new LimitedTime(limit);
	}

	@Test
	public void limitedTimeIncreasing() throws InvalidAbsoluteTimeLimitException, TimeLimitReachedException {
		final int limit = 1000;
		final int iterationLimit = limit - 1;
		final Time limitedTime = new LimitedTime(limit);
		for (int i = 0; i < iterationLimit; i++) {
			limitedTime.increase();
		}
		Assert.assertTrue(limitedTime.getAbsolute() == limit - 1);
		Assert.assertNull(limitedTime.getRelative());
	}

	@Test(expected = TimeLimitReachedException.class)
	public void limitedTimeIncreasingBeyondLimit() throws InvalidAbsoluteTimeLimitException, TimeLimitReachedException {
		final int limit = 1000;
		final int amount = 1000;
		final Time limitedTime = new LimitedTime(limit);
		for (int i = 0; i < amount; i++) {
			limitedTime.increase();
		}
	}

	@Test
	public void dimensionalTimeConstructor()
			throws InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = 1000;
		final Time dimensionalTime = new DimensionalTime(absoluteLimit, relativeLimit);
		Assert.assertNotNull(dimensionalTime);
		Assert.assertTrue(dimensionalTime.getAbsolute() == 0);
		Assert.assertNotNull(dimensionalTime.getRelative());
		Assert.assertTrue(dimensionalTime.getRelative().size() == 1);
		Assert.assertNotNull(dimensionalTime.getRelative().get(0));
		Assert.assertTrue(dimensionalTime.getRelative().get(0).getClass().equals(DimensionalTime.class));
	}

	@Test(expected = InvalidRelativeTimeLimitException.class)
	public void dimensionalTimeConstructorNoLimits()
			throws InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		new DimensionalTime(absoluteLimit);
	}

	@Test(expected = InvalidRelativeTimeLimitException.class)
	public void dimensionalTimeConstructorZeroLimits()
			throws InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = 0;
		new DimensionalTime(absoluteLimit, relativeLimit);
	}

	@Test(expected = InvalidRelativeTimeLimitException.class)
	public void dimensionalTimeConstructorNegativeLimits()
			throws InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = -1;
		new DimensionalTime(absoluteLimit, relativeLimit);
	}

	@Test
	public void dimensionalTimeIncrease() throws InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException,
			TimeLimitReachedException, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = 1000;
		final int timeEvolutionLimit = (absoluteLimit * relativeLimit) - 1;
		final Time dimensionalTime = new DimensionalTime(absoluteLimit, relativeLimit);
		for (int i = 0; i < timeEvolutionLimit; i++) {
			dimensionalTime.increase();
		}
		Assert.assertTrue(dimensionalTime.getAbsolute() == absoluteLimit - 1);
		Assert.assertTrue(dimensionalTime.getRelative().get(0).getAbsolute() == relativeLimit - 1);
	}

	@Test(expected = TimeLimitReachedException.class)
	public void dimensionalTimeIncreasingBeyondLimits() throws CloneNotSupportedException,
			InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, TimeLimitReachedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = 1000;
		final int timeEvolutionLimit = (absoluteLimit * relativeLimit);
		final Time dimensionalTime = new DimensionalTime(absoluteLimit, relativeLimit);
		for (int i = 0; i < timeEvolutionLimit; i++) {
			dimensionalTime.increase();
		}
	}
}
