package com.example.xieyo.roam.MyAdapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.moviebean.MovieFragList;
import com.willy.ratingbar.BaseRatingBar;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieFragAdapter extends BaseMultiItemQuickAdapter<MovieFragList, BaseViewHolder> {
    public MovieFragAdapter(@Nullable List<MovieFragList> data) {
        super( data);

        addItemType(1, R.layout.movie_frag_title);
        addItemType(2, R.layout.movie_frag_movielist_with_star);
        addItemType(3, R.layout.movie_frag_movielist_nostar);
        addItemType(4, R.layout.movie_with_star);
        addItemType(5, R.layout.book_digest);

    }

    @Override
    protected void convert(BaseViewHolder helper, MovieFragList item) {

        ImageView coverview;
        RequestOptions options = new RequestOptions()
                .optionalTransform(new RoundedCornersTransformation(5,0, RoundedCornersTransformation.CornerType.ALL))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        //.override(200,280)
        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();

        switch (helper.getItemViewType())
        {
            case 1:
                if(item.from==1)
                {
                    helper.setText(R.id.tv_content,"正在上映");
                }
                if(item.from==2)
                {
                    helper.setText(R.id.tv_content,"最近热门电影");
                }
                if(item.from==3)
                {
                    //helper.setText(R.id.tv_content,"畅销图书榜");
                    helper.setText(R.id.tv_content,"最近热门电视剧");
                }
                break;
            case 2:
            helper.setText(R.id.movie_name, item.name)
                    .setText(R.id.movie_rating,item.rating);
            coverview = helper.getView(R.id.movie_cover);
            Glide.with(mContext).load(item.coveruri)
                    .apply(options).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .into(coverview);
            BaseRatingBar ratingBar = helper.getView(R.id.simpleRatingBar);
            if(item.rating.length()==3)
            {
                ratingBar.setRating(Float.valueOf(item.rating)/2);
            }
            break;
            case 3:
                helper.setText(R.id.movie_name, item.name);
                coverview = helper.getView(R.id.movie_cover);
                Glide.with(mContext).load(item.coveruri)
                        .apply(options).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .into(coverview);
                break;

            case 4:
                helper.setText(R.id.movie_name, item.name)
                        .setText(R.id.movie_auther, item.artist)
                        .setText(R.id.movie_rating, item.rating);
                coverview = helper.getView(R.id.book_cover);
                Glide.with(mContext).load(item.coveruri)
                        .apply(options)
                        .into(coverview);


                ratingBar = helper.getView(R.id.simpleRatingBar);

                // ScaleRatingBar ratingBar = new ScaleRatingBar(mContext);
                if (item.rating.length() == 3) {
                    ratingBar.setRating(Float.valueOf(item.rating) / 2);
                }
                break;
            case 5:
                helper.setText(R.id.book_name, item.name)
                        .setText(R.id.part_digest, item.part_digest)
                        .setText(R.id.num_digest, item.num_digest);
                coverview = helper.getView(R.id.book_cover);

                Glide.with(mContext).load(item.coveruri)
                        .apply(options)
                        .into(coverview);
                break;
            default:
                break;

        }

    }

}
