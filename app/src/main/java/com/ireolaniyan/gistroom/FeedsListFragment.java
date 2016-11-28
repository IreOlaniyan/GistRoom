package com.ireolaniyan.gistroom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

/**
 * Created by Ire Olaniyan on 11/6/2016.
 */
public class FeedsListFragment extends Fragment {
    private Feed mFeed;
    private RecyclerView mFeedsRecyclerView;
    private FeedsAdapter mAdapter;
    private static final String DIALOG_COMPOSE = "DialogCompose";
//    The target fragment can use the request code to identify which fragment is reporting back.
    private static final int REQUEST_FEED = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//        Creating a recycler view.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_feeds_list, container, false);
        mFeedsRecyclerView = (RecyclerView) view.findViewById(R.id.feeds_recycler_view);
        mFeedsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_FEED){
            String feed = (String) data.getSerializableExtra(ComposeGistFragment.EXTRA_FEED);
            mFeed.setFeed(feed);
        }
    }*/

    private void updateUI() {
        FeedsLab feedsLab = FeedsLab.get(getActivity());
        List<Feed> feeds = feedsLab.getFeeds();
        mAdapter = new FeedsAdapter(feeds);
        mFeedsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//         Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.feed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    Setting a different fab action for this fragment.
//    Prevent getActivity() from returning null while extending fab.
    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        SingleFragmentActivity activity = (SingleFragmentActivity)getActivity();
        activity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                ComposeGistFragment dialog = new ComposeGistFragment();

//                Set this fragment as the target fragment. ie changes made to the dialog are shown here.
                dialog.setTargetFragment(FeedsListFragment.this, REQUEST_FEED);
                dialog.show(manager, DIALOG_COMPOSE);
            }
        });
    }

//            Defining the ViewHolder.
    private class FeedsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mFeedsTextView;
        private ImageView mImageView;
        private Feed mFeed;


        public void bindFeed(Feed feed) {
            mFeed = feed;
            mFeedsTextView.setText(mFeed.getFeed());
        }

        public FeedsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.simple_image);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bm);
            drawable.setCircular(true);
            mFeedsTextView = (TextView) itemView.findViewById(R.id.list_feeds_textView);
            mImageView = (ImageView) itemView.findViewById(R.id.simple_imageView);
            mImageView.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mFeed.getFeed() + " clicked", Toast.LENGTH_SHORT).show();

            Intent intent = FeedsActivity.newIntent(getActivity(), mFeed.getId());
            startActivity(intent);
//            Intent intent = new Intent(getActivity(), FeedsActivity.class);
//            startActivity(intent);
//            Toast.makeText(getActivity(), mTitleTextView + "clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private class FeedsAdapter extends RecyclerView.Adapter<FeedsHolder>{
        private List<Feed> mFeeds;

        public FeedsAdapter(List<Feed> feeds){
            mFeeds = feeds;
        }

//                Called by the RecyclerView when it needs a new View to display an item.
        @Override
        public FeedsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_feeds, parent, false);
            return new FeedsHolder(view);
        }

//                Bind a ViewHolder’s View to your model object.
        @Override
        public void onBindViewHolder(FeedsHolder holder, int position) {
            Feed feed = mFeeds.get(position);
//            holder.mFeedsTextView.setText(feed.getFeed());
            holder.bindFeed(feed);
//            holder.mImageView.setImageBitmap(getCircleBitmap(bm));
        }

        @Override
        public int getItemCount() {
            return mFeeds.size();
        }
    }
}
