package com.example.myapplication.externalService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.Ep_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Import_chat extends AppCompatActivity implements Ep_adapter.ItemClickListener, ActionMode.Callback {

    DatabaseReference mDatabase,mDatabase2;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static  final int READ_REQUEST_CODE = 42;
    private static  final int PERMISSION_REQUEST_STORAGE = 1000;

    FloatingActionButton uploadFile,refresh;
    RecyclerView chatsList;
    ImageView importChat;
    TextView textView;
    ActionMode actionMode;
    BufferedReader br;

    //private long chat_count;
    private String userID = user.getUid();
    private String entry;
    private String fileName;
    private static ArrayList<HashMap<String,String>> chatData;
    private static int count;
    public String KEY = "DEFAULT";
    final Ep_adapter[] adapter = new Ep_adapter[1];

    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_chat);
        this.setTitle("Import Chat");

        chatsList = findViewById(R.id.chatsList);
        uploadFile = findViewById(R.id.import_chat);
       // refresh = findViewById(R.id.refresh);
        textView = findViewById(R.id.nullItem);

        chatsList.setLayoutManager(new LinearLayoutManager(this));
        count = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        // START BINDING LIST OF CHATS NAMES LIST SAVED IN DATABASE WITH RECYCLERVIEW

        mDatabase2 = FirebaseDatabase.getInstance().getReference("chatList");
        mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        mDatabase2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                long chat_count = snapshot.getChildrenCount();
                Log.e("Chat counts", ""+chat_count);
                if(snapshot.getChildrenCount() > 0) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Set_chat_name getName = ds.getValue(Set_chat_name.class);
                       String name = getName.getFileName();
                       Log.d("CHAT NAME: ", ""+name);
                       list.add(name);
                    }
                    Log.d("FILE NAME: ", String.valueOf(list.size()));
                    adapter[0] = new Ep_adapter(list,Import_chat.this);
                    int recyclerViewSize = adapter[0].getItemCount();
                    Log.d("RECYCLER VIEW SIZE: ", " " + recyclerViewSize);
                    adapter[0].notifyItemInserted(recyclerViewSize);
                    chatsList.setAdapter(adapter[0]);

                }
                else
                    {
                        textView.setText("NO CHAT AVAILABLE \n UPLOAD CHAT");
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }


        });
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                //Snackbar.make(v,"File Uploaded", Snackbar.LENGTH_LONG).setAction("Action",null).show();
                selectChatFromStorage();
            }

        });

        // END BINDING DATA TO RECYCLERVIEW

        getData(mDatabase,userID);





    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_STORAGE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission Given.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Permission not Given.", Toast.LENGTH_SHORT).show();
            }
        }

    }


//    private String readChat(String chat)
//    {
//        File file = new File( chat);
//        StringBuilder msg = new StringBuilder();
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String line;
//            while((line = br.readLine()) != null)
//            {
//                msg.append(line);
//                msg.append("\n");
//            }
//        }catch (IOException e){e.printStackTrace();}
//        return msg.toString();
//    }

    private void selectChatFromStorage()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                //path = this.getFilesDir()+"/"+"chat.txt";
                path = path.substring(path.indexOf(":") + 1);
//                if (path.contains("Emulated"))
//                {
//                    path = path.substring(path.indexOf("0")+1);
//
//                }
               // path = "file://sdcard/chat.txt";
                Toast.makeText(this, "Path is " + path, Toast.LENGTH_SHORT).show();
                Log.d("PATH",path);
                chatData = Chat_importer.getData(path);
                fileName = Chat_importer.getFileName(path);
                mDatabase2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int chatCount = (int) snapshot.getChildrenCount();
                        Set_chat_name saveName = new Set_chat_name(fileName);
                        mDatabase2.child(userID).child(""+chatCount).setValue(saveName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_SHORT).show();
                    }
                });

                //mDatabase2.child(userID).getRef().setValue(saveName);
                Log.d("Chat Name", fileName);
               // Log.d("Chat", "Chat Data : " + chatImporter.getData(path).toString());

                for (HashMap<String,String> i : chatData){
//                    Log.d("date", " :    "+ i.get("date"));
//                    Log.d("sender", " :    "+ i.get("sender"));
//                    Log.d("time", " :    "+ i.get("time"));
//                    Log.d("message", " :    "+ i.get("message"));
//                    Log.d("-------", "--------------------------------------------------------------------------------------------------------------\n\n\n");
                    Log.d("CHAT ITEMS", "List items " + chatData.size());
                    uploadData(mDatabase,i,userID,fileName,count);
                    count+=1;
                    Log.d("", "COUNT = "+ count);
                }
                    //list.add(fileName);
                    //adapter[0].notifyDataSetChanged();
