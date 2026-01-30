package com.amit.mybankapp.frontcontroller.web.controller;

import com.amit.mybankapp.frontcontroller.application.result.HomePageResult;
import com.amit.mybankapp.frontcontroller.application.usecase.DepositCashUseCase;
import com.amit.mybankapp.frontcontroller.application.usecase.GetHomePageUseCase;
import com.amit.mybankapp.frontcontroller.application.usecase.WithdrawCashUseCase;
import com.amit.mybankapp.frontcontroller.web.controller.util.PageProvider;
import com.amit.mybankapp.frontcontroller.web.mapper.CashWebMapper;
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
public class WalletController {

    private final DepositCashUseCase depositCashUseCase;

    private final WithdrawCashUseCase withdrawCashUseCase;

    private final GetHomePageUseCase getHomePageUseCase;

    private final HomePageModelFactory homePageModelFactory;

    private final CashWebMapper cashWebMapper;

    @Autowired
    public WalletController(DepositCashUseCase depositCashUseCase,
                            WithdrawCashUseCase withdrawCashUseCase,
                            GetHomePageUseCase getHomePageUseCase,
                            HomePageModelFactory homePageModelFactory,
                            CashWebMapper cashWebMapper) {
        this.depositCashUseCase = depositCashUseCase;
        this.withdrawCashUseCase = withdrawCashUseCase;
        this.getHomePageUseCase = getHomePageUseCase;
        this.homePageModelFactory = homePageModelFactory;
        this.cashWebMapper = cashWebMapper;
    }


    @PostMapping(path = "/cash/deposit")
    public String deposit(@Valid @ModelAttribute("cashForm") CashForm form,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            HomePageResult homePageResult = this.getHomePageUseCase.getHomePage();
            this.homePageModelFactory.fillWithExistingForms(model, homePageResult, null, form, TransferForm.empty());
            return PageProvider.HOME_PAGE;
        }

        this.depositCashUseCase.deposit(this.cashWebMapper.toWalletOperationCommand(form));
        redirectAttributes.addFlashAttribute("cashSuccess", "Deposit completed");
        return PageProvider.REDIRECT_TO_HOME_PAGE;
    }

    @PostMapping(path = "/cash/withdraw")
    public String withdraw(@Valid @ModelAttribute(value = "cashForm") CashForm form,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            HomePageResult homePageResult = this.getHomePageUseCase.getHomePage();
            this.homePageModelFactory.fillWithExistingForms(model, homePageResult, null, form, TransferForm.empty());
            return PageProvider.HOME_PAGE;
        }

        this.withdrawCashUseCase.withdraw(this.cashWebMapper.toWalletOperationCommand(form));
        redirectAttributes.addFlashAttribute("cashSuccess", "Withdrawal completed");
        return PageProvider.REDIRECT_TO_HOME_PAGE;
    }

}
