package api;

public class SetupAPI {
    private String apiKey;
    private String apiURL;

    public SetupAPI() {
        SetupKey();
    }

    private void SetupKey() {
        this.apiKey = "f957d940491e5b56826d2d3a92b10fce";
    }

    public String getApiURL(String City) {
        this.apiURL = "https://api.openweathermap.org/data/2.5/weather?q=" + City + "&appid=" + this.apiKey;
        return this.apiURL;
    }
}
