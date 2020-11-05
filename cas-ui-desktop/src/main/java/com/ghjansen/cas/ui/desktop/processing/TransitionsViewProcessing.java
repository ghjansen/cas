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

import com.ghjansen.cas.ui.desktop.manager.EventManager;
import com.ghjansen.cas.unidimensional.ca.UnidimensionalTransition;

import java.util.HashMap;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class TransitionsViewProcessing extends PApplet {
	
	private transient EventManager em;
	private transient UnidimensionalTransition transitionHighlight;
	private transient ViewCommonsProcessing commons;
	private int viewWidth = 301;
	private int viewHeight = 34;
	private int viewBackground = 255;
	private float transitionSquareWidth = 37.6F;
	private int transitionSquareHeight = 33;
	private int stateSize = 8;
	private int leftNeighborMargin = 7;
	private int referenceMargin = leftNeighborMargin + stateSize;
	private int rightNeighborMargin = referenceMargin + stateSize;
	private int combinationPosition = 9;
	private int nextStatePosition = 17;
	private int[] states;
	private boolean highlight = false;
	private boolean mouseEnabled = true;
	
	public TransitionsViewProcessing(ViewCommonsProcessing commons, EventManager em){
		this.commons = commons;
		this.em = em;
	}

	@Override
	public void setup() {
		size(viewWidth, viewHeight);
		background(viewBackground);
		states = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
	}

	@Override
	public void draw() {
		drawElementaryTransitions();
		drawHighlight();
	}
	
	private void drawElementaryTransitions() {
		drawTransitionBorders();
		drawStates();
	}
	
	private void drawTransitionBorders(){
		stroke(204);
		strokeWeight(1);
		fill(255);
		for(int i = 0; i < 8; i++){
			rect(transitionSquareWidth * i, 0, transitionSquareWidth, transitionSquareHeight);
		}
	}
	
	private void drawStates(){
		stroke(127);
		strokeWeight(1);
		fill(255);
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
			rect(transitionSquareWidth * (7-i) + referenceMargin, nextStatePosition, stateSize, stateSize);
		}
		stroke(204);
	}
	
	private void drawHighlight(){
		if(highlight && transitionHighlight != null){
			int transition = -1;
			int leftState = transitionHighlight.getCombination().getNeighborhood().get(0).getValue();
			int referenceState = transitionHighlight.getCombination().getReferenceState().getValue();
			int rightState = transitionHighlight.getCombination().getNeighborhood().get(1).getValue();
			transition = getTransitionHighlightNumber(leftState, referenceState, rightState);
			if(transition != -1){
				stroke(255.0F, commons.getGlowIntensity(), commons.getGlowIntensity());
				strokeWeight(5);
				strokeCap(ROUND);
				noFill();
				float a = transitionSquareWidth * (7-transition);
				float b = 3;
				float c = transitionSquareWidth;
				float d = transitionSquareHeight-6F;
				rect(a, b, c, d);
			}
		}
	}

	private int getTransitionHighlightNumber(int leftState, int referenceState, int rightState){
		final int leftScore = 1;
		final int referenceScore = 3;
		final int rightScore = 5;
		HashMap<Integer,Integer> scoreMap = new HashMap<Integer, Integer>();
		scoreMap.put(leftScore*1 + referenceScore*1 + rightScore*1,7);
		scoreMap.put(leftScore*1 + referenceScore*1 + rightScore*0,6);
		scoreMap.put(leftScore*1 + referenceScore*0 + rightScore*1,5);
		scoreMap.put(leftScore*1 + referenceScore*0 + rightScore*0,4);
		scoreMap.put(leftScore*0 + referenceScore*1 + rightScore*1,3);
		scoreMap.put(leftScore*0 + referenceScore*1 + rightScore*0,2);
		scoreMap.put(leftScore*0 + referenceScore*0 + rightScore*1,1);
		scoreMap.put(leftScore*0 + referenceScore*0 + rightScore*0,0);
		int score = leftState*leftScore + referenceState*referenceScore + rightState*rightScore;
		if(scoreMap.containsKey(score)){
			return scoreMap.get(score);
		} else {
			return -1;
		}
	}

	@Override
	public void mousePressed() {
		super.mousePressed();
		if(mouseEnabled){
			float n = mouseX / transitionSquareWidth;
			int[][] rangesIndex = new int[][]{{7,8}, {6,7}, {5,6}, {4,5}, {3,4}, {2,3}, {1,2}, {0,1}};
			for(int i = 0; i < rangesIndex.length; i++){
				if(n >= rangesIndex[i][0] && n < rangesIndex[i][1]){
					states[i] = getNextState(states[i]);
					break;
				}
			}
			em.transitionsEvent();
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
	
	public int[] getStates(){
		return states;
	}
	
	public void setStates(int[] states){
		this.states = states;
	}
	
	public void showHighlight(UnidimensionalTransition transition){
		highlight = true;
		transitionHighlight = transition;
	}
	
	public void hideHighlight(){
		highlight = false;
	}

	public boolean isMouseEnabled() {
		return mouseEnabled;
	}

	public void setMouseEnabled(boolean mouseEnabled) {
		this.mouseEnabled = mouseEnabled;
	}

}
