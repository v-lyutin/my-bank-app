package com.amit.mybankapp.frontcontroller.web.advice;

import com.amit.mybankapp.frontcontroller.application.exception.ExternalServiceException;
import com.amit.mybankapp.frontcontroller.application.result.HomePageResult;
import com.amit.mybankapp.frontcontroller.application.usecase.GetHomePageUseCase;
import com.amit.mybankapp.frontcontroller.web.controller.util.PageProvider;
import com.amit.mybankapp.frontcontroller.web.view.HomePageModelFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final GetHomePageUseCase getHomePageUseCase;

    private final HomePageModelFactory homePageModelFactory;

    @Autowired
    public GlobalExceptionHandler(GetHomePageUseCase getHomePageUseCase, HomePageModelFactory homePageModelFactory) {
        this.getHomePageUseCase = getHomePageUseCase;
        this.homePageModelFactory = homePageModelFactory;
    }

    @ExceptionHandler(value = ExternalServiceException.class)
    public String handleExternalServiceException(ExternalServiceException exception,
                                                 HttpServletRequest request,
                                                 Model model) {
        HomePageResult homePageResult = this.getHomePageUseCase.getHomePage();
        this.homePageModelFactory.fillDefault(model, homePageResult);

        String path = request.getRequestURI();

        if (path.startsWith("/cash/")) {
            model.addAttribute("cashError", exception.getMessage());
        } else if (path.startsWith("/transfers")) {
            model.addAttribute("transferError", exception.getMessage());
        } else if (path.startsWith("/profile")) {
            model.addAttribute("profileError", exception.getMessage());
        } else {
            model.addAttribute("globalError", exception.getMessage());
        }

        return PageProvider.HOME_PAGE;
    }

}
