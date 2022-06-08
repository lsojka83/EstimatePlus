package pl.estimateplus.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import pl.estimateplus.entity.*;
import pl.estimateplus.repository.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.multi.MultiOptionPaneUI;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@Scope("session")
@SessionAttributes("estimate")
//@SessionAttributes("user")
@RequestMapping("/user")
public class UserController {

    String userName = "mockuser";

    private final PriceListRepository priceListRepository;
    private final PriceListItemRepository priceListItemRepository;
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;
    private final EstimateItemRepository estimateItemRepository;


    public UserController(PriceListRepository priceListRepository,
                          PriceListItemRepository priceListItemRepository,
                          UserRepository userRepository,
                          EstimateRepository estimateRepository,
                          EstimateItemRepository estimateItemRepository) {
        this.priceListRepository = priceListRepository;
        this.priceListItemRepository = priceListItemRepository;
        this.userRepository = userRepository;
        this.estimateRepository = estimateRepository;
        this.estimateItemRepository = estimateItemRepository;
    }


    @GetMapping("")
    public String dashboard(Model model) {

        model.addAttribute("numberOfEstimates", userRepository.findByUserName(userName).getEstimates().size());
        model.addAttribute("estimates",userRepository.findByUserName(userName).getEstimates());

        return "user-dashboard";
    }

    @GetMapping("/edit")
    public String editUser() {
        return "edit-user";
    }

    //Add/edit estimate
    @GetMapping("/estimate")
    public String estimate(
            Model model
//            ,
//            HttpServletRequest request
    ) {
//        model.addAttribute("user",userRepository.findByUserName(userName));
//        model.addAttribute("estimates", userRepository.findByUserName(userName).getEstimates());
//        request.getSession().setAttribute("estimate", new Estimate());
//        User user = (User) model.getAttribute("user");
        User user = userRepository.findByUserName(userName);
        model.addAttribute("user", user);
        List<String> estimatesNames = user.getEstimates().stream().map(e -> e.getName()).collect(Collectors.toList());
        model.addAttribute("estimatesNames",estimatesNames);

        return "estimate-action";
    }

    @PostMapping("/estimate")
    public String estimatePost(
            Model model
            , HttpServletRequest request
            , @RequestParam String button
            , @RequestParam(required = false) String selectedEstimate
            ) {

        if (button != null && button.equals("Create new"))
        {
            model.addAttribute("estimate", new Estimate());

            System.out.println("!!! Create new");
        }
        if (button != null && button.equals("Edit")) {
//            request.getSession().setAttribute("estimate", estimateRepository.findByName(selectedEstimate));
            model.addAttribute("estimate", estimateRepository.findByName(selectedEstimate));
            System.out.println("!!! Edit");
        }

//        return "forward:/user/estimateform";
        return "estimate-form";
    }

    @GetMapping("/estimateform")
    public String showEstimateForm1(Model model,
                                    @RequestParam(required = false) Long estimateId
    )
    {
        model.addAttribute("estimate",estimateRepository.findById(estimateId).get());

        return "estimate-form";
    }

