package com.fdc.pixelcare.Adapters.Opinions;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdc.pixelcare.DataModel.ReportsImages;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Views.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SALMA on 31/12/18.
 */

public class ReportsImageAdapter  extends RecyclerView.Adapter<ReportsImageAdapter.RecyclerViewHolder> {

    private List<ReportsImages> arrayList;
    private Context context;
    boolean isImageFitToScreen;
    public ArrayList<String> GET_EPISODES_PHOTOS = new ArrayList<String>();
    String ENTRY_TYPE;

    public ReportsImageAdapter(Context context,List<ReportsImages> arrayList, ArrayList<String> photos, String types) {
        this.context = context;
        this.arrayList = arrayList;
        this.GET_EPISODES_PHOTOS = photos;
        this.ENTRY_TYPE = types;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final ReportsImages model = arrayList.get(position);

        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder
        Log.d(AppUtils.TAG + " image: ", String.valueOf(model.getFundusImage()));

        mainHolder.title.setVisibility(View.VISIBLE);
        Bitmap bm = BitmapFactory.decodeFile(model.getFundusImage().trim());
        mainHolder.imageview.setImageBitmap(bm);

        mainHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DisplayMetrics metrics = context.getResources().getDisplayMetrics();

                int DeviceTotalWidth = metrics.widthPixels;
                int DeviceTotalHeight = metrics.heightPixels;

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_atachview);
                dialog.getWindow().setLayout(DeviceTotalWidth ,DeviceTotalHeight);
                //  dialog.addContentView(new View(context), (new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)));
                TouchImageView imageZoom = dialog.findViewById(R.id.image_attachview);
                if(ENTRY_TYPE.equalsIgnoreCase("VIEW")) {
                    mainHolder.title.setVisibility(View.GONE);
                    String urlStr = model.getFundusImage().trim();
                    URL url = null;
                    try {
                        url = new URL(urlStr);
                        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                        url = uri.toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    Picasso.with(context).load(String.valueOf(url))
                            .placeholder(R.drawable.blogs_empty_img)
                            .error(R.drawable.blogs_empty_img)
                            .fit()
                            .into(imageZoom, new Callback() {
                                @Override
                                public void onSuccess() {
                                }
                                @Override
                                public void onError() {
                                }
                            });

                }
                else {
                    mainHolder.title.setVisibility(View.VISIBLE);
                    Bitmap bm = BitmapFactory.decodeFile(model.getFundusImage().trim());
                    imageZoom.setImageBitmap(bm);
                }
                dialog.show();
            }
        });

        mainHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemDismiss(position);
                GET_EPISODES_PHOTOS.remove(position);
            }
        });

    }

    public void onItemDismiss(int position) {
        if(position!=-1 && position<arrayList.size())
        {
            arrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.fundus_image_item_row, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
        // View holder for gridview recycler view as we used in listview
        public TextView title;
        public ImageView imageview;

        public RecyclerViewHolder(View view) {
            super(view);
            // Find all views ids

            this.title = (TextView) view.findViewById(R.id.title);
            this.imageview = (ImageView) view.findViewById(R.id.image);
        }
    }
}
