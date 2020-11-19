/*
 * CAS - Cellular Automata Simulator
 * Copyright (C) 2019  Guilherme Humberto Jansen
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

package com.ghjansen.cas.ui.desktop.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationController;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalCell;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class SimulationExportUtil {

	private SimulationExportUtil() {
		throw new IllegalStateException("Utility class");
	}
	
	public static BufferedImage createBufferedImage(UnidimensionalSimulationController simulationController, 
			int cellScale, boolean showGrid, int gridLineThickness, Color gridLineColour){
		
		//replace invalid values by default values, for cellScale and gridLineThickness
		if(cellScale < 1) cellScale = 1;
		boolean bellowLowerLimit = gridLineThickness < 1;
		boolean overHigherLimit = gridLineThickness > (cellScale-1)/2;
		if(bellowLowerLimit || overHigherLimit) gridLineThickness = 1;
		
		//calculate the size of the simulation and image
		int simulationWidth = simulationController.getSimulation().getUniverse().getTime().getRelative().get(0).getLimit();
		int simulationHeight = simulationController.getSimulation().getUniverse().getTime().getLimit() + 1;
		int bufferWidth = simulationWidth * cellScale;
		int bufferHeight = simulationHeight * cellScale;
		
		//create buffered image and graphics
		BufferedImage buffer = new BufferedImage(bufferWidth, bufferHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffer.createGraphics();
		
		//draw cells
		drawCells(g, simulationController, cellScale, bufferHeight);
		
		//draw grid lines
		if(showGrid && cellScale >= 3){
			drawGrid(g, cellScale, bufferWidth, bufferHeight, gridLineThickness, gridLineColour);
		}
		
		return buffer;
	}
	
	private static void drawCells(Graphics2D g, UnidimensionalSimulationController simulationController, int cellScale, int bufferHeight){
		// initial condition cells
		for(int i = 0; i < simulationController.getSimulation().getUniverse().getSpace().getInitial().size(); i++){
			UnidimensionalCell c = simulationController.getSimulation().getUniverse().getSpace().getInitial().get(i);
			Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
			g.setColor(color);
			g.fillRect(i * cellScale, 0, cellScale, cellScale);
		}
		// history cells
		for(int j = 0; j < simulationController.getSimulation().getUniverse().getSpace().getHistory().size(); j++){
			List<UnidimensionalCell> cells = simulationController.getSimulation().getUniverse().getSpace().getHistory().get(j);
			int y = (j+1) * cellScale;
			for(int i = 0; i < cells.size(); i++){
				UnidimensionalCell c = cells.get(i);
				Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
				g.setColor(color);
				g.fillRect(i * cellScale, y, cellScale, cellScale);
			}
		}
		// current/last cells
		for(int i = 0; i < simulationController.getSimulation().getUniverse().getSpace().getCurrent().size(); i++){
			UnidimensionalCell c = simulationController.getSimulation().getUniverse().getSpace().getCurrent().get(i);
			Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
			g.setColor(color);
			g.fillRect(i * cellScale, bufferHeight - cellScale, cellScale, cellScale);
		}
	}
	
	private static void drawGrid(Graphics2D g, int cellScale, int bufferWidth, int bufferHeight, int gridLineThickness, Color colour){
		int px = 0;
		int py = cellScale;
		g.setColor(colour);
		//horizontal lines (bottom)
		for(int i = 1; py <= bufferHeight; i++){
			g.fillRect(0, py-gridLineThickness, bufferWidth, gridLineThickness);
			py = cellScale * (i+1);
		}
		//vertical lines (left)
		for(int i = 0; px < bufferWidth; i++){
			g.fillRect(px, 0, gridLineThickness, bufferHeight);
			px = cellScale * (i+1);
		}
		//horizontal line (top)
		g.fillRect(0, 0, bufferWidth, gridLineThickness);
		//vertical line (right)
		g.fillRect(bufferWidth-gridLineThickness, 0, gridLineThickness, bufferHeight);
	}
	
}
