package me.guillem.criptoviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.guillem.criptoviewer.api.APIInterface;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewAdapter adapter;
    RecyclerView rv_list;
    private APIInterface apiInterface;
    private List<Datum> cryptoList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        getCoinList();
    }

    private void initRecyclerView() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        // Lookup the recyclerview in activity layout
        rv_list = findViewById(R.id.rv_list);
        // Initialize data
        cryptoList = new ArrayList<>();
        // Create adapter passing in the sample user data
        adapter = new RecyclerViewAdapter(cryptoList);
        // Attach the adapter to the recyclerview to populate items
        rv_list.setAdapter(adapter);
        // Set layout manager to position the items
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setItemAnimator(new DefaultItemAnimator());
        adapter.setClickListener(new RecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("XXXX", "You clicked " + adapter.getItem(position).getName() + " on nr " + position);
                //Toast.makeText(MainActivity.this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, CoinPage.class);
                intent.putExtra("coin", adapter.getItem(position));
                intent.putExtra("icon", adapter.getCryptoListIcons().get(adapter.getItem(position).getSymbol()).getLogo());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d("XXXX", "You LONG clicked " + adapter.getItem(position).getName() + " on nr " + position);
                Toast.makeText(MainActivity.this, "You LONG clicked " + adapter.getItem(position).getName() + " on nr " + position, Toast.LENGTH_LONG).show();
            }
        });
    }
}