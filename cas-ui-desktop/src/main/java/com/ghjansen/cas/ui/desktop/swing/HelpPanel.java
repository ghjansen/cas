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

package com.ghjansen.cas.ui.desktop.swing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.ghjansen.cas.ui.desktop.processing.SimulationViewProcessing;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class HelpPanel extends JPanel {

	private BufferedImage image;

	public HelpPanel() {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/com/ghjansen/cas/ui/desktop/processing/welcome-pt-br.png"));
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);            
    }

}
