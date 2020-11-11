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
import java.util.regex.Pattern;

import com.ghjansen.cas.ui.desktop.i18n.TranslationKey;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class GUIValidator {
	
	private Main main;
	private Color invalidFieldColor;
	private boolean activityLocked = false;
	private Boolean aOEActivityLocked = false;
	private static final String HEX_PATTERN = "^#?(?:[0-9a-fA-F]{2}){3}$";
    private Pattern hexValidator;

	public GUIValidator(Main main, Color invalidFieldColor) {
		this.main = main;
		this.invalidFieldColor = invalidFieldColor;
		this.hexValidator = Pattern.compile(HEX_PATTERN);
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
	
	public boolean isAEOCellScaleValid(){
		String value = main.aeo.getTxtAEOCellScale().getText();
		if(isValidPositiveInteger(value) && Integer.valueOf(value) > 0){
			main.aeo.getTxtAEOCellScale().setBackground(SystemColor.text);
			updateStatusAEO(1);
			return true;
		} else {
			lockActivityAOE(1);
			main.aeo.getTxtAEOCellScale().setBackground(invalidFieldColor);
			updateStatusAEO(1);
			return false;
		}
	}
	
	public boolean isAEOCellLinesThicknessValid(){
		String value = main.aeo.getTxtAEOGridLinesThickness().getText();
		if(isValidPositiveInteger(value)){
			int t = Integer.valueOf(value);
			int c = Integer.valueOf(main.aeo.getTxtAEOCellScale().getText());
			if(t >= 1 && t <= (c-1)/2){
				main.aeo.getTxtAEOGridLinesThickness().setBackground(SystemColor.text);
				updateStatusAEO(3);
				return true;
			}
		}
		lockActivityAOE(3);
		main.aeo.getTxtAEOGridLinesThickness().setBackground(invalidFieldColor);
		updateStatusAEO(3);
		return false;
	}
	
	public boolean isAEOCellColourValid(){
		String value = main.aeo.getTxtAEOCellLinesColour().getText();
		if(!String.valueOf(value).isEmpty() && hexValidator.matcher(value).matches()){
			main.aeo.getTxtAEOCellLinesColour().setBackground(SystemColor.text);
			updateStatusAEO(4);
			return true;
		} else {
			lockActivityAOE(4);
			main.aeo.getTxtAEOCellLinesColour().setBackground(invalidFieldColor);
			updateStatusAEO(4);
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
	
	public void updateStatusAEO(int level){
		if(main.aeo.getTxtAEOCellScale().getBackground().equals(invalidFieldColor)){
			main.aeo.getLblAEOStatus().setText("Cell Scale must be >= 1");
			main.aeo.getLblAEOStatus().setForeground(invalidFieldColor);
		} else if (main.aeo.getTxtAEOGridLinesThickness().getBackground().equals(invalidFieldColor)){
			main.aeo.getLblAEOStatus().setText("Grid Lines Thickness must be >= 1 and <= " +
		(Integer.valueOf(main.aeo.getTxtAEOCellScale().getText()) -1 ) / 2);
			main.aeo.getLblAEOStatus().setForeground(invalidFieldColor);
		} else if (main.aeo.getTxtAEOCellLinesColour().getBackground().equals(invalidFieldColor)) {
			main.aeo.getLblAEOStatus().setText("Cell Lines Colour must be a RGB hexadecimal");
			main.aeo.getLblAEOStatus().setForeground(invalidFieldColor);
		} else {
			main.aeo.getLblAEOStatus().setText("");
			main.aeo.getLblAEOStatus().setForeground(SystemColor.text);
			releaseActivityAEO(level);
		}
	}
	
	public void setErrorStatus(String key, String info){
		main.setStatus(key, info);
		main.lblStatus.setForeground(invalidFieldColor);
	}

	public void setErrorStatus(TranslationKey tk, String info){
		setErrorStatus(tk.getKey(), info);
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
	
	private void lockActivityAOE(int level){
		this.aOEActivityLocked = true;
		main.aeo.getBtnAEOExport().setEnabled(false);
		if(level <= 1) {
			main.aeo.getRdbtnAEOYes().setEnabled(false);
			main.aeo.getRdbtnAEONo().setEnabled(false);
		}
		if(level <= 2) {
			main.aeo.getTxtAEOGridLinesThickness().setEnabled(false);
		}
		if(level <= 3) {
			main.aeo.getTxtAEOCellLinesColour().setEnabled(false);
		}
	}
	
	private void releaseActivity(){
		this.activityLocked = false;
		main.btnSimulateComplete.setEnabled(true);
		main.btnSimulateIteration.setEnabled(true);
		main.btnSave.setEnabled(true);
	}
	
	private void releaseActivityAEO(int level){
		this.aOEActivityLocked = false;
		main.aeo.getBtnAEOExport().setEnabled(true);
		if(level >= 1) {
			main.aeo.getRdbtnAEOYes().setEnabled(true);
			main.aeo.getRdbtnAEONo().setEnabled(true);
		}
		if(level >= 2) {
			main.aeo.getTxtAEOGridLinesThickness().setEnabled(true);
		}
		if(level >= 3) {
			main.aeo.getTxtAEOCellLinesColour().setEnabled(true);
		}
	}
	
	public boolean isActivityLocked(){
		return this.activityLocked;
	}
	
	public boolean isAEOActivityLocked(){
		return this.aOEActivityLocked;
	}

}
