package com.techqwerty.cmsshoppingcart;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.techqwerty.cmsshoppingcart.models.Cart;
import com.techqwerty.cmsshoppingcart.models.CategoryRepository;
import com.techqwerty.cmsshoppingcart.models.PageRepository;
import com.techqwerty.cmsshoppingcart.models.data.Category;
import com.techqwerty.cmsshoppingcart.models.data.Page;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
@SuppressWarnings("unchecked")
public class Common {
    @Autowired
    private PageRepository pageRepo;

    @Autowired
    private CategoryRepository categoryRepo;
    
    @ModelAttribute
    public void sharedData(Model model, HttpSession session, Principal principal){
        
        // someone is logged in 
        if(principal != null){
            model.addAttribute("principal", principal.getName()); // gets the username 
        }
        
        List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
        List<Category> categories = categoryRepo.findAll();
        boolean cartActive = false;
        // check the cart session 

        if(session.getAttribute("cart") != null){
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");
            int size = 0;
            double total = 0;
            for(Cart value : cart.values()){
                size += value.getQuantity();
                total += value.getQuantity() * Double.parseDouble(value.getPrice());
            }
            model.addAttribute("csize", size);
            model.addAttribute("ctotal", total);
            cartActive = true;
        }

        model.addAttribute("cpages", pages);
        model.addAttribute("ccategories", categories);
        model.addAttribute("cartActive", cartActive);
    }
}
