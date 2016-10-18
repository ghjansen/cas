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

package com.ghjansen.cas.ui.desktop.processing;

import java.util.List;

import processing.core.PApplet;

import com.ghjansen.cas.unidimensional.physics.UnidimensionalCell;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalUniverse;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class SimulationViewProcessing extends PApplet {
	
	private UnidimensionalUniverse universe;
	private int x = -1;
	private int y = 0;

	public void setup() {
		size(582, 582);
		textSize(24);
		textAlign(CENTER);
		background(204);
	}

	public void draw() {
		//fill(255);
		//rect(25, 25, 50, 50);
		if(universe != null && universe.getSpace().getHistory().size() > 0){
			List<List> history = universe.getSpace().getHistory();
			if(history.get(y).size()-1 > x){
				while(history.get(y).size()-1 > x){
					x++;
					UnidimensionalCell c = (UnidimensionalCell) history.get(y).get(x);
					drawCell(c);
				}
			} else if (history.size()-1 > y){
				y++;
				x = -1;
			}
			
			
			//rect(100, 25, 50, 50);
		} else {
			text("Mensagem de boas vindas aqui!", 291, 291);
		}
	}
	
	public void drawCell(UnidimensionalCell c){
		if(c.getState().getValue() == 0){
			fill(255);
		} else if (c.getState().getValue() == 1){
			fill(0);
		}
		noStroke();
		rect(x, y, 1, 1);
	}
	
	public void setUniverse(UnidimensionalUniverse universe){
		this.universe = universe;
	}

}
