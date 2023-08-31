package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.setting.contry.CountryService;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingLable;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingController {
	@Autowired 
	private SettingService settingService;
	
	@Autowired
	private CurrencyRepository currencyRepo;
	
	@Autowired
	private CountryService countryService;

	@Autowired
	private StateRepository stateRepository;
	
	@GetMapping("/settings")
	public String viewSetting(Model model) {
		List<Currency> listCurrenies = currencyRepo.findAllByOrderByNameAsc();
		List<Setting> listSettings = settingService.listAllSettings();
		
		model.addAttribute("listCurrenies", listCurrenies);
		model.addAttribute("listCountries", countryService.list());
		model.addAttribute("listStates", stateRepository.findAll());
		
		listSettings.forEach(setting -> {
			model.addAttribute(setting.getKey(), setting.getValue());
		});
		
		return "/settings/settings";
	}
	
	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplate(HttpServletRequest request, 
			RedirectAttributes redirectAttributes) {
		List<Setting> listSettingMailTemplate = settingService.getListContentByLable(SettingLable.MAIL_TEMPLATE);
		
		listSettingMailTemplate.forEach((setting) -> {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		});
		settingService.saveAll(listSettingMailTemplate);
		redirectAttributes.addFlashAttribute("message", "Genaral Settings have saved.");
		return "redirect:/settings";
	}
	
	@PostMapping("/settings/save_mailserver")
	public String saveMailServer(HttpServletRequest request, 
			RedirectAttributes redirectAttributes) {
		List<Setting> listSettingGenaral = settingService.getListContentByLable(SettingLable.MAIL_SERVER);
		listSettingGenaral.forEach((setting) -> {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		});
		settingService.saveAll(listSettingGenaral);
		redirectAttributes.addFlashAttribute("message", "Genaral Settings have saved.");
		return "redirect:/settings";
	}
	
	@PostMapping("/settings/save_genaral")
	public String saveGenaral(HttpServletRequest request, 
			@RequestParam(name = "fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		List<Setting> listSettingGenaral = settingService.getGenaral();
		listSettingGenaral.forEach((setting) -> {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		});
		
		int indexCurrency = listSettingGenaral.indexOf(new Setting("CURRENCY_SYMBOL"));
		Integer currId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		String symbol = currencyRepo.findById(currId).get().getSymbol();
		listSettingGenaral.get(indexCurrency).setValue(symbol);
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			
			int indexLogo = listSettingGenaral.indexOf(new Setting("SITE_LOGO"));
			listSettingGenaral.get(indexLogo).setValue(value);
			
			String uploadDir = "../site-logo";
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
		
		settingService.saveAll(listSettingGenaral);
		
		redirectAttributes.addFlashAttribute("message", "Genaral Settings have saved.");
		return "redirect:/settings";
	}
	
}
