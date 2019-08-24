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

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class ExportFrame extends JFrame {
	public ExportFrame() {
		super();
		setTitle("Advanced Export Options (ALPHA)");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 425, 329);
		JPanel panelExportAO = new JPanel();
		panelExportAO.setBorder(new TitledBorder(null, "Advanced Export Options", TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelExportAO, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelExportAO, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JLabel lblWarningAO = new JLabel("WARNING: This feature is a prototype and still under development.");
		lblWarningAO.setForeground(Color.RED);
		lblWarningAO.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		panelExportAO.add(lblWarningAO);
		getContentPane().setLayout(groupLayout);
		this.setResizable(false);
		this.setVisible(false);
	}
}
