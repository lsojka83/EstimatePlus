package pl.estimateplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.estimateplus.entity.PriceListItem;
import pl.estimateplus.model.Excel;
import pl.estimateplus.repository.PriceListItemRepository;
import pl.estimateplus.repository.PriceListRepository;

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


    @GetMapping("/import")
    public String importData() {
        List<PriceListItem> priceListItems = Excel.importExcelData();
        if(priceListItems != null) {
            priceListItemRepository.saveAll(priceListItems);
            return "show-pricelist-data";
        }
        return "";
    }

    @GetMapping("/list")
    public String list() {

            return "show-pricelist-data";
    }

    @ModelAttribute("priceListItems")
    public List<PriceListItem> findAllPriceListItems()
    {
        return priceListItemRepository.findAll();
    }


}
