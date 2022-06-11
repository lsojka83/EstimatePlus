package pl.estimateplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import pl.estimateplus.entity.*;
import pl.estimateplus.repository.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@Scope("session")
@SessionAttributes({"estimate", "user", "userId"})
@RequestMapping("/user")
public class UserController {

//    String userName;

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

//        this.userName = userRepository.findById(1l).get().getUserName();
    }


    @GetMapping("")
    public String dashboard(Model model,
                            HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
        }

        User user = (User) httpSession.getAttribute("user");

        model.addAttribute("numberOfEstimates", userRepository.findByIdWithEstimates(user.getId()).getEstimates().size());
        model.addAttribute("estimates", userRepository.findByIdWithEstimates(user.getId()).getEstimates());

        return "user-dashboard";
    }

    @GetMapping("/edit")
    public String editUser(HttpSession httpSession,
                           Model model
    ) {
        model.addAttribute("user", httpSession.getAttribute("user"));
        return "user-edit-account";
    }

    @PostMapping("/edit")
    public String editUser(
            @Valid User user,
            BindingResult results,
            Model model
    ) {
        if (results.hasErrors()) {
            return "user-edit-account";
        }
        userRepository.save(user);
        model.addAttribute("numberOfEstimates", userRepository.findByIdWithEstimates(user.getId()).getEstimates().size());
        model.addAttribute("estimates", userRepository.findByIdWithEstimates(user.getId()).getEstimates());
        return "user-dashboard";
//        return "redirect:/user/";
    }


    //Add/edit estimate
    @GetMapping("/estimate")
    public String estimate(
            Model model,
            HttpSession httpSession
//            ,
//            HttpServletRequest request
    ) {
//        model.addAttribute("user",userRepository.findByUserName(userName));
//        model.addAttribute("estimates", userRepository.findByUserName(userName).getEstimates());
//        request.getSession().setAttribute("estimate", new Estimate());
//        User user = (User) model.getAttribute("user");
//        System.out.println("!!!"+(User) httpSession.getAttribute("user"));
        User user = (User) httpSession.getAttribute("user");
        user = userRepository.findByIdWithEstimates(user.getId());
//        model.addAttribute("user", user);
        List<String> estimatesNames = user.getEstimates().stream().map(e -> e.getName()).collect(Collectors.toList());
        model.addAttribute("estimatesNames", estimatesNames);

        return "estimate-action";
    }

    @PostMapping("/estimate")
    public String estimatePost(
            Model model
            , HttpServletRequest request
            , @RequestParam String button
            , @RequestParam(required = false) String selectedEstimate,
            HttpSession httpSession

    ) {

        if (button != null && button.equals("Create new")) {
//            model.addAttribute("estimate", new Estimate());
            model.addAttribute("estimate", new Estimate());
//            System.out.println("!!! Create new");
        }
        if (button != null && button.equals("Edit")) {

//            request.getSession().setAttribute("estimate", estimateRepository.findByName(selectedEstimate));
            if (selectedEstimate != null) {
                model.addAttribute("estimate", estimateRepository.findByName(selectedEstimate));
            } else {
                model.addAttribute("estimate", new Estimate());
            }
//            httpSession.setAttribute("estimate",estimateRepository.findByName(selectedEstimate));

//            System.out.println("!!! Edit");
        }

//        return "forward:/user/estimateform";
        return "estimate-form";
    }

    @GetMapping("/estimateform")
    public String showEstimateForm1(Model model,
                                    HttpSession httpSession,
                                    @RequestParam(required = false) Long estimateId
    ) {

//        model.addAttribute("estimate",(Estimate) httpSession.getAttribute("estimate"));
        model.addAttribute("estimate", estimateRepository.findById(estimateId).get());

        return "estimate-form";
    }

    @PostMapping("/estimateform")
    public String showEstimateForm(Model model,
                                   @RequestParam(required = false) String button,
                                   @RequestParam(required = false) String searchedItem,
                                   @RequestParam(required = false) String priceListItemId,
                                   HttpServletRequest request,
                                   HttpSession httpSession,
//                                   @Valid @ModelAttribute("estimate")Estimate estimate,
                                   @Valid Estimate estimate,
                                   BindingResult result,
//                                   @ModelAttribute User user,
                                   @RequestParam(required = false) Long estimateId

    ) {
        User user = (User) httpSession.getAttribute("user");
//        Estimate estimate = (Estimate) httpSession.getAttribute("estimate");
//        estimate = (Estimate) httpSession.getAttribute("estimate");
//        System.out.println("!!!!" + estimate.getId());


//        System.out.println("!!!!estimateIdModel1" + request.getAttribute("estimateIdModel1"));
//        System.out.println("!!!!estimateId" + request.getAttribute("estimateId"));
//        System.out.println("!!!!estimateId" + estimateId);
//        System.out.println("!!!!estimate" + model.getAttribute("estimate"));
//        System.out.println("!!!!estimate" + estimate);
//        System.out.println("!!!!searchedItem" + request.getAttribute("searchedItem"));
//        System.out.println("!!!!button"+ request.getAttribute("button"));


        //SAVE
        if (button != null && button.equals("save")) {
            if (result.hasErrors()) {
                return "estimate-form";
            }
//            estimate.setName();
            estimate.getEstimateItems().stream().forEach(ei -> estimateItemRepository.save(ei));
            estimate.setEstimateItems(estimate.getEstimateItems());
            estimate.setNumberOfItems(Long.valueOf(estimate.getEstimateItems().size()));
//            user = (User) model.getAttribute("user");
//            user = userRepository.findByUserName(userName);
//            System.out.println(estimate.getEstimateItems().size());
            estimate.calculateAmounts();
//            System.out.println(user);
            estimateRepository.save(estimate);
            user = userRepository.findByIdWithEstimates(user.getId());
            if (user.getEstimates().stream()
                    .filter(e -> e.getId() == estimate.getId())
                    .collect(Collectors.toList())
                    .size() == 0) {

                user.getEstimates().add(estimate);
                userRepository.save(user);
            }
        }
        //SAVE


        //DELETDELETE
        if (button != null && button.equals("delete")) {
            user = (User) httpSession.getAttribute("user");
//            user = userRepository.findById(user.getId());
            user.getEstimates().removeIf(e -> e.getId() == estimate.getId());
            userRepository.save(user);
            estimateRepository.delete(estimate);
            model.addAttribute("estimate", new Estimate());
        }
        //DELETE


        //FIND
        if (button != null && button.equals("findPriceListItem")) {

            if (priceListItemRepository.findByReferenceNumber(searchedItem).isPresent()) {
                model.addAttribute("priceListItem", priceListItemRepository.findByReferenceNumber(searchedItem).get());
            }
        }
        //FIND


        //ADD
        if (button != null && button.equals("addEstimateItem")) {
            if (priceListItemId != null && !priceListItemId.isEmpty()) {
//                System.out.println("!!!!" + priceListItemId);
//                System.out.println("!!!!" + priceListItemId.getClass());
                PriceListItem priceListItem = priceListItemRepository.findById(Long.parseLong(priceListItemId)).get();
                EstimateItem estimateItem = null;
                if (estimate.getEstimateItems().stream()
                        .map(e -> e.getPriceListItem()
                                .getReferenceNumber())
                        .collect(Collectors.toList())
                        .contains(priceListItem.getReferenceNumber())) {
                    List<EstimateItem> estimateItems = estimate.getEstimateItems();

//                    System.out.println(estimateItems);
                    for (EstimateItem e : estimateItems) {
                        if (e.getPriceListItem().getReferenceNumber().equals(priceListItem.getReferenceNumber())) {
                            e.setQuantity(e.getQuantity() + 1);
                            e.setTotalNetPrice(e.getPriceListItem()
                                    .getUnitNetPrice().multiply(BigDecimal.valueOf(e.getQuantity())));
                            estimateItems.set(estimateItems.indexOf(e), e);

                        }
                    }
                    estimate.setEstimateItems(estimateItems);
                } else {
                    estimateItem = new EstimateItem();
                    estimateItem.setPriceListItem(priceListItem);
                    estimateItem.setIndividualVatRate(priceListItem.getBaseVatRate());
                    estimateItem.setQuantity(1);
                    estimateItem.setTotalNetPrice(estimateItem.getPriceListItem()
                            .getUnitNetPrice().multiply(BigDecimal.valueOf(estimateItem.getQuantity())));
                    estimate.getEstimateItems().add(estimateItem);
                }

                estimate.calculateAmounts();

//                //Total net amount
//                BigDecimal totalNetAmount = estimate.getEstimateItems().stream()
//                        .map(ei -> ei.getTotalNetPrice())
//                        .reduce(BigDecimal::add).get();
//                //Total VAT amount
//                BigDecimal totalVatAmount = estimate.getEstimateItems().stream()
//                        .map(ei -> (ei.getTotalNetPrice().multiply(BigDecimal.valueOf(ei.getIndividualVatRate())).divide(BigDecimal.valueOf(100))))
//                        .reduce(BigDecimal::add).get();
//                //TotalGrossAmount
//                BigDecimal totalGrossAmount = totalNetAmount.add(totalVatAmount);
//
//                estimate.setTotalNetAmount(totalNetAmount);
//                estimate.setTotalVatAmount(totalVatAmount);
//                estimate.setTotalGrossAmount(totalGrossAmount);
            }
        }
        //ADD


        model.addAttribute("estimate", estimate);
        return "estimate-form";
    }


    //Add user pricelist item
    @GetMapping("/additem")
    public String addUserItem(Model model,
                              HttpSession httpSession) {

        model.addAttribute("userPriceListItem", new PriceListItem());
        model.addAttribute("userName", userRepository.findById(((User) httpSession.getAttribute("user")).getId()).get().getUserName());
        return "user-add-item-form";
    }


    @PostMapping("/additem")
    public String addUserItem(@Valid @ModelAttribute("userPriceListItem") PriceListItem
                                      priceListItem, BindingResult result, Model model,
                              HttpSession httpSession
    ) {

        User user = (User) httpSession.getAttribute("user");
        String userName = user.getUserName();

        if (result.hasErrors()) {
            System.out.println(result);
            return "user-add-item-form";
        }
        priceListItem.setVendorName(userName);
        priceListItemRepository.save(priceListItem);
        PriceList userPR = null;
//        if (priceListRepository.findByName(userName) == null) {
//        if (priceListRepository.findById(user.getUserPriceList().getId()) == null) {
        if (userRepository.findById(user.getId()).get().getUserPriceList() == null) {
            userPR = new PriceList();
            userPR.setNumberOfItems(0l);
            userPR.setName(userName);
            userPR.setPriceListItems(new ArrayList<>());
            priceListRepository.save(userPR);
            user.setUserPriceList(userPR);
            userRepository.save(user);
        } else {
            userPR = priceListRepository.findByIdWithPriceListItems(
                    userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId()
            );
        }
        userPR.getPriceListItems().add(priceListItem);
//        userPR.setNumberOfItems(Long.valueOf(priceListRepository.findByName(userName).getPriceListItems().size()) + 1);
        userPR.setNumberOfItems(Long.valueOf(priceListRepository.findByIdWithPriceListItems(
                userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId()
        ).getPriceListItems().size()) + 1);
        priceListRepository.save(userPR);
        model.addAttribute("userPriceList", userPR);
        return "user-pricelist";
    }

    //Edit user pricelist item
    @GetMapping("/edititem")
    public String editUserItem(Model model,
                               HttpSession httpSession,
                               @RequestParam String id
    ) {

        model.addAttribute("userPriceListItem", priceListItemRepository.findById(Long.parseLong(id)));
        model.addAttribute("userName", userRepository.findById(((User) httpSession.getAttribute("user")).getId()).get().getUserName());
        return "user-add-item-form";
    }

    @PostMapping("/edititem")
    public String editUserItem(@Valid @ModelAttribute("userPriceListItem") PriceListItem
                                       priceListItem, BindingResult result, Model model,
                               HttpSession httpSession
    ) {

        User user = (User) httpSession.getAttribute("user");
        String userName = user.getUserName();

        if (result.hasErrors()) {
            System.out.println(result);
            return "user-add-item-form";
        }
        priceListItemRepository.save(priceListItem);
        model.addAttribute("userPriceList", priceListRepository.findByIdWithPriceListItems(
                        userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId()
                )
        );


        return "user-pricelist";
    }

    //Delete pricelist item
    @GetMapping("/deleteitem")
    public String deleteUserItem(
            Model model,
            HttpSession httpSession,
            @RequestParam String id

    ) {

        User user = (User) httpSession.getAttribute("user");
//        PriceList pr = priceListRepository.findByIdWithPriceListItems(user.getUserPriceList().getId());

        PriceList userPR = priceListRepository.findByIdWithPriceListItems(
                userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId());
//        priceListItemRepository.delete(priceListItemRepository.findById(Long.parseLong(id)).get());
        userPR.getPriceListItems().removeIf(i->i.getId()==Long.parseLong(id));

        if (priceListRepository.findByIdWithPriceListItems(
                userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId()
        ).getPriceListItems().size() > 0) {
            userPR.setNumberOfItems(Long.valueOf(priceListRepository.findByIdWithPriceListItems(
                    userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId()
            ).getPriceListItems().size()) - 1);
        }

        priceListRepository.save(userPR);
        model.addAttribute("userPriceList", userPR);
//        model.addAttribute("userPriceList", priceListRepository.findByIdWithPriceListItems(
//                        userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId()
//                )
//        );
        return "user-pricelist";
    }


    @GetMapping("/estimateitemedit")
    public String editEstimateItem(@RequestParam String id,
                                   Model model,
                                   @RequestParam String estimateId,
                                   HttpSession httpSession
//                                   @ModelAttribute Estimate estimate
//                                   @ModelAttribute Estimate estimate
    ) {

        Estimate estimate = (Estimate) httpSession.getAttribute("estimate");
//        estimate.setId(Long.parseLong(estimateId));
//        System.out.println("!!!"+estimateId);
//        System.out.println("!!!"+estimate.getId());
//        System.out.println(estimate);
//        model.addAttribute("estimateItem", estimateItemRepository.findById(Long.parseLong(id)).get());

//        System.out.println("!!!"+estimate.getEstimateItems());
        if (id != "") {
            model.addAttribute("estimateItem", estimate.getEstimateItems()
                    .stream()
                    .filter(ei -> ei.getId() == Long.parseLong(id))
                    .collect(Collectors.toList()).get(0));
        } else {
            model.addAttribute("estimateItem", estimate.getEstimateItems()
                    .stream()
                    .filter(ei -> ei.getId() == null)
                    .collect(Collectors.toList()).get(0));
        }

        model.addAttribute("estimateId", estimateId);
        return "estimateitem-edit";
    }

    @PostMapping("/estimateitemedit")
    public String editEstimateItem(@Valid EstimateItem estimateItem,
                                   BindingResult result,
                                   @RequestParam String priceListItemId,
                                   @RequestParam String estimateId,
                                   Model model,
//                                   @ModelAttribute Estimate estimate,
                                   HttpSession httpSession

    ) {
        Estimate estimate = (Estimate) httpSession.getAttribute("estimate");

        PriceListItem priceListItem = priceListItemRepository.findById(Long.parseLong(priceListItemId)).get();
        estimateItem.setPriceListItem(priceListItem);
        estimateItem.setTotalNetPrice(
                priceListItem.getUnitNetPrice().multiply(BigDecimal.valueOf(estimateItem.getQuantity())));
        if (result.hasErrors()) {
            return "estimateitem-edit";
        }
//        estimateItemRepository.save(estimateItem);
//        Estimate estimate = (Estimate) httpSession.getAttribute("estimate");


        int i = estimate.getEstimateItems().indexOf(estimate.getEstimateItems()
                .stream()
                .filter(ei -> ei.getId() == estimateItem.getId())
                .collect(Collectors.toList()).get(0));
//        int i = estimate.getEstimateItems().indexOf(estimateItem);
//        System.out.println(estimate.getEstimateItems()
//                .stream()
//                .filter(ei->ei.getId()==estimateItem.getId())
//                .collect(Collectors.toList()).toString());

        estimate.getEstimateItems().removeIf(ei -> ei.getId() == estimateItem.getId());
        estimate.getEstimateItems().add(i, estimateItem);
        estimate.calculateAmounts();

//        model.addAttribute("estimate",estimateRepository.findById(Long.parseLong(estimateId)).get());
        model.addAttribute("estimate", estimate);
        return "estimate-form";
//        return "redirect:/user/estimateform";
//        return "forward:/user/estimateform";
    }

    @GetMapping("/estimateitemdelete")
    public String deleteEstimateItem(@RequestParam String id,
                                     HttpServletRequest request,
                                     Model model,
                                     @RequestParam String estimateId,
                                     HttpSession httpSession
    ) {
        Estimate estimate = (Estimate) httpSession.getAttribute("estimate");
        if (id != "") {
            estimate.getEstimateItems().removeIf(ei -> ei.getId() == Long.parseLong(id));
        } else {
            estimate.getEstimateItems().removeIf(ei -> ei.getId() == null);
        }


//        estimateItemRepository.deleteFromRelationTableById(id);
//        estimateItemRepository.deleteById(id);
//        estimate = estimateRepository.findById(Long.parseLong(estimateId)).get();
        estimate.calculateAmounts();
        model.addAttribute("estimate", estimate);
        return "estimate-form";
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
    public String showUserPriceList(Model model,
                                    HttpSession httpSession
    ) {

        User user = (User) httpSession.getAttribute("user");
        Long id = null;
        PriceList priceList = null;

        if (userRepository.findByIdWithPricelist(user.getId()).getUserPriceList() != null) {
            id = userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId();
            priceList = priceListRepository.findByIdWithPriceListItems(id);

        } else {
            priceList = new PriceList();
        }
        model.addAttribute("userPriceList", priceList);

//        model.addAttribute("userPriceList", priceListRepository.findByName(
//                userRepository.findById(((User) httpSession.getAttribute("user")).getId()).get().getUserName()
//        ));
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

//    @ModelAttribute("userPriceListItem")
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

    //
    @ModelAttribute("user")
    public User getUser(
            HttpSession httpSession
    ) {
//        return userRepository.findByUserName(userName);
        return userRepository.findById(((User) httpSession.getAttribute("user")).getId()).get();
    }


}
