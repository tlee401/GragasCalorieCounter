package GragasApp.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class APICaller {
    private static final String APIKey = "Not_Actual_Key_Nice_Try";
    private static final String baseURL = "https://api.api-ninjas.com/v1/";

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

