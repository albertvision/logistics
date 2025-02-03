package bg.nbu.cscb532.logistics.controller;

import bg.nbu.cscb532.logistics.data.ActionResult;
import bg.nbu.cscb532.logistics.data.ResultType;
import bg.nbu.cscb532.logistics.data.dto.SaveShippingDto;
import bg.nbu.cscb532.logistics.data.dto.ShippingListDto;
import bg.nbu.cscb532.logistics.data.entity.City;
import bg.nbu.cscb532.logistics.data.entity.Office;
import bg.nbu.cscb532.logistics.data.entity.Shipping;
import bg.nbu.cscb532.logistics.data.entity.User;
import bg.nbu.cscb532.logistics.data.enumeration.ServiceType;
import bg.nbu.cscb532.logistics.data.enumeration.ShippingStatusType;
import bg.nbu.cscb532.logistics.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
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

    @GetMapping
    public String getIndex(
        @ModelAttribute ShippingListDto shippingListDto,
        Model model
    ) {
        model.addAttribute("title", "Shippings");
        model.addAttribute("shippingListDto", shippingListDto);

        List<Shipping> shippings = shippingService.findAll(shippingListDto);
        model.addAttribute("shippings", shippings);

        model.addAttribute(
            "shippingStatuses",
            shippings.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        shippingService::getLastStatusType
                    )
                )
        );

        model.addAttribute(
            "shippingStatusTypes",
            Arrays.stream(ShippingStatusType.values())
                .collect(Collectors.toMap(
                        ShippingStatusType::name,
                        ShippingStatusType::getLabel,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                    )
                )
        );

        model.addAttribute(
            "shippingReceivers",
            shippings.stream()
                .map(Shipping::getReceiver)
                .toList()
        );

        model.addAttribute("users", userService.findAll());

        return "shippings/index";
    }

    @GetMapping("/create")
    public String getCreate(Model model) {
        model.addAttribute("title", "Create Shipping");
        SaveShippingDto saveShippingDto = SaveShippingDto.builder()
            .senderId(authService.getLoggedInUser().getId())
            .build();

        return getSaveController(saveShippingDto, model);
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
        redirectAttributes.addFlashAttribute("result", new ActionResult("Shipping saved", ResultType.SUCCESS));

        return "redirect:/shippings/" + shipping.getId();
    }

    @GetMapping("/{id}")
    public String editShipping(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        Optional<Shipping> shippingMaybe = shippingService.findById(id);
        if (shippingMaybe.isEmpty()) {
            redirectAttributes.addFlashAttribute("result", new ActionResult("Shipping not found", ResultType.ERROR));

            return "redirect:/shippings";
        }

        Shipping shipping = shippingMaybe.get();
        model.addAttribute("title", "Shipping %s".formatted(shipping.getId()));

        SaveShippingDto shippingDto = SaveShippingDto.fromEntity(shipping);

        return getSaveController(shippingDto, model);
    }

    private String getSaveController(SaveShippingDto saveShippingDto, Model model) {
        if (!model.containsAttribute("shipping")) {
            model.addAttribute("shipping", saveShippingDto);
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
}
