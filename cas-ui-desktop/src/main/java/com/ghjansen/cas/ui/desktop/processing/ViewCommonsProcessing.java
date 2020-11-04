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

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class ViewCommonsProcessing {
	
	private boolean glowControl = true;
	private float glowIntensity = 0.0F;
	
	public void glowOscillator(){
		if(glowControl){
			if(glowIntensity < 255.0F){
				glowIntensity = glowIntensity +20;
			} else {
				glowControl = false;
			}
		} else {
			if(glowIntensity > 0.0F){
				glowIntensity = glowIntensity -20;
			} else {
				glowControl = true;
			}
		}
	}
	
	public double logOfBase(int base, int num) {
		// thanks to http://blog.dreasgrech.com/2010/02/finding-logarithm-of-any-base-in-java.html
	    return Math.log(num) / Math.log(base);
	}

	public float getGlowIntensity() {
		return glowIntensity;
	}
}
