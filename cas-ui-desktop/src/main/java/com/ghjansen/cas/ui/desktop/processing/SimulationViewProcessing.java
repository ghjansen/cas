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
	private int mousePressedX;
	private int mousePressedY;
	private int translationX;
	private int translationY;

	public void setup() {
		size(width, height);
		textAlign(CENTER);
		background(background);
	}

	public void draw() {
		if (isSpaceAvailable()) {
			drawSpace();
			drawTools();
		} else {
			drawWelcome();
		}
	}
	
	private boolean isSpaceAvailable(){
		return universe != null && universe.getSpace().getInitial().size() > 0;
	}
	
	private void drawSpace(){
		pushMatrix();
		translate(translationX, translationY);
		drawInitialCondition();
		if(universe.getSpace().getHistory().size() > 0){
			drawHistory();
		}
		if(universe.getSpace().getCurrent().size() > 0){
			drawCurrent();
		}
		popMatrix();
	}
	
	private void drawInitialCondition(){
		List<?> initialCondition = universe.getSpace().getInitial();
		for(int i = 0; i < initialCondition.size(); i++){
			UnidimensionalCell c = (UnidimensionalCell) initialCondition.get(i);
			if (c.getState().getValue() == 0) {
				fill(255);
			} else if (c.getState().getValue() == 1) {
				fill(0);
			}
			noStroke();
			rect(i, 0, 1, 1);
		}
	}
	
	private void drawHistory() {
		List<List> history = universe.getSpace().getHistory();
		// Draw all complete lines
		int iterations = 0;
		while (history.size() - 1 > y && iterations < feedbackRate) {
			while (history.get(y).size() - 1 > x) {
				nextColumn();
				drawCell((UnidimensionalCell) history.get(y).get(x));
			}
			nextLine();
			iterations++;
		}
		// Draw incomplete or last line
		if (history.size() - 1 == y && history.size() - 1 > y) {
			while (history.get(y).size() - 1 > x) {
				nextColumn();
				drawCell((UnidimensionalCell) history.get(y).get(x));
			}
		}
	}
	
	private void drawCurrent(){
		List<?> current = universe.getSpace().getCurrent();
		for(int i = 0; i < current.size(); i++){
			UnidimensionalCell c = (UnidimensionalCell) current.get(i);
			if (c.getState().getValue() == 0) {
				fill(255);
			} else if (c.getState().getValue() == 1) {
				fill(0);
			}
			noStroke();
			rect(i, y+1, 1, 1);
		}
	}

	private void drawTools() {

	}

	private void drawWelcome() {
		textSize(25);
		text("Mensagem de boas vindas aqui!", 291, 291);
	}
	
	private void drawCell(UnidimensionalCell c) {
		if (c.getState().getValue() == 0) {
			fill(255);
		} else if (c.getState().getValue() == 1) {
			fill(0);
		}
		noStroke();
		rect(x, y+1, 1, 1);
	}

	private void nextColumn() {
		x++;
	}

	private void nextLine() {
		y++;
		x = -1;
	}

	public void setUniverse(UnidimensionalUniverse universe) {
		this.universe = universe;
	}
	
	public void reset(){
		translationX = 0;
		translationY = 0;
		refresh();
	}
	
	private void refresh(){
		x = -1;
		y = 0;
		background(background);
	}
	
	public void mousePressed(){
		mousePressedX = mouseX;
		mousePressedY = mouseY;
	}
	
	public void mouseReleased(){
		translationX = translationX + (mouseX - mousePressedX);
		translationY = translationY + (mouseY - mousePressedY);
		refresh();
	}

}
