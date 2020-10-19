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

import com.ghjansen.cas.control.exception.InvalidSimulationParameterException;
import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalCell;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */

@Ignore
public class MainTest {

    @Test
    public void elementaryRandomInitialCondition() throws InterruptedException {
        int msDelay = 3000;
        String txtCells = "540";
        String txtIterations = "539";
        Main main = new Main();
        main.em.omitDiscardConfirmation = true;
        for(int i = 0; i < 256; i++){
            main.txtCells.setText(txtCells);
            main.txtIterations.setText(txtIterations);
            main.txtRuleNumber.setText(String.valueOf(i));
            main.rdbtnRandom.setSelected(true);
            System.out.println("Simulating rule "+i);
            main.btnSimulateComplete.doClick();
            Thread.sleep(msDelay);
            System.out.println("Saving rule "+i);
            saveAction(main, "/tmp/elementary"+i+"random.cas");
            Thread.sleep(msDelay);
            System.out.println("Exporting rule "+i);
            exportAction(main, "/tmp/elementary"+i+"random.png");
            Thread.sleep(msDelay);
            main.btnDiscard.doClick();
        }
    }

    @Test
    public void elementaryUniqueBlackCellInitialCondition() throws InterruptedException {
        int msDelay = 3000;
        String txtCells = "540";
        String txtIterations = "539";
        Main main = new Main();
        main.em.omitDiscardConfirmation = true;
        for(int i = 0; i < 256; i++){
            main.txtCells.setText(txtCells);
            main.txtIterations.setText(txtIterations);
            main.txtRuleNumber.setText(String.valueOf(i));
            System.out.println("Simulating rule "+i);
            main.btnSimulateComplete.doClick();
            Thread.sleep(msDelay);
            System.out.println("Saving rule "+i);
            saveAction(main, "/tmp/elementary"+i+"uniqueblackcell.cas");
            Thread.sleep(msDelay);
            System.out.println("Exporting rule "+i);
            exportAction(main, "/tmp/elementary"+i+"uniqueblackcell.png");
            Thread.sleep(msDelay);
            main.btnDiscard.doClick();
        }
    }

    private void saveAction(Main main, String path){
        ActivityState previous = main.em.activityState;
        main.em.setActivityState(ActivityState.SAVING_FILE);
        if(main.em.simulationParameter == null){
            try {
                main.em.createSimulationParameter();
            } catch (InvalidSimulationParameterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SimulationBuilderException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        String fileName = String.valueOf(path);
        if(!fileName.endsWith(".cas")){
            fileName = fileName + ".cas";
        }
        String content = main.em.gson.toJson(main.em.simulationParameter);
        FileWriter fw;
        try {
            fw = new FileWriter(fileName);
            fw.write(content);
            fw.close();
            main.em.validator.setNormalStatus("msgSaveSuccess");
            main.em.setActivityState(previous);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void exportAction(Main main, String path){
        ActivityState previous = main.em.activityState;
        main.em.setActivityState(ActivityState.EXPORTING_FILE);
        String fileName = String.valueOf(path);
        if(!fileName.endsWith(".png")){
            fileName = fileName + ".png";
        }
        int width = main.em.simulationController.getSimulation().getUniverse().getTime().getRelative().get(0).getLimit();
        int height = main.em.simulationController.getSimulation().getUniverse().getTime().getLimit() + 1;
        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // initial condition
        for(int i = 0; i < main.em.simulationController.getSimulation().getUniverse().getSpace().getInitial().size(); i++){
            UnidimensionalCell c = (UnidimensionalCell) main.em.simulationController.getSimulation().getUniverse().getSpace().getInitial().get(i);
            Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
            buffer.setRGB(i, 0, color.getRGB());
        }
        // history
        for(int j = 0; j < main.em.simulationController.getSimulation().getUniverse().getSpace().getHistory().size(); j++){
            List<UnidimensionalCell> cells = (List<UnidimensionalCell>) main.em.simulationController.getSimulation().getUniverse().getSpace().getHistory().get(j);
            for(int i = 0; i < cells.size(); i++){
                UnidimensionalCell c = (UnidimensionalCell) cells.get(i);
                Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
                buffer.setRGB(i, j+1, color.getRGB());
            }
        }
        // current/last
        for(int i = 0; i < main.em.simulationController.getSimulation().getUniverse().getSpace().getCurrent().size(); i++){
            UnidimensionalCell c = (UnidimensionalCell) main.em.simulationController.getSimulation().getUniverse().getSpace().getCurrent().get(i);
            Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
            buffer.setRGB(i, height-1, color.getRGB());
        }
        File f = new File(fileName);
        try {
            ImageIO.write(buffer, "PNG", f);
            main.em.validator.setNormalStatus("msgExportSuccess");
            main.em.setActivityState(previous);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
