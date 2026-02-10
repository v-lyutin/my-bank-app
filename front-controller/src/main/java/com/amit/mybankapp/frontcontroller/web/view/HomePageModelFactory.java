package com.amit.mybankapp.frontcontroller.web.view;

import com.amit.mybankapp.frontcontroller.application.result.HomePageResult;
import com.amit.mybankapp.frontcontroller.web.model.CashForm;
import com.amit.mybankapp.frontcontroller.web.model.ProfileForm;
import com.amit.mybankapp.frontcontroller.web.model.TransferForm;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class HomePageModelFactory {

    public void fillDefault(Model model, HomePageResult homePageResult) {
        this.fillPageData(model, homePageResult);
        this.ensureForms(model, homePageResult);
    }

    public void fillWithExistingForms(Model model,
                                      HomePageResult homePageResult,
                                      ProfileForm profileForm,
                                      CashForm cashForm,
                                      TransferForm transferForm) {
        this.fillPageData(model, homePageResult);

        if (!model.containsAttribute("profileForm")) {
            model.addAttribute("profileForm", profileForm != null ? profileForm : defaultProfileForm(homePageResult));
        }
        if (!model.containsAttribute("cashForm")) {
            model.addAttribute("cashForm", cashForm != null ? cashForm : CashForm.empty());
        }
        if (!model.containsAttribute("transferForm")) {
            model.addAttribute("transferForm", transferForm != null ? transferForm : TransferForm.empty());
        }
    }

    private void fillPageData(Model model, HomePageResult homePageResult) {
        model.addAttribute("me", homePageResult.currentCustomerView());
        model.addAttribute("balance", homePageResult.balance());
        model.addAttribute("recipients", homePageResult.recipients());
    }

    private void ensureForms(Model model, HomePageResult homePageResult) {
        if (!model.containsAttribute("profileForm")) {
            model.addAttribute("profileForm", defaultProfileForm(homePageResult));
        }
        if (!model.containsAttribute("cashForm")) {
            model.addAttribute("cashForm", CashForm.empty());
        }
        if (!model.containsAttribute("transferForm")) {
            model.addAttribute("transferForm", TransferForm.empty());
        }
    }

    private ProfileForm defaultProfileForm(HomePageResult homePageResult) {
        HomePageResult.CurrentCustomerView customer = homePageResult.currentCustomerView();
        return ProfileForm.fromCustomer(customer.firstName(), customer.lastName(), customer.birthDate());
    }

}
