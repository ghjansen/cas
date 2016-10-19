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

import processing.core.PApplet;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class RuleTransitionsProcessing extends PApplet {
	
	private int width = 301;
	private int height = 34;
	private float transitionSquareWidth = 37.6F;
	private int transitionSquareHeight = 33;
	private int stateSize = 8;
	private int leftNeighborMargin = 7;
	private int referenceMargin = leftNeighborMargin + stateSize;
	private int rightNeighborMargin = referenceMargin + stateSize;
	private int combinationPosition = 9;
	private int nextStatePosition = 17;
	private int[] states;

	public void setup() {
		size(width, height);
		background(255);
		states = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
	}

	public void draw() {
		drawElementaryTransitions();
	}
	
	private void drawElementaryTransitions(){
		drawTransitionBorders();
		drawStates();
	}
	
	private void drawTransitionBorders(){
		stroke(204);
		fill(255);
		for(int i = 0; i < 8; i++){
			rect(transitionSquareWidth * i, 0, transitionSquareWidth, transitionSquareHeight);
		}
	}
	
	private void drawStates(){
		stroke(127);
		// Left neighbor
		for(int i = 0; i < 8; i++){
			if(i < 4){
				fill(0);
			} else {
				fill(255);
			}
			rect(transitionSquareWidth * i + leftNeighborMargin, combinationPosition, stateSize, stateSize);
		}
		// Reference
		for(int i = 0; i < 8; i++){
			if(i == 0 || i == 1 || i == 4 || i == 5){
				fill(0);
			} else {
				fill(255);
			}
			rect(transitionSquareWidth * i + referenceMargin, combinationPosition, stateSize, stateSize);
		}
		// Right neighbor
		for(int i = 0; i < 8; i++){
			if(i%2 == 0){
				fill(0);
			} else {
				fill(255);
			}
			rect(transitionSquareWidth * i + rightNeighborMargin, combinationPosition, stateSize, stateSize);
		}
		// Next state
		fill(255);
		for(int i = 0; i < 8; i++){
			fill(getFillForStateValue(states[i]));
			rect(transitionSquareWidth * i + referenceMargin, nextStatePosition, stateSize, stateSize);
		}
		stroke(204);
	}
	
	@Override
	public void mousePressed() {
		super.mousePressed();
		float n = mouseX / transitionSquareWidth;
		if(n >= 0 && n < 1){
			states[0] = getNextState(states[0]);
		} else if (n >= 1 && n < 2){
			states[1] = getNextState(states[1]);
		} else if (n >= 2 && n < 3){
			states[2] = getNextState(states[2]);
		} else if (n >= 3 && n < 4){
			states[3] = getNextState(states[3]);
		} else if (n >= 4 && n < 5){
			states[4] = getNextState(states[4]);
		} else if (n >= 5 && n < 6){
			states[5] = getNextState(states[5]);
		} else if (n >= 6 && n < 7){
			states[6] = getNextState(states[6]);
		} else if (n >= 7){
			states[7] = getNextState(states[7]);
		}
	}
	
	private int getNextState(int currentState){
		if(currentState == 0){
			return 1;
		} else if( currentState == 1){
			return 0;
		} else {
			return 0;
		}
	}
	
	private int getFillForStateValue(int currentState){
		if(currentState == 0){
			return 255;
		} else if( currentState == 1){
			return 0;
		} else {
			return 255;
		}
	}

}
