package com.example.restofo.Core.Photo;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class PhotoPresenter implements PhotoContract.Presenter {
    private PhotoContract.View mView;
    private PhotoInteractor mInteractor;

    public PhotoPresenter(PhotoContract.View view) {
        this.mView = view;
        this.mInteractor = new PhotoInteractor();
    }

    @Override
    public void selectPhoto(Activity activity) {
        mInteractor.performPhotoPick(activity);
    }

    @Override
    public void onPhotoResult(int requestCode, int resultCode, Intent data) {
        // Handle result of photo pick
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String imagePath = data.getData().toString(); // Simplified for example
            mView.onPhotoChosen(imagePath);
        } else {
            mView.onFailure("Photo pick cancelled or failed");
        }
    }
}