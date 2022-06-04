package pl.estimateplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.estimateplus.entity.PriceList;
import pl.estimateplus.entity.PriceListItem;
import pl.estimateplus.repository.PriceListItemRepository;
import pl.estimateplus.repository.PriceListRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    String userName = "mockuser";

    private final PriceListRepository priceListRepository;
    private final PriceListItemRepository priceListItemRepository;

    public UserController(PriceListRepository priceListRepository, PriceListItemRepository priceListItemRepository) {
        this.priceListRepository = priceListRepository;
        this.priceListItemRepository = priceListItemRepository;
    }


    @GetMapping("")
    public String dashboard() {
        return "user-dashboard";
    }

    @GetMapping("/edit")
    public String editUser() {
        return "edit-user";
    }

    //Add/edit estimate
    @GetMapping("/estimate")
    public String estimate() {
        return "estimate-action";
    }

    @GetMapping("/addestimate")
    public String addEstimste() {
        return "estimate-add";
    }

    //Add user pricelist item
    @GetMapping("/additem")
    public String addUserItem(Model model) {
        model.addAttribute("newUserPriceListItem", new PriceListItem());
        model.addAttribute("userName", userName);
        return "add-user-item-form";
    }

    @GetMapping("/test1")
    public String t1(Model model) {

        model.addAttribute("userPriceList",priceListRepository.findByName(userName));
        System.out.println(priceListRepository.findByName(userName));

        return "user-pricelist";
    }

    @PostMapping("/additem")
    public String addUserItem(@Valid @ModelAttribute("newUserPriceListItem") PriceListItem priceListItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result);
//            System.out.println("!!!!HAS ERRORS");
            return "add-user-item-form";
        }
        priceListItem.setVendorName(userName);
        priceListItemRepository.save(priceListItem);
        PriceList userPR = null;
        if(priceListRepository.findByName(userName)==null)
        {
            userPR = new PriceList();
            userPR.setNumberOfItems(0l);
            userPR.setName(userName);
            userPR.setPriceListItems(new ArrayList<>());
            priceListRepository.save(userPR);
        }
        else {
            userPR = priceListRepository.findByName(userName);
        }
        userPR.getPriceListItems().add(priceListItem);
        userPR.setNumberOfItems(Long.valueOf(priceListRepository.findByName(userName).getPriceListItems().size())+1);
        priceListRepository.save(userPR);
        model.addAttribute("userPriceList", userPR);
        return "user-pricelist";
    }

    //View price list - user's and other
    @GetMapping("selectpricelist")
    public String selectPriceList() {
        return "price-list-select";
    }

    @GetMapping("showpricelist")
    public String showPriceList() {
        return "price-list-show";
    }

//    @ModelAttribute("userPriceList")
//    public PriceList getUserPriceList(String userName) {
//        return priceListRepository.findByName(userName);
//    }

//    @ModelAttribute("userName")
//    public String getUserName()
//    {
//        return "mockuser";
//    }

//    @ModelAttribute("newUserPriceListItem")
//    public PriceListItem getNewPriceListItem() {
//        return new PriceListItem();
//    }


}
