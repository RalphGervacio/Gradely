package com.gwa.Gradely.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Utility class for sending JSON responses in a standardized format. Helps
 * controllers return consistent JSON structures for AJAX requests.
 *
 * Example response structure: { "success": true, "message": "Action
 * completed.", "data": {...} }
 *
 * Author: RalphTheGreat
 */
public class JsonResponseUtil {

    // Reusable Gson instance for converting Java objects to JSON
    private static final Gson gson = new Gson();

    /**
     * Writes a JSON response to the HttpServletResponse output stream.
     *
     * @param response HttpServletResponse to write the JSON to
     * @param success boolean indicating success or failure of the operation
     * @param message message to send back (e.g., "Login successful", "Error
     * occurred")
     * @param data optional object to include in the "data" field (can be null)
     * @throws IOException if the response writer fails
     */
    public static void writeJsonResponse(HttpServletResponse response, boolean success, String message, Object data) throws IOException {
        // Set response content type to JSON
        response.setContentType("application/json");

        // Build the JSON structure
        JsonObject json = new JsonObject();
        json.addProperty("success", success);  // e.g., true or false
        json.addProperty("message", message);  // human-readable message

        // Add optional data (any object - list, map, bean, etc.)
        if (data != null) {
            json.add("data", gson.toJsonTree(data)); // serialize object to JSON
        }

        // Write the JSON string to the response output
        response.getWriter().write(json.toString());
    }
}
