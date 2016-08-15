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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ghjansen.cas.core.physics.exception.time.InvalidAbsoluteTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.InvalidRelativeTimeClass;
import com.ghjansen.cas.core.physics.exception.time.InvalidRelativeTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.TimeLimitReached;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Time {

	private AtomicInteger absoluteTime;
	private List<Time> relativeTime;
	private AtomicInteger limit;

	public Time() {
		this.absoluteTime = new AtomicInteger();
	}

	public Time(final int limit) throws InvalidAbsoluteTimeLimit {
		initializeLimit(limit);
		this.absoluteTime = new AtomicInteger();
	}

	public Time(final int limit, Class<?> clazz, int... limits) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			InvalidRelativeTimeClass, InvalidRelativeTimeLimit, InvalidAbsoluteTimeLimit {
		initializeLimit(limit);
		initializeRelativeTime(clazz, limits);
		this.absoluteTime = new AtomicInteger();
	}

	private void initializeRelativeTime(Class<?> clazz, int... limits)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InvalidRelativeTimeClass, InvalidRelativeTimeLimit {
		if (clazz != null && clazz.getSuperclass().equals(Time.class)) {
			if (limits != null && limits.length > 0) {
				ArrayList<Time> relativeTime = new ArrayList<Time>();
				for (int i = 0; i < limits.length; i++) {
					Time t = (Time) clazz.getConstructor(Integer.TYPE).newInstance(limits[i]);
					relativeTime.add(t);
				}
				this.relativeTime = (List<Time>) relativeTime.clone();
			} else {
				throw new InvalidRelativeTimeLimit();
			}
		} else {
			throw new InvalidRelativeTimeClass();
		}
	}

	private void initializeLimit(int limit) throws InvalidAbsoluteTimeLimit {
		if (limit > 0) {
			this.limit = new AtomicInteger(limit);
		} else {
			throw new InvalidAbsoluteTimeLimit();
		}
	}

	public void increase() throws TimeLimitReached {
		if (this.limit == null || this.absoluteTime.get() < this.limit.get()) {
			if (this.relativeTime != null) {
				for (int i = this.relativeTime.size(); i > 0; i--) {
					Time r = this.relativeTime.get(i - 1);
					try {
						r.increase();
						return;
					} catch (TimeLimitReached e) {
						r.resetAbsoluteTime();
					}
				}
				this.absoluteTime.incrementAndGet();
			} else {
				this.absoluteTime.incrementAndGet();
			}
		} else {
			throw new TimeLimitReached();
		}
	}

	protected void resetAbsoluteTime() {
		this.absoluteTime.set(0);
	}

	public int getAbsolute() {
		return this.absoluteTime.get();
	}

	public List<Time> getRelative() {
		return this.relativeTime;
	}
	
}
