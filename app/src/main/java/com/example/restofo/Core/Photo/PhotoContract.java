package com.example.restofo.Core.Photo;
import android.app.Activity;
import android.content.Intent;

public interface PhotoContract {
    interface View {
        void onPhotoChosen(String imagePath);
        void onFailure(String message);
    }

    interface Presenter {
        void selectPhoto(Activity activity);
        void onPhotoResult(int requestCode, int resultCode, Intent data);
    }

    interface Interactor {
        void performPhotoPick(Activity activity);
    }
}
