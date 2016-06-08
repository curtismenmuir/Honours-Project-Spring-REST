package test_app.controllers;

import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import test_app.models.Picture;
import test_app.models.UserPicture;
import test_app.models.UserPictureDao;

/**
 * This Controller will handle URL requests for User Pictures. 
 * 
 * @author Dreads
 *
 */
@Controller
public class UserPictureController {

	
	/**
	 * This method will handle picture uploads
	 * 
	 * @param userPicture
	 * @return True/False
	 */
	@RequestMapping(value = "/upload-user-picture", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean uploadUserPicture(@RequestBody Picture userPicture){
		try{
			String picString = userPicture.getImageString();
			String username  = userPicture.getUsername();
			UserPicture userPic = new UserPicture(username, picString);
			userPictureDao.save(userPic);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * This method will handle updating user pictures. 
	 * 
	 * @param userPicture
	 * @return True/False
	 */
	@RequestMapping(value = "/update-user-picture", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean updateUserPicture(@RequestBody Picture userPicture){
		try{
			String imageString = userPicture.getImageString();
			String username  = userPicture.getUsername();
			UserPicture userPic = userPictureDao.findByUsername(username);
			userPic.setImageString(imageString);
			userPictureDao.save(userPic);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * This method will return a User's picture.
	 * 
	 * @param username
	 * @return picture (User Picture)
	 */
	@RequestMapping(value = "/get-user-picture", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public Picture getUserPicture(String username){
		try{
			UserPicture userPic = userPictureDao.findByUsername(username);
			String picString = userPic.getImageString();
			Picture pic = new Picture(picString, username);
			return pic;
		}catch(Exception ex){
			return null;
		}
	}
	
	@Autowired
	private UserPictureDao userPictureDao;
}
