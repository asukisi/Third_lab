package com.example.lab3;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        // Восстанавливаем данные, если таблица пуста
        databaseHelper.restoreInitialData();

        LinearLayout layout = findViewById(R.id.dataLayout);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button addButton = findViewById(R.id.addButton);
        Button updateLastButton = findViewById(R.id.updateLastButton);

        // Отображение данных
        displayAllUsers(layout);

        // Обработка нажатия кнопки удаления всех записей
        deleteButton.setOnClickListener(v -> {
            databaseHelper.deleteAllUsers(); // Удаляем данные
            layout.removeAllViews();        // Очищаем интерфейс
            displayAllUsers(layout);        // Отображаем обновленные данные (будет пусто)
        });

        // Обработка нажатия кнопки добавления записи
        addButton.setOnClickListener(v -> {
            String name = "Фурри";
            String date = "2024-11-20"; // Пример текущей даты
            databaseHelper.addUser(name, date); // Добавляем новую запись
            layout.removeAllViews();           // Очищаем текущий интерфейс
            displayAllUsers(layout);           // Отображаем обновленные данные
        });

        // Обработка нажатия кнопки изменения имени последней записи
        updateLastButton.setOnClickListener(v -> {
            databaseHelper.updateLastUserName("Иван"); // Меняем имя последней записи на "Иван"
            layout.removeAllViews();                  // Очищаем текущий интерфейс
            displayAllUsers(layout);                  // Отображаем обновленные данные
        });
    }

    // Метод для отображения всех пользователей
    private void displayAllUsers(LinearLayout layout) {
        Cursor cursor = databaseHelper.getAllUsers();

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);

                TextView textView = new TextView(this);
                textView.setText("ID: " + id + ", Имя: " + name + ", Дата: " + date);
                textView.setTextSize(16);
                layout.addView(textView);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}
