package haythem.hd.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mChooseImageBtn;
    private Button mSaveImageBtn;
    private Button mCameraIntentBtn;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsByID();

        mChooseImageBtn.setOnClickListener(this);
        mSaveImageBtn.setOnClickListener(this);
        mCameraIntentBtn.setOnClickListener(this);

    }

    private void findViewsByID() {
        mChooseImageBtn = findViewById(R.id.choose);
        mSaveImageBtn = findViewById(R.id.save);
        mCameraIntentBtn = findViewById(R.id.custom_camera);
        mImageView = findViewById(R.id.image_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose:
                choosePicture();
                break;
            case R.id.save:
                TakePicture();
                break;
            case R.id.custom_camera:
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        // Show only images, png,jpeg,gif
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constantes.PICK_IMAGE_REQUEST);
    }


    private void TakePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.CAMERA)) {

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            1);
                }
            }
            else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constantes.REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == Constantes.PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    mImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == Constantes.REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                mImageView.setImageBitmap(bitmap);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constantes.REQUEST_TAKE_PHOTO);
                } else {

                }
                return;
            }

        }
    }


}
