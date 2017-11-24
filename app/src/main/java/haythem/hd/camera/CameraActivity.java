package haythem.hd.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity {
    private FrameLayout mFrameLayout;
    private Camera mCamera;
    private ImageView mImageView;
    private Button mCaptureButton;
    private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mImageView.setImageBitmap(bitmap);
            camera.startPreview();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        // Create an instance of Camera
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        CameraPreview mPreview = new CameraPreview(this, mCamera);

        findViewsById();

        mFrameLayout.addView(mPreview);

        mCaptureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
    }

    private void findViewsById() {
        mImageView = findViewById(R.id.imageview);
        mFrameLayout = findViewById(R.id.camera_preview);
        mCaptureButton = findViewById(R.id.button_capture);

    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
        }
        return c;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCamera.release();
    }
}

