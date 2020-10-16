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

package com.ghjansen.cas.unidimensional.ca;

import org.junit.Assert;
import org.junit.Test;

import com.ghjansen.cas.core.ca.State;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalStateTest {

	@Test
	public void unidimensionalStateConstructor() {
		final String nameBlack = "black";
		final int valueBlack = 0;
		final String nameWhite = "white";
		final int valueWhite = 1;
		final UnidimensionalState unidimensionalBlackState = new UnidimensionalState(nameBlack, valueBlack);
		final UnidimensionalState unidimensionalWhiteState = new UnidimensionalState(nameWhite, valueWhite);
		Assert.assertTrue(unidimensionalBlackState.getName().equals(nameBlack));
		Assert.assertTrue(unidimensionalBlackState.getValue() == valueBlack);
		Assert.assertTrue(unidimensionalWhiteState.getName().equals(nameWhite));
		Assert.assertTrue(unidimensionalWhiteState.getValue() == valueWhite);
	}

}
