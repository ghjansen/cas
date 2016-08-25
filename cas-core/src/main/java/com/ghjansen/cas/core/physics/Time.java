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
import java.util.concurrent.atomic.AtomicInteger;

import com.ghjansen.cas.core.physics.exception.time.InvalidAbsoluteTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.InvalidRelativeTimeLimit;
import com.ghjansen.cas.core.physics.exception.time.TimeLimitReached;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Time implements Cloneable {

	private AtomicInteger absoluteTime;
	private List<Time> relativeTime;
	private AtomicInteger limit;

	protected Time() {
		this.absoluteTime = new AtomicInteger();
	}

	protected Time(final int limit) throws InvalidAbsoluteTimeLimit {
		initializeLimit(limit);
		this.absoluteTime = new AtomicInteger();
	}

	protected Time(final int limit, int... limits)
			throws InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit, CloneNotSupportedException {
		initializeLimit(limit);
		initializeRelativeTime(limits);
		this.absoluteTime = new AtomicInteger();
	}

	private void initializeRelativeTime(int... limits) throws InvalidRelativeTimeLimit, CloneNotSupportedException {
		if (limits != null && limits.length > 0) {
			ArrayList<Time> relativeTime = new ArrayList<Time>();
			for (int i = 0; i < limits.length; i++) {
				try {
					Time t = (Time) this.clone();
					t.initializeLimit(limits[i]);
					t.absoluteTime = new AtomicInteger();
					relativeTime.add(t);
				} catch (InvalidAbsoluteTimeLimit e) {
					throw new InvalidRelativeTimeLimit();
				}
			}
			this.relativeTime = (List<Time>) relativeTime.clone();
		} else {
			throw new InvalidRelativeTimeLimit();
		}
	}

	private void initializeLimit(int limit) throws InvalidAbsoluteTimeLimit {
		if (limit > 0) {
			this.limit = new AtomicInteger(limit);
		} else {
			throw new InvalidAbsoluteTimeLimit();
		}
	}

	/**
	 * The complete evolution of time is the multiplication of the limit of
	 * absolute time by the product of all limits of relative times. 
	 * - lm(t) = limit of absolute time
	 * - d = amount of dimensions (or relative times)
	 * - lim(d-1) = the iteration to get the limit of each dimension (relative
	 * time) 
	 * LaTeX formula: ${lim(t)\displaystyle \prod_{i=1}^{d} lim(d-1)}$
	 * 
	 * @throws TimeLimitReached
	 */
	public void increase() throws TimeLimitReached {
		ArrayList<Time> resetPool = new ArrayList<Time>();
		if (this.relativeTime != null) {
			for (int i = this.relativeTime.size(); i > 0; i--) {
				Time r = this.relativeTime.get(i - 1);
				try {
					r.increase();
					for (Time t : resetPool) {
						t.resetAbsoluteTime();
					}
					return;
				} catch (TimeLimitReached e) {
					resetPool.add(r);
				}
			}
		}
		if (this.limit == null || this.absoluteTime.get() < this.limit.get() - 1) {
			this.absoluteTime.incrementAndGet();
			for (Time t : resetPool) {
				t.resetAbsoluteTime();
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
