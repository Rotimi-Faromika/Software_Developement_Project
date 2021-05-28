package com.example.software_development_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.software_development_project.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("FieldCanBeLocal")
public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEdit, userPhoneEdit, addressEdit, passwordEdit;
    private TextView profileChange, closeText, updateText;

    private Uri ImageUri;
    private String myUrl = "";
    @SuppressWarnings("rawtypes")
    private StorageTask uploadTask;
    private StorageReference storageProfilePicture;
    private String checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePicture = FirebaseStorage.getInstance().getReference().child("Profile " +
                "Pictures");

        profileImageView = findViewById(R.id.settings_profile_picture);
        fullNameEdit = findViewById(R.id.settings_full_name);
        userPhoneEdit = findViewById(R.id.settings_phone_number);
        addressEdit = findViewById(R.id.settings_address);
        passwordEdit = findViewById(R.id.settings_password);
        profileChange = findViewById(R.id.profile_change);
        closeText = findViewById(R.id.close_settings);
        updateText = findViewById(R.id.update_settings);


        userInfoDisplay(profileImageView, fullNameEdit, userPhoneEdit, addressEdit, passwordEdit, profileChange, closeText, updateText);

        closeText.setOnClickListener(v -> finish());
        updateText.setOnClickListener(v -> {
            if (checker.equals("clicked")) {
                userInfoSaved();
            } else {
                updateOnlyUserInfo();
            }
        });

        profileChange.setOnClickListener(v -> {
            checker = "clicked";
            CropImage.activity(ImageUri).setAspectRatio(1, 1).start(SettingsActivity.this);
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(
                "Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", fullNameEdit.getText().toString());
        userMap.put("address", addressEdit.getText().toString());
        userMap.put("phoneOrder", userPhoneEdit.getText().toString());
        userMap.put("password", passwordEdit.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info Updated.",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ImageUri = result.getUri();

            profileImageView.setImageURI(ImageUri);

        } else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEdit.getText().toString())) {
            Toast.makeText(this, "Name is Mandatory.", Toast.LENGTH_SHORT).show();

        } /*else  if(TextUtils.isEmpty(addressEdit.getText().toString())){
            Toast.makeText(this, "Name is Mandatory.", Toast.LENGTH_SHORT).show();

        }*/ else if (TextUtils.isEmpty(userPhoneEdit.getText().toString())) {
            Toast.makeText(this, "Phone Number is Mandatory.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(passwordEdit.getText().toString())) {
            Toast.makeText(this, "Password is Mandatory.", Toast.LENGTH_SHORT).show();

        } else if (checker.equals("clicked")) {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, While your account gets updated.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (ImageUri != null) {
            final StorageReference fileRef =
                    storageProfilePicture.child(Prevalent.currentOnlineUser.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(ImageUri);

            //noinspection unchecked
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return fileRef.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    assert downloadUrl != null;
                    myUrl = downloadUrl.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(
                            "Users");
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("name", fullNameEdit.getText().toString());
                    userMap.put("address", addressEdit.getText().toString());
                    userMap.put("phoneOrder", userPhoneEdit.getText().toString());
                    userMap.put("password", passwordEdit.getText().toString());
                    userMap.put("image", myUrl);
                    ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                    progressDialog.dismiss();

                    startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                    Toast.makeText(SettingsActivity.this, "Profile Info Updated.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SettingsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Image is Not Selected.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("unused")
    private void userInfoDisplay(CircleImageView profileImageView, EditText fullNameEdit,
                                 EditText userPhoneEdit, EditText addressEdit,
                                 EditText passwordEdit, TextView profileChange, TextView closeText,
                                 TextView updateText) {
        DatabaseReference UsersRef =
                FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("image").exists()) {
                        String image =
                                Objects.requireNonNull(snapshot.child("Image").getValue()).toString();
                        String name = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                        String phone = Objects.requireNonNull(snapshot.child("Phone").getValue()).toString();
                        String password = Objects.requireNonNull(snapshot.child("Password").getValue()).toString();
                        String address =
                                Objects.requireNonNull(snapshot.child("Address").getValue()).toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEdit.setText(name);
                        userPhoneEdit.setText(phone);
                        passwordEdit.setText(password);
                        addressEdit.setText(address);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
