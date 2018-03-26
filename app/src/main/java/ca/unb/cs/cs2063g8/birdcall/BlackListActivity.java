package ca.unb.cs.cs2063g8.birdcall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import ca.unb.cs.cs2063g8.birdcall.database.BlackListDBHelper;

/**
 * Created by jason on 16/03/18.
 */

public class BlackListActivity extends AppCompatActivity {

    private BlackListDBHelper blackListDBHelper;
    private Button addRemoveButton;
    private Spinner type;
    private EditText name;
    private RecyclerView recyclerView;
    private static final String TAG = "BlackListActivity";

    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blacklist_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addRemoveButton = findViewById(R.id.add_remove);
        type = findViewById(R.id.type);
        name = findViewById(R.id.name);


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
