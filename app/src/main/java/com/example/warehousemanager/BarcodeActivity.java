package com.example.warehousemanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class BarcodeActivity extends CaptureActivity {
    private static final int REQUEST_CODE = 20;
    private static final String TAG = "BarcodeActivity";
    FloatingActionButton photo_library_floating_button;
    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_barcode);
        return findViewById(R.id.zxing_barcode_scanner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photo_library_floating_button = findViewById(R.id.photo_library_floating_button);

        photo_library_floating_button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            // Do something with the selected image URI
            decodeFromUri(selectedImage);
        }
    }

    private void decodeFromUri(Uri selectedImage) {
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, true);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Set up hints for the decoder
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            Reader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap, hints);

            BarcodeResult barcodeResult = new BarcodeResult(result,null);
            Intent intent = CaptureManager.resultIntent(barcodeResult,null);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (IOException | NotFoundException | ChecksumException | FormatException e) {
            Log.e(TAG, "onActivityResult: ", e);
            Toast.makeText(BarcodeActivity.this, "Barcode not found.", Toast.LENGTH_LONG).show();
        }
    }


}