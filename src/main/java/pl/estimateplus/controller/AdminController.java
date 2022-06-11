package pl.estimateplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.estimateplus.entity.PriceList;
import pl.estimateplus.entity.PriceListItem;
import pl.estimateplus.entity.User;
import pl.estimateplus.model.Excel;
import pl.estimateplus.repository.PriceListItemRepository;
import pl.estimateplus.repository.PriceListRepository;
import pl.estimateplus.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("user")
@RequestMapping("/admin")
public class AdminController {

    private final PriceListRepository priceListRepository;
    private final PriceListItemRepository priceListItemRepository;
    private final UserRepository userRepository;

    public AdminController(PriceListRepository priceListRepository, PriceListItemRepository priceListItemRepository, UserRepository userRepository) {
        this.priceListRepository = priceListRepository;
        this.priceListItemRepository = priceListItemRepository;
        this.userRepository = userRepository;
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
    public String dashboard(Model model)
    {
        model.addAttribute("userCount", userRepository.count());
        return "admin-dashboard";
    }

    @GetMapping("/list")
    public String list() {

        return "admin-show-pricelist-data";
    }

    @GetMapping("/uploadfile")
    public String uploadFile(Model model) {
        return "asmin-file-upload-form";
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
                return "admin-show-pricelist-data";
            }
        }
        model.addAttribute("file", file);
        return "file-upload-view";
    }

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


}
