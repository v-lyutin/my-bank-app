package com.amit.mybankapp.frontcontroller.web.controller;

import com.amit.mybankapp.frontcontroller.application.result.CreateTransferResult;
import com.amit.mybankapp.frontcontroller.application.result.HomePageResult;
import com.amit.mybankapp.frontcontroller.application.usecase.CreateTransferUseCase;
import com.amit.mybankapp.frontcontroller.application.usecase.GetHomePageUseCase;
import com.amit.mybankapp.frontcontroller.web.controller.util.PageProvider;
import com.amit.mybankapp.frontcontroller.web.mapper.TransferWebMapper;
import com.amit.mybankapp.frontcontroller.web.model.CashForm;
import com.amit.mybankapp.frontcontroller.web.model.TransferForm;
import com.amit.mybankapp.frontcontroller.web.view.HomePageModelFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransferController {

    private final CreateTransferUseCase createTransferUseCase;

    private final GetHomePageUseCase getHomePageUseCase;

    private final HomePageModelFactory homePageModelFactory;

    private final TransferWebMapper transferWebMapper;

    @Autowired
    public TransferController(CreateTransferUseCase createTransferUseCase,
                              GetHomePageUseCase getHomePageUseCase,
                              HomePageModelFactory homePageModelFactory,
                              TransferWebMapper transferWebMapper) {
        this.createTransferUseCase = createTransferUseCase;
        this.getHomePageUseCase = getHomePageUseCase;
        this.homePageModelFactory = homePageModelFactory;
        this.transferWebMapper = transferWebMapper;
    }

    @PostMapping(path = "/transfers")
    public String transfer(@Valid @ModelAttribute(value = "transferForm") TransferForm form,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            HomePageResult homePageResult = this.getHomePageUseCase.getHomePage();
            this.homePageModelFactory.fillWithExistingForms(model, homePageResult, null, CashForm.empty(), form);
            return PageProvider.HOME_PAGE;
        }

        CreateTransferResult createTransferResult = this.createTransferUseCase.createTransfer(transferWebMapper.toCreateTransferCommand(form));
        redirectAttributes.addFlashAttribute(
                "transferSuccess",
                "Transfer: " + createTransferResult.transferId() + " (" + createTransferResult.status() + ")"
        );
        return PageProvider.REDIRECT_TO_HOME_PAGE;
    }

}
