package com.example.weatherreport;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherArrayAdapter extends ArrayAdapter<Weather> {
    private Map<String, Bitmap> bitmaps = new HashMap<>();
    // Конструктор для инициализации унаследованных членов суперкласса
    public WeatherArrayAdapter(Context context, List<Weather> forecast) {
        //super(context, R.layout.list_item, forecast);
        super(context, -1, forecast);
    }
    // Класс для повторного использования представлений списка при прокрутке
    private static class ViewHolder {
        ImageView conditionImageView;
        TextView dayTextView;
        TextView timeTextView;
        TextView lowTextView;
        TextView hiTextView;
        TextView humidityTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Получение объекта Weather для заданной позиции ListView
        Weather day = getItem(position);
        ViewHolder viewHolder; // Объект, содержащий ссылки на представления элемента списка
        // Проверить возможность повторного использования ViewHolder
        // для элемента, вышедшего за границы экрана
        if (convertView == null) { // Объекта ViewHolder нет, создать его
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.conditionImageView = (ImageView) convertView.findViewById(R.id.conditionImageView);
            viewHolder.dayTextView = (TextView) convertView.findViewById(R.id.dayTextView);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            viewHolder.lowTextView = (TextView) convertView.findViewById(R.id.lowTextView);
            viewHolder.hiTextView = (TextView) convertView.findViewById(R.id.hiTextView);
            viewHolder.humidityTextView = (TextView) convertView.findViewById(R.id.humidityTextView);
            convertView.setTag(viewHolder);
        } else { // Cуществующий объект ViewHolder используется заново
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Если значок погодных условий уже загружен, использовать его;
        // в противном случае загрузить в отдельном потоке
        if (bitmaps.containsKey(day.iconURL)) {
            viewHolder.conditionImageView.setImageBitmap(bitmaps.get(day.iconURL));
        } else {
            // Загрузить и вывести значок погодных условий
            new LoadImageTask(viewHolder.conditionImageView).execute(day.iconURL);
        }
        // Получить данные из объекта Weather и заполнить представления
        Context context = getContext(); // Для загрузки строковых ресурсов
        viewHolder.dayTextView.setText(context.getString(R.string.day_description, day.dayOfWeek, day.description));
        viewHolder.timeTextView.setText(day.time);
        viewHolder.lowTextView.setText(context.getString(R.string.low_temp, day.minTemp));
        viewHolder.hiTextView.setText(context.getString(R.string.high_temp, day.maxTemp));
        viewHolder.humidityTextView.setText(context.getString(R.string.humidity, day.humidity));

        return convertView; // Вернуть готовое представление элемента
    }
	// AsyncTask для загрузки изображения в отдельном потоке
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView; // Для вывода миниатюры 101
        // Сохранение ImageView для загруженного объекта Bitmap
        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
            }
        // загрузить изображение; params[0] содержит URL-адрес изображения
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]); // Создать URL для изображения
                // Открыть объект HttpURLConnection, получить InputStream
                // и загрузить изображение
                connection = (HttpURLConnection) url.openConnection();
                try (InputStream inputStream = connection.getInputStream()) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(params[0], bitmap); // Кэширование
                    }
                catch (Exception e) {
                    e.printStackTrace();
                    }
                }
            catch (Exception e) {
                e.printStackTrace();
                }
            finally {
                connection.disconnect(); // Закрыть HttpURLConnection
                }
            return bitmap;
            }
        // Связать значок погодных условий с элементом списка
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            }
	}
}



