package com.amit.mybankapp.frontcontroller.web.controller;

import com.amit.mybankapp.frontcontroller.application.result.HomePageResult;
import com.amit.mybankapp.frontcontroller.application.usecase.GetHomePageUseCase;
import com.amit.mybankapp.frontcontroller.web.controller.util.PageProvider;
import com.amit.mybankapp.frontcontroller.web.view.HomePageModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final GetHomePageUseCase getHomePageUseCase;

    private final HomePageModelFactory homePageModelFactory;

    @Autowired
    public HomeController(GetHomePageUseCase getHomePageUseCase, HomePageModelFactory homePageModelFactory) {
        this.getHomePageUseCase = getHomePageUseCase;
        this.homePageModelFactory = homePageModelFactory;
    }

    @GetMapping(path = "/")
    public String index(Model model) {
        HomePageResult homePageResult = this.getHomePageUseCase.getHomePage();
        this.homePageModelFactory.fillDefault(model, homePageResult);
        return PageProvider.HOME_PAGE;
    }

}
