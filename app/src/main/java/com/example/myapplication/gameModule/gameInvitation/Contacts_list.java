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
import com.example.myapplication.gameModule.Contact_model;
import com.example.myapplication.adapters.Adapter;
import com.example.myapplication.gameModule.Player_registration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contacts_list extends AppCompatActivity implements Adapter.ItemClickListener
{
    DatabaseReference mDatabase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    private APIService apiService;
    String title = "Game Invitation";
    String body = "Would you like to play game with me?";

    //FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    RecyclerView contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        this.setTitle("Players List");

        contactList = findViewById(R.id.rec_contact_list);
        contactList.setLayoutManager(new LinearLayoutManager(this));
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        mDatabase = FirebaseDatabase.getInstance().getReference("Device Tokens");

        List<Contact_model> contacts = requestContactPermission();
        for (final Contact_model i : contacts){
            Log.e("Name", i.name);
            Log.e("Phone Number", i.mobileNumber);

        }
        Adapter myAdapter = new Adapter(this,contacts, this);
        contactList.setAdapter(myAdapter);

    }

    public List<Contact_model> requestContactPermission() {
        List<Contact_model> contacts = new ArrayList<>();
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
           // Log.e("Contacts list", contacts.get(0).mobileNumber);
            //contact.setText(contacts.get(0).mobileNumber);
        }
        return contacts;
    }


    public List<Contact_model> getContacts(Context ctx) {
        List<Contact_model> list = new ArrayList<>();
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
                    if (inputStream != null)
                    {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    Contact_model info = new Contact_model();
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
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot dt : dataSnapshot.getChildren()) {
                        //Token token = dt.getValue(Token.class);
                        String userToken = dt.getKey();
                        sendNotifications(userToken, body, title);
                    }
                }
                else
                {
                    Toast.makeText(Contacts_list.this, "There is no player available", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updateToken(mDatabase);
    }

    private void updateToken(DatabaseReference db)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        //Player_registration profile_registration = new Player_registration();
        //profile_registration.setToken(refreshToken);
        db.child("token").setValue(refreshToken);
        Log.d("Token: ",refreshToken  );
    }
    public void sendNotifications(String userToken,String body, String title)
    {

        Notification_data data = new Notification_data(body,title);
        Notification_sender sender = new Notification_sender(data, userToken);

        apiService.sendNotification(sender).enqueue(new Callback<My_response>() {
            @Override
            public void onResponse(Call<My_response> call, Response<My_response> response) {
                int responcecode = response.code();
                Log.e("RESPONCE CODE FROM SERVER: ", ""+responcecode);
                if (responcecode == 200)
                {
                    //Toast.makeText(contactsList.this, "Send Notification Failed", Toast.LENGTH_LONG).show();
                    if (!response.isSuccessful())
                    {
                        //Toast.makeText(contactsList.this, "Send Notification Failed", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    //Toast.makeText(contactsList.this, "Send Notification Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<My_response> call, Throwable t)
            {

            }
        });
    }
}