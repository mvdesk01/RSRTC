package com.mtech.rsrtcsc.ui.activity.capture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityImageCaptureBinding;
import com.mtech.rsrtcsc.ui.activity.concession.ConcessionDetailActivity;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.ImageUtil;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageCaptureActivity extends BaseActivity<ActivityImageCaptureBinding> implements View.OnClickListener {

    public static final int CAPTURE_IMAGE = 100, PICK_IMAGE = 221;
    private Bitmap bitmapImage;
    private String str;
    private Uri photoURI;

    @Override
    protected ActivityImageCaptureBinding getActivityBinding() {
        return ActivityImageCaptureBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        str = intent.getStringExtra("message");
    }

    @Override
    protected void initCtrl() {
        binding.clear.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.capture.setOnClickListener(this);
        binding.browse.setOnClickListener(this);
        binding.prev.setOnClickListener(this);
        binding.ivHumberger.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.capture) {
            binding.image.setImageBitmap(null);
            if (checkPermission(CAPTURE_IMAGE)) {
                openCamera();
            }
        } else if (v.getId() == R.id.browse) {
            binding.image.setImageBitmap(null);
            if (checkPermission(PICK_IMAGE)) {
                openGallery();
            }
        } else if (v.getId() == R.id.clear) {
            RegisterationDataHelper.getInstance().getApplicationData().setPhoto("");
            RegisterationDataHelper.getInstance().getApplicationData().setHexPhoto("");
            binding.image.setImageBitmap(null);
        } else if (v.getId() == R.id.next) {
            if (bitmapImage != null) {
                RegisterationDataHelper.getInstance().getApplicationData().setPhoto(ImageUtil.convertBaseString(bitmapImage));
                RegisterationDataHelper.getInstance().getApplicationData().setHexPhoto(RegisterationDataHelper.getInstance().getApplicationData().getPhoto());
                startActivity(new Intent(ImageCaptureActivity.this, ConcessionDetailActivity.class));
            } else {
                Toast.makeText(ImageCaptureActivity.this, "Please Select The Image!", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.prev) {
            onBackPressed();
        } else if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(ImageCaptureActivity.this, MainActivity.class));
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
                photoURI = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAPTURE_IMAGE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Could not create image file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE:
                if (resultCode == RESULT_OK && photoURI != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
//                        loadImage(bitmap);
                        loadImage(rotateIfRequired(bitmap, photoURI)); // apply Fix 2 here;
                        Toast.makeText(this, "Photo uploaded successfully", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } /*else {
                    startNewActivity(ImageCaptureActivity.class);
                }*/
                break;

            case PICK_IMAGE:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    try {
                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, data.getData());
                        loadImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } /*else {
                    startNewActivity(ImageCaptureActivity.class);
                }*/
                break;
        }
    }

    private Bitmap rotateIfRequired(Bitmap bitmap, Uri selectedImage) throws IOException {
        InputStream input = getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            default:
                return bitmap;
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    private Bitmap compressBitmap(Bitmap original) {
        // Step 1: Resize dimensions
        int maxWidth = 1024;
        int maxHeight = 1024;

        int width = original.getWidth();
        int height = original.getHeight();

        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;

        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((float) maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float) maxWidth / ratioBitmap);
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(original, finalWidth, finalHeight, true);

        // Step 2: Compress resized bitmap to under 2MB
        int quality = 100;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        while (stream.toByteArray().length > 2 * 1024 * 1024 && quality > 10) {
            stream.reset();
            quality -= 5;
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        }

        byte[] compressedData = stream.toByteArray();
        return BitmapFactory.decodeByteArray(compressedData, 0, compressedData.length);
    }


    private void loadImage(Bitmap bitmap) {
        Bitmap compressedBitmap = compressBitmap(bitmap);
        bitmapImage = compressedBitmap;

        if (ImageUtil.checkImageSize(compressedBitmap)) {
            Glide.with(this).load(compressedBitmap).into(binding.image);
        } else {
            Toast.makeText(ImageCaptureActivity.this, "Image too large even after compression. Please choose a smaller image.", Toast.LENGTH_SHORT).show();
            binding.image.setImageBitmap(null);
            bitmapImage = null;
        }
    }


   /* private void loadImage(Bitmap bitmap) {
        bitmapImage = bitmap;
        if (ImageUtil.checkImageSize(bitmap)) {
            Glide.with(this).load(bitmap).into(binding.image);
        } else {
            Toast.makeText(ImageCaptureActivity.this, "Please upload photo up to 2 MB", Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if (RegisterationDataHelper.getInstance().getApplicationData() != null) {
            String image = RegisterationDataHelper.getInstance().getApplicationData().getPhoto();
            if (image != null && !image.equals("")) {
                bitmapImage = ImageUtil.convertBitmap(image);
                binding.image.setImageBitmap(bitmapImage);
            } else {
                binding.image.setImageBitmap(null);
            }
        }
    }

    private boolean checkPermission(int requestCode) {
        boolean read;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
            }, requestCode);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, requestCode);
        }


      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            boolean read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            if (!camera || !read) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, requestCode);
                return false;
            }
        }*/
        return read;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (allGranted) {
           /* if (requestCode == CAPTURE_IMAGE) {
                openCamera();
            } else if (requestCode == PICK_IMAGE) {
                openGallery();
            }*/
        } else {
            CommonUtils.showSnackBar(binding.getRoot(), "Please allow permission");
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startNewActivity(Class<?> className) {
        startActivity(new Intent(this, className));
    }
}

















