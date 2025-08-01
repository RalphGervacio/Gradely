package com.gwa.Gradely.controller.dashboard;

import com.gwa.Gradely.beans.StudentBean;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author RalphTheGreat
 */
@Controller
public class DashboardController {

    /**
     * Handles GET request to "/dashboard"
     *
     * - Checks if the student is logged in (via session). - If not logged in,
     * redirects to the login form. - If logged in, shows the dashboard page
     * with student data.
     *
     * @param session - the current HTTP session, used to retrieve login info
     * @return ModelAndView - view name + student data (if logged in)
     */
    @GetMapping("/dashboard")
    public ModelAndView showDashboard(HttpSession session) {
        // Get the logged-in student from the session
        StudentBean student = (StudentBean) session.getAttribute("loggedInStudent");

        // If not logged in, redirect to login page
        if (student == null) {
            return new ModelAndView("redirect:/login/form"); // fallback login path
        }

        // If logged in, show the dashboard view and pass the student data
        ModelAndView mv = new ModelAndView("pages/dashboard/dashboard");
        mv.addObject("student", student); // this will be available in Thymeleaf using ${student}
        return mv;
    }
}
