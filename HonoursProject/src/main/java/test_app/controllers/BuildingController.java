package test_app.controllers;

import test_app.models.Building;
import test_app.models.BuildingDao;

import org.springframework.http.HttpStatus;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Controller will handle the URL's related to the buildings
 * 
 * @author Dreads
 *
 */
@Controller
public class BuildingController {

	/**
	 * This method will handle the url for returning the list of buildings
	 * 
	 * @return buildingList (List of Buildings)
	 */
	@RequestMapping(value = "/get-all-buildings", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<Building> getAllBuildings(){
		try{
			LinkedList<Building> buildingList = buildingDao.findAll();
			return buildingList;
		}
		catch(Exception ex){
			return null;
		}
	}
	@Autowired
	private BuildingDao buildingDao;
}
