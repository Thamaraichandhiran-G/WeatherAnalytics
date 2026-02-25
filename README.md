# WeatherAnalytics
Developed a modular Spring Boot application that analyze weather details by date, month, and yearly temperature statistics.

Load Weather Data

API Endpoints:

1 POST /api/weather/load
* Loads weather data from CSV into database.

2️ Get Weather by Date
GET /api/weather/date?date=YYYY-MM-DD
* Returns weather condition, temperature, humidity, and pressure for that date.

3️ Get Weather by Month
GET /api/weather/month?month=1
* Returns weather details for the given month.

4️ Get Monthly Temperature Stats
GET /api/weather/stats?year=2005
* Returns minimum, maximum, and median temperature for each month of that year.
