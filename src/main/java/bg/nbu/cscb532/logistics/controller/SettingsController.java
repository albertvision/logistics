package bg.nbu.cscb532.logistics.controller;

import bg.nbu.cscb532.logistics.data.ActionResult;
import bg.nbu.cscb532.logistics.data.ResultType;
import bg.nbu.cscb532.logistics.data.dto.SaveCompanyInfoDto;
import bg.nbu.cscb532.logistics.data.entity.Company;
import bg.nbu.cscb532.logistics.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
@PreAuthorize("hasRole('ADMIN')")
public class SettingsController {
    private final CompanyService companyService;

    @GetMapping
    public String settings(Model model) {
        Company company = companyService.find();

        if (!model.containsAttribute("company")) {
            model.addAttribute(
                "company",
                SaveCompanyInfoDto.builder()
                    .companyName(company.getName())
                    .phoneNumber(company.getPhoneNumber())
                    .build()
            );
        }

        model.addAttribute("title", "Settings");

        return "settings";
    }

    @PostMapping
    public String saveSettings(
        @ModelAttribute @Valid SaveCompanyInfoDto saveCompanyInfoDto,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("company", saveCompanyInfoDto);
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("result", new ActionResult("Form validation failed", ResultType.ERROR));

            return "redirect:/settings";
        }

        redirectAttributes.addFlashAttribute("result", new ActionResult("Settings updated", ResultType.SUCCESS));

        companyService.save(saveCompanyInfoDto);

        return "redirect:/settings";
    }
}
