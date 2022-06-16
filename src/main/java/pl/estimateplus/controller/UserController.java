package pl.estimateplus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import pl.estimateplus.entity.*;
import pl.estimateplus.repository.*;

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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


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
    public String showDashboard(Model model,
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
    ) {

        User user = (User) httpSession.getAttribute("user");
        user = userRepository.findByIdWithEstimates(user.getId());
        List<String> estimatesNames = user.getEstimates().stream().map(e -> e.getName()).collect(Collectors.toList());
        model.addAttribute("estimatesNames", estimatesNames);

        return "estimate-action";
    }

    @PostMapping("/estimate")
    public String estimatePost(
            Model model
            , @RequestParam String button
            , @RequestParam(required = false) String selectedEstimate

    ) {

        if (button != null && button.equals("Create new")) {
            model.addAttribute("estimate", new Estimate());
        }
        if (button != null && button.equals("Edit")) {

            if (selectedEstimate != null) {
                model.addAttribute("estimate", estimateRepository.findByName(selectedEstimate));
            } else {
                model.addAttribute("estimate", new Estimate());
            }
        }
        return "estimate-form";
    }

    @GetMapping("/estimateform")
    public String showEstimateForm1(Model model,
                                    @RequestParam(required = false) Long estimateId
    ) {
        model.addAttribute("estimate", estimateRepository.findById(estimateId).get());

        return "estimate-form";
    }

    @PostMapping("/estimateform")
    public String showEstimateForm(Model model,
                                   @RequestParam(required = false) String button,
                                   @RequestParam(required = false) String searchedItemReferenceNumber,
                                   @RequestParam(required = false) String priceListItemId,
                                   HttpSession httpSession,
//                                   @Valid @ModelAttribute("estimate")Estimate estimate,
                                   @Valid Estimate estimate,
                                   BindingResult result,
                                   @RequestParam(required = false) Long estimateId

    ) {

        User user = (User) httpSession.getAttribute("user");
        if (estimate != null) {
            estimate.calculateAmounts();
        }


//        logger.info("this is a info message");
//        logger.warn("this is a warn message");
//        logger.error("this is a error message");

//        System.out.println("!!!!" + estimate.getId());
//        System.out.println("!!!!estimateIdModel1" + request.getAttribute("estimateIdModel1"));
//        System.out.println("!!!!estimateId" + request.getAttribute("estimateId"));
//        System.out.println("!!!!estimateId" + estimateId);
//        System.out.println("!!!!estimate" + model.getAttribute("estimate"));
//        System.out.println("!!!!searchedItem" + request.getAttribute("searchedItem"));
//        System.out.println("!!!!button"+ request.getAttribute("button"));

        //Save estimate
        logger.info("!!!!Save-start" + estimate);
        if (button != null && button.equals("save")) {
//            if (result.hasErrors()) {
//                return "estimate-form";
//            }
//            estimate.setName();
            //???
//            estimate.getEstimateItems().stream().forEach(ei -> estimateItemRepository.save(ei));

            estimate.setEstimateItems(estimate.getEstimateItems());
            if (estimate.getEstimateItems().stream().toList().stream().filter(ei -> ei.getId() == null).count() > 0)  //adds not save eis to current estimateitem list
            {
                List<EstimateItem> newItems = estimate.getEstimateItems().stream()
                        .filter(ei -> ei.getId() == null)
                        .collect(Collectors.toList());

                List<EstimateItem> alreadySavedItems = estimate.getEstimateItems().stream()
                        .filter(ei -> ei.getId() != null)
                        .collect(Collectors.toList());

                alreadySavedItems.addAll(newItems);
                estimate.setEstimateItems(alreadySavedItems);
            }
            estimate.calculateAmounts();
            logger.info("!!!! " + estimate);
            estimate.getEstimateItems().stream().forEach(ei -> estimateItemRepository.save(ei));

//            try {
                estimateRepository.save(estimate);
//            } catch (Exception e) {
//                logger.warn("!!!!"+e.getMessage());
//            }


            //Delete eis in ei table when not present in joing table
            estimateItemRepository.findAllItemsNotPresentInParentJoiningTable()
                    .stream()
                    .forEach(ei-> estimateItemRepository.delete(ei));


            //Save user when estimate is new and not exists in DB
            user = userRepository.findByIdWithEstimates(user.getId());
            if (user.getEstimates().stream()
                    .filter(e -> e.getId().equals(estimate.getId()))
                    .collect(Collectors.toList())
                    .size() == 0) {
                user.getEstimates().add(estimate);
                userRepository.save(user);
            }
        }


        //Delete estimate
        if (button != null && button.equals("delete")) {
            user = (User) httpSession.getAttribute("user");
            user = userRepository.findByIdWithEstimates(user.getId());
            user.getEstimates().removeIf(e -> e.getId() == estimate.getId());
            userRepository.save(user);
            logger.info("!!! Delete"+estimate);

            estimate.getEstimateItems().stream().forEach(ei->
                    {
                        if(ei.getId()!=null) {
                            estimateItemRepository.deleteFromParentRelationTableById(ei.getId());
//                            estimateItemRepository.deleteById(ei.getId());
                        }
                    }
            );
            estimateRepository.delete(estimate);

            model.addAttribute("estimate", new Estimate());
            return "estimate-form";
        }


        //Find price list item on user pricelist and all general pricelists

        if (button != null && button.equals("findPriceListItem")) {
            if (!priceListItemRepository.findAllByUserIdAndReferenceNumber(user.getId(), searchedItemReferenceNumber).isEmpty()) {
                model.addAttribute("searchResult",
                        priceListItemRepository.findAllByUserIdAndReferenceNumber(user.getId(), searchedItemReferenceNumber));
            }
        }

        //Add pricelist item as estimate item do estimate. Not saves estimate do DB
        if (button != null && button.equals("addEstimateItem")) {
            logger.info("priceListItemId: " + priceListItemId);
            if (priceListItemId != null && !priceListItemId.isEmpty()) {
                PriceListItem priceListItem = priceListItemRepository.findById(Long.parseLong(priceListItemId)).get();
                EstimateItem estimateItem = null;
                if (estimate.getEstimateItems().stream()
                        .map(e -> e.getPriceListItem()
                                .getId())
                        .collect(Collectors.toList())
                        .contains(priceListItem.getId())) {
                    List<EstimateItem> estimateItems = estimate.getEstimateItems();
                    for (EstimateItem e : estimateItems) {
                        if (e.getPriceListItem().getId().equals(priceListItem.getId())) {
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
                    try {
                        estimateItem.setTotalNetPrice(estimateItem.getPriceListItem()
                                .getUnitNetPrice().multiply(BigDecimal.valueOf(estimateItem.getQuantity())));
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                    }

                    estimate.getEstimateItems().add(estimateItem);
                }
                try {
                    estimate.calculateAmounts();
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                }
            }
        }

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
        if (userRepository.findById(user.getId()).get().getUserPriceList() == null) {
            userPR = new PriceList();
            userPR.setUserOwned(true);
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
        userPR.setNumberOfItems(Long.valueOf(userPR.getPriceListItems().size()));
        priceListRepository.save(userPR);
        model.addAttribute("priceList", userPR);
        return "user-show-pricelist";
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

        if (result.hasErrors()) {
            System.out.println(result);
            return "user-add-item-form";
        }
        priceListItemRepository.save(priceListItem);
        model.addAttribute("userPriceList", priceListRepository.findByIdWithPriceListItems(
                        userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId()
                )
        );
        return "user-show-pricelist";
    }

    //Delete pricelist item
    @GetMapping("/deleteitem")
    public String deleteUserItem(
            Model model,
            HttpSession httpSession,
            @RequestParam String id
    ) {

        User user = (User) httpSession.getAttribute("user");

        PriceList userPR = priceListRepository.findByIdWithPriceListItems(
                userRepository.findByIdWithPricelist(user.getId()).getUserPriceList().getId());
        userPR.getPriceListItems().removeIf(i -> i.getId() == Long.parseLong(id));
        userPR.countItems();
        priceListRepository.save(userPR);

        if (estimateItemRepository.findByPriceListItemId(Long.parseLong(id)) != null) //get unempty list
        {
//            Long estimateItemId = estimateItemRepository.findByPriceListItemId(Long.parseLong(id)).getId();

            List<Long> estimateItemIds =  estimateItemRepository.findByPriceListItemId(Long.parseLong(id))
                    .stream()
                    .map(ei->ei.getId())
                    .collect(Collectors.toList());

            estimateItemIds.stream()
                            .forEach(eiId-> estimateItemRepository.deleteFromParentRelationTableById(eiId)); //remove from parent table
            if(estimateItemRepository.findAllById(estimateItemIds).size()>0)
            {
                estimateItemIds.stream()
                        .forEach(eiId-> estimateItemRepository.deleteById(eiId)); //remove from table
            }
        }

        priceListItemRepository.delete(priceListItemRepository.findById(Long.parseLong(id)).get());

        List<Estimate> userEstimates = userRepository.findByIdWithEstimates(user.getId()).getEstimates();
        for (Estimate ue : userEstimates) {
            ue.calculateAmounts();
            estimateRepository.save(ue);
        }
        user.setEstimates(userEstimates);
        userRepository.save(user);

        model.addAttribute("priceList", userPR);

        return "user-show-pricelist";
    }

    //Edit estimate item
    @GetMapping("/editestimateitem")
    public String editEstimateItem(
            @RequestParam String piId,
            Model model,
            @RequestParam String estimateId,
            HttpSession httpSession
    ) {

        Estimate estimate = (Estimate) httpSession.getAttribute("estimate");
        logger.info("!!! " + piId);
        logger.info("!!! " + estimate);

        model.addAttribute("estimateItem", estimate.getEstimateItems()
                .stream()
                .filter(ei -> ei.getPriceListItem().getId().equals(Long.parseLong(piId)))
                .collect(Collectors.toList()).get(0));

        model.addAttribute("estimateId", estimateId);
        return "estimateitem-edit";
    }

    @PostMapping("/editestimateitem")
    public String editEstimateItem(
            @Valid EstimateItem estimateItem,
            BindingResult result,
            @RequestParam String priceListItemId,
            Model model,
            HttpSession httpSession

    ) {
        Estimate estimate = (Estimate) httpSession.getAttribute("estimate");
        PriceListItem priceListItem = priceListItemRepository.findById(Long.parseLong(priceListItemId)).get();
        estimateItem.setPriceListItem(priceListItem);
        estimateItem.calculateAmounts(estimateItem.getQuantity());
        if (result.hasErrors()) {
            return "estimateitem-edit";
        }

        int i = 0;
        i = estimate.getEstimateItems().indexOf(estimate.getEstimateItems()
                .stream()
                .filter(ei -> ei.getPriceListItem().getId().equals(estimateItem.getPriceListItem().getId()))
                .collect(Collectors.toList()).get(0));
        estimate.getEstimateItems().remove(i);
        estimate.getEstimateItems().add(i, estimateItem);
        estimate.calculateAmounts();
        model.addAttribute("estimate", estimate);
        return "estimate-form";
    }

    @GetMapping("/deleteestimateitem")
    public String deleteEstimateItem(
            @RequestParam String id,
            @RequestParam String refNo,
            @RequestParam String piId,
            Model model,
            HttpSession httpSession
    ) {
        Estimate estimate = (Estimate) httpSession.getAttribute("estimate");
        logger.info("!!! " + estimate);

        Long eiId = estimate.getEstimateItems()
                .stream()
                .filter(ei -> ei.getPriceListItem().getId().equals(Long.parseLong(piId)))
                .collect(Collectors.toList()).get(0).getId();
//        estimateItemRepository.deleteFromRelationTableById(eiId);
//        if(eiId!=null) {
//            estimateItemRepository.deleteById(eiId);
//        }
        estimate.getEstimateItems().removeIf(ei -> ei.getPriceListItem().getId().equals(Long.parseLong(piId)));


        estimate.calculateAmounts();
        model.addAttribute("estimate", estimate);
        return "estimate-form";
    }


    @GetMapping("/selectpricelist")
    public String selectPriceList(
            HttpSession httpSession,
            Model model
    )
    {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("userAvailablePriceLists",priceListRepository.findAllByUserAndAllGeneral(user.getId()));
        return "user-select-price-list-to-show";
    }

    @PostMapping("/showpricelist")
    public String showPriceList(
            Model model,
            HttpSession httpSession,
            @RequestParam String selectedPriceListId
    ) {
        User user = (User) httpSession.getAttribute("user");
        if(user.getUserPriceList().getId().equals(priceListRepository.findById(Long.parseLong(selectedPriceListId)).get().getId()))
        {
            model.addAttribute("isUserPricelist","true");
        }
        PriceList priceList = priceListRepository.findByIdWithPriceListItems(Long.parseLong(selectedPriceListId));
        logger.info("!!! "+priceList);
        model.addAttribute("priceList", priceList);
        return "user-show-pricelist";
    }


    //View price list - user's and other
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
        model.addAttribute("p" +
                "riceList", priceList);
//        return "user-pricelist";
        return "user-show-pricelist";
    }
}
