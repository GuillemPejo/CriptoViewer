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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import me.guillem.criptoviewer.adapter.rvAdapter;
import me.guillem.criptoviewer.api.APIInterface;
import me.guillem.criptoviewer.api.ApiClient;
import me.guillem.criptoviewer.retrofit.CryptoList;
import me.guillem.criptoviewer.retrofit.Datum;
import me.guillem.criptoviewer.retrofit.Info;

public class MainActivity extends AppCompatActivity {

    private rvAdapter adapter;
    RecyclerView rv_list;
    private APIInterface apiInterface;
    private List<Datum> cryptoList = null;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        getCoinList();
    }

    private void initRecyclerView() {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        // Lookup the recyclerview in activity layout
        rv_list = findViewById(R.id.rv_list);
        // Initialize data
        cryptoList = new ArrayList<>();
        // Create adapter passing in the sample user data
        adapter = new rvAdapter(cryptoList);
        // Attach the adapter to the recyclerview to populate items
        rv_list.setAdapter(adapter);
        // Set layout manager to position the items
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setItemAnimator(new DefaultItemAnimator());
        adapter.setClickListener(new rvAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("XXXX", "You clicked " + adapter.getItem(position).getName() + " on nr " + position);
                //Toast.makeText(MainActivity.this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, CoinPage.class);
                //intent.putExtra("coin", adapter.getItem(position));
                //intent.putExtra("icon", adapter.getCryptoListIcons().get(adapter.getItem(position).getSymbol()).getLogo());
                //startActivity(intent);
            }

        });
    }

    private void getCoinList() {
        //IDE is satisfied that the Disposable is being managed.
        compositeDisposable.add(apiInterface.getMarketPairsLatest("100")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CryptoList>() {
                    @Override
                    public void onSuccess(CryptoList list) {
                        cryptoList.clear();
                        cryptoList.addAll(list.getData());
                        updateLogoList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                        Log.d("XXXX", e.getLocalizedMessage());
                    }
                }));
    }
    private void updateLogoList() {
        String SEPARATOR = ",";
        StringBuilder csvBuilder = new StringBuilder();
        for (Datum datumx : cryptoList) {
            csvBuilder.append(datumx.getSymbol());
            csvBuilder.append(SEPARATOR);
        }
        String csv = csvBuilder.toString();
        csv = csv.substring(0, csv.length() - SEPARATOR.length()); //Remove last comma

        final APIInterface apiInterface = ApiClient.getClient().create(APIInterface.class);
        compositeDisposable.add(apiInterface.getCryptocurrencyInfo(csv)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Info>() {
                    @Override
                    public void onSuccess(Info coinInfo) {
                        adapter.getCryptoListIcons().clear();
                        adapter.getCryptoListIcons().putAll(coinInfo.getData());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "getCryptocurrencyInfo onFailure", Toast.LENGTH_SHORT).show();
                        Log.d("XXXX", e.getLocalizedMessage());
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Placed wherever we'd like to dispose our Disposables (i.e. in onDestroy()).
        compositeDisposable.dispose();
    }

}
