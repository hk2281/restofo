package com.example.restofo.Core.Photo;

import android.app.Activity;
import android.content.Intent;

public class PhotoInteractor implements PhotoContract.Interactor {

    @Override
    public void performPhotoPick(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, 1); // 1 is the request code
    }
}
