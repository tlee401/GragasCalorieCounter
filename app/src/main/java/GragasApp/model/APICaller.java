package GragasApp.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 *  Minimal HTTP client for the API Ninjas service.
 *
 */
public class APICaller {
    private static final String APIKey = loadAPIKey();
    private static final String baseURL = "https://api.api-ninjas.com/v1/";

    /**
     * Loads the API key from a local {@code config.properties} file.
     * @return the API key, or an empty string if the file cannot be read or the key is missing
     */
    private static String loadAPIKey() {
    try (FileInputStream input = new FileInputStream("config.properties")) {
        Properties prop = new Properties();
        prop.load(input);
        return prop.getProperty("API_KEY");
    } catch (IOException e) {
        e.printStackTrace();
        return "";
    }
}

    /**
     * Calls an API Ninjas endpoint with a single {@code query} parameter and returns the
     * @param endpoint the endpoint path
     * @param query
     * @return the total calories summed across all array elements that contain a "calories" field
     * @throws Exception if the HTTP response code is not 200 OK or if an I/O/JSON error occurs
     */
    public double APICall(String endpoint, String query) throws Exception {
        String fullURL = baseURL + endpoint + "?query=" + 
                         java.net.URLEncoder.encode(query, "UTF-8");

        URL url = URI.create(fullURL).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Api-Key", APIKey);

        int status = con.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
            );
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();
            con.disconnect();

            JSONArray arr = new JSONArray(content.toString());
            double totalCalories = 0.0;

            for (int i = 0; i < arr.length(); i++) {
                JSONObject foodItem = arr.getJSONObject(i);
                if (foodItem.has("calories")) {
                    totalCalories += foodItem.getDouble("calories");
                }
            }

            return totalCalories;
        } else {
            BufferedReader err = new BufferedReader(
                new InputStreamReader(con.getErrorStream())
            );
            StringBuilder errorContent = new StringBuilder();
            String line;
            while ((line = err.readLine()) != null) {
                errorContent.append(line);
            }
            err.close();
            con.disconnect();
            throw new Exception("Error: " + status + " - " + errorContent);
        }
    }
}

