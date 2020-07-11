package com.securesurveillance.skili.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.PostImagevideoActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.model.responsemodel.GetAllProfileDetailsModel;
import com.securesurveillance.skili.profile.MyProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hp-R on 15/09/2016.
 */
public class PostsGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private static ArrayList<GetAllProfileDetailsModel.Result.Post> arrImage;
    boolean isItMe=false;

    public PostsGridViewAdapter(Context c, ArrayList<GetAllProfileDetailsModel.Result.Post> arrImage, boolean isItMe) {
        mContext = c;
        this.arrImage = arrImage;
        this.isItMe=isItMe;
    }


    @Override
    public int getCount() {
        return arrImage.size();
    }

    @Override
    public Object getItem(int position) {
        return arrImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.listitem_post, null);
            final ImageView ivOptions = (ImageView) grid.findViewById(R.id.ivOptions);
            final ImageView ivIconMedia = (ImageView) grid.findViewById(R.id.ivIconMedia);
            final RelativeLayout rlPost = (RelativeLayout) grid.findViewById(R.id.rlPost);
            rlPost.setTag(position);
            rlPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=Integer.parseInt(view.getTag().toString());
                    Intent i = new Intent(mContext, PostImagevideoActivity.class);
                    i.putExtra("ISME",isItMe);
                    if(arrImage.get(pos).getMedia().getMediaLink().contains("mp4")){
                        i.putExtra("VIDEO_URL", arrImage.get(pos).getMedia().getMediaLink());

                    }else{
                        i.putExtra("IMAGE_URL", arrImage.get(pos).getMedia().getMediaLink());

                    }
                    i.putExtra("POSTID",arrImage.get(pos).getPostId());
                    mContext.startActivity(i);
                }
            });
            if (arrImage.get(position).getMedia() != null) {

                if (arrImage.get(position).getMedia().getMediaLink().contains("mp4")) {

                    Bitmap curThumb = null;
                    ivIconMedia.setVisibility(View.VISIBLE);

                    new AsyncTask<String, Bitmap, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(String... strings) {
                            try {

                                Bitmap curThumb = retriveVideoFrameFromVideo(arrImage.get(position).getMedia().getMediaLink());
                                return curThumb;
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            super.onPostExecute(bitmap);
                            ivOptions.setImageBitmap(bitmap);

                        }
                    }.execute();


                } else {
                    ivIconMedia.setVisibility(View.GONE);

                    Picasso.get().load(arrImage.get(position).getMedia().getMediaLink()).into(ivOptions);
                }


            }

        } else {
            grid = (View) convertView;
        }

        return grid;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
