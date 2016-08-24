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

import com.ghjansen.cas.core.physics.exception.time.InvalidAbsoluteTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.InvalidRelativeTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.TimeLimitReached;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class TimeTest {

	@Test
	public void unlimitedTimeContructor() {
		final UnlimitedTime ut = new UnlimitedTime();
		Assert.assertTrue(ut.getAbsolute() == 0);
		Assert.assertNull(ut.getRelative());
	}

	@Test
	public void unlimitedTimeIncreasing() throws TimeLimitReached {
		final int amount = 1000;
		UnlimitedTime ut = new UnlimitedTime();
		for (int i = 0; i < amount; i++) {
			ut.increase();
		}
		Assert.assertTrue(ut.getAbsolute() == amount);
		Assert.assertNull(ut.getRelative());
	}

	@Test
	public void unlimitedTimeReset() throws TimeLimitReached {
		final int amount = 1000;
		final int reset = 0;
		UnlimitedTime ut = new UnlimitedTime();
		for (int i = 0; i < amount; i++) {
			ut.increase();
		}
		Assert.assertTrue(ut.getAbsolute() == amount);
		ut.resetAbsoluteTime();
		Assert.assertTrue(ut.getAbsolute() == reset);
		Assert.assertNull(ut.getRelative());
	}

	@Test
	public void limitedTimeConstructor() throws InvalidAbsoluteTimeLimit {
		final int limit = 10;
		final LimitedTime lt = new LimitedTime(limit);
		Assert.assertNotNull(lt);
		Assert.assertTrue(lt.getAbsolute() == 0);
		Assert.assertNull(lt.getRelative());
	}

	@Test(expected = InvalidAbsoluteTimeLimit.class)
	public void limitedTimeConstructorZero() throws InvalidAbsoluteTimeLimit {
		final int limit = 0;
		new LimitedTime(limit);
	}

	@Test(expected = InvalidAbsoluteTimeLimit.class)
	public void limitedTimeConstructorNegative() throws InvalidAbsoluteTimeLimit {
		final int limit = -1;
		new LimitedTime(limit);
	}

	@Test
	public void limitedTimeIncreasing() throws InvalidAbsoluteTimeLimit, TimeLimitReached {
		final int limit = 1000;
		final int iterationLimit = limit - 1;
		final LimitedTime lt = new LimitedTime(limit);
		for (int i = 0; i < iterationLimit; i++) {
			lt.increase();
		}
		Assert.assertTrue(lt.getAbsolute() == limit - 1);
		Assert.assertNull(lt.getRelative());
	}

	@Test(expected = TimeLimitReached.class)
	public void limitedTimeIncreasingBeyondLimit() throws InvalidAbsoluteTimeLimit, TimeLimitReached {
		final int limit = 1000;
		final int amount = 1000;
		final LimitedTime lt = new LimitedTime(limit);
		for (int i = 0; i < amount; i++) {
			lt.increase();
		}
	}

	@Test
	public void dimensionalTimeConstructor()
			throws InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = 1000;
		DimensionalTime dt = new DimensionalTime(absoluteLimit, relativeLimit);
		Assert.assertNotNull(dt);
		Assert.assertTrue(dt.getAbsolute() == 0);
		Assert.assertNotNull(dt.getRelative());
		Assert.assertTrue(dt.getRelative().size() == 1);
		Assert.assertNotNull(dt.getRelative().get(0));
		Assert.assertTrue(dt.getRelative().get(0).getClass().equals(DimensionalTime.class));
	}

	@Test(expected = InvalidRelativeTimeLimit.class)
	public void dimensionalTimeConstructorNoLimits()
			throws InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		new DimensionalTime(absoluteLimit);
	}

	@Test(expected = InvalidRelativeTimeLimit.class)
	public void dimensionalTimeConstructorZeroLimits()
			throws InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = 0;
		new DimensionalTime(absoluteLimit, relativeLimit);
	}

	@Test(expected = InvalidRelativeTimeLimit.class)
	public void dimensionalTimeConstructorNegativeLimits()
			throws InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = -1;
		new DimensionalTime(absoluteLimit, relativeLimit);
	}

	@Test
	public void dimensionalTimeIncreasing()
			throws InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit, TimeLimitReached, CloneNotSupportedException {
		final int absoluteLimit = 1000;
		final int relativeLimit = 1000;
		final int timeEvolutionLimit = (absoluteLimit * relativeLimit) - 1;
		DimensionalTime dt = new DimensionalTime(absoluteLimit, relativeLimit);
		for (int i = 0; i < timeEvolutionLimit; i++) {
			dt.increase();
		}
		Assert.assertTrue(dt.getAbsolute() == absoluteLimit - 1);
		Assert.assertTrue(dt.getRelative().get(0).getAbsolute() == relativeLimit - 1);
	}

	@Test(expected = TimeLimitReached.class)
	public void dimensionalTimeIncreasingBeyondLimits()
			throws CloneNotSupportedException, InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit, TimeLimitReached {
		final int absoluteLimit = 1000;
		final int relativeLimit = 1000;
		final int timeEvolutionLimit = (absoluteLimit * relativeLimit);
		DimensionalTime dt = new DimensionalTime(absoluteLimit, relativeLimit);
		for (int i = 0; i < timeEvolutionLimit; i++) {
			dt.increase();
		}
	}
}

final class UnlimitedTime extends Time {

	public UnlimitedTime() {
		super();
	}

}

final class LimitedTime extends Time {

	public LimitedTime(int limit) throws InvalidAbsoluteTimeLimit {
		super(limit);
	}

}

final class DimensionalTime extends Time {

	public DimensionalTime(final int limit, int... limits)
			throws InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit, CloneNotSupportedException {
		super(limit, limits);
	}

}
