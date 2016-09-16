package org.qwan.energy.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.qwan.energy.R;
import org.qwan.energy.utils.AppGlobals;
import org.qwan.energy.utils.Helpers;

import java.io.IOException;
import java.util.ArrayList;

public class ViewCreditsFragment extends Fragment {

    private View mBaseView;
    private GridView gridView;
    private ProgressDialog mProgressDialog;
    private View baseView;
    private ArrayList<String> imagesArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imagesArrayList = new ArrayList<>();
        mBaseView = inflater.inflate(R.layout.fragment_image, container, false);
        baseView = inflater.inflate(R.layout.fragment_image, container, false);
        gridView = (GridView) mBaseView.findViewById(R.id.images_grid);
        new GetImagesTask().execute();
        return mBaseView;
    }

    class ImageAdapter extends BaseAdapter {

        private ArrayList<String> items;

        public ImageAdapter(ArrayList<String> totalImages) {
            items = totalImages;

        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.single_image_delegate, parent, false);
                holder.textView = (TextView) convertView.findViewById(R.id.single_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            /*
            Picasso.with(getActivity().getApplicationContext())
                    .load(AppGlobals.IMAGES_LOCATION+items.get(position))
                    .placeholder(R.drawable.progress_animation)
                    .into(holder.textView);
                    */


           return convertView;
        }
    }

    public class ViewHolder {
        TextView textView;
    }


    class GetImagesTask extends AsyncTask<String, String, ArrayList<String>> {

        private boolean internetAvailability = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> list = new ArrayList<>();

            String sessionid = Helpers.getStringDataFromSharedPreference(AppGlobals.KEY_ER_SESSIONID);
            String result;
            if (Helpers.isNetworkAvailable() && Helpers.isInternetWorking()) {
                internetAvailability = true;
                try {
                    result =   Helpers.connectionRequest
                            (String.format(
                                    AppGlobals.GET_CREDITS_URL +"sessionid="+"%s",
                                    sessionid), "POST");

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("entries");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        Log.i("TAG", json.toString());
                        if (!list.contains(json.getString("file_location"))) {
                            list.add(json.getString("file_location"));
                        }
                    }
                    return list;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                internetAvailability = false;
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();

            if (!internetAvailability) {
                Helpers.alertDialog(getActivity(), AppGlobals.NO_INTERNET_TITLE,
                        AppGlobals.NO_INTERNET_MESSAGE, null);
            } else {
                ImageAdapter imageAdapter = new ImageAdapter(s);
                gridView.setAdapter(imageAdapter);
            }

        }
    }
}
