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

/**
 * Abstract representation of a n-dimensional universe, where time progress
 * affects space by applying the rules that separates the old state from the new
 * state.
 * 
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Universe {

	/**
	 * Abstract representation of a n-dimensional space
	 */
	private Space space;
	/**
	 * Keep the amount of iterations applied to the space of the universe
	 */
	private Time time;

	/**
	 * Creates the universe given the n-dimensional space and time
	 * 
	 * @param space
	 *            Instance of a n-dimensional space
	 */
	protected Universe(Space space, Time time) {
		this.space = space;
		this.time = time;
	}
	
	/**
	 * Perform one new iteration of the whole universe, transforming space and increasing one time unit
	 */
	protected void update(){
		//this.space.nextIteration()
		//this.time.increase();
		
	}

}
