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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;

import processing.core.PApplet;
import processing.core.PImage;

import com.ghjansen.cas.ui.desktop.i18n.Language;
import com.ghjansen.cas.ui.desktop.i18n.Translator;
import com.ghjansen.cas.ui.desktop.swing.GUIValidator;
import com.ghjansen.cas.ui.desktop.swing.HelpFrame;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalCell;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalUniverse;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class SimulationViewProcessing extends PApplet {

	private transient UnidimensionalUniverse universe;
	private TransitionsViewProcessing transitions;
	private transient ViewCommonsProcessing commons;
	private transient PImage[] welcomeImgs = new PImage[2];
	private HelpFrame helpFrame;
	private int viewWidth = 582;
	private int viewHeight = 582;
	private int viewBackground = 204;
	private float squareSize = 100.0F;
	private int viewX = -1;
	private int viewY = 0;
	private int feedbackRate = 60;
	private float translationX;
	private float translationY;
	private float minScale = 0.01F;
	private float maxScale = 1.28F;
	private float alphaScale = 2.0F;
	private float deltaScale = 0.0F;
	private float scale = minScale;
	private float lastScale = scale; 
	private float mousePressedX;
	private float mousePressedY;
	private boolean cellInspector = false;
	private float inspectionSubjectX;
	private float inspectionSubjectY;
	private boolean resetControl = true;
	private float toolsX = viewWidth - 50;
	private float toolsY = 50;
	private float helpButtonX = toolsX + 15;
	private float helpButtonY = toolsY;
	private float inspectionButtonX = toolsX + 15;
	private float inspectionButtonY = toolsY + 50;
	private float zoomSliderX = toolsX + 3;
	private float zoomSliderY = toolsY + 100;
	private boolean overHelp = false;
	private boolean overInspector = false;
	private boolean overSlider = false;
	private boolean overPlus = false;
	private boolean overMinus = false;
	private boolean draggingSlider = false;
	private JProgressBar progress;
	private transient GUIValidator validator;
	private boolean viewFinished = false;
	
	public SimulationViewProcessing(ViewCommonsProcessing commons, TransitionsViewProcessing transitions){
		this.commons = commons;
		this.transitions = transitions;
	}

	@Override
	public void setup() {
		size(viewWidth, viewHeight);
		String welcomeFile0 = "welcome-"+Language.PORTUGUESE_BRAZIL.getLangtag().toLowerCase()+".png";
		String welcomeFile1 = "welcome-"+Language.ENGLISH_UNITED_KINGDOM.getLangtag().toLowerCase()+".png";
		welcomeImgs[0] = loadImage(SimulationViewProcessing.class.getResource(welcomeFile0).toString());
		welcomeImgs[1] = loadImage(SimulationViewProcessing.class.getResource(welcomeFile1).toString());
		helpFrame = new HelpFrame();
		textAlign(CENTER);
		background(viewBackground);
		frameRate(30);
	}

	@Override
	public void draw() {
		if(resetControl){
			background(viewBackground);
			delay(feedbackRate);
		}
		if (isSpaceAvailable()) {
			drawSpace();
			drawTools();
		} else {
			drawWelcome();
		}
		updateLastScale();
		updateResetControl();
	}
	
	private boolean isSpaceAvailable(){
		return universe != null && !universe.getSpace().getInitial().isEmpty();
	}
	
	private void drawSpace(){
		pushMatrix();
		calibrateScaleAndTranslation();
		updateInspectionSubject();
		translate(translationX, translationY);
		scale(scale);
		strokeControl();
		drawInitialCondition();
		if(universe != null && !universe.getSpace().getHistory().isEmpty()){
			drawHistory();
		}
		if(universe != null && !universe.getSpace().getCurrent().isEmpty()){
			drawCurrent();
		}
		drawInspector();
		popMatrix();
	}
	
	private void calibrateScaleAndTranslation(){
		if(resetControl){
			// calibrate initial scale
			ScaleCalibration isc = calibrateInitialScale();
			// verify horizontal limit
			verifyHorizontalLimit(isc);
			// verify vertical limit
			verifyVerticalLimit(isc);
			// use smallest limit
			useSmallestLimit(isc);
			// if smallest limit is greater than maxScale, assume maxScale, otherwise use smallest
			assertScale(isc);
			// recalculate emptySpace
			recalculateEmptySpace(isc);
			// calibrate initial translation
			calibrateInitialTranslation(isc);
		} else if (lastScale != scale){
			// hold center when zooming in or out
			holdCenterForZoom();
		}
	}

	private ScaleCalibration calibrateInitialScale(){
		float emptySpaceX = viewWidth - universe.getTime().getRelative().get(0).getLimit();
		float emptySpaceY = viewHeight - universe.getTime().getLimit();
		double deltaScaleLimitX = 0;
		double deltaScaleLimitY = 0;
		float newScale = 0.0F;
		return new ScaleCalibration(emptySpaceX,emptySpaceY,deltaScaleLimitX,deltaScaleLimitY,newScale);
	}

	private ScaleCalibration verifyHorizontalLimit(ScaleCalibration isc){
		if(isc.getEmptySpaceX() > 0){
			float horizontalSize = universe.getTime().getRelative().get(0).getLimit() * squareSize;
			float potentialScale = minScale * (float) (Math.pow((double) alphaScale, isc.getDeltaScaleLimitX()));
			while(horizontalSize * potentialScale < viewWidth){
				isc.setDeltaScaleLimitX(isc.getDeltaScaleLimitX()+1);
				potentialScale = minScale * (float) (Math.pow((double) alphaScale, isc.getDeltaScaleLimitX()));
			}
			if(horizontalSize * potentialScale > viewWidth){
				isc.setDeltaScaleLimitX(isc.getDeltaScaleLimitX()-1);
			}
		}
		return isc;
	}

	private ScaleCalibration verifyVerticalLimit(ScaleCalibration isc){
		if(isc.getEmptySpaceY() > 0){
			float verticalSize = universe.getTime().getLimit() * squareSize;
			float potentialScale = minScale * (float) (Math.pow((double) alphaScale, isc.getDeltaScaleLimitY()));
			while(verticalSize * potentialScale < viewHeight){
				isc.setDeltaScaleLimitY(isc.getDeltaScaleLimitY()+1);
				potentialScale = minScale * (float) (Math.pow((double) alphaScale, isc.getDeltaScaleLimitY()));
			}
			if(verticalSize * potentialScale > viewHeight){
				isc.setDeltaScaleLimitY(isc.getDeltaScaleLimitY()-1);
			}
		}
		return isc;
	}

	private ScaleCalibration useSmallestLimit(ScaleCalibration isc){
		if((isc.getDeltaScaleLimitX() < isc.getDeltaScaleLimitY() || isc.getDeltaScaleLimitX() == isc.getDeltaScaleLimitY())){
			isc.setNewScale(minScale * (float) (Math.pow((double) alphaScale, isc.getDeltaScaleLimitX())));
			deltaScale = (float) isc.getDeltaScaleLimitX();
		} else if(isc.getDeltaScaleLimitY() < isc.getDeltaScaleLimitX()){
			isc.setNewScale(minScale * (float) (Math.pow((double) alphaScale, isc.getDeltaScaleLimitY())));
			deltaScale = (float) isc.getDeltaScaleLimitY();
		}
		return isc;
	}

	private void assertScale(ScaleCalibration isc){
		if(isc.getNewScale() > maxScale){
			scale = maxScale;
			deltaScale = (float) commons.logOfBase((int)alphaScale, (int) (maxScale * squareSize));
		} else {
			scale = isc.getNewScale();
		}
	}

	private ScaleCalibration recalculateEmptySpace(ScaleCalibration isc){
		isc.setEmptySpaceX(viewWidth - (universe.getTime().getRelative().get(0).getLimit() * (scale * squareSize)));
		isc.setEmptySpaceY(viewHeight - (universe.getTime().getLimit() * (scale * squareSize)));
		return isc;
	}

	private void calibrateInitialTranslation(ScaleCalibration isc){
		if(isc.getEmptySpaceX() > 0){
			translationX = (float) Math.floor((double) isc.getEmptySpaceX() / 2);
		}
		if(isc.getEmptySpaceY() > 0){
			translationY = (float) Math.floor((double) isc.getEmptySpaceY() / 2);
		}
	}

	private void holdCenterForZoom(){
		float centralShiftX;
		float centralShiftY;
		if(lastScale > scale){
			centralShiftX = ((viewWidth / 2) - translationX) / (lastScale / scale);
			centralShiftY = ((viewHeight / 2) - translationY) / (lastScale / scale);
			translationX = (float) Math.floor((double) translationX + centralShiftX);
			translationY = (float) Math.floor((double) translationY + centralShiftY);
			inspectionSubjectX = inspectionSubjectX + centralShiftX;
			inspectionSubjectY = inspectionSubjectY + centralShiftY;
		} else {
			centralShiftX = ((viewWidth / 2) - translationX);
			centralShiftY = ((viewHeight / 2) - translationY);
			translationX = (float) Math.floor((double) translationX - centralShiftX);
			translationY = (float) Math.floor((double) translationY - centralShiftY);
			inspectionSubjectX = inspectionSubjectX - centralShiftX;
			inspectionSubjectY = inspectionSubjectY - centralShiftY;
		}
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
			rect(squareSize * i, 1, squareSize, squareSize);
		}
	}
	
	private void strokeControl(){
		if(scale > 0.16){
			stroke(viewBackground);
			strokeWeight(1);
		} else {
			strokeWeight(0);
			noStroke();
		}
	}
	
	private void drawHistory() {
		List<List<UnidimensionalCell>> history = universe.getSpace().getHistory();
		int iterations = 0;
		while (history.size() > viewY && iterations < feedbackRate) {
			while (history.get(viewY).size() - 1 > viewX) {
				nextColumn();
				drawHistoryCell(history.get(viewY).get(viewX));
			}
			nextLine();
			iterations++;
			if(progress != null && progress.getValue() < viewY){
				progress.setValue(viewY);
			}
		}
	}
	
	private void drawCurrent(){
		List<?> current = (List<?>) ((ArrayList) universe.getSpace().getCurrent()).clone();
		List<?> last = null;
		if(universe.getSpace().getLast() != null){
			last = (List<?>) ((ArrayList) universe.getSpace().getLast()).clone();
		}
		if(!current.equals(last)){
			for(int i = 0; i < current.size(); i++){
				drawCurrentCell(current, i);
			}
		}
	}

	private void drawCurrentCell(List<?> current, int i) {
		UnidimensionalCell c = (UnidimensionalCell) current.get(i);
		if (c.getState().getValue() == 0) {
			fill(255);
		} else if (c.getState().getValue() == 1) {
			fill(0);
		}
		rect(squareSize * i, squareSize * (viewY + 1), squareSize, squareSize);
		if (progress != null && progress.getValue() < viewY + 1) {
			progress.setValue(viewY + 1);
		}
		if (!viewFinished && viewY + 1 == universe.getTime().getLimit()) {
			this.validator.setNormalStatus("msgRenderingSuccess");
			viewFinished = true;
		}
	}
	
	private void drawInspector(){
		if(cellInspector){
			float scaledSquareSize = scale * squareSize;
			float floatCellX = (inspectionSubjectX - translationX) / scaledSquareSize;
			float floatCellY = (inspectionSubjectY - translationY) / scaledSquareSize;
			int intCellX = (int) floatCellX;
			int intCellY = (int) floatCellY;
			drawInspectorBorders(intCellX, intCellY);
		} else {
			transitions.hideHighlight();
		}
	}
	
	private void drawInspectorBorders(int xCell, int yCell){
		int absoluteTimeLimit = universe.getTime().getLimit();
		int relativeTimeLimit = universe.getTime().getRelative().get(0).getLimit();
		if(xCell >= 0 && xCell < relativeTimeLimit && 
				yCell > 0 && yCell < absoluteTimeLimit + 1){
			
			commons.glowOscillator();
			stroke(255.0F, commons.getGlowIntensity(), commons.getGlowIntensity());
			strokeWeight(squareSize/10);
			strokeCap(ROUND);
			noFill();
			
			float cx = xCell * squareSize;
			float cy = yCell * squareSize;
			float p1x;
			float p1y;
			float p2x;
			float p2y;
			float p3x;
			float p3y;
			float p4x;
			float p4y;
			float p5x;
			float p5y;
			float p6x;
			float p6y;
			float p7x;
			float p7y;
			float p8x;
			float p8y;
			float p9x;
			float p9y;
			float p10x;
			float p10y;
			
			/**
			 * P1---P2---P3---P4
			 * |    |    |    |
			 * P10--P9---P6---P5
			 *      |    |
			 *      P8---P7
			 */
			
			p2x = cx;
			p2y = cy - squareSize;
			p3x = cx + squareSize;
			p3y = cy - squareSize;
			p6x = cx + squareSize;
			p6y = cy;
			p7x = cx + squareSize;
			p7y = cy + squareSize;
			p8x = cx;
			p8y = cy + squareSize;
			p9x = cx;
			p9y = cy;
			
			
			if(xCell == 0){ 
				// extreme left, include combination cell from right side
				p1x = cx + (squareSize * relativeTimeLimit) - squareSize; //different
				p1y = cy - squareSize;
				p4x = cx + (squareSize * 2);
				p4y = cy - squareSize;
				p5x = cx + (squareSize * 2);
				p5y = cy;
				p10x = cx + (squareSize * relativeTimeLimit) - squareSize; //different
				p10y = cy;
				line(p1x, p1y, p1x + squareSize, p2y); //different
				line(p2x, p2y, p3x, p3y);
				line(p3x, p3y, p4x, p4y);
				line(p4x, p4y, p5x, p5y);
				line(p5x, p5y, p6x, p6y);
				line(p6x, p6y, p7x, p7y);
				line(p7x, p7y, p8x, p8y);
				line(p8x, p8y, p9x, p9y);
				line(p10x, p10y, p10x + squareSize, p10y); //different
				line(p10x, p10y, p1x, p1y);
				
			} else if (xCell == relativeTimeLimit - 1){
				// extreme right, include combination cell from left side
				p1x = cx - squareSize;
				p1y = cy - squareSize;
				p4x = squareSize; //different
				p4y = cy - squareSize;
				p5x = squareSize; //different
				p5y = cy;
				p10x = cx - squareSize;
				p10y = cy;
				line(p1x, p1y, p2x, p2y);
				line(p2x, p2y, p3x, p3y);
				line(0, p4y, p4x, p4y); //different
				line(p4x, p4y, p5x, p5y);
				line(p5x, p5y, 0, p5y); //different
				line(p6x, p6y, p7x, p7y);
				line(p7x, p7y, p8x, p8y);
				line(p8x, p8y, p9x, p9y);
				line(p9x, p9y, p10x, p10y);
				line(p10x, p10y, p1x, p1y);
			} else {
				// regular mid location, draw borders normally
				p1x = cx - squareSize;
				p1y = cy - squareSize;
				p4x = cx + (squareSize * 2);
				p4y = cy - squareSize;
				p5x = cx + (squareSize * 2);
				p5y = cy;
				p10x = cx - squareSize;
				p10y = cy;
				line(p1x, p1y, p2x, p2y);
				line(p2x, p2y, p3x, p3y);
				line(p3x, p3y, p4x, p4y);
				line(p4x, p4y, p5x, p5y);
				line(p5x, p5y, p6x, p6y);
				line(p6x, p6y, p7x, p7y);
				line(p7x, p7y, p8x, p8y);
				line(p8x, p8y, p9x, p9y);
				line(p9x, p9y, p10x, p10y);
				line(p10x, p10y, p1x, p1y);
			}
			UnidimensionalCell inspectedCell = getInspectedCell(xCell, yCell);
			transitions.showHighlight(inspectedCell.getTransition());
		}
	}
	
	private UnidimensionalCell getInspectedCell(int xCell, int yCell){
		if(yCell == universe.getTime().getLimit()){
			return universe.getSpace().getCurrent().get(xCell);
		} else {
			return universe.getSpace().getHistory().get(--yCell).get(xCell);
		}
	}
	
	private void updateInspectionSubject(){
		if(lastScale != scale){
			if(lastScale > scale){
				inspectionSubjectX = ((inspectionSubjectX - translationX) / alphaScale) + translationX;
				inspectionSubjectY = ((inspectionSubjectY - translationY) / alphaScale) + translationY;
			} else {
				inspectionSubjectX = ((inspectionSubjectX - translationX) * alphaScale) + translationX;
				inspectionSubjectY = ((inspectionSubjectY - translationY) * alphaScale) + translationY;
			}
		}
	}

	private void drawHistoryCell(UnidimensionalCell c) {
		if (c.getState().getValue() == 0) {
			fill(255);
		} else if (c.getState().getValue() == 1) {
			fill(0);
		}
		int sX = (int) squareSize * viewX;
		int sY = (int) squareSize * (viewY + 1);
		rect((float) sX, (float) sY, squareSize, squareSize);
	}
	
	private void drawTools() {
		drawHelpButton();
		drawInspectionButton();
		drawZoomSlider();
	}
	
	private void drawHelpButton() {
		int helpRadius = 23;
		textSize(18);
		float fillCircle = 255.0F;
		float fillText = 0.0F;
		if (overHelp) {
			fillCircle = 0.0F;
			fillText = 255.0F;
		}
		fill(255.0F, fillCircle, fillCircle);
		stroke(255.0F, 0.0F, 0.0F);
		strokeWeight(1);
		ellipse(helpButtonX, helpButtonY, helpRadius, helpRadius);
		fill(255.0F, fillText, fillText);
		text("?", helpButtonX + 1, toolsY + 7);

	}
	
	private void drawInspectionButton(){
		int inspecRadius = 23;
		float fillCircle = 255.0F;
		float fillContent = 0.0F;
		if(overInspector || cellInspector){
			fillCircle = 0.0F;
			fillContent = 255.0F;
		}
		fill(255.0F, fillCircle, fillCircle);
		stroke(255.0F, 0.0F, 0.0F);
		ellipse(toolsX+15, toolsY+50, inspecRadius, inspecRadius);
		strokeWeight(1);
		stroke(255.0F, fillContent, fillContent);
		//eye
//		ellipse(toolsX+15, toolsY+50, inspecRadius-4, inspecRadius-15);
//		arc(toolsX+15, toolsY+48, inspecRadius-15, inspecRadius-15, radians(0), radians(180));
//		stroke(255.0F, fillContent, fillContent);
//		strokeWeight(2);
//		point(toolsX+15, toolsY+48);
		//cross
		line(toolsX+15, toolsY+43, toolsX+15, toolsY+57);
		line(toolsX+8, toolsY+50, toolsX+22, toolsY+50);
		noFill();
		rect(toolsX+12, toolsY+47, 6, 6);
	}
	
	private void drawZoomSlider(){
		int zoomWidth = 23;
		int zoomHeight = 250;
		textSize(18);
		fill(255);
		stroke(255.0F, 0.0F, 0.0F);
		strokeWeight(1);
		rect(zoomSliderX, zoomSliderY, zoomWidth, zoomHeight+4);
		arc(zoomSliderX+12, zoomSliderY+1, zoomWidth, zoomWidth, radians(180), radians(360));
		arc(zoomSliderX+12, zoomSliderY+zoomHeight, zoomWidth, zoomWidth, radians(0), radians(180));
		stroke(255.0F, 150.0F, 150.0F);
		line(zoomSliderX+12, zoomSliderY+15, zoomSliderX+12, zoomSliderY+234);
		for(int i = 1; i < 9; i++){
			line(zoomSliderX+9, zoomSliderY-10+(30*i), zoomSliderX+15, zoomSliderY-10+(30*i));
		}
		if(overPlus){
			stroke(255.0F, 0.0F, 0.0F);
			fill(255.0F, 0.0F, 0.0F);
			ellipse(zoomSliderX+12, zoomSliderY+3, 15, 15);
			fill(255);
			text("+", zoomSliderX+12, zoomSliderY+8);
		} else {
			fill(255.0F, 0.0F, 0.0F);
			text("+", zoomSliderX+12, zoomSliderY+8);
		}
		if(overMinus){
			stroke(255.0F, 0.0F, 0.0F);
			stroke(255.0F, 0.0F, 0.0F);
			ellipse(zoomSliderX+12, zoomSliderY + zoomHeight, 15, 15);
			fill(255);
			text("-", zoomSliderX+12, zoomSliderY + zoomHeight+5);
		} else {
			fill(255.0F, 0.0F, 0.0F);
			text("-", zoomSliderX+12, zoomSliderY + zoomHeight+5);
		}
		if(overSlider){
			stroke(255.0F, 0.0F, 0.0F);
			fill(255.0F, 0.0F, 0.0F);
		} else {
			stroke(255.0F, 150.0F, 150.0F);
			fill(255.0F, 150.0F, 150.0F);
		}
		rect(zoomSliderX+4, zoomSliderY+zoomHeight-(30*deltaScale)-25, 16, 9);
	}

	private void drawWelcome() {
		helpFrame.setVisible(false);
		image(welcomeImgs[Translator.getInstance().getLanguage().getId()], 0, 0);
	}
	
	private void updateLastScale(){
		lastScale = scale;
	}
	
	private void updateResetControl(){
		resetControl = false;
	}

	private void nextColumn() {
		viewX++;
	}

	private void nextLine() {
		viewY++;
		viewX = -1;
	}

	public void setUniverse(UnidimensionalUniverse universe) {
		this.universe = universe;
	}
	
	public void reset(){
		translationX = 0.0F;
		translationY = 0.0F;
		scale = minScale;
		lastScale = scale;
		deltaScale = 0.0F;
		cellInspector = false;
		resetControl = true;
		viewFinished = false;
		refresh();
	}
	
	private void refresh(){
		viewX = -1;
		viewY = 0;
		background(viewBackground);
	}
	
	private void zoomIn(){
		if(scale < maxScale){
			lastScale = scale;
			double pow = ++deltaScale;
			scale = minScale * (float) Math.pow( (double) alphaScale, pow);
			refresh();
		}
	}
	
	private void zoomOut(){
		if(scale > minScale){
			lastScale = scale;
			double pow = --deltaScale;
			scale = minScale * (float) Math.pow( (double) alphaScale, pow);
			refresh();
		}
	}
	
	private void switchInspector(){
		if(cellInspector){
			disableInspector();
		} else {
			cellInspector = true;
		}
	}
	
	private void disableInspector() {
		cellInspector = false;
		if(!isOverInspectorButton()){
			overInspector = false;
		}
		refresh();
	}

	private boolean isOverHelpButton() {
		return (mouseX > helpButtonX - 11 && mouseX < helpButtonX + 11
				&& mouseY > helpButtonY - 11 && mouseY < helpButtonY + 11);

	}

	private boolean isOverInspectorButton() {
		return (mouseX > inspectionButtonX - 11 && mouseX < inspectionButtonX + 11
				&& mouseY > inspectionButtonY - 11 && mouseY < inspectionButtonY + 11);
	}

	private boolean isOverPlus() {
		return (mouseX > zoomSliderX && mouseX < zoomSliderX + 23
				&& mouseY > zoomSliderY - 11 && mouseY < zoomSliderY + 11);
	}

	private boolean isOverMinus() {
		return (mouseX > zoomSliderX && mouseX < zoomSliderX + 20
				&& mouseY > zoomSliderY + 242 && mouseY < zoomSliderY + 261);
	}

	private boolean isOverSlider() {
		float zoomY = zoomSliderY + 250 - (30 * deltaScale) - 25;
		return (mouseX > zoomSliderX + 4 && mouseX < zoomSliderX + 20
				&& mouseY > zoomY && mouseY < zoomY + 14);
	}

	@Override
	public void mousePressed(){
		if(overHelp){
			helpFrame.setVisible(true);
		} else if (overInspector){
			switchInspector();
		} else if (overPlus) {
			zoomIn();
		} else if (overMinus){
			zoomOut();
		} else if(overSlider){
			draggingSlider = true;
		} else {
			mousePressedX = mouseX;
			mousePressedY = mouseY;
		}
	}

	@Override
	public void mouseDragged(){
		if(draggingSlider){
			float delta = mouseY - (zoomSliderY+225-(30*deltaScale));
			if(delta > 35){
				zoomOut();
			} else if (delta < -20){
				zoomIn();
			}
		}
	}

	@Override
	public void mouseReleased(){
		if(!overHelp == overInspector == overMinus == overPlus == overSlider){
			if(cellInspector){
				inspectionSubjectX = mousePressedX;
				inspectionSubjectY = mousePressedY;
				refresh();
			} else if (!draggingSlider) {
				translationX = translationX + (mouseX - mousePressedX);
				translationY = translationY + (mouseY - mousePressedY);
				inspectionSubjectX = inspectionSubjectX + (mouseX - mousePressedX);
				inspectionSubjectY = inspectionSubjectY + (mouseY - mousePressedY);
				refresh();
			}
		}
		draggingSlider = false;
	}

	@Override
	public void mouseMoved(){
		if (isOverHelpButton()) {
			overHelp = true;
			overInspector = overMinus = overPlus = overSlider = false;
		} else if (isOverInspectorButton()) {
			overInspector = true;
			overHelp = overMinus = overPlus = overSlider = false;
		} else if (isOverPlus()) {
			overPlus = true;
			overHelp = overMinus = overInspector = overSlider = false;
		} else if (isOverMinus()) {
			overMinus = true;
			overHelp = overPlus = overInspector = overSlider = false;
		} else if (isOverSlider()) {
			overSlider = true;
			overHelp = overPlus = overInspector = overMinus = false;
		} else {
			overHelp = overInspector = overMinus = overPlus = overSlider = false;
		}
		if(overHelp || overInspector || overMinus || overPlus || overSlider){
			cursor(ARROW);
		} else if (cellInspector){
			cursor(CROSS);
		} else {
			cursor(MOVE);
		}
	}

	@Override
	public void keyPressed(){
		if(key == '1'){
			zoomOut();
		} else if (key == '2'){
			zoomIn();
		} else if (key == '3') {
			switchInspector();
		} else if (key == ESC) {
			disableInspector();
			key = 0;
		}
	}
	
	public void setProgressBar(JProgressBar progress){
		this.progress = progress;
	}
	
	public void setValidator(GUIValidator validator){
		this.validator = validator;
	}
}
