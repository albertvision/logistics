package bg.nbu.cscb532.logistics.controller;

import bg.nbu.cscb532.logistics.data.ActionResult;
import bg.nbu.cscb532.logistics.data.ResultType;
import bg.nbu.cscb532.logistics.data.dto.SaveShippingDto;
import bg.nbu.cscb532.logistics.data.entity.City;
import bg.nbu.cscb532.logistics.data.entity.Office;
import bg.nbu.cscb532.logistics.data.entity.Shipping;
import bg.nbu.cscb532.logistics.data.entity.User;
import bg.nbu.cscb532.logistics.data.enumeration.ServiceType;
import bg.nbu.cscb532.logistics.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shippings")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;
    private final UserService userService;
    private final CityService cityService;
    private final OfficeService officeService;
    private final AuthService authService;
    private final ServiceService serviceService;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("title", "Shippings");

        List<Shipping> shippings = shippingService.findAll();
        model.addAttribute("shippings", shippings);

        model.addAttribute(
                "shippingStatuses",
                shippings.stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                shippingService::getLastStatusType
                        ))
        );

        model.addAttribute("users", userService.findAll());

        return "_index";
    }

    @GetMapping("/create")
    public String getCreate(Model model) {
        model.addAttribute("title", "Create Shipping");

        if (!model.containsAttribute("shipping")) {
            model.addAttribute(
                    "shipping",
                    SaveShippingDto.builder()
                            .senderId(authService.getLoggedInUser().getId())
                            .build()
            );
        }

        List<User> users = userService.findAll();
        Map<Long, String> senders = users
                .stream()
                .collect(Collectors.toMap(
                                User::getId,
                                it -> "(%d) %s".formatted(it.getId(), it.getName())
                        )
                );
        model.addAttribute("senders", senders);

        Map<Long, String> receivers = users
                .stream()
                .collect(Collectors.toMap(
                                User::getId,
                                it -> "(%d) %s".formatted(it.getId(), it.getName())
                        )
                );
        model.addAttribute("receivers", receivers);

        Map<Long, String> cities = cityService.findAll()
                .stream()
                .collect(Collectors.toMap(
                                City::getId,
                                City::toString
                        )
                );
        model.addAttribute("cities", cities);

        Map<Long, String> offices = officeService.findAll()
                .stream()
                .collect(Collectors.toMap(
                                Office::getId,
                                it -> " %s, %s".formatted(it.getAddress().getCity(), it.getName())
                        )
                );
        model.addAttribute("offices", offices);

        var collecitonTypes = new LinkedHashMap<String, String>();
        collecitonTypes.put(ServiceType.COLLECTION_FROM_OFFICE.name(), ServiceType.COLLECTION_FROM_OFFICE.getLabel());
        collecitonTypes.put(ServiceType.COLLECTION_FROM_ADDRESS.name(), ServiceType.COLLECTION_FROM_ADDRESS.getLabel());
        model.addAttribute("collectionTypes", collecitonTypes);

        var deliveryTypes = new LinkedHashMap<String, String>();
        deliveryTypes.put(ServiceType.DELIVERY_TO_OFFICE.name(), ServiceType.DELIVERY_TO_OFFICE.getLabel());
        deliveryTypes.put(ServiceType.DELIVERY_TO_ADDRESS.name(), ServiceType.DELIVERY_TO_ADDRESS.getLabel());
        model.addAttribute("deliveryTypes", deliveryTypes);

        return "shippings/save";
    }

    @PostMapping("/save")
    public String saveShipping(
            @Valid SaveShippingDto saveShippingDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("shipping", saveShippingDto);
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("result", new ActionResult("Form validation failed", ResultType.ERROR));

            return saveShippingDto.getId() == null
                    ? "redirect:/shippings/create"
                    : "redirect:/shippings/" + saveShippingDto.getId();
        }

        Shipping shipping = shippingService.save(saveShippingDto);

        return "redirect:/shippings/" + shipping.getId();
    }
}
