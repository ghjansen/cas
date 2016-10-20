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
	private int width = 582;
	private int height = 582;
	private int background = 204;
	private int x = -1;
	private int y = 0;
	private int feedbackRate = 60;
	private boolean isWelcomeVisible;
	private boolean reset;

	public void setup() {
		size(width, height);
		textAlign(CENTER);
		background(background);
		this.reset = false;
	}

	public void draw() {
		if(reset){
			background(background);
			reset = false;
		} else if (universe != null && universe.getSpace().getHistory().size() > 0) {
			hideWelcome();
			drawSpace();
			drawTools();
		} else {
			drawWelcome();
		}
	}

	private void drawSpace() {
		List<List> history = universe.getSpace().getHistory();
		// Draw all complete lines
		int iterations = 0;
		while (history.size() - 1 > y && iterations < feedbackRate) {
			while (history.get(y).size() - 1 > x) {
				shiftColumn();
				drawCell((UnidimensionalCell) history.get(y).get(x));
			}
			shiftLine();
			iterations++;
		}
		// Draw incomplete or last line
		if (history.size() - 1 == y && history.size() - 1 > y) {
			while (history.get(y).size() - 1 > x) {
				shiftColumn();
				drawCell((UnidimensionalCell) history.get(y).get(x));
			}
		}
	}

	private void drawTools() {

	}

	private void drawWelcome() {
		textSize(25);
		text("Mensagem de boas vindas aqui!", 291, 291);
		isWelcomeVisible = true;
	}
	
	private void hideWelcome(){
		if(isWelcomeVisible){
			background(204);
			isWelcomeVisible = false;
		}
		
	}

	private void drawCell(UnidimensionalCell c) {
		if (c.getState().getValue() == 0) {
			fill(255);
		} else if (c.getState().getValue() == 1) {
			fill(0);
		}
		noStroke();
		rect(x, y, 1, 1);
	}

	private void shiftColumn() {
		x++;
	}

	private void shiftLine() {
		y++;
		x = -1;
	}

	public void setUniverse(UnidimensionalUniverse universe) {
		this.universe = universe;
	}
	
	public void reset(){
		this.reset = true;
		x = -1;
		y = 0;
	}

}
