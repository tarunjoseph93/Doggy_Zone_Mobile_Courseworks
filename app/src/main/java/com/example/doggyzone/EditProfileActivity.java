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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doggyzone.functions.ImageLoadASyncTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

public class EditProfileActivity extends AppCompatActivity {

    ImageButton backButton;
    ImageView profilePicIV, browseButtonIV;
    EditText dogNameET, dogAgeET, dogBreedET, dogColorET;
    Button saveButton;
    Uri imageFilePath;
    Bitmap bitmap;

    private FirebaseAuth firebaseAuth;
    public static final String REALTIME_DATABASE_URL = "https://doggy-zone-default-rtdb.firebaseio.com/";
    private ProgressDialog progressDialog;

    String userId = "", dogName = "", dogAge = "", dogBreed = "", dogColor = "", dogPic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        backButton = findViewById(R.id.edit_profile_backButton);
        profilePicIV = findViewById(R.id.dog_image_edit_profile);
        browseButtonIV = findViewById(R.id.edit_image_edit_profile);
        dogNameET = findViewById(R.id.edit_profile_dogname_edittext);
        dogAgeET = findViewById(R.id.edit_profile_dogage_edittext);
        dogBreedET = findViewById(R.id.edit_profile_dogbreed_edittext);
        dogColorET = findViewById(R.id.edit_profile_dogcolor_edittext);
        saveButton = findViewById(R.id.edit_profile_save_button);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        loadUI();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        browseButtonIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(EditProfileActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Image being uploaded...");

                dogName = dogNameET.getText().toString().trim();
                dogAge = dogAgeET.getText().toString().trim();
                dogBreed = dogBreedET.getText().toString().trim();
                dogColor = dogColorET.getText().toString().trim();

                String userId = firebaseAuth.getUid();
                HashMap<String, Object> hashMap = new HashMap<>();

                if(imageFilePath != null) {
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference storageReference = firebaseStorage.getReference(dogName + "-" + userId);

                    storageReference.putFile(imageFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            String dogImageUri = uri.toString();

                                            hashMap.put("dog_name", dogName);
                                            hashMap.put("dog_age", dogAge);
                                            hashMap.put("dog_breed", dogBreed);
                                            hashMap.put("dog_color", dogColor);
                                            if(!dogImageUri.isEmpty()) {
                                                hashMap.put("dog_image", dogImageUri);
                                            }
                                            else
                                            {
                                                hashMap.put("dog_image", "");
                                            }
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance(REALTIME_DATABASE_URL)
                                                    .getReference("Users");

                                            databaseReference.child(userId).updateChildren(hashMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfileActivity.this, "Edited Profile Successfully", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                else
                {
                    hashMap.put("dog_name", dogName);
                    hashMap.put("dog_age", dogAge);
                    hashMap.put("dog_breed", dogBreed);
                    hashMap.put("dog_color", dogColor);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance(REALTIME_DATABASE_URL)
                            .getReference("Users");

                    databaseReference.child(userId).updateChildren(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, "Edited Profile Successfully without Image", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
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
                profilePicIV.setImageBitmap(bitmap);

            }catch (Exception e) {
                Toast.makeText(this, "" + Arrays.toString(e.getStackTrace()), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadUI() {
        userId = firebaseAuth.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(REALTIME_DATABASE_URL)
                .getReference("Users");
        databaseReference.child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dogName = "" + dataSnapshot.child("dog_name").getValue();
                        dogAge = "" + dataSnapshot.child("dog_age").getValue();
                        dogBreed = "" + dataSnapshot.child("dog_breed").getValue();
                        dogColor = "" + dataSnapshot.child("dog_color").getValue();
                        dogPic = "" + dataSnapshot.child("dog_image").getValue();

                        ImageLoadASyncTask imageLoadASyncTask = new ImageLoadASyncTask(dogPic, profilePicIV);
                        imageLoadASyncTask.execute();

                        dogNameET.setText(dogName);
                        dogAgeET.setText(dogAge);
                        dogBreedET.setText(dogBreed);
                        dogColorET.setText(dogColor);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}