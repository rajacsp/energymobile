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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.qwan.energy.R;
import org.qwan.energy.utils.AppGlobals;
import org.qwan.energy.utils.CreditItem;
import org.qwan.energy.utils.Helpers;

import java.io.IOException;
import java.util.ArrayList;

public class ViewCreditsFragment extends Fragment {

    private static final String TAG = "ViewCreditFragment";

    private View mBaseView;
    private ListView listView;
    private ProgressDialog mProgressDialog;
    private View baseView;
    //private ArrayList<String> imagesArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //imagesArrayList = new ArrayList<>();
        mBaseView = inflater.inflate(R.layout.fragment_image, container, false);
        baseView = inflater.inflate(R.layout.fragment_image, container, false);
        listView = (ListView) mBaseView.findViewById(R.id.images_grid);
        new GetCreditsTask().execute();
        return mBaseView;
    }

    class CreditItemAdapter extends BaseAdapter {

        //private ArrayList<String> items;
        private ArrayList<CreditItem> creditItems;

        public CreditItemAdapter(ArrayList<CreditItem> creditItems) {
            this.creditItems = creditItems;
        }

        @Override
        public int getCount() {
            return creditItems.size();
        }

        @Override
        public Object getItem(int position) {
            return creditItems.get(position);
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

                holder.tvCategory = (TextView) convertView.findViewById(R.id.title);
                holder.tvContent  = (TextView) convertView.findViewById(R.id.content);
                holder.tvClue  = (TextView) convertView.findViewById(R.id.clue);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CreditItem currentCreditItem = creditItems.get(position);

            holder.tvCategory.setText(currentCreditItem.getCategory());
            holder.tvContent.setText(currentCreditItem.getContent());
            holder.tvClue.setText(currentCreditItem.getClue());

            return convertView;
        }
    }

    public class ViewHolder {
        TextView tvCategory;
        TextView tvContent;
        TextView tvClue;
    }


    class GetCreditsTask extends AsyncTask<String, String, ArrayList<CreditItem>> {

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
        protected ArrayList<CreditItem> doInBackground(String... strings) {
            ArrayList<CreditItem> list = new ArrayList<>();

            String sessionid = Helpers.getStringDataFromSharedPreference(AppGlobals.KEY_ER_SESSIONID);
            String result;
            if (Helpers.isNetworkAvailable() && Helpers.isInternetWorking()) {
                internetAvailability = true;
                try {
                    result =   Helpers.connectionRequest
                            (String.format(
                                    AppGlobals.GET_CREDITS_URL +"%s",
                                    sessionid), "POST");



                    JSONObject jsonObject = new JSONObject(result);

                    // check apiresult
                    // JSONObject apiValueObject = jsonObject.getJSONObject("apivalue");

                    JSONArray jsonArray = jsonObject.getJSONArray("apivalue");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        Log.i(TAG, json.toString());

                        String CATEGORY_CONST = "CATEGORY";
                        String CONTENT_CONST = "CONTENT";
                        String CLUE_CONST = "CLUE";

                        String category = json.getString(CATEGORY_CONST);
                        String clue =  json.has(CONTENT_CONST) ? json.getString(CONTENT_CONST) : "";
                        String content = json.has(CLUE_CONST) ? json.getString(CLUE_CONST) : "";

                        CreditItem cCreditItem = new CreditItem(category, content, clue);

                        list.add(cCreditItem);
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
        protected void onPostExecute(ArrayList<CreditItem> creditItems) {
            super.onPostExecute(creditItems);
            mProgressDialog.dismiss();

            if (!internetAvailability) {
                Helpers.alertDialog(getActivity(), AppGlobals.NO_INTERNET_TITLE,
                        AppGlobals.NO_INTERNET_MESSAGE, null);
            } else {
                CreditItemAdapter creditItemAdapter = new CreditItemAdapter(creditItems);
                listView.setAdapter(creditItemAdapter);
            }

        }
    }
}
