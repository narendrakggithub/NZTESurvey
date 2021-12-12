package com.nkg.pmbot.model;

import org.hibernate.annotations.NaturalId;
import org.springframework.lang.NonNull;

import com.nkg.pmbot.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Set;

@Entity
@Table(name = "hexstrings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "value"
        })
})
public class HexStrings extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String value;

    @NotBlank
    @Size(max = 15)
    private String category;
    
    @NotBlank
    @Size(max = 15)
    private String subCategory;
    
    @NotBlank
    @Size(max = 15)
    private String action;
    
    private Long primaryId;

    public HexStrings() {

    }

    public HexStrings(String value, String category, String subCategory, String action, Long primaryId ) {
    	this.value = value;
    	this.category = category;
        this.subCategory = subCategory;
        this.action = action;
        this.primaryId = primaryId;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getPrimaryId() {
		return primaryId;
	}

	public void setPrimaryId(Long primaryId) {
		this.primaryId = primaryId;
	}

    
}