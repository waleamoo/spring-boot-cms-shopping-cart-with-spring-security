package com.techqwerty.cmsshoppingcart.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.techqwerty.cmsshoppingcart.models.CategoryRepository;
import com.techqwerty.cmsshoppingcart.models.data.Category;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {
    
    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model){
        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();
        //List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories/index";
    }

    // provides the Category object to all methods 
    // @ModelAttribute("category")
    // public Category getCategory(){
    //     return new Category();
    // }

    @GetMapping("/add")
    public String add(Category category){
        return "admin/categories/add";
    }

    @PostMapping("/add")
	public String add(@Valid Category category, BindingResult bindinResult, RedirectAttributes redirectAttributes, Model model) {  
		// if the model has errors 
		if (bindinResult.hasErrors()) {
			return "admin/categories/add";
		}
		redirectAttributes.addFlashAttribute("message", "Category added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		// create the slug
		String slug = category.getName().toLowerCase().replace(" ", "-");
		// check if the slug already exist 
		Category categoryExists = categoryRepo.findByName(category.getName());
		if (categoryExists != null) {
			redirectAttributes.addFlashAttribute("message", "Category exists, chose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("categoryInfo", category); // passes incorrect object back to the page 
		}else {
			category.setSlug(slug);
			category.setSorting(100);
			// save the page 
			categoryRepo.save(category);
		}
		return "redirect:/admin/categories/add";
	}
	
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Category category = categoryRepo.getOne(id);
        model.addAttribute("category", category);
        return "admin/categories/edit";
    }

    @PostMapping("/edit")
	public String edit(@Valid Category category, BindingResult bindinResult, RedirectAttributes redirectAttributes, Model model) {  
		Category categoryCurrent = categoryRepo.getOne(category.getId());
        
        // if the model has errors 
		if (bindinResult.hasErrors()) {
            model.addAttribute("categoryName", categoryCurrent.getName());
			return "admin/categories/edit";
		}

		redirectAttributes.addFlashAttribute("message", "Category edited");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		// create the slug
		String slug = category.getName().toLowerCase().replace(" ", "-");
		// check if the slug already exist 
		Category categoryExists = categoryRepo.findByName(category.getName());
		if (categoryExists != null) {
			redirectAttributes.addFlashAttribute("message", "Category exists, chose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
		}else {
			category.setSlug(slug);
			// save the page 
			categoryRepo.save(category);
		}

		return "redirect:/admin/categories/edit/" + category.getId();
	}

    // /admin/categories/delete/1
	@GetMapping("/delete/{id}") 
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) { 
        categoryRepo.deleteById(id);
		return "redirect:/admin/categories";
    }

    @PostMapping("/reorder")
    public String reorder(@RequestParam("id[]") int[] id){
        int count = 1;
        Category category;
        for (int categoryId : id) {
            category = categoryRepo.getOne(categoryId);
            category.setSorting(count);
            categoryRepo.save(category);
            count++;
        }

        return "ok";
    }

    
}
