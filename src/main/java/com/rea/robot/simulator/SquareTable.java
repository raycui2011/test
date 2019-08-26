package com.rea.robot.simulator;

import com.rea.robot.simulator.Location;
public class SquareTable implements Board {

    int maxRows;
    int maxColumns;

    public SquareTable (int x, int y) {
        this.maxRows = x;
        this.maxColumns = y;
    }

    @Override
    /** This function is used to check the location is valid or not*/
    public boolean isCoordinateWithinBounds(Location location) {
        if (location.getXValue() < 0 || location.getXValue() > this.maxColumns) {
                return false;
        }
        if (location.getYValue() < 0 || location.getYValue() > this.maxRows) {
                return false;
        }
            return true;
	}
}
 
