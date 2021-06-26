package com.example.myapplication.gameModule.gameInvitation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.gameModule.ContactModel;
import com.example.myapplication.gameModule.adapter;
import com.example.myapplication.gameModule.profileRegistration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class contactsList extends AppCompatActivity implements adapter.ItemClickListener {
    private APIService apiService;
    String title = "Game Invitation";
    String body = "Would you like to play game with me?";

    //FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    RecyclerView contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        this.setTitle("Players List");
        contactList = findViewById(R.id.rec_contact_list);
        contactList.setLayoutManager(new LinearLayoutManager(this));

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        List<ContactModel> contacts = requestContactPermission();
        for (final ContactModel i : contacts){
            Log.e("Name", i.name);
            Log.e("Phone Number", i.mobileNumber);

        }
        adapter myadapter = new adapter(this,contacts, this);
        contactList.setAdapter(myadapter);

    }

    public List<ContactModel> requestContactPermission() {
        List<ContactModel> contacts = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            1);
                }
            } else {
//                Toast.makeText(getApplicationContext(),getContacts(getApplicationContext()).size(),Toast.LENGTH_LONG).show();
                  contacts = getContacts(getApplicationContext());

                //Log.e("Contacts list", contact.mobileNumber);
            }
        } else {
            contacts = getContacts(getApplicationContext());
            Log.e("Contacts list", contacts.get(0).mobileNumber);
            //contact.setText(contacts.get(0).mobileNumber);
        }
        return contacts;
    }


    public List<ContactModel> getContacts(Context ctx) {
        List<ContactModel> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    ContactModel info = new ContactModel();
                    while (cursorInfo.moveToNext()) {

                        info.id = id;
                        info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        info.photo = photo;
                        info.photoURI = pURI;
                        list.add(info);
                        //final Boolean[] cond = new Boolean[1];

                    }

                    cursorInfo.close();
                }
            }
            cursor.close();
        }
        return list;
    }

    @Override
    public void onItemClick(View view, int position)
    {
        FirebaseDatabase.getInstance().getReference("Device Tokens").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt: dataSnapshot.getChildren())
                {
                     if(dt.child("token").getValue().toString() != null) {
                        String userToken = dt.child("token").getValue().toString();
                        Log.d("Token is: ", userToken);
                        sendNotifications(userToken, body, title);
                    }
                    else
                    {
                        Toast.makeText(contactsList.this, "Token has null value", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updateToken();
    }

    private void updateToken()
    {
        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        profileRegistration profile_registration = new profileRegistration();
        //profile_registration.setToken(refreshToken);
        //FirebaseDatabase.getInstance().getReference("Device Tokens").child("token").setValue(profile_registration.getToken());
        //Toast.makeText(this, "Token =: "+refreshToken + "Root: " +FirebaseDatabase.getInstance().getReference("players").getRoot().toString(), Toast.LENGTH_LONG).show();
        Log.d("Token: ",refreshToken  );
    }
    public void sendNotifications(String userToken,String body, String title)
    {
        //Toast.makeText(contactsList.this, "Send Notification Failed", Toast.LENGTH_LONG).show();
        NotificationData data = new NotificationData(body,title);
        NotificationSender sender = new NotificationSender(data, userToken);

        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200)
                {

                    if (!response.isSuccessful())
                    {
                        Toast.makeText(contactsList.this, "Send Notification Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}