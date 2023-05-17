package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer  {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "user-photos";
//		Path pathPhotoDir = Paths.get(dirName);
//		String userPhotoPath = pathPhotoDir.toFile().getAbsolutePath();
//		registry.addResourceHandler("/" + dirName + "/**")
//				.addResourceLocations("file:/" + userPhotoPath + "/");
		exposeDirectory(dirName, registry);
		
		String categoryImageDirName = "../category-images";
//		Path categoryImageDir = Paths.get(categoryImageDirName);
//		String categoryImagePath = categoryImageDir.toFile().getAbsolutePath();
//		registry.addResourceHandler("/category-images/**")
//				.addResourceLocations("file:/" + categoryImagePath + "/");
		exposeDirectory(categoryImageDirName, registry);
		
		String brandLogoFDirName = "../brand-logos";
//		Path pathBrand = Paths.get(brandLogoFDirName);
//		String logoBrandPath = pathBrand.toFile().getAbsolutePath();
//		registry.addResourceHandler("/brand-logos/**")
//				.addResourceLocations("file:/" + logoBrandPath + "/");
		exposeDirectory(brandLogoFDirName, registry);
		
		String productDirName = "../product-images";
		exposeDirectory(productDirName, registry);
		
		String logoDirName = "../site-logo";
		exposeDirectory(logoDirName, registry);
	}
	
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		
		String logicalPath = pathPattern.replace("..", "") + "/**";
		registry.addResourceHandler(logicalPath)
				.addResourceLocations("file:/" + absolutePath + "/");
	}
	
}
