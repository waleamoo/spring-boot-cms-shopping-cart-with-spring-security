package com.techqwerty.cmsshoppingcart.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.techqwerty.cmsshoppingcart.models.CategoryRepository;
import com.techqwerty.cmsshoppingcart.models.ProductRepository;
import com.techqwerty.cmsshoppingcart.models.data.Category;
import com.techqwerty.cmsshoppingcart.models.data.Product;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {
    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;


    @GetMapping
    public String index(Model model, @RequestParam(value = "page", required = false) Integer p){

        int perPage = 6;
        int page = (p != null)? p : 0;

        Pageable pageable = PageRequest.of(page, perPage); // data domain

        //List<Product> products = productRepo.findAll();
        Page<Product> products = productRepo.findAll(pageable);

        List<Category> categories = categoryRepo.findAll();
        HashMap<Integer, String> cats = new HashMap<>();
        for(Category cat : categories){
            cats.put(cat.getId(), cat.getName());
        }

        model.addAttribute("products", products);
        model.addAttribute("cats", cats);

        long count = productRepo.count();
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/products/index";
    }

    // /admin/products/add
	@GetMapping("/add") 
	public String add(Product product, Model model) {
        model.addAttribute("categories", categoryRepo.findAll());
		
        return "admin/products/add";
	}

    @PostMapping("/add")
	public String add(@Valid Product product, 
                        BindingResult bindinResult, 
                        MultipartFile file, 
                        RedirectAttributes redirectAttributes, 
                        Model model) throws IOException {  
		// if the model has errors 
		if (bindinResult.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
			return "admin/products/add";  
		}

        // check if file is ok 
        boolean fileOk = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename); // java.nio.file
		
        if (filename.endsWith("jpg") || filename.endsWith("png")) {
            fileOk = true;
        }
        
        
		// create the slug
		String slug = product.getName().toLowerCase().replace(" ", "-");
		// check if the slug already exist 
		Product poductExists = productRepo.findBySlug(slug);

		if (!fileOk) {
            model.addAttribute("categories", categoryRepo.findAll());
            redirectAttributes.addFlashAttribute("message", "Image must be jpg or png");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);
		}else if(poductExists != null) {
            model.addAttribute("categories", categoryRepo.findAll());
			redirectAttributes.addFlashAttribute("message", "Product exist, chose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");		
			redirectAttributes.addFlashAttribute("product", product);
        } else {
			product.setSlug(slug);
			product.setImage(filename);
			productRepo.save(product);
            // upload the image 
            Files.write(path, bytes);
		}

        // show the success message 
        redirectAttributes.addFlashAttribute("message", "Product added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/admin/products/add";
	}
	
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Product product = productRepo.getOne(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin/products/edit";
    }

    @PostMapping("/edit")
	public String edit(@Valid Product product, 
                        BindingResult bindinResult, 
                        MultipartFile file, 
                        RedirectAttributes redirectAttributes, 
                        Model model) throws IOException {  
        Product currentProduct = productRepo.getOne(product.getId());
		// if the model has errors 
		if (bindinResult.hasErrors()) {
            model.addAttribute("productName", currentProduct.getName());
            model.addAttribute("categories", categoryRepo.findAll());
			return "admin/products/edit";  
		}

        // check if file is ok 
        boolean fileOk = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename); // java.nio.file
		
        if(!file.isEmpty()){
            if (filename.endsWith("jpg") || filename.endsWith("png")) {
                fileOk = true;
            }
        }else{
            fileOk = true;
        }

		// create the slug
		String slug = product.getName().toLowerCase().replace(" ", "-");
		// check if the slug already exist 
		Product poductExists = productRepo.findBySlugAndIdNot(slug, product.getId());

		if (!fileOk) {
            model.addAttribute("categories", categoryRepo.findAll());
            redirectAttributes.addFlashAttribute("message", "Image must be jpg or png");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);
		}else if(poductExists != null) {
            model.addAttribute("categories", categoryRepo.findAll());
			redirectAttributes.addFlashAttribute("message", "Product exist, chose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");		
			redirectAttributes.addFlashAttribute("product", product);
        } else {
			product.setSlug(slug);

            if(!file.isEmpty()){
                Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage()); // java.nio.file
                Files.delete(path2);
			    product.setImage(filename);
                Files.write(path, bytes);
            }else{
                product.setImage(currentProduct.getImage());
            }
			productRepo.save(product);
            // upload the image 
		}

        // show the success message 
        redirectAttributes.addFlashAttribute("message", "Product edited");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/admin/products/edit/" + product.getId();
	}
	

    @GetMapping("/delete/{id}") 
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException { 
        Product product = productRepo.getOne(id);
        Product currentProduct = productRepo.getOne(product.getId());
        
        Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage()); // java.nio.file
        Files.delete(path2);
        productRepo.deleteById(id);
		return "redirect:/admin/products";
    }

}
