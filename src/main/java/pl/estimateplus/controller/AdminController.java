package pl.estimateplus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.estimateplus.entity.Estimate;
import pl.estimateplus.entity.PriceList;
import pl.estimateplus.entity.PriceListItem;
import pl.estimateplus.entity.User;
import pl.estimateplus.model.Excel;
import pl.estimateplus.repository.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("user")
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    private final PriceListRepository priceListRepository;
    private final PriceListItemRepository priceListItemRepository;
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;
    private final EstimateItemRepository estimateItemRepository;

    public AdminController(PriceListRepository priceListRepository, PriceListItemRepository priceListItemRepository, UserRepository userRepository, EstimateRepository estimateRepository, EstimateItemRepository estimateItemRepository) {
        this.priceListRepository = priceListRepository;
        this.priceListItemRepository = priceListItemRepository;
        this.userRepository = userRepository;
        this.estimateRepository = estimateRepository;
        this.estimateItemRepository = estimateItemRepository;
    }


//    @GetMapping("/import")
//    public String importData() {
//        List<PriceListItem> priceListItems = Excel.importExcelData();
//        if(priceListItems != null) {
//            priceListItemRepository.saveAll(priceListItems);
//            return "show-pricelist-data";
//        }
//        return "";
//    }

    @GetMapping("")
    public String dashboard(Model model) {
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("priceListCount", priceListRepository.count());
        return "admin-dashboard";
    }

    @GetMapping("/uploadfile")
    public String uploadFile(Model model) {
        return "admin-file-upload-form";
    }


    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public String submit(@RequestParam("file") MultipartFile file, Model model) {
        List<PriceListItem> priceListItems;
        final PriceList priceList;
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            PriceList existingPriceList;
            priceList = Excel.importExcelData(file);
            if(priceListRepository.findByName(priceList.getName())!= null)
            {
//                logger.info("!!!price list exists");

               existingPriceList =  priceListRepository.findByName(priceList.getName());
//               logger.info("!!!"+existingPriceList.getPriceListItems());
               logger.info("!!!"+priceList.getPriceListItems());

                existingPriceList.getPriceListItems().stream() //updated existing PLIs with elements from loaded PL
                                .forEach(pi->
                                {
                                    if (priceList.getPriceListItems().stream()
                                            .filter(npi -> npi.getReferenceNumber()
                                                    .equals(pi.getReferenceNumber()))
                                            .collect(Collectors.toList()) != null
                                    &&
                                            priceList.getPriceListItems().stream()
                                                    .filter(npi -> npi.getReferenceNumber()
                                                            .equals(pi.getReferenceNumber()))
                                                    .collect(Collectors.toList()).size() >0
                                    )
                                    {
                                        if (priceList.getPriceListItems().stream()
                                            .filter(npi -> npi.getReferenceNumber()
                                                    .equals(pi.getReferenceNumber()))
                                            .collect(Collectors.toList()).get(0) != null) {
                                        PriceListItem newPLI = priceList.getPriceListItems().stream()
                                                .filter(npi -> npi.getReferenceNumber().equals(pi.getReferenceNumber())).collect(Collectors.toList()).get(0);
                                        pi.setVendorName(newPLI.getVendorName());
                                        pi.setDescription(newPLI.getDescription());
                                        pi.setBrand(newPLI.getBrand());
                                        pi.setComment(newPLI.getComment());
                                        pi.setUnitNetPrice(newPLI.getUnitNetPrice());
                                        pi.setUnit(newPLI.getUnit());
                                        pi.setBaseVatRate(newPLI.getBaseVatRate());
                                        priceListItemRepository.save(pi);
                                    }
                                }
                                });
                priceList.getPriceListItems().stream() // add new items from loaded PL to existing PL
                        .forEach(npi->
                        {
                            if (existingPriceList.getPriceListItems().stream()
                                    .anyMatch(pi-> pi.getReferenceNumber().equals(npi.getReferenceNumber())))
                            {
                            }
                            else
                            {
                                priceListItemRepository.save(npi);
                                existingPriceList.getPriceListItems().add(npi);

                            }
                        });

                // TODO comment removed items from loaded PL to existing PL

                existingPriceList.countItems();
                priceListRepository.save(existingPriceList);
                model.addAttribute("priceList",existingPriceList);
            }
            else{
                priceList.getPriceListItems().stream().forEach(pi->priceListItemRepository.save(pi));
                priceListRepository.save(priceList);

                model.addAttribute("priceList",priceList);
            }
                return "admin-show-pricelist";

        }
        model.addAttribute("file", file);
        return "file-upload-view";
    }


    @GetMapping("/selectpricelist")
    public String selectPriceList(
            Model model
    ) {
        model.addAttribute("availablePriceLists", priceListRepository.findAll());
        return "admin-select-price-list-to-show";
    }

    @PostMapping("/showpricelist")
    public String showPriceList(
            Model model,
            @RequestParam String selectedPriceListId
    ) {
        PriceList priceList = priceListRepository.findByIdWithPriceListItems(Long.parseLong(selectedPriceListId));
        model.addAttribute("priceList", priceList);
        return "admin-show-pricelist";
    }


    @GetMapping("/deletepricelist")
    public String deletePriceList(
            @RequestParam String deletePriseListId
    )
    {
//        priceListItemRepository.
        List<PriceListItem> priceListItems = priceListRepository.findByIdWithPriceListItems(Long.parseLong(deletePriseListId)).getPriceListItems();
        priceListRepository.deleteById(Long.parseLong(deletePriseListId));
        //remove from all estimates, where PRI is present
        for (PriceListItem pli : priceListItems) {
            removeEstimateItemByPriceListItemId(pli.getId());
        }
        priceListItemRepository.deleteAll(priceListItems);
        //update all estimates, where PRI is present
        recalculateALlEstimates();

        return "forward:/admin/selectpricelist";
    }


    @GetMapping("/list")
    public String list() {

        return "admin-show-pricelist";
    }


    //Edit pricelist item
    @GetMapping("/edititem")
    public String editPriceListItem(Model model,
                                    @RequestParam String id,
                                    @RequestParam String priceListId
    ) {
        logger.info("!!! " + priceListId);
        model.addAttribute("priceListId", priceListId);
        model.addAttribute("priceListItem", priceListItemRepository.findById(Long.parseLong(id)));
        return "admin-edit-pricelistitem-form";
    }

    @PostMapping("/edititem")
    public String editPriceListItem(
            @Valid @ModelAttribute("priceListItem") PriceListItem priceListItem, BindingResult result,
            @RequestParam String priceListId,
            Model model

    ) {

        if (result.hasErrors()) {
            System.out.println(result);
            return "admin-edit-pricelistitem-form";
        }
        logger.info("!!! " + priceListId);
        priceListItemRepository.save(priceListItem);
        model.addAttribute("priceList", priceListRepository.findByIdWithPriceListItems(Long.parseLong(priceListId)));
        return "admin-show-pricelist";
    }

    //Delete pricelist item
    @GetMapping("/deleteitem")
    public String deleteUserItem(
            Model model,
            @RequestParam String id,
            @RequestParam String priceListId

    ) {

        PriceList currentPriceList = priceListRepository.findByIdWithPriceListItems(Long.parseLong(priceListId));
        currentPriceList.getPriceListItems().removeIf(i -> i.getId() == Long.parseLong(id));
        currentPriceList.countItems();
        priceListRepository.save(currentPriceList);

        //remove from all estimates, where PRI is present
        removeEstimateItemByPriceListItemId(Long.parseLong(id));

        priceListItemRepository.delete(priceListItemRepository.findById(Long.parseLong(id)).get());


        //update all estimates, where PRI is present
        recalculateALlEstimates();

        if (currentPriceList.getNumberOfItems().equals(0l))
        {
            if(!currentPriceList.isUserOwned()) {
                currentPriceList = priceListRepository.findById(currentPriceList.getId()).get();
                priceListRepository.delete(currentPriceList);
                return "forward:/admin/selectpricelist";
            }

        }
        model.addAttribute("priceList", currentPriceList);

        return "admin-show-pricelist";
    }

    //edit admin account
    @GetMapping("/edit")
    public String editAdmin(HttpSession httpSession,
                            Model model
    ) {
        model.addAttribute("user", httpSession.getAttribute("user"));
        return "admin-edit-account";
    }

    @PostMapping("/edit")
    public String editAdmin(
            @Valid User user,
            BindingResult results,
            Model model
    ) {
        if (results.hasErrors()) {
            return "admin-edit-account";
        }
        userRepository.save(user);
        model.addAttribute("userCount", userRepository.count());
        return "admin-dashboard";
    }


    @ModelAttribute("priceListItems")
    public List<PriceListItem> findAllPriceListItems() {
        return priceListItemRepository.findAll();
    }


    public void removeEstimateItemByPriceListItemId(Long id)
    {
        //remove from all estimates, where PRI is present
        if (estimateItemRepository.findByPriceListItemId(id) != null) //get unempty list
        {

            List<Long> estimateItemIds = estimateItemRepository.findByPriceListItemId(id)
                    .stream()
                    .map(ei -> ei.getId())
                    .collect(Collectors.toList());

            estimateItemIds.stream()
                    .forEach(eiId -> estimateItemRepository.deleteFromParentRelationTableById(eiId)); //remove from parent table

            if (estimateItemRepository.findAllById(estimateItemIds).size() > 0) {
                estimateItemIds.stream()
                        .forEach(eiId -> estimateItemRepository.deleteById(eiId)); //remove from table
            }
        }
    }

    private void recalculateALlEstimates() {
        List<Estimate> allEstimates = estimateRepository.findAll();
        for (Estimate ue : allEstimates) {
            ue.calculateAmounts();
            estimateRepository.save(ue);
        }
    }



}
