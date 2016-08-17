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

	public Time(final int limit, int... limits) throws InvalidAbsoluteTimeLimit, InvalidRelativeTimeLimit{
		initializeLimit(limit);
		initializeRelativeTime(limits);
		this.absoluteTime = new AtomicInteger();
	}

	private void initializeRelativeTime(int... limits) throws InvalidRelativeTimeLimit {
		if (limits != null && limits.length > 0) {
			ArrayList<Time> relativeTime = new ArrayList<Time>();
			for (int i = 0; i < limits.length; i++) {
				try{
					RelativeTime t = new RelativeTime(limits[i]);
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
	 * lm(t) = limit of absolute time
	 * d = amount of dimensions (or relative times)
	 * lim(d-1) = the iteration to get the limit of each dimension (relative time)
	 * LaTeX formula:
	 * ${lim(t)\displaystyle \prod_{i=1}^{d} lim(d-1)}$
	 * @throws TimeLimitReached
	 */
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

final class RelativeTime extends Time {
	public RelativeTime(int limit) throws InvalidAbsoluteTimeLimit{
		super(limit);
	}
}