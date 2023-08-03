package Wheather;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;
public class WeatherAPIProgram 
{
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=" + API_KEY;

    public static void main(String[] args) {
        try {
            int option;

            do {
                displayMenu();
                option = getUserInput();

                switch (option) {
                    case 1:
                        getWeather();
                        break;
                    case 2:
                        getWindSpeed();
                        break;
                    case 3:
                        getPressure();
                        break;
                    case 0:
                        System.out.println("Exiting the program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } while (option != 0);
        } catch (IOException e) {
            System.out.println("Error occurred while fetching data from API: " + e.getMessage());
        }
    }

    private static void displayMenu() {
        System.out.println("Select an option:");
        System.out.println("1. Get weather");
        System.out.println("2. Get Wind Speed");
        System.out.println("3. Get Pressure");
        System.out.println("0. Exit");
    }

    private static int getUserInput() {
        System.out.print("Enter your choice: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            return Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input. Please try again.");
            return -1;
        }
    }

    private static JSONObject fetchWeatherData() throws IOException {
        URL url = new URL(API_BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        return new JSONObject(response.toString());
    }

    private static void getWeather() throws IOException {
        JSONObject weatherData = fetchWeatherData();

        // Get user input for date (hourly forecast data has multiple entries for a single date)
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String date = getUserInputDate();
        JSONArray list = weatherData.getJSONArray("list");
        double temp = -1;

        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            String itemDate = item.getString("dt_txt").split(" ")[0];

            if (itemDate.equals(date)) {
                JSONObject main = item.getJSONObject("main");
                temp = main.getDouble("temp");
                break;
            }
        }

        if (temp != -1) {
            System.out.println("Temperature on " + date + " is: " + temp + " °C");
        } else {
            System.out.println("Weather data not available for the specified date.");
        }
    }

    private static void getWindSpeed() throws IOException {
        JSONObject weatherData = fetchWeatherData();

        // Get user input for date (hourly forecast data has multiple entries for a single date)
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String date = getUserInputDate();

        JSONArray list = weatherData.getJSONArray("list");
        double windSpeed = -1;

        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            String itemDate = item.getString("dt_txt").split(" ")[0];

            if (itemDate.equals(date)) {
                JSONObject wind = item.getJSONObject("wind");
                windSpeed = wind.getDouble("speed");
                break;
            }
        }

        if (windSpeed != -1) {
            System.out.println("Wind speed on " + date + " is: " + windSpeed + " m/s");
        } else {
            System.out.println("Weather data not available for the specified date.");
        }
    }

    private static void getPressure() throws IOException {
        JSONObject weatherData = fetchWeatherData();

        // Get user input for date (hourly forecast data has multiple entries for a single date)
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String date = getUserInputDate();

        JSONArray list = weatherData.getJSONArray("list");
        double pressure = -1;

        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            String itemDate = item.getString("dt_txt").split(" ")[0];

            if (itemDate.equals(date)) {
                JSONObject main = item.getJSONObject("main");
                pressure = main.getDouble("pressure");
                break;
            }
        }

        if (pressure != -1) {
            System.out.println("Pressure on " + date + " is: " + pressure + " hPa");
        } else {
            System.out.println("Weather data not available for the specified date.");
        }
    }

    private static String getUserInputDate() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
}

