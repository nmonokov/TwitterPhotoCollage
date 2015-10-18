package com.twitapp.controller;

import com.twitapp.user.data.TwitterUser;
import com.twitapp.user.service.TwitterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class AppController {

    @Autowired
    private TwitterUserService twitterUserService;

    @RequestMapping(method = RequestMethod.GET)
    public String start(){
        return "login";
    }

    @RequestMapping(value = "collage", method = RequestMethod.GET)
    public String generateCollage(@RequestParam("login") String login,
                                  @RequestParam("width") String widthView,
                                  @RequestParam("height") String heightView,
                                  @RequestParam(value = "diffSize", required = false) String diffSizeView,
                                  HttpServletRequest request){
        Integer width = null;
        Integer height = null;
        boolean diffSize = "on".equals(diffSizeView);
        String message = "";
        if(widthView != null && heightView != null) {
            width = Integer.parseInt(widthView);
            height = Integer.parseInt(heightView);
        }
        if(login != null && width != null && height != null) {
            try {
                login = login.charAt(0) == '@' ? login.split("@")[1].trim() : login;
                List<TwitterUser> list = twitterUserService.getListOfUsersByLogin(login, width, height, diffSize);
                twitterUserService.generateCollage(list, width, height, diffSize);
            } catch (TwitterException e) {
                if (e.getStatusCode() == 404) {
                    message = "Користувача з таким логіном не існує.";
                } else if (e.getStatusCode() == 429) {
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    message = "Вибачте, цей сервер тимчасово недоступний. Спробуйте знову через 15 хвилин." + dateFormat.format(cal.getTime());
                } else {
                    message = "Вибачте, на сервері виникла помилка. Спробуйте знову.";
                }
                request.getSession().setAttribute("error", message);
                return "redirect:/";
            } catch (IOException e) {
                message = "Вибачте, на сервері виникла помилка. Спробуйте знову.";
                request.getSession().setAttribute("error", message);
                return "redirect:/";
            }
        }
        request.getSession().removeAttribute("error");
        return "collage";
    }

    @RequestMapping(value = "collage/image", method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> drawCollage() throws IOException{
        String filename = "../webapps/project-data/images/collage.png";
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(filename)));
        byte[] image = new byte[inputStream.available()];
        for(int i = 0, count = 0; (i = inputStream.read()) != -1;){
            image[count++] = (byte)i;
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(image, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "back", method = RequestMethod.GET)
    public String back(){
        return "redirect:/";
    }
}
