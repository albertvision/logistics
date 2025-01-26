package bg.nbu.cscb532.logistics.controller;

import bg.nbu.cscb532.logistics.data.enumeration.Authority;
import bg.nbu.cscb532.logistics.data.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final ShippingRepository shippingRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Home");

        return "index";
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String test() {
        return "index";
    }
}