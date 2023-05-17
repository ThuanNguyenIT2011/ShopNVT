package com.shopme.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, nullable = false, length = 256)
	private String name;
	
	@Column(unique = true, nullable = false, length = 256)
	private String alias;
	
	@Column(nullable = false, length = 512, name = "short_description")
	private String shortDescription;
	
	@Column(nullable = false, length = 4096, name = "full_description")	
	private String fullDescription;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "updated_time")
	private Date updatedTime;
	
	private boolean enabled;
	
	@Column(name = "in_stock")
	private boolean inStock;
	
	private double cost;
	
	private double price;
	
	@Column(name = "discount_percent")
	private double discountPercent;
	
	private float length;
	private float width;
	private float height;
	private float weight;
	
	@Column(name = "main_image", nullable = false)
	private String mainImage;
	
	@ManyToOne()
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne()
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductImage> productImages = new HashSet<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	List<ProductDetail> productDetailts = new ArrayList<>();
	
	public Product() {}
	
	public Product(Integer id) {
		this.id = id;
	}

	public Product(String name, String alias, String sortDescription, String fullDescription, Date createdTime,
			Date updateTime, boolean enabled, boolean inStock, double cost, double price, double discountPercent,
			float length, float width, float height, float weight, Category category, Brand brand) {
		super();
		this.name = name;
		this.alias = alias;
		this.shortDescription = sortDescription;
		this.fullDescription = fullDescription;
		this.createdTime = createdTime;
		this.updatedTime = updateTime;
		this.enabled = enabled;
		this.inStock = inStock;
		this.cost = cost;
		this.price = price;
		this.discountPercent = discountPercent;
		this.length = length;
		this.width = width;
		this.height = height;
		this.weight = weight;
		this.category = category;
		this.brand = brand;
	}
	
	public void addProductDetail(String nameDetail, String valueDetail) {
		this.productDetailts.add(new ProductDetail(nameDetail, valueDetail, this));
	}
	
	public void addProductDetail(Integer detailId, String nameDetail, String valueDetail) {
		this.productDetailts.add(new ProductDetail(detailId, nameDetail, valueDetail, this));
	}

	public void addImage(String imageName) {
		this.productImages.add(new ProductImage(imageName, this));
	}

	public Set<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(Set<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdateTime() {
		return updatedTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updatedTime = updateTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	
	public List<ProductDetail> getProductDetailts() {
		return productDetailts;
	}

	public void setProductDetailts(List<ProductDetail> productDetailts) {
		this.productDetailts = productDetailts;
	}

	@Transient
	public String getMainImagePath() {
		if (this.id == null || this.mainImage == null || this.mainImage.isEmpty()) {
			return "/images/default-product.png";
		}
		return "/product-images/" + this.id + "/" + this.mainImage;
	}
	
	public boolean contaisImageName(String fileName) {
		Iterator<ProductImage> iterator = productImages.iterator();
		while (iterator.hasNext()) {
			ProductImage  productImage = iterator.next();
			if(productImage.getName().equals(fileName)) {
				return true;
			}
			
		}
		return false;
	}
	
	@Transient
	public String getShortName() {
		if (this.name.length() > 70) {
			return this.name.substring(0,70).concat("...");
		}
		return this.name;
	}
	
	@Transient
	public double getPriceDiscountPercent() {
		if (this.discountPercent > 0) {
			double priceDiscountPercent = ((100 - this.discountPercent) / 100) * this.price;
			return priceDiscountPercent;
		}
		return this.getPrice();
	}
}
