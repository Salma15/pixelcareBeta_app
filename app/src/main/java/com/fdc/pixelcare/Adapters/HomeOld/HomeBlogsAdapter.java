package com.fdc.pixelcare.Adapters.HomeOld;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fdc.pixelcare.Activities.Others.BlogDetailActivity;
import com.fdc.pixelcare.DataModel.BlogsList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewBold;
import com.fdc.pixelcare.Views.CustomTextViewItalicBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by medisense on 28/03/19.
 */

public class HomeBlogsAdapter extends RecyclerView.Adapter<HomeBlogsAdapter.MyViewHolder> {

    private List<BlogsList> blogsList;
    private Context mContext;
    private String DOWNLOAD_BLOG_IMAGE_URL;
    URL url;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView posted_on, description;
        public ImageView feeds_images;
        public CustomTextViewItalicBold read_more;
        public CustomTextViewBold title;
        public LinearLayout  item_layout;


        public MyViewHolder(View view) {
            super(view);
            title = (CustomTextViewBold) view.findViewById(R.id.feeds_title);
            posted_on = (CustomTextView) view.findViewById(R.id.feeds_date);
            description = (CustomTextView) view.findViewById(R.id.feeds_description);
            read_more = (CustomTextViewItalicBold) view.findViewById(R.id.feeds_readmore);
            feeds_images = (ImageView) view.findViewById(R.id.feeds_image);
            item_layout = (LinearLayout)  view.findViewById(R.id.home_list_layout);

           }
    }


    public HomeBlogsAdapter(Context context, List<BlogsList> blgList) {
        this.mContext = context;
        this.blogsList = blgList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_blog_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BlogsList blog = blogsList.get(position);
        holder.title.setText(stripHtml(blog.getBlogTitle()));
        holder.description.setText(stripHtml(blog.getBlogDescription()));

        if(blog.getBlogPostdate().equals("")) {

        }
        else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date newDate = null;
            try {
                newDate = format.parse(blog.getBlogPostdate());
                format = new SimpleDateFormat("dd MMM yyyy");
                String post_date = format.format(newDate);
                holder.posted_on.setText("Posted On: "+ post_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


      //  DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_BLOGS_IMAGE_URL+String.valueOf(blog.getBlogId())+"/"+blog.getBlogImage();
        DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_HEALTH_BLOGS_IMAGE_URL+String.valueOf(blog.getBlogId())+"/"+blog.getBlogImage();

        if(blog.getBlogImage().equals("")) {
            holder.feeds_images.setVisibility(View.VISIBLE);
        }
        else {
            holder.feeds_images.setVisibility(View.VISIBLE);
            loadImage(holder, DOWNLOAD_BLOG_IMAGE_URL);
        }

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(mContext, BlogDetailActivity.class);
                i1.putExtra("title","Blog Details");
                i1.putExtra("BLOG_ID", blog.getBlogId());
                i1.putExtra("BLOG_IMAGE", blog.getBlogImage());
                i1.putExtra("BLOG_TITLE", blog.getBlogTitle().trim());
                i1.putExtra("BLOG_DATE", blog.getBlogPostdate());
                i1.putExtra("BLOG_DESCRIPTION", blog.getBlogDescription());
                i1.putExtra("BLOG_TYPE", blog.getBlogPostType());
                i1.putExtra("BLOG_USERNAME", blog.getBlogUserName());
                i1.putExtra("BLOG_USERIMAGE", blog.getBlogUserImage());
                i1.putExtra("BLOG_USERPROFESSION", blog.getBlogUserProf());
                i1.putExtra("BLOG_CONTACTINFO", blog.getBlogContactInfo());
                i1.putExtra("BLOG_ATTACHMENT", blog.getBlogAttachments());
                i1.putExtra("BLOG_COMPANY_ID", blog.getBlogCompanyId());
                i1.putExtra("BLOG_TRANSACTIONID", blog.getBlogTransactionID());
                i1.putExtra("BLOG_VIEWS", blog.getBlogNumberViews());
                mContext.startActivity(i1);
            }
        });

    }

    private void loadImage(final MyViewHolder holder, String DOWNLOAD_BLOG_IMAGE_URL) {
        try {
            url = new URL(DOWNLOAD_BLOG_IMAGE_URL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Log.d(AppUtils.TAG, " pic: "+DOWNLOAD_BLOG_IMAGE_URL);
        Picasso.with(mContext).load(String.valueOf(url))
                .placeholder(R.drawable.blogs_empty_img)
                .error(R.drawable.blogs_empty_img)
                .resize(240, 200)
                .centerCrop()
                .into(holder.feeds_images, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.feeds_images.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError() {
                        holder.feeds_images.setVisibility(View.VISIBLE);
                    }
                });
        Picasso.with(mContext).invalidate(String.valueOf(url));
    }

    @Override
    public int getItemCount() {
        return blogsList.size();
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }


}
