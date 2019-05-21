package com.fdc.pixelcare.Adapters.Others;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fdc.pixelcare.Activities.Others.BlogDetailActivity;
import com.fdc.pixelcare.DataModel.BlogsList;
import com.fdc.pixelcare.Network.APIClass;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Utils.AppUtils;
import com.fdc.pixelcare.Views.CustomTextViewBold;
import com.fdc.pixelcare.Views.CustomTextViewItalic;
import com.fdc.pixelcare.Views.CustomTextViewItalicBold;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by SALMA on 31-12-2017.
 */

public class BlogsAdapter extends RecyclerView.Adapter<BlogsAdapter.MyViewHolder>  {

    private List<BlogsList> blogsList;
    Context mContext;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    String DOWNLOAD_BLOG_IMAGE_URL, DOWNLOAD_AUTHOR_PROFILE;
    int item_position = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardViewBlogs;
        RelativeLayout blogslist;
        public ImageView author_image, blogs_image;
        public CustomTextViewItalic post_date;
        CustomTextViewItalicBold author_name;
        public CustomTextViewBold title;
        public CustomTextViewItalic shortDescription;
        public CustomTextViewSemiBold blog_types;
        public CustomTextViewSemiBold num_views;
        public CustomTextViewSemiBold posted_by;


        public MyViewHolder(View view) {
            super(view);
            title = (CustomTextViewBold) view.findViewById(R.id.blogs_title);
            blogs_image = (ImageView) view.findViewById(R.id.blogs_image);
            shortDescription = (CustomTextViewItalic) view.findViewById(R.id.blog_short_desc);
            cardViewBlogs = (CardView) view.findViewById(R.id.blogs_list_cardview);
            blog_types = (CustomTextViewSemiBold)  view.findViewById(R.id.blogs_type);
            blogslist = (RelativeLayout)view.findViewById(R.id.relativeLayout_blogslist);
            num_views = (CustomTextViewSemiBold)  view.findViewById(R.id.blog_num_views);
            posted_by = (CustomTextViewSemiBold)  view.findViewById(R.id.blog_postedby);

        }
    }

    public BlogsAdapter(Context context, List<BlogsList> blogList) {
        this.blogsList = blogList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blogs_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BlogsList blogs = blogsList.get(position);

        if(position % 2==0)
            holder.cardViewBlogs.setCardBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        else
            holder.cardViewBlogs.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));

        DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_HEALTH_BLOGS_IMAGE_URL+String.valueOf(blogs.getBlogId())+"/"+blogs.getBlogImage();

        if(blogs.getBlogImage().equals("")) {
            holder.blogs_image.setVisibility(View.GONE);
        }
        else {
            holder.blogs_image.setVisibility(View.VISIBLE);

            String urlStr = DOWNLOAD_BLOG_IMAGE_URL;
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

            Log.d(AppUtils.TAG, "path: "+ String.valueOf(url));
            Picasso.with(mContext).load(String.valueOf(url))
                    .placeholder(R.drawable.blogs_empty_img)
                    .error(R.drawable.blogs_empty_img)
                    .resize(400, 400)
                    .centerInside()
                    .into(holder.blogs_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
            Picasso.with(mContext).invalidate(String.valueOf(url));

        }

        holder.blog_types.setVisibility(View.GONE);

        holder.title.setText( stripHtml(blogs.getBlogTitle().trim()));
        holder.shortDescription.setText( stripHtml(blogs.getBlogDescription().trim()));
        holder.num_views.setText("Views: "+ blogs.getBlogNumberViews());
        holder.posted_by.setText("Posted By: "+blogs.getBlogUserName());

        Log.d(AppUtils.TAG, "ATTACH: " + blogs.getBlogAttachments());
        Log.d(AppUtils.TAG, "COMP: " + blogs.getBlogCompanyId());

        holder.blogslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("BLOG_ID", blogs.getBlogId());
                bundle.putString("BLOG_IMAGE", blogs.getBlogImage());
                bundle.putString("BLOG_TITLE", blogs.getBlogTitle().trim());
                bundle.putString("BLOG_DATE", blogs.getBlogPostdate());
                bundle.putString("BLOG_DESCRIPTION", blogs.getBlogDescription());
                bundle.putString("BLOG_TYPE", blogs.getBlogPostType());
                bundle.putString("BLOG_USERNAME", blogs.getBlogUserName());
                bundle.putString("BLOG_USERIMAGE", blogs.getBlogUserImage());
                bundle.putString("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                bundle.putString("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                bundle.putString("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                bundle.putInt("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                bundle.putString("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                bundle.putString("BLOG_VIEWS", blogs.getBlogNumberViews());

                    Intent blog_intent = new Intent(mContext, BlogDetailActivity.class);
                    blog_intent.putExtra("title","Blog Details");
                    blog_intent.putExtra("BLOG_ID", blogs.getBlogId());
                    blog_intent.putExtra("BLOG_IMAGE", blogs.getBlogImage());
                    blog_intent.putExtra("BLOG_TITLE", blogs.getBlogTitle().trim());
                    blog_intent.putExtra("BLOG_DATE", blogs.getBlogPostdate());
                    blog_intent.putExtra("BLOG_DESCRIPTION", blogs.getBlogDescription());
                    blog_intent.putExtra("BLOG_TYPE", blogs.getBlogPostType());
                    blog_intent.putExtra("BLOG_USERNAME", blogs.getBlogUserName());
                    blog_intent.putExtra("BLOG_USERIMAGE", blogs.getBlogUserImage());
                    blog_intent.putExtra("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                    blog_intent.putExtra("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                    blog_intent.putExtra("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                    blog_intent.putExtra("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                    blog_intent.putExtra("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                    blog_intent.putExtra("BLOG_VIEWS", blogs.getBlogNumberViews());
                    mContext.startActivity(blog_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogsList.size();
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }
}
