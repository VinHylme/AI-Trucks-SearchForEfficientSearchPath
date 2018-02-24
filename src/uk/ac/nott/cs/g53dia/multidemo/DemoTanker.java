package uk.ac.nott.cs.g53dia.multidemo;
import uk.ac.nott.cs.g53dia.multilibrary.*;

import java.util.Random;


/**
 * A simple example Tanker
 * 
 * @author Julian Zappala
 */
/*
 * 
 * Copyright (c) 2011 Julian Zappala
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */


public class DemoTanker extends Tanker  {
	
	private  Point LATEST_FUEL_PUMP_LOCATION = FUEL_PUMP_LOCATION;
	private  Point LATEST_WELL_LOCATION = null;
	private  Point LATEST_STATION_LOCATION = null;
	private int direction=0;
    public DemoTanker(int direction) {
    	this.direction = direction;
    }

    /*
     * The following is a very simple demonstration of how to write a tanker. The
     * code below is very stupid and pretty much randomly picks actions to perform. 
     */
    public Action senseAndAct(Cell[][] view, long timestep) {
    	
    	// If fuel tank is low, move towards the fuel pump and refuel
        if ((getFuelLevel() <= MAX_FUEL/2)) {
        	if(getCurrentCell(view) instanceof FuelPump){
            	return new RefuelAction();
            }
        	
        	Point p = scanViewRange(view, "FuelPump");
        	if(p != null) {
        		LATEST_FUEL_PUMP_LOCATION = p;
        		return new MoveTowardsAction(p);
        	}
            return new MoveTowardsAction(LATEST_FUEL_PUMP_LOCATION);
        } 
        
        // If high on waste, move towards well and dispose of it
        if ((getWasteLevel() > MAX_WASTE*4/5)) {
        	if(getCurrentCell(view) instanceof Well){
            	return new DisposeWasteAction();
            }
        	Point p = scanViewRange(view, "Well");
        	if(p != null) {
        		LATEST_WELL_LOCATION = p;
        		return new MoveTowardsAction(p);
        	}
        	if(LATEST_WELL_LOCATION!=null){
        		return new MoveTowardsAction(LATEST_WELL_LOCATION);
        	}
        	return new MoveAction(newDirection(direction));
        }
        
        // else find closest station with a task and load the waste 
        if(getCurrentCell(view) instanceof Station){
        	if(((Station)getCurrentCell(view)).getTask() instanceof Task) {
        		return new LoadWasteAction(((Station)getCurrentCell(view)).getTask());
        	}
        	else {
        		LATEST_STATION_LOCATION = null;
        	}
        }
        Point p = scanViewRange(view, "Station");
        if(p != null) {
        	LATEST_STATION_LOCATION = p;
        	return new MoveTowardsAction(p);
        }     
        
        if(LATEST_STATION_LOCATION!=null){
    		return new MoveTowardsAction(LATEST_STATION_LOCATION);
    	}
    	return new MoveAction(newDirection(direction));
            
    }
    
    private int newDirection(int direction){
    	if(direction > 7){
    		//System.out.println(direction);
    		return ((int) Math.random() * 8);
    	}
    	if(direction < 4){
    		return direction+4;
    	}
    	return direction-4;
    }
  
    private boolean checkCell(Cell cell, String cellType) {
    	if(cellType == "FuelPump" && cell instanceof FuelPump) {
    		return true;
    	}
    	if(cellType == "Well" && cell instanceof Well) {
    		return true;
    	}
    	if(cellType == "Station" && cell instanceof Station && ((Station)cell).getTask() instanceof Task) {
    		return true;
    	}

    	return false;
    }
    
    
    private Point scanViewRange(Cell[][] view, String cellType){
    	for(int i = 1; i <= VIEW_RANGE; ++i) {
    		int j;
    		for(j = 1; j < 2*i ; ++j) {
    			if(checkCell(view[VIEW_RANGE - i][VIEW_RANGE - i + j], cellType)) {
    				return view[VIEW_RANGE - i][VIEW_RANGE - i + j].getPoint();
    			}
    			if(checkCell(view[VIEW_RANGE - i + j][VIEW_RANGE + i], cellType)){
    				return view[VIEW_RANGE - i + j][VIEW_RANGE + i].getPoint();
    			}
    			if(checkCell(view[VIEW_RANGE + i][VIEW_RANGE + i - j], cellType)){
    				return view[VIEW_RANGE + i][VIEW_RANGE + i - j].getPoint();
    			}
    			if(checkCell(view[VIEW_RANGE + i - j][VIEW_RANGE - i], cellType)){
    				return view[VIEW_RANGE + i - j][VIEW_RANGE - i].getPoint();
    			}
    		}
    		if(checkCell(view[VIEW_RANGE - i][VIEW_RANGE - i], cellType)){
    			return view[VIEW_RANGE - i][VIEW_RANGE - i].getPoint();
    		}
			if(checkCell(view[VIEW_RANGE - i][VIEW_RANGE + i], cellType)){
				return view[VIEW_RANGE - i][VIEW_RANGE + i].getPoint();
			}
			if(checkCell(view[VIEW_RANGE + i][VIEW_RANGE + i], cellType)){
				return view[VIEW_RANGE + i][VIEW_RANGE + i].getPoint();
			}
			if(checkCell(view[VIEW_RANGE + i][VIEW_RANGE - i], cellType)){
				return view[VIEW_RANGE + i][VIEW_RANGE - i].getPoint();
			}
			
    	}
    	return null;	
    }
    
    

}
