package com.gwa.Gradely.controller.grade11;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author RalphTheGreat
 */
@Controller
@RequestMapping(value = "/grade11")
public class Grade11Controller {

    /**
     * GET request handler for /grade11/strand
     *
     * - Shows the strand selection page for Grade 11 students - Typically used
     * to allow students to pick their academic strand (e.g., STEM, HUMSS, ABM)
     *
     * @return ModelAndView for "pages/grade11/strand"
     */
    @GetMapping("/strand")
    public ModelAndView showStrandSelectionPage() {
        ModelAndView mv = new ModelAndView("pages/grade11/strand");
        return mv;
    }

    /**
     * GET request handler for /grade11/subject
     *
     * - Displays a list or table of Grade 11 subjects - Usually depends on the
     * strand selected (though not yet implemented here)
     *
     * @return ModelAndView for "pages/grade11/subjects"
     */
    @GetMapping("/subject")
    public ModelAndView showSubjectTablePage() {
        ModelAndView mv = new ModelAndView("pages/grade11/subjects");
        return mv;
    }
}
