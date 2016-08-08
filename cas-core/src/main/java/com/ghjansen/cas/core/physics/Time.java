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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Time {
	
	/**
	 * Keep the time iterations of the universe
	 */
	private AtomicInteger time;
	
	/**
	 * Start counting the time using AtomicInteger's default value 0
	 */
	public Time(){
		time = new AtomicInteger();
	}
	
	/**
	 * Increase one unit in time
	 * @return Current time after increase one unit
	 */
	public int addOneUnit(){
		return time.incrementAndGet();
	}
	
	/**
	 * Get current time
	 * @return Current time
	 */
	public int getCurrentTime(){
		return time.get();
	}
	
	/**
	 * Reset time to AtomicInteger's default value 0
	 */
	public void resetTime(){
		time = new AtomicInteger();
	}

}
