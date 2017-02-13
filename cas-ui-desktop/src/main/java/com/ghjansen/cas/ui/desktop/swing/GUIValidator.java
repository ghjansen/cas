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

import java.awt.Color;
import java.awt.SystemColor;

import com.ghjansen.cas.ui.desktop.i18n.Translator;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class GUIValidator {
	
	private Main main;
	private Color invalidFieldColor;
	private boolean activityLocked = false;

	public GUIValidator(Main main, Color invalidFieldColor) {
		this.main = main;
		this.invalidFieldColor = invalidFieldColor;
	}
	
	public boolean isRuleNumberValid(){
		String value = main.txtRuleNumber.getText();
		if(isValidPositiveInteger(value) && Integer.valueOf(value) > -1 && Integer.valueOf(value) < 256){
			main.txtRuleNumber.setBackground(SystemColor.text);
			updateStatus();
			return true;
		} else {
			lockActivity();
			main.txtRuleNumber.setBackground(invalidFieldColor);
			updateStatus();
			return false;
		}
	}
	
	public boolean isCellsValid(){
		String value = main.txtCells.getText();
		if(isValidPositiveInteger(value) && Integer.valueOf(value) > 0){
			main.txtCells.setBackground(SystemColor.text);
			updateStatus();
			return true;
		} else {
			lockActivity();
			main.txtCells.setBackground(invalidFieldColor);
			updateStatus();
			return false;
		}
	}
	
	public boolean isIterationsValid(){
		String value = main.txtIterations.getText();
		if(isValidPositiveInteger(value) && Integer.valueOf(value) > 0){
			main.txtIterations.setBackground(SystemColor.text);
			updateStatus();
			return true;
		} else {
			lockActivity();
			main.txtIterations.setBackground(invalidFieldColor);
			updateStatus();
			return false;
		}
	}
	
	private boolean isValidPositiveInteger(String value){
		try{
			int integer = Integer.valueOf(value);
			if(integer > -1){
				return true;
			} 
			return false;
		} catch(Exception e){
			return false;
		}
	}
	
	public void updateStatus(){
		if(main.txtRuleNumber.getBackground().equals(invalidFieldColor)){
			setErrorStatus("errRuleNumber", "");
		} else if (main.txtCells.getBackground().equals(invalidFieldColor)) {
			setErrorStatus("errCells", "");
		} else if (main.txtIterations.getBackground().equals(invalidFieldColor)){
			setErrorStatus("errIterations", "");
		} else {
			setNormalStatus("lblStatus");
			releaseActivity();
		}
	}
	
	public void setErrorStatus(String key, String info){
		main.setStatus(key, info);
		main.lblStatus.setForeground(invalidFieldColor);
	}
	
	public void setNormalStatus(String key){
		main.setStatus(key, "");
		main.lblStatus.setForeground(SystemColor.textText);
	}
	
	private void lockActivity(){
		this.activityLocked = true;
		main.btnSimulateComplete.setEnabled(false);
		main.btnSimulateIteration.setEnabled(false);
		main.btnSave.setEnabled(false);
	}
	
	private void releaseActivity(){
		this.activityLocked = false;
		main.btnSimulateComplete.setEnabled(true);
		main.btnSimulateIteration.setEnabled(true);
		main.btnSave.setEnabled(true);
	}
	
	public boolean isActivityLocked(){
		return this.activityLocked;
	}

}
