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
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            StringBuilder inputData = new StringBuilder();

            BufferedReader reader = req.getReader();
            Scanner scanner = new Scanner(reader);

            while (scanner.hasNext()) {
                inputData.append(scanner.next());
            }

            JSONObject jsonRequest = new JSONObject(inputData.toString());

            String fromCurrency = jsonRequest.getString("fromCurrency");
            String toCurrency = jsonRequest.getString("toCurrency");
            double amount = jsonRequest.getDouble("amount");

            reader.close();
            scanner.close();

            String apiUrl = new SetupAPI().getAPIUrl(fromCurrency);

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            Scanner apiScanner = new Scanner(inputStreamReader);
            StringBuilder responseContent = new StringBuilder();

            while (apiScanner.hasNext()) {
                responseContent.append(apiScanner.nextLine());
            }

            inputStream.close();
            inputStreamReader.close();
            apiScanner.close();

            JSONObject jsonObject = new JSONObject(responseContent.toString());
            JSONObject exchangeRates = jsonObject.getJSONObject("conversion_rates");

            double exchangeRate = exchangeRates.getDouble(toCurrency);

            double totalExchangeAmount = amount * exchangeRate;

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("toAmount", totalExchangeAmount);

            resp.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
