package com.products.productservice.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class for configuring and sending HTTP requests.
 */
public class HttpUrlConfig {

    /**
     * Sends an HTTP POST request to the specified URL with the provided API key and request body.
     *
     * @param url         The URL to which the POST request is sent.
     * @param apiKey      The API key to include in the request headers for authentication.
     * @param requestBody The JSON-formatted request body to send in the POST request.
     * @return The response body as a String if the request is successful, or null if an error occurs.
     * @throws Exception If an error occurs while sending the request or reading the response.
     */
    public static String sendPostRequest(String url, String apiKey, String requestBody) throws Exception {
        // Create a URL object from the provided URL string
        URL uri = new URL(url);

        // Open an HTTP connection to the URL
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();

        // Set the HTTP method to POST
        connection.setRequestMethod("POST");

        // Set the request headers
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("api-key", apiKey);

        // Enable output for the connection to send the request body
        connection.setDoOutput(true);

        // Write the request body to the output stream
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.getBytes());

        // Establish the connection
        connection.connect();

        // Flush and close the output stream
        outputStream.flush();
        outputStream.close();

        // Get the HTTP response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // If the response code indicates success (HTTP 200 OK)
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Print the response message
            System.out.println("Response: " + connection.getResponseMessage());

            // Read the response body
            InputStream inputStream = connection.getInputStream();
            byte[] responseBytes = new byte[1024];
            int bytesRead;
            StringBuilder responseBuilder = new StringBuilder();

            // Read the response in chunks and append to the response builder
            while ((bytesRead = inputStream.read(responseBytes)) != -1) {
                responseBuilder.append(new String(responseBytes, 0, bytesRead));
            }

            // Return the complete response as a String
            return responseBuilder.toString();
        } else {
            // Print the error message if the response code is not HTTP 200 OK
            System.out.println("Error: " + connection.getResponseMessage());
            return null;
        }
    }
}