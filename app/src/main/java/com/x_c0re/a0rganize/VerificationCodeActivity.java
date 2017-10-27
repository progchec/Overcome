package com.x_c0re.a0rganize;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class VerificationCodeActivity extends AppCompatActivity
{
    public static String codeVerifying;

    private EditText mCodeField;

    public static String entered_login;
    public static String entered_password;
    public static String entered_name;
    public static String entered_surname;
    public static String entered_phone;

    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Number Verification");

        mCodeField = (EditText)findViewById(R.id.codeField);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.top_right_start_mission_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.start_mission_button:
                if (codeVerifying.equals(mCodeField.getText().toString()))
                {
                    helper = new DBHelper(this);
                    SQLiteDatabase database = helper.getWritableDatabase();

                    // тут не нужен курсор, это надо убрать
                    Cursor cursor2 = database.query(DBHelper.TABLE_CONTACTS,
                            null, null, null, null, null, null);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_NAME, entered_name);
                    contentValues.put(DBHelper.KEY_SURNAME, entered_surname);
                    contentValues.put(DBHelper.KEY_LOGIN, entered_login);
                    contentValues.put(DBHelper.KEY_PASSWORD, entered_password);
                    contentValues.put(DBHelper.KEY_PHONE, entered_phone);

                    database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

                    cursor2.close();
                    database.close();

                    Toast toast = Toast.makeText(getApplicationContext(), "Phone number confirmed", Toast.LENGTH_LONG);
                    toast.show();

                    Intent intent = new Intent(this, UploadPhotoActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Incorrect code", Toast.LENGTH_LONG);
                    toast.show();
                }
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
