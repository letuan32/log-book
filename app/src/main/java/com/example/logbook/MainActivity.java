package com.example.logbook;

import android.content.ContentValues;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.*;
import com.bumptech.glide.Glide;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.ui.AppBarConfiguration;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.example.logbook.dbcontext.SqlLiteHelper;
import com.example.logbook.dbcontext.contract.PictureContract;
import com.example.logbook.dbcontext.models.PictureModel;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    ImageView image;
    Button loadPictureButton, prevButton, nextButton, savePictureButton;
    int currentIndex = 0;
    int numberOfSavedPicture;
    EditText editImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.imageView2);
        loadPictureButton = findViewById(R.id.buttonLoadUrl);
        savePictureButton = findViewById(R.id.buttonSavePicture);
        prevButton = findViewById(R.id.buttonPrev);
        nextButton = findViewById(R.id.buttonNext);
        editImageUrl = findViewById(R.id.editImageUrl);


        SqlLiteHelper dbHelper = new SqlLiteHelper(getApplicationContext());
        // Seed a URL to database
        ContentValues seedContentValues = new ContentValues();
        seedContentValues.put(PictureContract.PictureEntry.COLUMN_NAME_URL, "https://picsum.photos/200/300");
        dbHelper.addImageUrl(seedContentValues);


        ArrayList<PictureModel> pictureModels = dbHelper.getImageUrls();
        ArrayList<String> savedUrls = (ArrayList<String>) pictureModels.stream().map(PictureModel::getUrl).collect(Collectors.toList());

        numberOfSavedPicture = savedUrls.size();
        if(currentIndex <= 0)
        {
            nextButton.setEnabled(false);
        }
        if(currentIndex + 1 >= numberOfSavedPicture)
        {
            prevButton.setEnabled(false);
        }

        loadImage(savedUrls.get(currentIndex));

        ArrayAdapter<String> urlAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        savedUrls
                );
        ListView listView = findViewById(R.id.listview_url);
        listView.setAdapter(urlAdapter);


        editImageUrl.setText("https://picsum.photos/200/300");
        loadPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editImageUrl.isEnabled())
                {
                    editImageUrl.setEnabled(true);
                    savePictureButton.setEnabled(false);
                    loadPictureButton.setText("Load");
                }
                else {
                    String inputUrl = editImageUrl.getText().toString();
                    if (Patterns.WEB_URL.matcher(inputUrl).matches()) {
                        Glide.with(MainActivity.this).load(inputUrl).into(image);
                        editImageUrl.setEnabled(false);
                        loadPictureButton.setText("Unload");
                        savePictureButton.setEnabled(true);
                    } else {
                        editImageUrl.setError("Invalid URL format");
                    }
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex + 1 < numberOfSavedPicture)
                {
                    loadImage(savedUrls.get(++currentIndex));
                    if(!nextButton.isEnabled())
                    {
                        nextButton.setEnabled(true);
                        nextButton.setError(null);
                    }
                    if (currentIndex +1 >= numberOfSavedPicture) {
                        prevButton.setEnabled(false);
                    }
                }
                else
                {
                    prevButton.setError("Not found");
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex - 1 >= 0)
                {
                    loadImage(savedUrls.get(--currentIndex));
                    if(!prevButton.isEnabled())
                    {
                        prevButton.setEnabled(true);
                        prevButton.setError(null);
                    }
                    if (currentIndex <= 0) {
                        nextButton.setEnabled(false);
                    }
                }
                else
                {
                    nextButton.setError("Not found");
                }
            }
        });

        savePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                try
                {
                    String url = editImageUrl.getText().toString();
                    values.put(PictureContract.PictureEntry.COLUMN_NAME_URL, url);
                    dbHelper.addImageUrl(values);
                    savedUrls.add(0, url);

                    editImageUrl.getText().clear();

                    editImageUrl.setEnabled(true);
                    loadPictureButton.setText("Load");
                    savePictureButton.setEnabled(false);

                    numberOfSavedPicture++;
                    urlAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    String a = e.getMessage();
                    e.printStackTrace();
                    Log.d("Failed create db instance", e.getMessage());
                    savePictureButton.setError("Can not save image url");
                }

            }
        });
    }


    private void loadImage(String url) {
        Picasso.get()
                .load(url)
                .into(image);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}