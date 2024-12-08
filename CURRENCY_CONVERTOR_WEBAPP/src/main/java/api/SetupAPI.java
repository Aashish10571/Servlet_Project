package api;

public class SetupAPI {
    private final String Key = "1c07f0e0bdc6b39e26c8036e";

    private String Url;

    public String getAPIUrl(String fromCurrency) {
        Url = "https://v6.exchangerate-api.com/v6/" + Key + "/latest/" + fromCurrency;
        return Url;
    }
}
