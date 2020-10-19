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

import com.ghjansen.cas.core.exception.InvalidAbsoluteTimeLimitException;
import com.ghjansen.cas.core.exception.InvalidRelativeTimeLimitException;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;

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

    protected Time(final int limit) throws InvalidAbsoluteTimeLimitException {
        initializeLimit(limit);
        this.absoluteTime = new AtomicInteger();
    }

    protected Time(final int limit, int... limits)
            throws InvalidAbsoluteTimeLimitException, InvalidRelativeTimeLimitException, CloneNotSupportedException {
        initializeLimit(limit);
        initializeRelativeTime(limits);
        this.absoluteTime = new AtomicInteger();
    }

    private void initializeRelativeTime(int... limits) throws InvalidRelativeTimeLimitException, CloneNotSupportedException {
        if (limits != null && limits.length > 0) {
            ArrayList<Time> relTimTmp = new ArrayList<Time>();
            for (int i = 0; i < limits.length; i++) {
                try {
                    Time t = (Time) this.clone();
                    t.initializeLimit(limits[i]);
                    t.absoluteTime = new AtomicInteger();
                    relTimTmp.add(t);
                } catch (InvalidAbsoluteTimeLimitException e) {
                    throw new InvalidRelativeTimeLimitException();
                }
            }
            this.relativeTime = (List<Time>) relTimTmp.clone();
        } else {
            throw new InvalidRelativeTimeLimitException();
        }
    }

    private void initializeLimit(int limit) throws InvalidAbsoluteTimeLimitException {
        if (limit > 0) {
            this.limit = new AtomicInteger(limit);
        } else {
            throw new InvalidAbsoluteTimeLimitException();
        }
    }

    /**
     * The complete evolution of time is the multiplication of the limit of
     * absolute time by the product of all limits of relative times, minus one.
     * Removing one is necessary since the initial time (0) is used. Without
     * removing one, we have the formula that calculates the amount of times
     * the rule will be executed to reach complete evolution.
     * - lim(a) = limit of absolute time
     * - d = amount of dimensions (or relative times)
     * - lim(ri) = limit of a relative time i
     * LaTeX formula: T = ${lim(a)\displaystyle \left(\prod_{i=1}^{d} lim(r_{i})\right)-1}$
     *
     * @throws TimeLimitReachedException
     */
    public void increase() throws TimeLimitReachedException {
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
                } catch (TimeLimitReachedException e) {
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
            throw new TimeLimitReachedException();
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

    public int getLimit() {
        return this.limit.get();
    }

}
