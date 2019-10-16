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
	
	public static BufferedImage createBufferedImage(UnidimensionalSimulationController simulationController, 
			int cellScale, boolean showGrid, int gridLineThickness, Color gridLineColour){
		
		int simulationWidth = simulationController.getSimulation().getUniverse().getTime().getRelative().get(0).getLimit();
		int simulationHeight = simulationController.getSimulation().getUniverse().getTime().getLimit() + 1;
		int bufferWidth = simulationWidth * cellScale;
		int bufferHeight = simulationHeight * cellScale;
		
		BufferedImage buffer = new BufferedImage(bufferWidth, bufferHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffer.createGraphics();
		
		// initial condition
		for(int i = 0; i < simulationController.getSimulation().getUniverse().getSpace().getInitial().size(); i++){
			UnidimensionalCell c = (UnidimensionalCell) simulationController.getSimulation().getUniverse().getSpace().getInitial().get(i);
			Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
			g.setColor(color);
			g.fillRect(i * cellScale, 0, cellScale, cellScale);
			//buffer.setRGB(i, 0, color.getRGB());
			
		}
		// history
		for(int j = 0; j < simulationController.getSimulation().getUniverse().getSpace().getHistory().size(); j++){
			List<UnidimensionalCell> cells = simulationController.getSimulation().getUniverse().getSpace().getHistory().get(j);
			int y = (j+1) * cellScale;
			for(int i = 0; i < cells.size(); i++){
				UnidimensionalCell c = (UnidimensionalCell) cells.get(i);
				Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
				g.setColor(color);
				g.fillRect(i * cellScale, y, cellScale, cellScale);
				//buffer.setRGB(i, j+1, color.getRGB());
			}
		}
		// current/last
		for(int i = 0; i < simulationController.getSimulation().getUniverse().getSpace().getCurrent().size(); i++){
			UnidimensionalCell c = (UnidimensionalCell) simulationController.getSimulation().getUniverse().getSpace().getCurrent().get(i);
			Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
			g.setColor(color);
			g.fillRect(i * cellScale, bufferHeight - cellScale, cellScale, cellScale);
			//buffer.setRGB(i, bufferHeight-1, color.getRGB());
		}
		return buffer;
	}

}
