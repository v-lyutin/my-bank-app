package com.amit.mybankapp.frontcontroller.web.controller;

import com.amit.mybankapp.frontcontroller.application.result.HomePageResult;
import com.amit.mybankapp.frontcontroller.application.usecase.GetHomePageUseCase;
import com.amit.mybankapp.frontcontroller.application.usecase.UpdateProfileUseCase;
import com.amit.mybankapp.frontcontroller.web.controller.util.PageProvider;
import com.amit.mybankapp.frontcontroller.web.mapper.ProfileWebMapper;
import com.amit.mybankapp.frontcontroller.web.model.CashForm;
import com.amit.mybankapp.frontcontroller.web.model.ProfileForm;
import com.amit.mybankapp.frontcontroller.web.model.TransferForm;
import com.amit.mybankapp.frontcontroller.web.view.HomePageModelFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UpdateProfileUseCase updateProfileUseCase;

    private final GetHomePageUseCase getHomePageUseCase;

    private final HomePageModelFactory homePageModelFactory;

    private final ProfileWebMapper profileWebMapper;

    @Autowired
    public ProfileController(UpdateProfileUseCase updateProfileUseCase,
                             GetHomePageUseCase getHomePageUseCase,
                             HomePageModelFactory homePageModelFactory,
                             ProfileWebMapper profileWebMapper) {
        this.updateProfileUseCase = updateProfileUseCase;
        this.getHomePageUseCase = getHomePageUseCase;
        this.homePageModelFactory = homePageModelFactory;
        this.profileWebMapper = profileWebMapper;
    }

    @PutMapping(path = "/profile")
    public String saveProfile(@Valid @ModelAttribute(value = "profileForm") ProfileForm profileForm,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            HomePageResult homePageResult = this.getHomePageUseCase.getHomePage();
            this.homePageModelFactory.fillWithExistingForms(model, homePageResult, profileForm, CashForm.empty(), TransferForm.empty());
            return PageProvider.HOME_PAGE;
        }

        this.updateProfileUseCase.updateProfile(this.profileWebMapper.toUpdateProfileCommand(profileForm));
        redirectAttributes.addFlashAttribute("profileSuccess", "Profile saved");
        return PageProvider.REDIRECT_TO_HOME_PAGE;
    }

}
