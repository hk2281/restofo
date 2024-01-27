package com.example.restofo.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.restofo.Core.API.ServiceApi;
import com.example.restofo.Core.Login.TokenManager;
import com.example.restofo.Core.Photo.PhotoContract;
import com.example.restofo.Core.Photo.PhotoPresenter;
import com.example.restofo.Helpers.ImageHelper;
import com.example.restofo.Helpers.RoundedTransformation;
import com.example.restofo.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;




import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoActivity extends AppCompatActivity implements PhotoContract.View , View.OnClickListener {
    private ImageView imageView;
    private  String ImgPath;
    private ImageButton sendBtn;
    private PhotoPresenter mPhotoPresenter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imageView = findViewById(R.id.selected_image);
        mPhotoPresenter = new PhotoPresenter(this);
        this.sendBtn = findViewById(R.id.button_send_photo);
        this.sendBtn.setOnClickListener(this);

        imageView.setOnClickListener(view -> mPhotoPresenter.selectPhoto(PhotoActivity.this));

        ImageButton shareButton = findViewById(R.id.button_choose_photo);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(v);
            }
        });
    }

    @Override
    public  void onClick(View view){
        if (view.getId() == R.id.button_send_photo){

            TokenManager tManager = new TokenManager(view.getContext());
            Log.d("ke", "huekin");
            Log.d("ke", tManager.getToken());

            try {
                sendPhotoToServer(ImgPath,tManager.getToken());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }

    }

    @Override
    public void onPhotoChosen(String imagePath)  {
        Picasso.get()
                .load(imagePath)
                .transform(new RoundedTransformation(50,0))
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                        Log.d("ke", "image>loded");
                        Log.d("ke", imagePath);
                        ImgPath = imagePath;
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("ke", "cant load image");
                    }
                });
    }

    @Override
    public void onFailure(String message) {
        // Обработка ошибки
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoPresenter.onPhotoResult(requestCode, resultCode, data);
    }

    public void shareImage(View view) {
        ImageView imageView = findViewById(R.id.selected_image);
        // Получение Drawable из ImageView
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Log.d("ke", "shere");
        try {
            // Создание файла изображения во внешнем кэше
            File file = new File(getExternalCacheDir(), "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Создание Intent для отправки файла
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            Uri imageUri = FileProvider.getUriForFile(this, "com.example.restofo", file);
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(intent, "Share Image"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendPhotoToServer(String imagePath, String customToken) throws Exception{
        Uri imageUri = Uri.parse(imagePath);
        File file = uriToFile(imageUri, this);
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS) // Таймаут подключения: 10 секунд
                    .writeTimeout(60, TimeUnit.SECONDS)   // Таймаут записи: 10 секунд
                    .readTimeout(60, TimeUnit.SECONDS)    // Таймаут чтения: 30 секунд
                    .build();

            // Создание тела запроса
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName()+".png",
                            RequestBody.create(MediaType.parse("application/octet-stream"), file))
                    .build();

            // Создание заголовка
            Request request = new Request.Builder()
                    .url("http://35.228.230.11/process_photo")
                    .addHeader("Authorization", "Bearer " + customToken)
                    .post(requestBody)
                    .build();

            // Выполнение запроса
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.d("ke", "error");
                    } else {

                        InputStream inputStream = response.body().byteStream();
//
                        Bitmap bitmap = ImageHelper.getRoundedCornerBitmap(BitmapFactory.decodeStream(inputStream),
                                20, 0);
//

//
                        Log.d("ke", String.valueOf(bitmap));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView imageView = findViewById(R.id.selected_image);
                                imageView.setImageBitmap(bitmap);
//
                            }
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    Log.d("ke", e.getMessage());
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File uriToFile(Uri uri, Context context) {
        File file = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            InputStream inputStream = resolver.openInputStream(uri);
            file = new File(context.getCacheDir(), "tempImage");
            FileOutputStream outputStream = new FileOutputStream(file);

            if (inputStream != null) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                inputStream.close();
            }
            outputStream.close();
        } catch (Exception e) {
            Log.e("FileConversion", "Error converting Uri to File", e);
        }
        return file;
    }

}