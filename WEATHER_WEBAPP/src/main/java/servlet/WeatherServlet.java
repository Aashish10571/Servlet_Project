package servlet;

import api.SetupAPI;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@WebServlet("/service")
public class WeatherServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            StringBuilder inputData = new StringBuilder();

            BufferedReader bufferedReader = req.getReader();
            Scanner inputScanner = new Scanner(bufferedReader);

            while (inputScanner.hasNext()) {
                inputData.append(inputScanner.nextLine());
            }

            JSONObject jsonObject = new JSONObject(inputData.toString());
            String city = jsonObject.getString("city");

            bufferedReader.close();
            inputScanner.close();

            String apiURL = new SetupAPI().getApiURL(city);

            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            JSONObject jsonResponseData = new JSONObject();

            if (responseCode == 200) {
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                Scanner apiScanner = new Scanner(reader);
                StringBuilder responseContent = new StringBuilder();

                while (apiScanner.hasNext()) {
                    responseContent.append(apiScanner.nextLine());
                }

                JSONObject apiOutput = new JSONObject(responseContent.toString());

                String weather = apiOutput.getJSONArray("weather").getJSONObject(0).getString("main");
                int temperature = apiOutput.getJSONObject("main").getInt("temp") - 273;
                String description = apiOutput.getJSONArray("weather").getJSONObject(0).getString("description");
                int humidity = apiOutput.getJSONObject("main").getInt("humidity");
                double windSpeed = apiOutput.getJSONObject("wind").getDouble("speed");

                inputStream.close();
                reader.close();
                apiScanner.close();
                connection.disconnect();

                jsonResponseData.put("weather", weather);
                jsonResponseData.put("code", responseCode);
                jsonResponseData.put("temperature", temperature);
                jsonResponseData.put("description", description);
                jsonResponseData.put("humidity", humidity);
                jsonResponseData.put("wind", windSpeed);

                resp.getWriter().write(jsonResponseData.toString());
            } else {
                jsonResponseData.put("code", responseCode);

                resp.getWriter().write(jsonResponseData.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