/*
package com.mtech.rsrtcsc.ui.activity.capture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityImageCaptureBinding;
import com.mtech.rsrtcsc.ui.activity.concession.ConcessionDetailActivity;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.ImageUtil;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.io.IOException;

public class ImageCaptureActivity extends BaseActivity<ActivityImageCaptureBinding> implements View.OnClickListener {
    public static final int CAPTURE_IMAGE = 100,PICK_IMAGE=221;
    private Bitmap bitmapImage;
    private String str;

    @Override
    protected ActivityImageCaptureBinding getActivityBinding() {
        return ActivityImageCaptureBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        str = intent.getStringExtra("message");
    }

    @Override
    protected void initCtrl() {
        binding.clear.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.capture.setOnClickListener(this);
        binding.browse.setOnClickListener(this);
        binding.prev.setOnClickListener(this);
        binding.ivHumberger.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
           // case CAPTURE_IMAGE: loadImage((Bitmap) data.getExtras().get("data")); break;

            case CAPTURE_IMAGE:
                if(resultCode==0){
                    startNewActivity(ImageCaptureActivity.class);
                }else {
                    loadImage((Bitmap) data.getExtras().get("data"));
                    Toast.makeText(ImageCaptureActivity.this, "Photo Upload successfully", Toast.LENGTH_SHORT).show();
                }
                break;

            case PICK_IMAGE:
                try {
                if(resultCode == 0){
                    startNewActivity(ImageCaptureActivity.class);
                }else{
                    loadImage(ImageUtil.getBitmapFromUri(this,data.getData()));
                }
            } catch (IOException e)
            { e.printStackTrace();
            }

            break;
        }
    }

    private void loadImage(Bitmap bitmap) {
        bitmapImage=bitmap;
        if(ImageUtil.checkImageSize(bitmap)){
            Glide.with(this).load(bitmap).into(binding.image);
        }else{
            Toast.makeText(ImageCaptureActivity.this, "Please upload photo upto of 2 MB", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(RegisterationDataHelper.getInstance().getApplicationData()!=null){
            String image=RegisterationDataHelper.getInstance().getApplicationData().getPhoto();
            if(image!=null){
                if(!image.equals("")) {
                    bitmapImage=ImageUtil.convertBitmap(image);
                    binding.image.setImageBitmap(bitmapImage);
                }else{
                    binding.image.setImageBitmap(null);
                }}}
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.capture) {
            binding.image.setImageBitmap(null);
            if (checkPermission(CAPTURE_IMAGE)) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAPTURE_IMAGE);
            }
        } else if (v.getId() == R.id.browse) {
            binding.image.setImageBitmap(null);
            if (checkPermission(PICK_IMAGE)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        } else if (v.getId() == R.id.clear) {
            RegisterationDataHelper.getInstance().getApplicationData().setPhoto("");
            RegisterationDataHelper.getInstance().getApplicationData().setHexPhoto("");
            binding.image.setImageBitmap(null);
        } else if (v.getId() == R.id.next) {
            if (bitmapImage != null) {
                RegisterationDataHelper.getInstance().getApplicationData().setPhoto(ImageUtil.convertBaseString(bitmapImage));
                RegisterationDataHelper.getInstance().getApplicationData().setHexPhoto(RegisterationDataHelper.getInstance().getApplicationData().getPhoto());
                startActivity(new Intent(ImageCaptureActivity.this, ConcessionDetailActivity.class));
            } else {
                Toast.makeText(ImageCaptureActivity.this, "Please Select The Image!", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.prev) {
            onBackPressed();
        } else if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(ImageCaptureActivity.this, MainActivity.class));
        }

    }

        private boolean checkPermission(int requestCode) {
            boolean ret=true;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                    ret = false;
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, requestCode);
                }
            }

            return  ret;
        }

    public void onBackPressed(){
        super.onBackPressed();
    }

    private void startNewActivity(Class className) {
        startActivity(new Intent(this,className));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isPermissionDenied=false;
        for(int i=0;i<grantResults.length;i++)
        {
            if(!(grantResults[i]==PackageManager.PERMISSION_GRANTED))
            {isPermissionDenied=true;
                break;
            }
        }
        if(!isPermissionDenied)
        {
            switch (requestCode){
                case CAPTURE_IMAGE: binding.capture.performClick(); break;
                case PICK_IMAGE: binding.browse.performClick(); break;
            }
        }else{
            CommonUtils.showSnackBar(binding.getRoot(),"Please allow permission");
        }
    }
}*/
