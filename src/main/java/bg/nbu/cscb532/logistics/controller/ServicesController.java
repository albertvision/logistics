package bg.nbu.cscb532.logistics.controller;

import bg.nbu.cscb532.logistics.data.ActionResult;
import bg.nbu.cscb532.logistics.data.ResultType;
import bg.nbu.cscb532.logistics.data.dto.SaveServiceDto;
import bg.nbu.cscb532.logistics.data.entity.Service;
import bg.nbu.cscb532.logistics.data.enumeration.ServiceType;
import bg.nbu.cscb532.logistics.service.ServiceService;
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/services")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ServicesController {
    private final ServiceService serviceService;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("title", "Services");
        model.addAttribute("services", serviceService.findAll());

        return "services/index";
    }

    @GetMapping("/create")
    public String getCreate(
        Model model
    ) {
        model.addAttribute("title", "Create Service");
        model.addAttribute(
            "services",
            Stream.of(ServiceType.values())
                .collect(Collectors.toMap(
                    ServiceType::name,
                    ServiceType::getLabel
                ))
        );

        if (!model.containsAttribute("service")) {
            model.addAttribute("service", SaveServiceDto.builder().build());
        }

        return "services/save";
    }

    @PostMapping("/save")
    public String postSave(
        @ModelAttribute @Valid SaveServiceDto saveServiceDto,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("service", saveServiceDto);
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("result", new ActionResult("Form validation failed", ResultType.ERROR));

            return saveServiceDto.getId() == null
                ? "redirect:/services/create"
                : "redirect:/services/" + saveServiceDto.getId();
        }

        redirectAttributes.addFlashAttribute("result", new ActionResult("Service saved", ResultType.SUCCESS));

        Service service = serviceService.save(saveServiceDto);

        return "redirect:/services/" + service.getId();
    }

    @GetMapping("/{id}")
    public String getEdit(
        @PathVariable Long id,
        Model model
    ) {
        Service service = serviceService.findById(id)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        model.addAttribute("title", "Edit Service");
        model.addAttribute(
            "services",
            Stream.of(ServiceType.values())
                .collect(Collectors.toMap(
                    ServiceType::name,
                    ServiceType::getLabel
                ))
        );

        if (!model.containsAttribute("service")) {
            model.addAttribute("service", SaveServiceDto.builder()
                .id(service.getId())
                .serviceType(service.getServiceType())
                .minWeight(service.getMinWeight())
                .basePriceEur(service.getBasePriceEur())
                .priceEurPerWeight(service.getPriceEurPerWeight())
                .build()
            );
        }

        return "services/save";
    }
}
