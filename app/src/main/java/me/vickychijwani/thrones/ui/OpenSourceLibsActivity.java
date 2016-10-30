package me.vickychijwani.thrones.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.vickychijwani.thrones.R;
import me.vickychijwani.thrones.ui.widget.DividerItemDecoration;

public class OpenSourceLibsActivity extends BaseActivity {

    private static final List<Library> LIBRARIES = Arrays.asList(
            new Library("Gson", "Google Inc.", "https://github.com/google/gson"),
            new Library("OkHttp", "Square Inc.", "http://square.github.io/okhttp/"),
            new Library("Picasso", "Square Inc.", "http://square.github.io/picasso/"),
            new Library("Retrofit", "Square Inc.", "http://square.github.io/retrofit/"),
            new Library("RxAndroid", "ReactiveX", "https://github.com/ReactiveX/RxAndroid"),
            new Library("jsoup", "Jonathan Hedley", "https://jsoup.org/")
    );

    private LibsAdapter mLibsAdapter;

    private RecyclerView mLibsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_libs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mLibsList = (RecyclerView) findViewById(R.id.libs_list);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // sort alphabetically
        Collections.sort(LIBRARIES, new Comparator<Library>() {
            @Override
            public int compare(Library lhs, Library rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });

        mLibsAdapter = new LibsAdapter(this, LIBRARIES, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mLibsList.getChildLayoutPosition(v);
                if (pos == RecyclerView.NO_POSITION) return;
                Library library = mLibsAdapter.getItem(pos);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(library.url));
                OpenSourceLibsActivity.this.startActivity(intent);
            }
        });
        mLibsList.setAdapter(mLibsAdapter);
        mLibsList.setLayoutManager(new LinearLayoutManager(this));
        mLibsList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }


    static class LibsAdapter extends RecyclerView.Adapter<LibsAdapter.LibraryViewHolder> {

        private final LayoutInflater mLayoutInflater;
        private final List<Library> mLibraries;
        private final View.OnClickListener mItemClickListener;

        public LibsAdapter(Context context, List<Library> libraries,
                           View.OnClickListener itemClickListener) {
            mLayoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            mLibraries = libraries;
            mItemClickListener = itemClickListener;
            setHasStableIds(true);
        }

        @Override
        public int getItemCount() {
            return mLibraries.size();
        }

        public Library getItem(int position) {
            return mLibraries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).name.hashCode();
        }

        @Override
        public LibraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.open_source_libs_list_item, parent, false);
            return new LibraryViewHolder(view, mItemClickListener);
        }

        @Override
        public void onBindViewHolder(LibraryViewHolder viewHolder, int position) {
            Library library = getItem(position);
            viewHolder.name.setText(library.name);
            viewHolder.author.setText(library.author);
        }

        static class LibraryViewHolder extends RecyclerView.ViewHolder {
            final TextView name;
            final TextView author;

            LibraryViewHolder(@NonNull View view, View.OnClickListener clickListener) {
                super(view);
                name = (TextView) view.findViewById(R.id.lib_name);
                author = (TextView) view.findViewById(R.id.lib_author);
                view.setOnClickListener(clickListener);
            }
        }

    }

    static class Library {
        final String name;
        final String author;
        final String url;

        Library(String name, String author, String url) {
            this.name = name;
            this.author = author;
            this.url = url;
        }
    }

}
