package com.techqwerty.cmsshoppingcart.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techqwerty.cmsshoppingcart.models.data.Page;

public interface PageRepository extends JpaRepository<Page, Integer> { // PagingAndSortingRepository, JpaRepository, CrudRepository
	Page findBySlug(String slug);

	@Query("SELECT p FROM Page p WHERE p.id != :id and p.slug = :slug")
	Page findBySlug(@Param("id") int id, @Param("slug") String slug);
	// Page findBySlugAndIdNot(String slug, int index); // same as Page findBySlug(@Param("id") int id, @Param("slug") String slug);

	List<Page> findAllByOrderBySortingAsc();
}