    @PostMapping("/estimateform")
    public String showEstimateForm(Model model,
                                   @RequestParam(required = false) String button,
                                   @RequestParam(required = false) String searchedItem,
                                   @RequestParam(required = false) String priceListItemId,
                                   HttpServletRequest request,
                                   @Valid @ModelAttribute("estimate")Estimate estimate,
                                   BindingResult result,
                                   @ModelAttribute User user,
                                   @RequestParam(required = false) Long estimateId

    ) {
        System.out.println("!!!!estimateIdModel1"+ request.getAttribute("estimateIdModel1"));
        System.out.println("!!!!estimateId"+ request.getAttribute("estimateId"));
        System.out.println("!!!!estimateId"+ estimateId);
        System.out.println("!!!!estimate"+ model.getAttribute("estimate"));
        System.out.println("!!!!estimate"+ estimate);
        System.out.println("!!!!searchedItem"+ request.getAttribute("searchedItem"));

        //SAVE
        if (button != null && button.equals("save")) {
//            if(result.hasErrors())
//            {
//                return "estimate-form";
//            }
//            estimate.setName();
            estimate.getEstimateItems().stream().forEach(ei -> estimateItemRepository.save(ei));
            estimate.setEstimateItems(estimate.getEstimateItems());
            estimate.setNumberOfItems(Long.valueOf(estimate.getEstimateItems().size()));
//            user = (User) model.getAttribute("user");
            user = userRepository.findByUserName(userName);
            estimateRepository.save(estimate);
            if(user.getEstimates().stream()
                    .map(e->e.getName())
                    .filter(s -> s.equals(estimate.getName()))
                    .collect(Collectors.toList())
                    .size()==0)
             {

                user.getEstimates().add(estimate);
                userRepository.save(user);
            }
        }
        //SAVE


        //FIND
        if (button != null && button.equals("findPriceListItem")) {
            if (priceListItemRepository.findByReferenceNumber(searchedItem).isPresent()) {
                model.addAttribute("priceListItem", priceListItemRepository.findByReferenceNumber(searchedItem).get());
            }
        }
        //FIND


        //ADD
        if (button != null && button.equals("addEstimateItem")) {
            PriceListItem priceListItem = priceListItemRepository.findById(Long.parseLong(priceListItemId)).get();
            EstimateItem estimateItem = null;
            if(estimate.getEstimateItems().stream()
                    .map(e->e.getPriceListItem()
                    .getReferenceNumber())
                    .collect(Collectors.toList())
                    .contains(priceListItem.getReferenceNumber()))
            {
                List<EstimateItem> estimateItems = estimate.getEstimateItems();

                System.out.println(estimateItems);
                for(EstimateItem e : estimateItems)
                {
                    if(e.getPriceListItem().getReferenceNumber().equals(priceListItem.getReferenceNumber()))
                    {
                        e.setQuantity(e.getQuantity()+1);
                        e.setTotalNetPrice(e.getPriceListItem()
                                .getUnitNetPrice().multiply(BigDecimal.valueOf(e.getQuantity())));
                        estimateItems.set(estimateItems.indexOf(e), e);

                    }
                }
                estimate.setEstimateItems(estimateItems);
            }
            else {
                estimateItem = new EstimateItem();
                estimateItem.setPriceListItem(priceListItem);
                estimateItem.setIndividualVatRate(priceListItem.getBaseVatRate());
                estimateItem.setQuantity(1);
                estimateItem.setTotalNetPrice(estimateItem.getPriceListItem()
                        .getUnitNetPrice().multiply(BigDecimal.valueOf(estimateItem.getQuantity())));
                estimate.getEstimateItems().add(estimateItem);
            }

            //Total net amount
            BigDecimal totalNetAmount = estimate.getEstimateItems().stream()
                    .map(ei->ei.getTotalNetPrice())
                    .reduce(BigDecimal::add).get();
            //Total VAT amount
            BigDecimal totalVatAmount = estimate.getEstimateItems().stream()
                    .map(ei->(ei.getTotalNetPrice().multiply(BigDecimal.valueOf(ei.getIndividualVatRate())).divide(BigDecimal.valueOf(100))))
                    .reduce(BigDecimal::add).get();
            //TotalGrossAmount
            BigDecimal totalGrossAmount = totalNetAmount.add(totalVatAmount);

            estimate.setTotalNetAmount(totalNetAmount);
            estimate.setTotalVatAmount(totalVatAmount);
            estimate.setTotalGrossAmount(totalGrossAmount);
        }
        //ADD

//        model.addAttribute("estimate",estimate);
        return "estimate-form";
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

        model.addAttribute("userPriceList", priceListRepository.findByName(userName));
        System.out.println(priceListRepository.findByName(userName));

        return "user-pricelist";
    }

    @PostMapping("/additem")
    public String addUserItem(@Valid @ModelAttribute("newUserPriceListItem") PriceListItem
                                      priceListItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result);
//            System.out.println("!!!!HAS ERRORS");
            return "add-user-item-form";
        }
        priceListItem.setVendorName(userName);
        priceListItemRepository.save(priceListItem);
        PriceList userPR = null;
        if (priceListRepository.findByName(userName) == null) {
            userPR = new PriceList();
            userPR.setNumberOfItems(0l);
            userPR.setName(userName);
            userPR.setPriceListItems(new ArrayList<>());
            priceListRepository.save(userPR);
        } else {
            userPR = priceListRepository.findByName(userName);
        }
        userPR.getPriceListItems().add(priceListItem);
        userPR.setNumberOfItems(Long.valueOf(priceListRepository.findByName(userName).getPriceListItems().size()) + 1);
        priceListRepository.save(userPR);
        model.addAttribute("userPriceList", userPR);
        return "user-pricelist";
    }


    //View price list - user's and other
    @GetMapping("/selectpricelist")
    public String selectPriceList() {
        return "price-list-select";
    }

    @GetMapping("/showpricelist")
    public String showPriceList() {
        return "price-list-show";
    }

    @GetMapping("/showuserpricelist")
    public String showUserPriceList(Model model)
    {
        model.addAttribute("userPriceList", priceListRepository.findByName(userName));
        return "user-pricelist";
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
//
//    @ModelAttribute("estimate")
//    public PriceListItem getNewPriceListItem() {
//        return new PriceListItem();
//    }

//        @ModelAttribute("estimates")
//    public List<Estimate> getUserEstimates() {
////        return estimateRepository.findAllByUser(userRepository.findByUserName(userName));
//            return estimateRepository.findAll();
//    }

    @ModelAttribute("user")
    public User getUser()
    {
        return userRepository.findByUserName(userName);
    }



}
