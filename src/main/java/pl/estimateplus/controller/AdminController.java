package pl.estimateplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.estimateplus.entity.PriceList;
import pl.estimateplus.entity.PriceListItem;
import pl.estimateplus.model.Excel;
import pl.estimateplus.repository.PriceListItemRepository;
import pl.estimateplus.repository.PriceListRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PriceListRepository priceListRepository;
    private final PriceListItemRepository priceListItemRepository;

    public AdminController(PriceListRepository priceListRepository, PriceListItemRepository priceListItemRepository) {
        this.priceListRepository = priceListRepository;
        this.priceListItemRepository = priceListItemRepository;
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

    @GetMapping("/list")
    public String list() {

        return "show-pricelist-data";
    }

    @GetMapping("/uploadfile")
    public String uploadFile(Model model) {
//        model.addAttribute("command",)
        return "file-upload-form";
    }


    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public String submit(@RequestParam("file") MultipartFile file, Model model) {
        List<PriceListItem> priceListItems = new ArrayList<>();
        PriceList priceList = null;
        if(file.getContentType().toString().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        {
                priceList = Excel.importExcelData(file);
                priceListItems = priceList.getPriceListItems();

            if (priceListItems != null) {
                model.addAttribute("priceListItems", priceListItems);
                model.addAttribute("itemsCount", priceList.getNumberOfItems());
                model.addAttribute("priceListName", priceList.getName());
                model.addAttribute("contentType", file.getContentType());
                priceListItemRepository.saveAll(priceListItems);
                priceListRepository.save(priceList);
                return "show-pricelist-data";
            }
        }
        model.addAttribute("file", file);
        return "file-upload-view";
    }

    @ModelAttribute("priceListItems")
    public List<PriceListItem> findAllPriceListItems() {
        return priceListItemRepository.findAll();
    }


}
