package bg.nbu.cscb532.logistics.controller;

import bg.nbu.cscb532.logistics.data.ActionResult;
import bg.nbu.cscb532.logistics.data.ResultType;
import bg.nbu.cscb532.logistics.data.dto.SaveOfficeDto;
import bg.nbu.cscb532.logistics.data.entity.BaseEntity;
import bg.nbu.cscb532.logistics.data.entity.Office;
import bg.nbu.cscb532.logistics.service.CityService;
import bg.nbu.cscb532.logistics.service.OfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/offices")
@RequiredArgsConstructor
public class OfficesController {
    private final OfficeService officeService;
    private final CityService citiesService;

    @GetMapping
    String getIndex(Model model) {
        model.addAttribute("title", "Offices");
        model.addAttribute("offices", officeService.findAll());

        return "offices/index";
    }

    @GetMapping("/create")
    String create(Model model) {
        model.addAttribute("title", "Create Office");
        model.addAttribute(
                "cities",
                citiesService.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                BaseEntity::getId,
                                it -> "%s %s".formatted(it.getPostalCode(), it.getName()),
                                (o1, o2) -> o1,
                                LinkedHashMap::new
                        ))
        );

        if (!model.containsAttribute("office")) {
            model.addAttribute("office", new SaveOfficeDto());
        }

        return "offices/save";
    }

    @GetMapping("/{id}")
    String edit(@PathVariable Long id, Model model) {
        Office office = officeService.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        model.addAttribute("title", office.getName() + " - Edit Office");
        model.addAttribute(
                "cities",
                citiesService.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                BaseEntity::getId,
                                it -> "%s %s".formatted(it.getPostalCode(), it.getName()),
                                (o1, o2) -> o1,
                                LinkedHashMap::new
                        ))
        );
        model.addAttribute(
                "office",
                SaveOfficeDto.builder()
                        .id(office.getId())
                        .name(office.getName())
                        .address(office.getAddress().getAddressLine())
                        .cityId(office.getAddress().getCity().getId())
                        .build()
        );

        return "offices/save";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    String save(
            @Valid @ModelAttribute SaveOfficeDto saveOfficeDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("office", saveOfficeDto);
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("result", new ActionResult("Form validation failed", ResultType.ERROR));

            return saveOfficeDto.getId() == null
                    ? "redirect:/offices/create"
                    : "redirect:/offices/" + saveOfficeDto.getId();
        }

        redirectAttributes.addFlashAttribute("result", new ActionResult("Office saved.", ResultType.SUCCESS));
        Office office = officeService.save(saveOfficeDto);

        return "redirect:/offices/" + office.getId();
    }
}
