package com.WeGather.WeGather.controllers;

import com.WeGather.WeGather.models.*;
import com.WeGather.WeGather.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.thymeleaf.util.ArrayUtils.toArray;

@Controller
public class RaisedWorkProjectController<T> {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RaisedWorkProjectRepository raisedWorkProjectRepository;
    @Autowired
    CharityWorkContributorsRepository charityWorkContributorsRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    CommentsRepository commentsRepository;

    @GetMapping(value = "/raisedWork")
    public String getRaisedWork() {
        return "AddRaisedWorkProject.html";
    }
//
    @PostMapping(value = "/raisedWork")
    public RedirectView addRaisedWork(@RequestParam(value = "startFrom") String startFrom,
                                      @RequestParam(value = "endAt") String endAt,
                                      @RequestParam(value = "image") String image,
                                      @RequestParam(value = "topic") String topic,
                                      @RequestParam(value = "requiredContAmount") Integer requiredContAmount,
                                      @RequestParam(value = "description") String description,

                                      @RequestParam(value = "longitude") String longitude,
                                      @RequestParam(value = "latitude") String latitude,
                                      @RequestParam(value = "locationDescription") String locationDescription
//                                      @RequestParam(value = "governorate_id") Long governorate_id,
//                                      @RequestParam(value = "district_id") Long district_id,
//                                      @RequestParam(value = "suburb_id") Long suburb_id
            , Principal p) {


//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter createdAt = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
//        //Date createAt = now.format(format);


        List<String> images = new ArrayList<>();
        images.add(image);
        System.out.println(images);
        Location location = new Location(longitude, latitude, locationDescription);
        System.out.println("location: " + location);
        String loggedInUserName = p.getName();
        locationRepository.save(location);

        Users loggedInUser = usersRepository.findByUsername(loggedInUserName);

        System.out.println("logged in user " + loggedInUser);
        RaisedWorkProject raisedWorkProject = new RaisedWorkProject(startFrom, endAt, images, topic, requiredContAmount, description, location, loggedInUser);
        System.out.println("raisedWorkProject " + raisedWorkProject);
        raisedWorkProjectRepository.save(raisedWorkProject);

        return new RedirectView("/raisedWorkView");

    }


    @GetMapping(value = "/raisedWorkView")
    public String getAllRaisedWork(Principal p, Model m) {
//        String userName = ((UsernamePasswordAuthenticationToken) p).getName();
//        Users user = usersRepository.findByUsername(userName);
//        m.addAttribute("user", usersRepository.findById(user.getId())
//                                              .get());
        Iterable<RaisedWorkProject> df =raisedWorkProjectRepository.findAll() ;
        System.out.println("This is DF:"+df);
        m.addAttribute("user",df);
        Integer amount = 0;
        ArrayList<Integer> array=new ArrayList<>();
        //----------------------------------------
                for (RaisedWorkProject don : df)
        {
            Long id=don.getId();
            List<CharityWorkContributors> donate = charityWorkContributorsRepository.findByUserWorkRaiserId(id);
            for (CharityWorkContributors donTow : donate)
            {
                amount+=donTow.getAvailableContAmount();
            }
            array.add(amount);
            amount=0;
        }
            System.out.println("This is the array:"+array);
        m.addAttribute("amountArray",array);
        return "ViewRaisedWork.html";
//        ------------------------------------------------
//        String userName = ((UsernamePasswordAuthenticationToken) p).getName();
//        Users user = usersRepository.findByUsername(userName);
//        m.addAttribute("user", usersRepository.findById(user.getId())
//                                              .get());
//        return "ViewRaisedWork.html";
    }

    @GetMapping(value = "/fundContributors/{id}")
    public String displayRaisedWork(@PathVariable Long id, Model m, Principal p) {
        Object principal = SecurityContextHolder.getContext(). getAuthentication(). getPrincipal();
        RaisedWorkProject raisedWorkProject = raisedWorkProjectRepository.findById(id)
                                                                         .get();
        m.addAttribute("raisedWorkProject", raisedWorkProject);

        if(principal instanceof UserDetails) {
            String loggedInUserName = p.getName();
            Users loggedInUser = usersRepository.findByUsername(loggedInUserName);
            m.addAttribute("userId", loggedInUser.getId());
        }

        List<Comments> raisedWorkFundComments =  commentsRepository.findRaisedWorkFundId(id);
        List<Comments> allComments =  commentsRepository.findComment(id);


//Users user = (Users) commentsRepository.findById(comments);
//        System.out.println(user);
        m.addAttribute("AllComment",allComments);
//        m.addAttribute("AllComment",Arrays.toString(allComments..toArray()));
//        m.addAttribute("AllComment",raisedWorkFundComments);

        return "raisedWorkView.html";
    }
//    @GetMapping("/displayCards")
//    public String  displayPost(Principal p,Model m){
//        Iterable<RaisedFundProject> df =rasisdFundProjectRepositorise.findAll() ;
//        m.addAttribute("user",df);
//        Integer amount = 0;
//        ArrayList<Integer> array=new ArrayList<>();
//        for (RaisedFundProject don : df)
//        {
//            Long id=don.getId();
//            List<CharityFundContributors> donate = charityRepository.findByUserFundRaiserId(id);
//            for (CharityFundContributors donTow : donate)
//            {
//                amount+=donTow.getAmountPaid();
//            }
//            array.add(amount);
//            amount=0;
//        }
//        m.addAttribute("amountArray",array);
//        return "ViewRaisedFund.html";
//    }

    @PostMapping(value = "/addContributors")
    public RedirectView addContribute(@RequestParam(value = "workedRaised_id") Long workedRaised_id,
                                      @RequestParam(value = "userWorkRaiser_id") Long userWorkRaiser_id,

                                      @RequestParam(value = "status") Integer status ) {

        CharityWorkContributors charityWorkContributors = new CharityWorkContributors(workedRaised_id, userWorkRaiser_id, 1, status);
        charityWorkContributorsRepository.save(charityWorkContributors);


        return new RedirectView("/raisedWorkView");
    }
}
