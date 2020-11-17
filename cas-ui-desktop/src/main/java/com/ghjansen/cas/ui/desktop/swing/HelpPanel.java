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
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.ghjansen.cas.ui.desktop.i18n.Translator;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class HelpPanel extends JPanel {

	private transient BufferedImage image;
	private static final String FILE_PATH = "/com/ghjansen/cas/ui/desktop/processing/";
	private static final  String FILE_NAME_PREFIX = "welcome-";
	private static final String FILE_EXTENSION = ".png";
	
	@Override
    protected void paintComponent(Graphics g) {
		loadImage();
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);            
    }
	
	private void loadImage(){
		try {
			String langTag = Translator.getInstance().getLanguage().getLangtag().toLowerCase();
			image = ImageIO.read(getClass().getResourceAsStream(FILE_PATH + FILE_NAME_PREFIX + langTag + FILE_EXTENSION));
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
