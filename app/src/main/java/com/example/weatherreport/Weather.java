package com.example.weatherreport;


// Weather.java
// Информация о погоде за один день
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Weather {
    public final String dayOfWeek;
    public final String time;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconURL;

    //Конструктор
    public Weather(long timeStamp, double minTemp, double maxTemp, double humidity, String description, String iconName)
    {
        // NumberFormat для форматирования температур в целое число
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        this.dayOfWeek = convertTimeStampToDay(timeStamp);
        this.time = convertTimeStampToTime(timeStamp);
        this.minTemp = numberFormat.format((((minTemp-32)*5)/9)) + "\u00B0C";
        this.maxTemp = numberFormat.format((((maxTemp-32)*5)/9)) + "\u00B0C";
        this.humidity = NumberFormat.getPercentInstance().format(humidity / 100.0);
        this.description = description;
        this.iconURL = "http://openweathermap.org/img/w/" + iconName + ".png";
    }
    // Преобразование полученной даты в тип Calendar
    private static Calendar calendar (long timeStamp){
        Calendar calendar = Calendar.getInstance(); // Объект Calendar
        calendar.setTimeInMillis(timeStamp * 1000); // Получение времени
        TimeZone tz = TimeZone.getDefault(); // Часовой пояс устройства 40
        // Поправка на часовой пояс устройства
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        return calendar;
    }
    // Преобразование временной метки в название дня недели (Monday, ...)
    private static String convertTimeStampToDay(long timeStamp) {
        // Объект SimpleDateFormat, возвращающий название дня недели
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE");
        return dateFormatter.format(calendar(timeStamp).getTime());
    }
    private static String convertTimeStampToTime(long timeStamp) {
        // Объект SimpleDateFormat, возвращающий название дня недели
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
        return dateFormatter.format(calendar(timeStamp).getTime());
    }

}


