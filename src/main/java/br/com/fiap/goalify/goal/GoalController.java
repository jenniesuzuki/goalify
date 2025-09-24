package br.com.fiap.goalify.goal;

import br.com.fiap.goalify.config.MessageHelper;
import br.com.fiap.goalify.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/goal")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final MessageSource messageSource;
    private final MessageHelper messageHelper;
    private final UserService userService;

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal OAuth2User user) {
        var goals = goalService.getAllGoals();
        model.addAttribute("goals", goals);
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/form")
    public String form(Goal goal){
        return "form";
    }

    @PostMapping("/form")
    public String create(@Valid Goal goal, BindingResult result, RedirectAttributes redirect ){ //biding

        if(result.hasErrors()) return "form";

        goalService.save(goal);
        redirect.addFlashAttribute("message", messageHelper.get("goal.create.success"));
        return "redirect:/goal"; //301
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect ){
        goalService.deleteById(id);
        redirect.addFlashAttribute("message", messageHelper.get("goal.delete.success"));
        return "redirect:/goal";
    }

    @PutMapping("/pick/{id}")
    public String pick(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal){
        goalService.pick(id, userService.register(principal));
        return "redirect:/goal";
    }

    @PutMapping("/drop/{id}")
    public String drop(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal){
        goalService.drop(id, userService.register(principal));
        return "redirect:/goal";
    }

    @PutMapping("/inc/{id}")
    public String increment(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal){
        goalService.incrementTaskStatus(id, userService.register(principal));
        return "redirect:/goal";
    }

    @PutMapping("/dec/{id}")
    public String decrement(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal){
        goalService.decrementTaskStatus(id, userService.register(principal));
        return "redirect:/goal";
    }
}

