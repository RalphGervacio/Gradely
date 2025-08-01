package com.gwa.Gradely.controller.auth;

import com.google.gson.Gson;
import com.gwa.Gradely.beans.StudentBean;
import com.gwa.Gradely.dao.StudentsDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static javax.crypto.Cipher.SECRET_KEY;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author RalphTheGreat
 */
@Controller
//@RequestMapping(value = "/auth")  will be your root path (example usage on AJAX url: "/auth/authenticate") 
//the url will call the endpoint from root path which is /auth then search for the endpoint /authenticate to start the process

//FOR THE ENDPOINT OF VIEW//
//In url tab of the browser it will show the your_domain_name/auth/login or localhost:8080/auth/login
@RequestMapping(value = "/auth") 
public class StudentController {

    // Inject the DAO for student database operations
    @Autowired
    StudentsDAO studentsDAO;

    // Encoder for securely comparing hashed passwords (BCrypt)
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * GET login form with optional remember-me logic If user is already logged
     * in or has a valid remember-me cookie, redirect to dashboard
     * @param returnTo
     * @param session
     * @param request
     * @param response
     * @return 
     */
    @GetMapping(value = "/login")
    public ModelAndView showLoginPage(@RequestParam(value = "returnTo", required = false) String returnTo,
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response) {

        // If session already has a logged-in student, redirect to dashboard
        if (session.getAttribute("loggedInStudent") != null) {
            return new ModelAndView("pages/dashboard/dashboard");
        }

        // Check for valid "remember-student" cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember-student".equals(cookie.getName())) {
                    String[] parts = cookie.getValue().split("\\|");

                    // Cookie must have 3 parts: studentId | expiryTime | md5Signature
                    if (parts.length == 3) {
                        String studentId = parts[0];
                        try {
                            long expiry = Long.parseLong(parts[1]);

                            // Expired cookie? Skip.
                            if (System.currentTimeMillis() > expiry) {
                                break;
                            }

                            // Validate signature (md5 hash of studentId:expiry:secret_key)
                            String expectedSignature = DigestUtils.md5DigestAsHex((studentId + ":" + expiry + ":" + SECRET_KEY).getBytes());
                            if (expectedSignature.equals(parts[2])) {
                                StudentBean student = studentsDAO.findByStudentId(studentId);

                                // Auto-login if student found
                                if (student != null) {
                                    session.setAttribute("loggedInStudent", student);
                                    return new ModelAndView("pages/dashboard/dashboard");
                                }
                            }
                        } catch (NumberFormatException e) {
                            break; // Malformed cookie
                        }
                    }
                }
            }
        }

        // Otherwise, show login page
        ModelAndView mv = new ModelAndView("pages/auth/login");
        if (returnTo != null && !returnTo.isEmpty()) {
            mv.addObject("returnTo", returnTo);
        }
        return mv;
    }

    /**
     * POST login logic (AJAX-based) Returns JSON response Supports optional
     * "remember me" persistent login via cookie
     * @param studentId
     * @param password
     * @param rememberMe
     * @param session
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @PostMapping(value = "/authenticate")
    public void handleLogin(@RequestParam("studentId") String studentId,
            @RequestParam("password") String password,
            @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        Map<String, Object> jsonResponse = new HashMap<>();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        // Trim inputs
        studentId = studentId.trim();
        password = password.trim();

        // Validate required fields
        if (studentId.isEmpty() || password.isEmpty()) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Student ID and password are required.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }

        try {
            // Look up student by ID
            StudentBean student = studentsDAO.findByStudentId(studentId);

            // If no match or password doesn't match, reject
            if (student == null || !passwordEncoder.matches(password, student.getPassword())) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid student ID or password.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            // Success: set session
            session.setAttribute("loggedInStudent", student);

            // If "remember me" is checked, create a secure cookie
            if (rememberMe) {
                long expiry = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000); // 30 days
                String signature = DigestUtils.md5DigestAsHex((studentId + ":" + expiry + ":" + SECRET_KEY).getBytes());
                String token = studentId + "|" + expiry + "|" + signature;

                Cookie cookie = new Cookie("remember-student", token);
                cookie.setHttpOnly(true); // Prevent JS access
                cookie.setPath("/");
                cookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                response.addCookie(cookie);
            }

            // JSON success response
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Login successful.");
            jsonResponse.put("redirectUrl", "/dashboard");
            jsonResponse.put("name", student.getFirstName());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(jsonResponse));

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An internal error occurred during login.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    /**
     * GET logout endpoint Clears session and deletes remember-me cookie
     * @param session
     * @param response
     * @return 
     */
    @GetMapping(value = "/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // Invalidate session
        session.invalidate();

        // Clear remember-me cookie
        Cookie cookie = new Cookie("remember-student", null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Delete cookie
        response.addCookie(cookie);

        // Redirect back to login page
        return "redirect:/auth/login";
    }
}
