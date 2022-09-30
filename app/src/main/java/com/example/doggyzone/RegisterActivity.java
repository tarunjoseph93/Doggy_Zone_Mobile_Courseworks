package com.example.doggyzone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    public static final String REALTIME_DATABASE_URL = "https://doggy-zone-default-rtdb.firebaseio.com/";
    ImageButton backButton;
    ImageView browseImage;
    TextView registerButton;
    EditText fname, lname, email, password, confirmPassword, dogName, dogAge, dogBreed, dogColor;
    Uri imageFilePath;
    ImageView profilePic;
    Bitmap bitmap;

    private String fNameString = "", lNameString = "", emailString = "", passwordString = "", dogNameString = "",
            dogAgeString = "", dogBreedString = "", dogColorString = "";

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backButton = findViewById(R.id.backButton);
        browseImage = findViewById(R.id.edit_image_register);
        registerButton = findViewById(R.id.register_text_button);
        profilePic = findViewById(R.id.dog_image_register);
        fname = findViewById(R.id.register_fname_edittext);
        lname = findViewById(R.id.register_lname_edittext);
        email = findViewById(R.id.register_email_edittext);
        password = findViewById(R.id.password_register_edittext);
        confirmPassword = findViewById(R.id.confirm_password_edittext);
        dogName = findViewById(R.id.register_dogname_edittext);
        dogAge = findViewById(R.id.register_dogage_edittext);
        dogBreed = findViewById(R.id.register_dogbreed_edittext);
        dogColor = findViewById(R.id.register_dogcolor_edittext);

        firebaseAuth= FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        browseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(RegisterActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select your dog's picture"), 10);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAuthentication();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK) {
            imageFilePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageFilePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profilePic.setImageBitmap(bitmap);

            }catch (Exception e) {
                Toast.makeText(this, "" + Arrays.toString(e.getStackTrace()), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void performAuthentication() {
        fNameString = fname.getText().toString().trim();
        lNameString = lname.getText().toString().trim();
        emailString = email.getText().toString().trim();
        passwordString = password.getText().toString().trim();
        dogNameString = dogName.getText().toString().trim();
        dogAgeString = dogAge.getText().toString().trim();
        dogBreedString = dogBreed.getText().toString().trim();
        dogColorString = dogColor.getText().toString().trim();
        String confirmPasswordString = confirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(emailString)) {
            email.setError("Email field is empty. Please enter your name!");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Your Email ID pattern is invalid! Please try again!");
        }
        else if(TextUtils.isEmpty(passwordString)) {
            password.setError("Password field is empty. Please enter a password!");
        }
        else if(passwordString.length() < 6) {
            password.setError("Password field is less than 6 characters. Please enter a bigger password!");
        }
        else if(TextUtils.isEmpty(confirmPasswordString)) {
            confirmPassword.setError("Confirm Password field is empty. Please confirm your entered password!");
        }
        else if(!passwordString.equals(confirmPasswordString)) {
            password.setError("Passwords do not match. Please enter your passwords again!");
            confirmPassword.setError("Passwords do not match. Please enter your passwords again!");
        }
        else {
            userProfileCreation();
        }
    }

    private void userProfileCreation() {
        progressDialog.setMessage("Creating your profile");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        uploadImage();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });

    }

    private void uploadImage() {
        progressDialog.setMessage("Image being uploaded...");

        String userId = firebaseAuth.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference(dogNameString + "-" + userId);

        storageReference.putFile(imageFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String dogImageUri = uri.toString();

                        hashMap.put("uid", userId);
                        hashMap.put("fname", fNameString);
                        hashMap.put("lname", lNameString);
                        hashMap.put("email", emailString);
                        hashMap.put("dog_name", dogNameString);
                        hashMap.put("dog_age", dogAgeString);
                        hashMap.put("dog_breed", dogBreedString);
                        hashMap.put("dog_color", dogColorString);
                        if(!dogImageUri.isEmpty()) {
                            hashMap.put("dog_image", dogImageUri);
                        }
                        else
                        {
                            hashMap.put("dog_image", "");
                        }
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance(REALTIME_DATABASE_URL)
                                .getReference("Users");

                        databaseReference.child(userId).setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        float percent = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded: " + (int)percent + "%");
                    }
                });
    }
}