//                int adapterSize = adapter[0].getItemCount();
//                if(adapterSize == 0)
//                {
//                    adapter[0].notifyItemInserted(adapterSize);
//                }
//                else
//                {
//                    adapter[0].notifyItemInserted(adapterSize+1);
//                }
            }
        }
    }

    public void uploadData(final DatabaseReference db, HashMap<String,String> msg, final String currentUser, final String fileName, final int messageNumber){
       // final DatabaseReference usr = db.child("chats");
        final HashMap<String,String> msg1 = msg;
        //final DatabaseReference[] currentUserRef = new DatabaseReference[1];
//        usr.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                  Log.d("DATA", ""+snapshot.getChildrenCount());
//
//                for(DataSnapshot user : snapshot.getChildren()){
//                    Log.d("DATA", ""+user.child("phoneNo").getValue());
//                    if(user.child("phoneNo").getValue(String.class).equals(currentUser)){
//                        //Log.d("DATA", ""+user.child("phoneNo").getValue());
//                        //currentUserRef[0] = user.getRef();
//                        KEY += user.getKey();
////                        count+=1;
////                        Log.d("Count check", "COUNT IS "+ count);
////                        String key = user.child("chats").getRef().push().getKey();
////                        user.child("chats").child(key).getRef().setValue(msg1);
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        db.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                long count = snapshot.getChildrenCount();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        final DatabaseReference database,database2;
        //database2 = FirebaseDatabase.getInstance().getReference("ChatList");
        database = FirebaseDatabase.getInstance().getReference("chats");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               long chat_count = snapshot. getChildrenCount();
                //Upload_chat upload_chat = new Upload_chat(msg1);
                //database2.child(currentUser).child(fileName);
                db.child(""+(chat_count+1)).child(""+(messageNumber+1)).setValue(msg1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
        //Toast.makeText(getApplicationContext(), "Number of chats: "+chat_count, Toast.LENGTH_SHORT).show();
        //String key = usr.child("chats").getRef().push().getKey();
        //SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        //String format = s.format(new Date());
       // Set_chat_name chat_name = new Set_chat_name(fileName);
        //adapter[0].notifyItemInserted(adapter[0].getItemCount()+1);
    }
    void getData(DatabaseReference db, String currentUser)
    {
        String p = "/chats/"+currentUser;
        DatabaseReference r = db.child(p).getRef();
        Task<DataSnapshot> d = r.get();
        d.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("GET TIMESTAMP", "DATA: "+task.getResult().getValue());
                }
            }
        });
    }


      @Override
    public void onItemClick(View view, final int position)
    {
          //Toast.makeText(getApplicationContext(), "Itme is clicked", Toast.LENGTH_SHORT).show();
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView name = findViewById(R.id.title);
                if(actionMode!=null)
                {
                    return false;
                }
                else
                {
                    actionMode = ((AppCompatActivity)v.getContext()).startSupportActionMode(Import_chat.this);
                       Toast.makeText(v.getContext(),name.getText(), Toast.LENGTH_SHORT).show();
                            Log.d("RESULT: ","item Selected");
                            return true;
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Em_profile_list.class);
                intent.putExtra("POSITION",position);
                startActivity(intent);
                Toast.makeText(Import_chat.this, "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.action_menu_1, menu);
        mode.setTitle("Delete Selected Item");
        return true;
    }
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                int itemId = item.getGroupId();
                Log.e("ITEM ID", ""+itemId);
                String itemName = list.get(item.getGroupId());
//                list.remove(item.getGroupId());
//                Log.d("item Removed: ", itemName);
//                adapter[0].notifyDataSetChanged();
//                Log.d("ACTION: ", "Item Deleted");
//                mDatabase.child(userID).removeValue();
//                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                removeChat(mDatabase2,userID,list,itemId,itemName);
                mode.finish();
                return true;


            default:
                return false;
        }
    }
    @Override
    public void onDestroyActionMode(ActionMode mode) {

        actionMode = null;
    }
    public void removeChat(DatabaseReference db, String userID, ArrayList<String> list, int itemID, String itemName)
    {
        list.remove(itemID);
        Log.d("item Removed: ", itemName);
        adapter[0].notifyDataSetChanged();
        Log.d("ACTION: ", "Item Deleted");
        db.child(userID).child(""+itemID).removeValue();
        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
    }
}