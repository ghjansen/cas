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
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import com.ghjansen.cas.ui.desktop.manager.EventManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class ExportFrame extends JFrame {
	private EventManager em;
	private JTextField txtCellScale;
	private JTextField txtGridLinesThickness;
	private JTextField txtCellLinesColour;
	public ExportFrame(EventManager em) {
		super();
		this.em = em;
		setAlwaysOnTop(true);
		setTitle("Advanced Export Options (ALPHA)");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 300, 248);
		getContentPane().setLayout(null);
		JPanel panelExportAO = new JPanel();
		panelExportAO.setBounds(6, 6, 288, 177);
		panelExportAO.setBorder(new TitledBorder(null, "Advanced Export Options", TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		
		getContentPane().add(panelExportAO);
		
		JLabel lblWarning1AO = new JLabel("WARNING: This is a development prototype!");
		lblWarning1AO.setBackground(Color.BLACK);
		lblWarning1AO.setForeground(Color.RED);
		lblWarning1AO.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblCellScale = new JLabel("Cell Scale:");
		lblCellScale.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblShowGridLines = new JLabel("Show Grid Lines:");
		lblShowGridLines.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblGridLinesThickness = new JLabel("Grid Lines Thickness:");
		lblGridLinesThickness.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblGridLinesColour = new JLabel("Grid Lines Colour:");
		lblGridLinesColour.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		txtCellScale = new JTextField();
		txtCellScale.setText("1");
		txtCellScale.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		txtCellScale.setColumns(10);
		
		JRadioButton rdbtnYes = new JRadioButton("Yes");
		rdbtnYes.setEnabled(false);
		rdbtnYes.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JRadioButton rdbtnNo = new JRadioButton("No");
		rdbtnNo.setEnabled(false);
		rdbtnNo.setSelected(true);
		rdbtnNo.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		txtGridLinesThickness = new JTextField();
		txtGridLinesThickness.setEnabled(false);
		txtGridLinesThickness.setText("1");
		txtGridLinesThickness.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		txtGridLinesThickness.setColumns(10);
		
		txtCellLinesColour = new JTextField();
		txtCellLinesColour.setEnabled(false);
		txtCellLinesColour.setText("#888888");
		txtCellLinesColour.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		txtCellLinesColour.setColumns(10);
		
		GroupLayout gl_panelExportAO = new GroupLayout(panelExportAO);
		gl_panelExportAO.setHorizontalGroup(
			gl_panelExportAO.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelExportAO.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelExportAO.createParallelGroup(Alignment.LEADING)
						.addComponent(lblWarning1AO, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addComponent(lblCellScale, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(txtCellScale, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addComponent(lblShowGridLines, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(rdbtnYes)
							.addGap(12)
							.addComponent(rdbtnNo))
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addComponent(lblGridLinesThickness)
							.addGap(12)
							.addComponent(txtGridLinesThickness, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addComponent(lblGridLinesColour, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(txtCellLinesColour, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panelExportAO.setVerticalGroup(
			gl_panelExportAO.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelExportAO.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblWarning1AO, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panelExportAO.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addGap(11)
							.addComponent(lblCellScale, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtCellScale, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelExportAO.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addGap(4)
							.addComponent(lblShowGridLines, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
						.addComponent(rdbtnYes)
						.addComponent(rdbtnNo))
					.addGroup(gl_panelExportAO.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addGap(9)
							.addComponent(lblGridLinesThickness, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addGap(4)
							.addComponent(txtGridLinesThickness, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_panelExportAO.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addGap(11)
							.addComponent(lblGridLinesColour, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelExportAO.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtCellLinesColour, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
					.addGap(76))
		);
		panelExportAO.setLayout(gl_panelExportAO);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				txtCellScale.requestFocus();
			}
		});
		btnCancel.setBounds(6, 189, 138, 29);
		btnCancel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		getContentPane().add(btnCancel);
		
		JButton btnExport = new JButton("Export");
		btnExport.setBounds(156, 189, 138, 29);
		btnExport.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		getContentPane().add(btnExport);
		this.setResizable(false);
		this.setVisible(false);
	}
}
