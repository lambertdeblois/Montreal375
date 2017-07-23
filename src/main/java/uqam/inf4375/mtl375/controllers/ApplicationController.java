package uqam.inf4375.mtl375.controllers;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class ApplicationController {

  @RequestMapping("/")
  public String index(Model model) {
    return "index";
  }
}
