package bg.nbu.cscb532.logistics.controller;

import bg.nbu.cscb532.logistics.data.ActionResult;
import bg.nbu.cscb532.logistics.data.ResultType;
import bg.nbu.cscb532.logistics.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String handleInvalidGlobalFilters(
        Exception ex,
        Model model,
        RedirectAttributes redirectAttributes,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        if (!(ex instanceof AppException)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            model.addAttribute("error", "Internal Error");
            model.addAttribute("message", "Please contact administrator.");
        } else {
            if (getPreviousUrl(request) != null) {
                redirectAttributes.addFlashAttribute("result", new ActionResult(ex.getMessage(), ResultType.ERROR));

                return "redirect:"+getPreviousUrl(request);
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            model.addAttribute("error", ex.getMessage());
            model.addAttribute("message", "");
        }

        return "error";
    }

    private String getPreviousUrl(HttpServletRequest request) {
        return request.getHeader("Referer");
    }
}
