package gianglong.app.chat.longchat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import gianglong.app.chat.longchat.R;

public class ChatActivity extends AppCompatActivity {
    RecyclerView rvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initUI();
        initConfig();
    }


    public void initUI(){
        rvMessage = (RecyclerView) findViewById(R.id.rvMessage);
    }


    public void initConfig(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
